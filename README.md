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
4. Run the downloaded file in your terminal as follows: `java -jar /path/to/MSW.jar /path/to/SpotifyDataFolder [-f] [-n [num]] [-y [year]]`

### Flags
- `-f`: Generate a full report. Shows all artists, all tracks.
- `-n [num]`: Generate a report that shows the top `num` artists and tracks. Defaults to 10.
- `-y [year]`: Generate a report for that year. Defaults to the current year.

**Note:** flags can be combined, but they each need a separate dash.  
Valid examples: `-f -n 5 -y 2021`, `-y 2022 -f`  
Invalid examples: `-fny 5 2021`, `-fy 2021`  
**Note:** `-f` and `-n` override each other. The last argument wins.
