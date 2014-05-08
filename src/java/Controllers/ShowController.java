/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import JDBC.ConnectionFactory;
import Utils.SQLcmd;
import Utils.SQLquerys;
import Utils.Season;
import Utils.Show;
import Utils.UserData;
import java.io.IOException;
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        RequestDispatcher rd = null;
        String process = (String) request.getParameter("Process");
        boolean success = false;
        ConnectionFactory.getInstance().init();
        if ("Show".equals(process)) {
            Show show = null;
            ArrayList<Season> seasons = new ArrayList<Season>();
            ConnectionFactory.getInstance().init();
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Object[] o = {id};
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
                }
                if (success) {
                    session.setAttribute("obj_show", show);
                    session.setAttribute("seasons_array", seasons);

                    if ((UserData) session.getAttribute("user") != null) {
                        int id_user = ((UserData) session.getAttribute("user")).getId();
                        session.setAttribute("following", checkFollowsShow(id_user, show.getId()));
                    }

                    rd = request.getRequestDispatcher("/ShowTemplate.jsp");
                }
            } catch (SQLException ex) {
                success = false;
            }
        } else if ("Follow".equals(process)) {
            int id_user = Integer.parseInt(request.getParameter("ID_User"));
            int id_show = Integer.parseInt(request.getParameter("ID_Show"));
            Object[] objs = {id_user, id_show};
            try {
                ConnectionFactory.getInstance().update(SQLquerys.getQuery(SQLcmd.ShowTemplate_show_follow), objs);
                rd = request.getRequestDispatcher("/ShowTemplate.jsp");
            } catch (SQLException ex) {
                success = false;
            }
        } else if ("Unfollow".equals(process)) {
            int id_user = Integer.parseInt(request.getParameter("ID_User"));
            int id_show = Integer.parseInt(request.getParameter("ID_Show"));
            Object[] objs = {id_user, id_show};
            try {
                ConnectionFactory.getInstance().update(SQLquerys.getQuery(SQLcmd.ShowTemplate_show_unfollow), objs);
                rd = request.getRequestDispatcher("/ShowTemplate.jsp");
            } catch (SQLException ex) {
                success = false;
            }
        } else if ("Episodes".equals(process)) {
            int id_season = Integer.parseInt(request.getParameter("ID_Season"));
        }

        if (!success) {
            rd = request.getRequestDispatcher("/index.jsp");
        }
        ConnectionFactory.getInstance().close();
        rd.forward(request, response);
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
        } catch (SQLException ex) {
            follows = false;
        }
        return follows;
    }

}
