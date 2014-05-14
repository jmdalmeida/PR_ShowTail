package Utils.Data;

import java.util.Date;

public class Comment {
    
    private final int id;
    private final String comment, user;
    private final Date timestamp;

    public Comment(int id, String comment, String user, Date timestamp) {
        this.id = id;
        this.comment = comment;
        this.user = user;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public String getUser() {
        return user;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
