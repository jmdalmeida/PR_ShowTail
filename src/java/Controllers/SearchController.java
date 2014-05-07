package Controllers;

import JDBC.ConnectionFactory;
import Utils.Genre;
import Utils.SQLcmd;
import Utils.SQLquerys;
import Utils.Show;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "SearchController", urlPatterns = {"/SearchController"})
public class SearchController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        RequestDispatcher rd = null;
        ResultSet rs = null;
        boolean success = true;

        String searchFor = request.getParameter("SearchFor");

        ArrayList<Genre> genres = new ArrayList<Genre>();
        ArrayList<Show> shows = new ArrayList<Show>();

        ConnectionFactory.getInstance().init();
        try {
            //Load genres
            rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.TVShows_genres), null);
            while (rs.next()) {
                genres.add(new Genre(rs.getInt("ID_Genre"), rs.getString("genre")));
            }
            
            //Load shows
            if("Query".equals(searchFor)){
                String query = request.getParameter("Query");
                Object[] objs = {("%" + query + "%")};
                rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.TVShows_search), objs);
            } else if ("Order".equals(searchFor)) {
                String orderBy = request.getParameter("OrderBy");
                if("All".equals(orderBy)){
                    rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.TVShows_order_all), null);
                } else if("MostFollowed".equals(orderBy)){
                    rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.TVShows_order_followed), null);
                } else if("Recommended".equals(orderBy)){
                    rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.TVShows_order_recommended), null);
                } else {
                    rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.TVShows_order_all), null);
                }
            } else if("Genre".equals(searchFor)){
                String genre = (String)request.getParameter("Genre");
                Object[] objs = {Integer.parseInt(genre)};
                rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.TVShows_search_by_genre), objs);
            } else {
                rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.TVShows_order_all), null);
            }

            while (rs.next()) {
                shows.add(new Show(rs.getInt("ID_Show"), rs.getInt("Followers"), rs.getInt("Episodes"),
                                   rs.getString("Title"), rs.getString("Image_Path"), "", "", "", 0.0));
            }
            session.setAttribute("genres_array", genres);
            session.setAttribute("shows_array", shows);

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            success = false;
        }
        ConnectionFactory.getInstance().close();
        rd = success ? request.getRequestDispatcher("/TV-Shows.jsp") : request.getRequestDispatcher("/index.jsp");
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

}
