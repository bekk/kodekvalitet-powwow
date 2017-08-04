package no.magicmusic.genius.jsonmodel.search;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class GenuisSearchResponse {
    public Response response;

    public boolean isValid() {
        return response != null && response.hits != null && response.hits.stream().noneMatch(hit -> hit.result == null || hit.result.primary_artist == null);
    }

    public List<Hit> getValidHitsSortedByAnnotationCount() {
        return response.hits.stream()
                .filter(hit -> hit.result.pyongs_count != null && hit.result.annotation_count != null)
                .sorted((o1, o2) -> o2.result.annotation_count.compareTo(o1.result.annotation_count))
                .collect(toList());
    }
}
