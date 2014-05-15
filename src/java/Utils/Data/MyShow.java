package Utils.Data;

public class MyShow {
    
    private final int id, episodesUnwatched, totalEpisodes;
    private final String title, imgPath;

    public MyShow(int id, int episodesUnwatched, int totalEpisodes, String title, String imgPath) {
        this.id = id;
        this.episodesUnwatched = episodesUnwatched;
        this.totalEpisodes = totalEpisodes;
        this.title = title;
        this.imgPath = imgPath;
    }

    public int getId() {
        return id;
    }

    public int getEpisodesUnwatched() {
        return episodesUnwatched;
    }

    public int getTotalEpisodes() {
        return totalEpisodes;
    }

    public String getTitle() {
        return title;
    }

    public String getImgPath() {
        return imgPath;
    }
}
