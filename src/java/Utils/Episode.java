package Utils;

public class Episode {

    private final int episodeNumber, seasonNumber;
    private final String title, overview;

    public Episode(int episodeNumber, int seasonNumber, String title, String overview) {
        this.episodeNumber = episodeNumber;
        this.seasonNumber = seasonNumber;
        this.title = title;
        this.overview = overview;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getShortOverview() {
        return overview.length() > 199 ? overview.substring(0, 199) + "..." : overview;
    }

    public int getOverviewLength() {
        return overview.length();
    }

}
