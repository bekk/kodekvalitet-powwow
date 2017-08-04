package no.magicmusic.genius;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.magicmusic.genius.jsonmodel.artist.GeniusArtistResponse;
import no.magicmusic.genius.jsonmodel.search.GenuisSearchResponse;
import no.magicmusic.genius.jsonmodel.search.Hit;
import no.magicmusic.genius.jsonmodel.song.GeniusSongResponse;
import no.magicmusic.genius.internalmodel.SearchResult;
import no.magicmusic.genius.internalmodel.SearchResults;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class GeniusClient {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final CloseableHttpClient HTTP_CLIENT = HttpClientBuilder.create().build();

    private static final Map<String, GeniusSongResponse> SONG_CACHE = new HashMap<>();
    private static final Map<String, GeniusArtistResponse> ARTIST_CACHE = new HashMap<>();

    private String apiToken;

    public GeniusClient(String apiToken) {
        this.apiToken = apiToken;
    }

    public SearchResults search(String query) {
        query = query.replace(" ", "%20");
        return httpGet("https://api.genius.com/search?q=" + query, GenuisSearchResponse.class)
                .filter(GenuisSearchResponse::isValid)
                .map(this::geniusResponseToSearchResults)
                .orElse(SearchResults.failed("Error when querying API"));
    }

    private SearchResults geniusResponseToSearchResults(GenuisSearchResponse genuisSearchResponse) {
        return SearchResults.success(
                genuisSearchResponse.getValidHitsSortedByAnnotationCount().parallelStream()
                        .map(this::hitToSearchResult)
                        .collect(toList())
        );
    }

    private SearchResult hitToSearchResult(Hit hit) {
        GeniusSongResponse song = fetchSong(hit.result.id);
        GeniusArtistResponse artist = fetchArtist(hit.result.primary_artist.id);
        return new SearchResult(hit.result.pyongs_count, hit.result.primary_artist.name, hit.result.title, song.response.song.release_date, artist.response.artist.instagram_name);
    }

    private GeniusSongResponse fetchSong(String songId) {
        return SONG_CACHE.computeIfAbsent(songId, id -> httpGet("https://api.genius.com/songs/" + id, GeniusSongResponse.class).orElse(null));
    }

    private GeniusArtistResponse fetchArtist(String artistId) {
        return ARTIST_CACHE.computeIfAbsent(artistId, id -> httpGet("https://api.genius.com/artists/" + id, GeniusArtistResponse.class).orElse(null));
    }

    private <T> Optional<T> httpGet(String uri, Class<T> clazz) {
        HttpGet fetch = new HttpGet(uri);
        fetch.addHeader("Authorization", "Bearer " + apiToken);
        try (CloseableHttpResponse resp = HTTP_CLIENT.execute(fetch)) {
            return Optional.of(JSON_MAPPER.readValue(resp.getEntity().getContent(), clazz));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
