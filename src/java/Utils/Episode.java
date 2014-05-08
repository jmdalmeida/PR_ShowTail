package Utils;

public class Episode {

    private final int episodeNumber;
    private final String title, overview;

    public Episode(int episodeNumber, String title, String overview) {
        this.episodeNumber = episodeNumber;
        this.title = title;
        this.overview = overview;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }
    
}
