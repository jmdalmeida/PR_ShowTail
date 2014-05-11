package Utils;

public class Show {
    private final int id, followers, episodesNumber;
    private final String title, imgPath, overview, status, premierDate;
    private final double rating;
    private final boolean following;

    public Show(int id, int followers, int episodesNumber, String title, String imgPath, String overview, String status, String premierDate, double rating, boolean userFollows) {
        this.id = id;
        this.followers = followers;
        this.episodesNumber = episodesNumber;
        this.title = title;
        this.imgPath = imgPath;
        this.overview = overview;
        this.status = status;
        this.premierDate = premierDate;
        this.rating = rating;
        this.following = userFollows;
    }

    public int getId() {
        return id;
    }

    public int getFollowers() {
        return followers;
    }

    public int getEpisodesNumber() {
        return episodesNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getStatus() {
        return status;
    }

    public String getPremierDate() {
        return premierDate;
    }

    public double getRating() {
        return rating;
    }

    public boolean isFollowing() {
        return following;
    }
    
}