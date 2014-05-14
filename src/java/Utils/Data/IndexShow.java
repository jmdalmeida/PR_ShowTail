package Utils.Data;

public class IndexShow {

    private final int id;
    private final String backdropImg, name;

    public IndexShow(int id, String backdropImg, String name) {
        this.id = id;
        this.backdropImg = backdropImg;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getBackdropImg() {
        return backdropImg;
    }

    public String getName() {
        return name;
    }
    
}
