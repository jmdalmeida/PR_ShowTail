/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import JDBC.ConnectionFactory;
import Utils.Data.Comment;
import Utils.Data.Episode;
import Utils.Data.IndexShow;
import Utils.Data.Season;
import Utils.Data.Show;
import Utils.Data.UserData;
import Utils.SQL.SQLcmd;
import Utils.SQL.SQLquerys;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ShowController", urlPatterns = {"/ShowController"})
public class ShowController extends HttpServlet {

    /*
     OBRIGATORIO: passar como parametro sempre pelo menos o Process e o ID_Show
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        RequestDispatcher rd = null;
        String process = (String) request.getParameter("Process");
        int id_show = request.getParameter("ID_Show") != null ? Integer.parseInt(request.getParameter("ID_Show")) : 0;
        boolean success = false;
        boolean isAJAX = false;
        UserData user = (UserData) session.getAttribute("user");
        boolean loggedIn = user != null;
        ConnectionFactory.getInstance().init();
        if ("Show".equals(process)) {
            Show show = null;
            ArrayList<Season> seasons = new ArrayList<Season>();
            ArrayList<Comment> comments = new ArrayList<Comment>();
            try {
                Object[] o = {id_show};
                ResultSet rs_show = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.ShowTemplate_show_info), o);
                if (rs_show.next()) {
                    //Gather show info
                    show = new Show(Integer.parseInt(rs_show.getString("ID_Show")), 0, rs_show.getInt("Episodes"), rs_show.getString("Title"),
                                    rs_show.getString("Image_Path"), rs_show.getString("Overview"), rs_show.getString("Status"), rs_show.getString("First_Air_Date"), rs_show.getDouble("Rating"),
                                    loggedIn ? checkFollowsShow(user.getId(), id_show) : false);

                    //Gather seasons
                    Object[] o2 = {show.getId()};
                    ResultSet rs_seasons = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.ShowTemplate_show_seasons), o2);
                    while (rs_seasons.next()) {
                        seasons.add(new Season(Integer.parseInt(rs_seasons.getString("ID_Season")), Integer.parseInt(rs_seasons.getString("Season_Number"))));
                    }
                    rs_seasons.close();

                    //Gather comments
                    ResultSet rs_comments = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.ShowTemplate_get_comments), o2);
                    while (rs_comments.next()) {
                        Comment c = new Comment(rs_comments.getInt("ID_Comment"), rs_comments.getInt("ID_User"),
                                                rs_comments.getString("Username"), rs_comments.getString("Image_Path"),
                                                rs_comments.getString("Comment"), rs_comments.getTimestamp("Timestamp"));
                        comments.add(c);
                    }
                    rs_comments.close();

                    success = true;
                }
                if (success) {
                    session.setAttribute("obj_show", show);
                    session.setAttribute("seasons_array", seasons);
                    session.setAttribute("comments_array", comments);

                    if (loggedIn) {
                        int id_user = user.getId();
                        session.setAttribute("following", checkFollowsShow(id_user, show.getId()));
                        session.setAttribute("rate", getRateShow(id_user, show.getId()));
                    }

                    rd = request.getRequestDispatcher("/ShowTemplate.jsp");
                }
                rs_show.close();
            } catch (SQLException ex) {
                success = false;
            }
        } else if ("Follow".equals(process)) {
            isAJAX = true;
            int id_user = Integer.parseInt(request.getParameter("ID_User"));
            Object[] objs = {id_user, id_show};
            try {
                ConnectionFactory.getInstance().update(SQLquerys.getQuery(SQLcmd.ShowTemplate_show_follow), objs);
            } catch (SQLException ex) {
                success = false;
            }
        } else if ("Unfollow".equals(process)) {
            isAJAX = true;
            int id_user = Integer.parseInt(request.getParameter("ID_User"));
            Object[] objs = {id_user, id_show};
            try {
                ConnectionFactory.getInstance().update(SQLquerys.getQuery(SQLcmd.ShowTemplate_show_unfollow), objs);
            } catch (SQLException ex) {
                success = false;
            }
        } else if ("Episodes".equals(process)) {
            int id_season = Integer.parseInt(request.getParameter("ID_Season"));
            ArrayList<Episode> episodes = new ArrayList<Episode>();
            try {
                int season_number = 0;

                Object[] objs = {id_season};
                ResultSet rs_season = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.Show_get_season), objs);
                while (rs_season.next()) {
                    season_number = Integer.parseInt(rs_season.getString("Season_Number"));
                }
                ResultSet rs_episodes = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.Show_get_episodes), objs);
                while (rs_episodes.next()) {
                    int id_episode = Integer.parseInt(rs_episodes.getString("ID_Episode"));
                    episodes.add(new Episode(id_episode, Integer.parseInt(rs_episodes.getString("Episode_Number")), season_number,
                                             rs_episodes.getString("Title"), rs_episodes.getString("Overview"),
                                             getDateObject(rs_episodes.getString("Air_Date")),
                                             loggedIn ? checkSeenEpisode(id_show, id_season, id_episode, user.getId()) : false));
                }
                success = true;
                if (loggedIn) {
                    session.setAttribute("following", checkFollowsShow(user.getId(), id_show));
                }
                session.setAttribute("episodes_array", episodes);
                rs_episodes.close();
                rs_season.close();

                rd = request.getRequestDispatcher("/Show.jsp");
            } catch (SQLException e) {
                success = false;
            }
        } else if ("Rate".equals(process)) {
            isAJAX = true;
            int id_user = Integer.parseInt(request.getParameter("ID_User"));
            int rate = Integer.parseInt(request.getParameter("Rate"));
            Object[] params = {id_user, id_show};
            try {
                ResultSet rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.ShowTemplate_show_rate_select), params);
                if (rs.next()) {
                    Object[] objs = {rate, id_user, id_show};
                    ConnectionFactory.getInstance().update(SQLquerys.getQuery(SQLcmd.ShowTemplate_show_rate_update), objs);
                } else {
                    Object[] objs = {id_user, id_show, rate};
                    ConnectionFactory.getInstance().update(SQLquerys.getQuery(SQLcmd.ShowTemplate_show_rate_insert), objs);
                }

                double new_rating = 0.0;
                Object[] params2 = {id_show};
                ResultSet rs_new_rating = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.ShowTemplate_show_info), params2);
                if (rs_new_rating.next()) {
                    new_rating = rs_new_rating.getDouble("Rating");
                }

                out.print(new_rating);
                out.flush();

                rs.close();
                rs_new_rating.close();

                success = true;
            } catch (SQLException ex) {
                success = false;
            }
        } else if ("SeenStatus".equals(process)) {
            int id_user = Integer.parseInt(request.getParameter("ID_User"));
            int id_episode = Integer.parseInt(request.getParameter("ID_Episode"));
            int id_season = Integer.parseInt(request.getParameter("ID_Season"));
            boolean seen = Boolean.parseBoolean(request.getParameter("SeenStatus"));

            if (checkFollowsShow(id_user, id_show)) {
                SQLcmd cmd = seen ? SQLcmd.Show_set_episode_seen : SQLcmd.Show_set_episode_unseen;
                Object[] objs = {id_show, id_season, id_episode, id_user};
                try {
                    ConnectionFactory.getInstance().update(SQLquerys.getQuery(cmd), objs);
                } catch (SQLException ex) {
                }
            }
        } else if ("Mark".equals(process)) {
            isAJAX = true;
            int id_user = Integer.parseInt(request.getParameter("ID_User"));
            int number_season = Integer.parseInt(request.getParameter("Number_Season"));
            int id_season = getSeasonID(id_show, number_season);
            String action = request.getParameter("Action");

            if (checkFollowsShow(id_user, id_show)) {
                switch (action) {
                    case "seasonSeen":
                        setSeasonStatus(id_user, id_season, false);
                        setSeasonStatus(id_user, id_season, true);
                        break;
                    case "seasonUnseen":
                        setSeasonStatus(id_user, id_season, false);
                        break;
                    case "showSeen":
                        setShowStatus(id_user, id_show, false);
                        setShowStatus(id_user, id_show, true);
                        break;
                    case "showUnseen":
                        setShowStatus(id_user, id_show, false);
                        break;
                    default:
                        break;
                }
            }
            success = true;
        } else if ("PopularShows".equals(process)) {
            ArrayList<IndexShow> shows = new ArrayList<IndexShow>();
            try {
                ResultSet rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.TVShows_order_followed) + " LIMIT 6", null);
                while (rs.next()) {
                    IndexShow is = new IndexShow(rs.getInt("ID_Show"), rs.getString("Backdrop_Image_Path"), rs.getString("Title"));
                    shows.add(is);
                }
            } catch (SQLException ex) {
            }
            session.setAttribute("array_popular_shows", shows);
            rd = request.getRequestDispatcher("/index.jsp");
        } else if ("Comment".equals(process)) {
            isAJAX = true;
            if (loggedIn) {
                String comment = (String) request.getParameter("Comment");
                Object[] objs = {id_show, user.getId(), comment};
                try {
                    long id = ConnectionFactory.getInstance().insertAndReturnId(SQLquerys.getQuery(SQLcmd.ShowTemplate_new_comment), objs);
                    String newCommentDiv = "<div id=\"comment" + id + "\" class=\"comment\">"
                                           + "<div id=\"image\">"
                                           + "<img src=\"" + user.getPathImagem() + "\" />"
                                           + "</div>"
                                           + "<span class=\"user_span\">" + user.getUsername() + " </span>"
                                           + "<span class=\"comment_span\">" + comment + "</span>"
                                           + "</div>";
                    out.print(newCommentDiv);
                    out.flush();
                    success = true;
                } catch (SQLException ex) {
                    success = false;
                }
            }
        }

        ConnectionFactory.getInstance().close();
        if (!isAJAX) {
            if (!success) {
                response.sendRedirect("index.jsp");
            }
            if (!response.isCommitted() && rd == null) {
                rd = request.getRequestDispatcher("/index.jsp");
            }
            if (!response.isCommitted() && rd != null) {
                rd.forward(request, response);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private boolean checkFollowsShow(int id_user, int id_show) {
        boolean follows = false;
        try {
            Object[] objs = {id_user, id_show};
            ResultSet rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.ShowTemplate_show_check_follows), objs);
            if (rs.next()) {
                follows = "YES".equals(rs.getString("Follows"));
            }
            rs.close();
        } catch (SQLException ex) {
            follows = false;
        }
        return follows;
    }

    private int getRateShow(int id_user, int id_show) {
        int rate = 0;
        try {
            Object[] objs = {id_user, id_show};
            ResultSet rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.ShowTemplate_show_rate_select), objs);
            if (rs.next()) {
                rate = rs.getInt("Rating");
            }
            rs.close();
        } catch (SQLException ex) {
        }
        return rate;
    }

    private boolean checkSeenEpisode(int id_show, int id_season, int id_episode, int id_user) {
        boolean check = false;
        Object[] objs = {id_show, id_season, id_episode, id_user};
        try {
            ResultSet rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.Show_get_episode_seen), objs);
            check = rs.next();
            rs.close();
        } catch (SQLException ex) {
        }
        return check;
    }

    private Date getDateObject(String string) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(string);
            return date;
        } catch (Exception ex) {
            return null;
        }
    }

    private boolean setSeasonStatus(int id_user, int id_season, boolean seen) {
        boolean success = true;
        Object[] objs = {id_season, id_user};
        try {
            ConnectionFactory.getInstance().update(SQLquerys.getQuery(seen ? SQLcmd.Show_set_season_seen : SQLcmd.Show_set_season_unseen), objs);
        } catch (SQLException ex) {
            success = false;
        }
        return success;
    }

    private boolean setShowStatus(int id_user, int id_show, boolean seen) {
        boolean success = true;
        Object[] objs = {id_show, id_user};
        try {
            ConnectionFactory.getInstance().update(SQLquerys.getQuery(seen ? SQLcmd.Show_set_show_seen : SQLcmd.Show_set_show_unseen), objs);
        } catch (SQLException ex) {
            success = false;
        }
        return success;
    }

    private int getSeasonID(int id_show, int season_number) {
        int id = 0;
        Object[] objs = {id_show, season_number};
        try {
            ResultSet rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.Show_get_season_id), objs);
            if (rs.next()) {
                id = rs.getInt("ID_Season");
            }
        } catch (SQLException ex) {
        }
        return id;
    }

}
