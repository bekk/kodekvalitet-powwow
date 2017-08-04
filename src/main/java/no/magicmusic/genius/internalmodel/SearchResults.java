package no.magicmusic.genius.internalmodel;

import java.util.List;

import static java.util.Collections.emptyList;

public class SearchResults {

    private String resultMessage;
    private List<SearchResult> results;

    private SearchResults(String resultMessage, List<SearchResult> results) {
        this.resultMessage = resultMessage;
        this.results = results;
    }

    public static SearchResults success(List<SearchResult> results) {
        String message = String.format("Found %d match%s…", results.size(), results.size() > 1 ? "es" : "");
        return new SearchResults(message, results);
    }

    public static SearchResults failed(String errorMessage) {
        return new SearchResults(errorMessage, emptyList());
    }

    public void printResults() {
        System.out.println(resultMessage);
        results.forEach(SearchResult::printResult);
    }
}
