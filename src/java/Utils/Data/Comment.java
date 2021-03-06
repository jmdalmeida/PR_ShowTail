package Utils.Data;

import java.util.Date;

public class Comment {
    
    private final int id_comment, id_user;
    private final String comment, username, imgPath;
    private final Date timestamp;

    public Comment(int id_comment, int id_user, String username, String imgPath, String comment, Date timestamp) {
        this.id_comment = id_comment;
        this.id_user = id_user;
        this.username = username;
        this.imgPath = imgPath;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public int getIdComment() {
        return id_comment;
    }

    public String getComment() {
        return comment;
    }

    public int getIdUser() {
        return id_user;
    }

    public String getUser() {
        return username;
    }

    public String getImgPath() {
        return imgPath;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
