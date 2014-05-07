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
        querys.put(TVShows_genres, "SELECT DISTINCT ID_Genre, Genre FROM SearchGenresView ORDER BY Genre ASC;");
        querys.put(TVShows_search, "SELECT * FROM SearchView WHERE Title LIKE ? ORDER BY Followers DESC;");
        querys.put(TVShows_order_all, "SELECT * FROM SearchView ORDER BY Title ASC;");
        querys.put(TVShows_order_followed, "SELECT * FROM SearchView ORDER BY Followers DESC;");
        querys.put(TVShows_order_recommended, "SELECT * FROM SearchView ORDER BY Rating DESC;");
        querys.put(TVShows_search_by_genre, "SELECT * FROM SearchView sv, SearchGenresView sgv WHERE sv.ID_Show = sgv.ID_Show AND sgv.ID_Genre = ?;");
    }
    
    public static String getQuery(SQLcmd key){
        if(querys.isEmpty())
            populate();
        return querys.get(key);
    }

}
