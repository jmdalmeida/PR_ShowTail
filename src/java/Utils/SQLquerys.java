/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import static Utils.SQLcmd.*;
import java.util.HashMap;

public class SQLquerys {
    
    private static final HashMap<SQLcmd, String> querys = new HashMap<SQLcmd, String>();
    
    private static void populate(){
        querys.put(TVShows_genres, "SELECT DISTINCT ID_Genre, Genre FROM SearchGenresView ORDER BY Genre ASC");
        querys.put(TVShows_search, "SELECT * FROM SearchView WHERE Title LIKE ? ORDER BY Followers DESC");
        querys.put(TVShows_order_all, "SELECT * FROM SearchView ORDER BY Title ASC");
        querys.put(TVShows_order_followed, "SELECT * FROM SearchView ORDER BY Followers DESC");
        querys.put(TVShows_order_recommended, "SELECT * FROM SearchView ORDER BY Rating DESC");
        querys.put(TVShows_search_by_genre, "SELECT * FROM SearchView sv, SearchGenresView sgv WHERE sv.ID_Show = sgv.ID_Show AND sgv.ID_Genre = ?");
        querys.put(Validation_password, "SELECT Password FROM user WHERE Username = ?");
        querys.put(ShowTemplate_show_info, "SELECT * FROM tv_show t, SearchView sv WHERE t.ID_Show = ? AND t.ID_Show = sv.ID_Show");
        querys.put(ShowTemplate_show_check_follows, "SELECT * FROM UserShowStatus WHERE ID_User = ? AND ID_Show = ?");
        querys.put(ShowTemplate_show_seasons, "SELECT * FROM season WHERE ID_Show = ?");
        querys.put(ShowTemplate_show_follow, "INSERT INTO Following(ID_User, ID_Show) VALUES(?,?)");
        querys.put(ShowTemplate_show_unfollow, "DELETE FROM Following WHERE ID_User = ? AND ID_Show = ?");
        querys.put(ShowTemplate_show_rate_select, "SELECT * FROM Rating WHERE ID_User = ? AND ID_Show = ?");
        querys.put(ShowTemplate_show_rate_insert, "INSERT INTO Rating(ID_User, ID_Show, Rating) VALUES(?,?,?)");
        querys.put(ShowTemplate_show_rate_update, "UPDATE Rating SET Rating = ? WHERE ID_User = ? AND ID_SHOW = ?");
        querys.put(Account_UserData, "SELECT * FROM User WHERE Username LIKE ?");
        querys.put(Account_followed_shows, "SELECT sv.* FROM Following f, Searchview sv WHERE f.ID_User = ? AND f.ID_Show = sv.ID_Show ORDER BY f.ID_Show DESC LIMIT 4");
        querys.put(Show_get_season, "SELECT * FROM season where ID_Season = ?");
        querys.put(Show_get_episodes, "SELECT * FROM episode where ID_Season = ?");
        querys.put(Show_set_episode_seen, "INSERT INTO Watched_Episode(ID_Show, ID_Season, ID_Episode, ID_User) VALUES(?,?,?,?)");
        querys.put(Show_set_episode_unseen, "DELETE FROM Watched_Episode WHERE ID_Show = ? AND ID_Season = ? AND ID_Episode = ? AND ID_User = ?");
        querys.put(Show_get_episode_seen, "SELECT * FROM Watched_Episode WHERE ID_Show = ? AND ID_Season = ? AND ID_Episode = ? AND ID_User = ?");
    }
    
    public static String getQuery(SQLcmd key){
        if(querys.isEmpty())
            populate();
        return querys.get(key);
    }

}