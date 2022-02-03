# Minified Spotify Wrapped
This tool written in Java 17 will give you a tiny overview of how your Spotify Wrapped looks like currently.
It is a simple CLI that shows you:

- Total time listened this year;
- Time listened per artist of your top 10;
- Time listened per track of your top 10.

It calculates this data based on the data that Spotify has collected from you.
You can request this data here: https://www.spotify.com/account/privacy/

## How to use
1. Download Java 17 LTS and set up your machine so that its runtime configuration is set to Java 17.
2. Download your Spotify data. This may take up to 30 days according to Spotify.
3. Download the latest version of this project.
4. Run the downloaded file in your terminal as follows: `java -jar /path/to/MSW.jar [/path/to/SpotifyDataFolder]`

## Future updates
I intend to add some small, additional features.

- [ ] Add `-f` flag to get the full report. Overrides `-n [num]`.
- [ ] Add `-n [num]` flag to get the report with the top specified number of tracks and artists.
- [ ] Add `-y [year]` flag to get the report from a certain year.
