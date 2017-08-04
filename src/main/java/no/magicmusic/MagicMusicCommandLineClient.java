package no.magicmusic;

import no.magicmusic.genius.GeniusClient;
import no.magicmusic.genius.internalmodel.SearchResults;

import java.util.Scanner;

class MagicMusicCommandLineClient {

    private final GeniusClient geniusClient;

    MagicMusicCommandLineClient(GeniusClient geniusClient) {
        this.geniusClient = geniusClient;
    }

    void start() {
        BrandPrinter.printHeading();
        System.out.println("\n\nSearch:");
        while (true) {
            String query = askUserForQueryString();
            if (query.isEmpty()) {
                System.out.println("Please write something to search for. Artist/song/whatever!...");
            } else if (query.equals("exit")) {
                System.out.println("Goodbye! :)");
                return;
            } else {
                SearchResults searchResults = geniusClient.search(query);
                searchResults.printResults();

                System.out.println("\nSearch again:");
            }
        }
    }

    private String askUserForQueryString() {
        return new Scanner(System.in).nextLine();
    }

}
