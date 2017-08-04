package no.magicmusic;

import no.magicmusic.genius.GeniusClient;

public class Main {

    private static final String GENIUS_API_TOKEN = "RonXd8_fl4L5RZ2CuCOoDHOGlZ-l_i6u0ytzJPDmiPXVihLWdw0cEY4Mj1u8NRaj";

    public static void main(String[] args) {
        GeniusClient geniusClient = new GeniusClient(GENIUS_API_TOKEN);

        MagicMusicCommandLineClient commandLineClient = new MagicMusicCommandLineClient(geniusClient);

        commandLineClient.start();
    }

}