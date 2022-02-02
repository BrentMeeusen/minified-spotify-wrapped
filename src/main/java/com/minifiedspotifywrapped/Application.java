package com.minifiedspotifywrapped;

public class Application {

    /**
     * The method that will run.
     *
     * First argument (required): path to downloaded Spotify data.
     * Optional arguments:
     *   -f     Full output
     *   -n [n] Number of tracks and artists to show. Default: 10
     *
     * @param args The arguments
     */
    public static void main(String[] args) {

        // If path is not filled in: return
        if(args.length == 0) {
            System.out.println("Please provide the path to the Spotify Data folder." +
                "You can request the data here: https://www.spotify.com/account/privacy/");
            return;
        }

    }

}
