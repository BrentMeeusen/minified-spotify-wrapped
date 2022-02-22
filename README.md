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
4. Run the downloaded file in your terminal as follows: `java -jar /path/to/MSW.jar`

### Commands
Once you get the application running like this, you should be able to enter commands.
The following commands are supported:

- help - Shows a list of valid commands.
- variables - Show the variables that are currently initialised.
- path - Set the path to the folder in which the StreamingHistoryX.json files are located.
- amount - Set the number of tracks and artists to show.
- full - Removes the limit of number of tracks and artists to show.
- year - Set the year for which the data should be looked up.
- show - Shows the output in the console.
- **[N/A]** save - Saves the output in the supported formats you wish. The output is saved in the folder where it is reading the data from.
- **[N/A]** sort - Allows you to choose whether you want to sort on time listened or number of streams.
- exit/quit - Quit the program.

Commands that are currently unavailable will be added in future updates.
Keep an eye on the [Release page](https://github.com/BrentMeeusen/minified-spotify-wrapped/releases/) if you want to download the newest version.
