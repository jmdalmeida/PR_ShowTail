package Utils;

public class ShowMovieDB {

    private final String title, imgPath;
    private final long id;
    private final boolean exists;

    public ShowMovieDB(String title, long id, boolean exists, String imgPath) {
        this.title = title;
        this.id = id;
        this.exists = exists;
        this.imgPath = imgPath;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public boolean exists() {
        return exists;
    }

    public String getImgPath() {
        return imgPath;
    }
    
}
