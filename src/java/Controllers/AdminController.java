package Controllers;

import JDBC.ConnectionFactory;
import Utils.SQLcmd;
import Utils.SQLquerys;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet(urlPatterns = {"/AdminController"})
public class AdminController extends HttpServlet {

    private static final String api_key = "c862d60174012383b25a24fbf9d62b33";

    private JSONParser parser;

    protected void addStuff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        boolean success = true;
        ConnectionFactory.getInstance().init();
        ConnectionFactory.getInstance().setAutoCommit(false);

        parser = new JSONParser();
        if ("GatherShow".equals(action)) {
            String moviedbID = request.getParameter("moviedbID");
            success = insertNewTvShow(moviedbID);
        } else if ("GatherPopularShows".equals(action)) {
            insertPopularShows();
        } else if ("GatherGenres".equals(action)) {
            success = insertGenres();
        }
        if (success) {
            try {
                ConnectionFactory.getInstance().commit();
            } catch (SQLException ex) {
                ConnectionFactory.getInstance().rollback();
            }
        } else {
            ConnectionFactory.getInstance().rollback();
        }
        parser = null;

        ConnectionFactory.getInstance().setAutoCommit(true);
        ConnectionFactory.getInstance().close();
        response.sendRedirect("AdminProfile.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        addStuff(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Admin Controller";
    }

    private boolean insertNewTvShow(String moviedbID) {
        boolean success = true;
        long generatedID;

        String result = makeRequest("https://api.themoviedb.org/3/tv/" + moviedbID + "?api_key=" + api_key);

        String tmp;
        try {
            Object obj = parser.parse(result);
            JSONObject json = (JSONObject) obj;

            String name = (String) json.get("name");
            String overview = (String) json.get("overview");
            int id = Integer.parseInt(moviedbID);
            String networks = arrayToString((JSONArray) json.get("networks"));
            String status = (String) json.get("status");
            tmp = (String) json.get("episode_run_time").toString();
            String episodeTimes = tmp.substring(1, tmp.length() - 1);
            String imagePath = "https://image.tmdb.org/t/p/original" + json.get("poster_path");
            String premierDate = (String) json.get("first_air_date");
            String backdrop_path = "https://image.tmdb.org/t/p/original" + json.get("backdrop_path");

            Object[] objs = {name, overview, id, networks, status, episodeTimes, imagePath, backdrop_path, premierDate};
            generatedID = ConnectionFactory.getInstance().insertAndReturnId(SQLquerys.getQuery(SQLcmd.Admin_insert_show), objs);

            associateGenresWithShow(generatedID, (JSONArray) json.get("genres"));

            JSONArray seasons = (JSONArray) json.get("seasons");
            Iterator<JSONObject> it = seasons.iterator();
            while (it.hasNext()) {
                JSONObject o = it.next();
                long season_number = o.get("season_number") != null ? (long) o.get("season_number") : -1;
                if (season_number > 0) {
                    insertNewSeason(moviedbID, season_number, generatedID);
                }
            }

        } catch (ParseException | NumberFormatException | SQLException ex) {
            ex.printStackTrace();
            success = false;
        }
        return success;
    }

    private void insertNewSeason(String moviedbID, long season_number, long tvshowID) throws SQLException, ParseException {
        long generatedID;

        String result = makeRequest("https://api.themoviedb.org/3/tv/" + moviedbID + "/season/" + season_number + "?api_key=" + api_key);
        Object obj = parser.parse(result);
        JSONObject json = (JSONObject) obj;

        JSONArray episodes = (JSONArray) json.get("episodes");
        int number_episodes = episodes.size();
        String air_date = (String) json.get("air_date");
        String img_path = "https://image.tmdb.org/t/p/original" + json.get("poster_path");

        Object[] objs = {tvshowID, season_number, number_episodes, air_date, img_path};
        generatedID = ConnectionFactory.getInstance().insertAndReturnId(SQLquerys.getQuery(SQLcmd.Admin_insert_season), objs);

        Iterator<JSONObject> it = episodes.iterator();
        while (it.hasNext()) {
            JSONObject o = it.next();
            insertNewEpisode(generatedID, tvshowID, o);
        }
    }

    private void insertNewEpisode(long seasonID, long tvshowID, JSONObject json) throws SQLException {
        String title = (String) json.get("name");
        String overview = (String) json.get("overview");
        long episode_number = json.get("episode_number") != null ? (Long) json.get("episode_number") : -1;
        String air_date = (String) json.get("air_date");
        String img_path = "https://image.tmdb.org/t/p/original" + json.get("still_path");

        Object[] objs = {tvshowID, seasonID, title, overview, episode_number, air_date, img_path};
        ConnectionFactory.getInstance().update(SQLquerys.getQuery(SQLcmd.Admin_insert_episode), objs);
    }

    private String makeRequest(String targetURL) {
        String result = "";
        try {
            URL url = new URL(targetURL);
            URLConnection mdb = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(mdb.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                result += inputLine;
            }
            in.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private String arrayToString(JSONArray array) {
        String tmp = "";
        for (Object obj : array) {
            tmp += ((JSONObject) obj).get("name") + "; ";
        }
        if (tmp.length() < 2) {
            return tmp;
        }
        return tmp.substring(0, tmp.length() - 2);
    }

    private boolean insertGenres() {
        boolean success = true;
        String result = makeRequest("https://api.themoviedb.org/3/genre/list?api_key=" + api_key);
        try {
            Object obj = parser.parse(result);
            JSONObject json = (JSONObject) obj;
            JSONArray genres = (JSONArray) json.get("genres");

            Iterator<JSONObject> it = genres.iterator();
            while (it.hasNext()) {
                JSONObject genre = it.next();

                String name = (String) genre.get("name");

                Object[] objs = {name};
                ConnectionFactory.getInstance().update(SQLquerys.getQuery(SQLcmd.Admin_insert_genre), objs);
            }
        } catch (ParseException | SQLException ex) {
            ex.printStackTrace();
            success = false;
        }
        return success;
    }

    private void insertPopularShows() {
        String result = makeRequest("https://api.themoviedb.org/3/discover/tv?sort_by=popularity.desc&api_key=" + api_key);
        try {
            Object obj = parser.parse(result);
            JSONObject json = (JSONObject) obj;

            JSONArray results = (JSONArray) json.get("results");
            Iterator<JSONObject> it = results.iterator();
            System.out.println("---------- INSERTING POPULAR SHOWS ----------");
            while (it.hasNext()) {
                JSONObject o = it.next();
                System.out.println("| Inserting " + o.get("name"));
                if (insertNewTvShow("" + o.get("id"))) {
                    ConnectionFactory.getInstance().commit();
                    System.out.println("| Status: success");
                } else {
                    System.out.println("| Status: failed");
                    ConnectionFactory.getInstance().rollback();
                }
                System.out.println("---------------------------------------------");
            }
            System.out.println("----------------- FINISHED ------------------\n");
        } catch (ParseException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void associateGenresWithShow(long id, JSONArray genres) throws SQLException {
        Iterator<JSONObject> it = genres.iterator();
        while (it.hasNext()) {
            JSONObject obj = it.next();
            Object[] objs = {((String) obj.get("name"))};
            ResultSet rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.Admin_select_genre), objs);
            if (rs.next()) {
                int id_genre = rs.getInt("ID_Genre");
                Object[] objs2 = {id_genre, id};
                ConnectionFactory.getInstance().update(SQLquerys.getQuery(SQLcmd.Admin_associate_genre), objs2);
            }
        }
    }

}
