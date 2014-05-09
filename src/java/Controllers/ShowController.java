/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import JDBC.ConnectionFactory;
import Utils.Episode;
import Utils.SQLcmd;
import Utils.SQLquerys;
import Utils.Season;
import Utils.Show;
import Utils.UserData;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        int id_show = Integer.parseInt(request.getParameter("ID_Show"));
        boolean success = false;
        boolean loggedIn = (UserData) session.getAttribute("user") != null;
        ConnectionFactory.getInstance().init();
        if ("Show".equals(process)) {
            Show show = null;
            ArrayList<Season> seasons = new ArrayList<Season>();
            try {
                Object[] o = {id_show};
                ResultSet rs_show = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.ShowTemplate_show_info), o);
                if (rs_show.next()) {
                    show = new Show(Integer.parseInt(rs_show.getString("ID_Show")), 0, rs_show.getInt("Episodes"), rs_show.getString("Title"),
                                    rs_show.getString("Image_Path"), rs_show.getString("Overview"), rs_show.getString("Status"), rs_show.getString("First_Air_Date"), rs_show.getDouble("Rating"));

                    Object[] o2 = {show.getId()};
                    ResultSet rs_seasons = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.ShowTemplate_show_seasons), o2);
                    while (rs_seasons.next()) {
                        seasons.add(new Season(Integer.parseInt(rs_seasons.getString("ID_Season")), Integer.parseInt(rs_seasons.getString("Season_Number"))));
                    }
                    success = true;
                    rs_seasons.close();
                }
                if (success) {
                    session.setAttribute("obj_show", show);
                    session.setAttribute("seasons_array", seasons);

                    if ((UserData) session.getAttribute("user") != null) {
                        int id_user = ((UserData) session.getAttribute("user")).getId();
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
            int id_user = Integer.parseInt(request.getParameter("ID_User"));
            Object[] objs = {id_user, id_show};
            try {
                ConnectionFactory.getInstance().update(SQLquerys.getQuery(SQLcmd.ShowTemplate_show_follow), objs);
                rd = request.getRequestDispatcher("/ShowTemplate.jsp");
            } catch (SQLException ex) {
                success = false;
            }
        } else if ("Unfollow".equals(process)) {
            int id_user = Integer.parseInt(request.getParameter("ID_User"));
            Object[] objs = {id_user, id_show};
            try {
                ConnectionFactory.getInstance().update(SQLquerys.getQuery(SQLcmd.ShowTemplate_show_unfollow), objs);
                rd = request.getRequestDispatcher("/ShowTemplate.jsp");
            } catch (SQLException ex) {
                success = false;
            }
        } else if ("Episodes".equals(process)) {
            int id_season = Integer.parseInt(request.getParameter("ID_Season"));
            ArrayList<Episode> episodes = new ArrayList<Episode>();
            try {
                int season_number = 0;
                int id_user = 0;
                if (loggedIn) {
                    id_user = ((UserData) session.getAttribute("user")).getId();
                }

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
                                             loggedIn ? checkSeenEpisode(id_show, id_season, id_episode, id_user) : false));
                }
                success = true;
                if (loggedIn) {
                    session.setAttribute("following", checkFollowsShow(id_user, id_show));
                }
                session.setAttribute("episodes_array", episodes);
                rs_episodes.close();
                rs_season.close();

                rd = request.getRequestDispatcher("/Show.jsp");
            } catch (SQLException e) {
                success = false;
            }
        } else if ("Rate".equals(process)) {
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

            SQLcmd cmd = seen ? SQLcmd.Show_set_episode_seen : SQLcmd.Show_set_episode_unseen;
            Object[] objs = {id_show, id_season, id_episode, id_user};
            try {
                ConnectionFactory.getInstance().update(SQLquerys.getQuery(cmd), objs);
            } catch (SQLException ex) {
            }
        }

        if (!success) {
            rd = request.getRequestDispatcher("/index.jsp");
        }
        ConnectionFactory.getInstance().close();
        if (rd != null) {
            rd.forward(request, response);
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
            if (rs.next()) {
                check = true;
            }
            rs.close();
        } catch (SQLException ex) {
        }
        return check;
    }

}
