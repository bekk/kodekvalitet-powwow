package no.magicmusic.genius.internalmodel;

public class SearchResult {

    private final Long pyongsCount;
    private final String primaryArtistName;
    private final String songTitle;
    private final String releaseDate;
    private final String primaryArtistInstagramName;

    public SearchResult(Long pyongsCount, String primaryArtistName, String songTitle, String releaseDate, String primaryArtistInstagramName) {
        this.pyongsCount = pyongsCount;
        this.primaryArtistName = primaryArtistName;
        this.songTitle = songTitle;
        this.releaseDate = releaseDate;
        this.primaryArtistInstagramName = primaryArtistInstagramName;
    }

    void printResult() {
        System.out.println(String.format("%d pyongs: %s with '%s' (released: %s, instagram: %s)", pyongsCount, primaryArtistName, songTitle, releaseDate, primaryArtistInstagramName));
    }
}
