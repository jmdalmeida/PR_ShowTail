package Utils;

public class Episode {

    private final int id, episodeNumber, seasonNumber;
    private final String title, overview;
    private final boolean seen;

    public Episode(int id, int episodeNumber, int seasonNumber, String title, String overview, boolean seen) {
        this.id = id;
        this.episodeNumber = episodeNumber;
        this.seasonNumber = seasonNumber;
        this.title = title;
        this.overview = overview;
        this.seen = seen;
    }

    public int getId() {
        return id;
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

    public boolean isSeen() {
        return seen;
    }

}
