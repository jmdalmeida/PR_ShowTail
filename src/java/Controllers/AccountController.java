package Controllers;

import JDBC.ConnectionFactory;
import Utils.Data.MyShow;
import Utils.Data.Show;
import Utils.Data.UserData;
import Utils.SQL.SQLcmd;
import Utils.SQL.SQLquerys;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/AccountController"})
public class AccountController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Pages toPage = Pages.INDEX;
        RequestDispatcher rd = null;
        UserData user = null;
        PrintWriter out = response.getWriter();
        ConnectionFactory.getInstance().init();
        String action = request.getParameter("action");
        String fromPage = "";
        String username = "";
        switch (action) {
            case "login":
                username = request.getParameter("username");
                String password = request.getParameter("password");
                boolean remember = request.getParameter("chkRemember") != null;
                String hashedPassword = encryptPassword(password);

                if (attemptLogin(username, hashedPassword)) {
                    String token = encryptPassword(username + "PR" + hashedPassword);
                    session.setAttribute("user", getUserObject(username));

                    Cookie c1 = new Cookie("username", username);
                    Cookie c2 = new Cookie("token", token);
                    c2.setMaxAge(remember ? 12 * 31 * 24 * 60 * 60 : -1);
                    c1.setMaxAge(remember ? 12 * 31 * 24 * 60 * 60 : -1);
                    response.addCookie(c1);
                    response.addCookie(c2);
                    toPage = Pages.NULL;
                } else {
                    request.getSession().setAttribute("failedlogin", "Invalid username/password.");
                    toPage = Pages.INDEX;
                }
                break;
            case "signUp":
                String nameR = request.getParameter("name");
                String usernameR = request.getParameter("username");
                String passwordR = request.getParameter("password");
                String emailR = request.getParameter("email");
                String dataNascimentoR = request.getParameter("dataNascimento");
                String hashedPasswordR = encryptPassword(passwordR);
                if (attemptUserCreation(usernameR, nameR, emailR, dataNascimentoR, hashedPasswordR)) {
                    String tokenR = encryptPassword(usernameR + "PR" + hashedPasswordR);
                    session.setAttribute("user", getUserObject(usernameR));
                    Cookie c1 = new Cookie("username", usernameR);
                    Cookie c2 = new Cookie("token", tokenR);
                    c2.setMaxAge(-1);
                    c1.setMaxAge(-1);
                    response.addCookie(c1);
                    response.addCookie(c2);

                    response.sendRedirect("index.jsp");
                } else {
                    toPage = Pages.INDEX;
                }
                break;
            case "UpdateUser":
                user = (UserData) session.getAttribute("user");
                username = user.getUsername();
                String nameU = request.getParameter("nomeEdit");
                String emailU = request.getParameter("emailEdit");
                attemptUserUpdate(nameU, emailU, username);
                response.sendRedirect("profile.jsp");
                break;
            case "DeleteUser":
                user = (UserData) session.getAttribute("user");
                username = user.getUsername();
                attemptDeleteUser(username);
                toPage = Pages.INDEX;
                break;
            case "Validation":
                username = (String) request.getParameter("username");
                String token = (String) request.getParameter("token");
                fromPage = (String) request.getParameter("fromPage");
                boolean valid = validateUser(username, token);
                if (valid) {
                    session.setAttribute("valid_user", true);
                    session.setAttribute("user", getUserObject(username));
                } else {
                    session.setAttribute("valid_user", false);
                }
                toPage = Pages.FROMPAGE;
                break;
            case "Profile":
                int id_user = ((UserData) session.getAttribute("user")).getId();
                ArrayList<Show> shows = new ArrayList<Show>();
                try {
                    Object[] objs = {id_user};
                    ResultSet rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.Account_followed_shows) + " LIMIT 4", objs);
                    while (rs.next()) {
                        shows.add(new Show(rs.getInt("ID_Show"), rs.getInt("Followers"), rs.getInt("Episodes"), rs.getString("Title"),
                                rs.getString("Image_Path"), "", "", "", 0.0, false));
                    }

                    session.setAttribute("array_shows_followed", shows);

                    toPage = Pages.PROFILE;
                } catch (SQLException ex) {
                    toPage = Pages.NULL;
                }
                break;
            case "MyShows":
                user = (UserData) session.getAttribute("user");
                ArrayList<MyShow> shows1 = new ArrayList<MyShow>();
                try {
                    Object[] objs = {user.getId()};
                    ResultSet rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.Account_followed_shows), objs);
                    while (rs.next()) {
                        int id = rs.getInt("ID_Show");
                        int count = getUnwatchedCount(id, user.getId());
                        int watchableEpisodes = getWatchableCount(id);
                        shows1.add(new MyShow(id, count, watchableEpisodes, rs.getString("Title"), rs.getString("Image_Path")));
                    }

                    session.setAttribute("array_shows_followed", shows1);

                    toPage = Pages.MYSHOWS;
                } catch (SQLException ex) {
                    toPage = Pages.NULL;
                }
                break;
            case "Validate":
                String validateUsername = request.getParameter("usernameV");
                String validateEmail = request.getParameter("emailV");
                boolean checkValidationUsername =false;
                boolean checkValidationEmail = false;
                try {
                    Object[] objs = {validateUsername};
                    ResultSet rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.Account_validate_username), objs);
                    if(rs.next()) {
                        checkValidationUsername= true;
                    }
                    Object[] objs2 = {validateEmail};
                    ResultSet rs2 = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.Account_validate_email), objs2);
                    if(rs2.next()) {
                        checkValidationEmail= true;
                    }
                    String checked = checkValidationUsername + ";" + checkValidationEmail;
                    out.print(checked);
                    out.flush();
                    System.out.println(checked);
                }catch(SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            default:
                break;
        }
        ConnectionFactory.getInstance().close();

        if (!response.isCommitted()) {
            switch (toPage) {
                case INDEX:
                    rd = request.getRequestDispatcher("/index.jsp");
                    break;
                case PROFILE:
                    rd = request.getRequestDispatcher("/profile.jsp");
                    break;
                case MYSHOWS:
                    rd = request.getRequestDispatcher("/MyShows.jsp");
                    break;
                case FROMPAGE:
                    rd = request.getRequestDispatcher("/" + fromPage);
                    break;
                case NULL:
                    rd = null;
                    response.sendRedirect("index.jsp");
                    break;
                default:
                    rd = request.getRequestDispatcher("/index.jsp");
                    break;
            }
            if (rd != null) {
                rd.forward(request, response);
            }
        }
    }

    private boolean attemptLogin(final String username, final String hashedPassword) {
        boolean success = false;
        try {
            Object[] o = {username, hashedPassword};
            ResultSet rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.Account_check_login), o);
            while (rs.next()) {
                success = true;
                break;
            }
        } catch (SQLException ex) {
            success = false;
        }
        return success;
    }

    private boolean attemptUserCreation(String username, String name, String email, String dataNascimento, String encryptedPassword) {
        boolean success = true;
        int i = (int) (Math.random() * 11);
        Date dataReg = new Date(System.currentTimeMillis());
        String imagePath = "UserAvatars/" + i + ".jpg";
        String account = "N";
        try {
            Object[] o = {name, username, encryptedPassword, email, account, dataNascimento, dataReg, imagePath};
            ConnectionFactory.getInstance().update(SQLquerys.getQuery(SQLcmd.Account_create_user), o);
        } catch (SQLException ex) {
            success = false;
        }
        return success;
    }

    private void attemptUserUpdate(String name, String email, String username) {
        try {
            Object[] o = {name, email, username};
            ConnectionFactory.getInstance().update(SQLquerys.getQuery(SQLcmd.Account_update_user), o);
        } catch (SQLException ex) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void attemptDeleteUser(String username) {
        try {
            Object[] o = {username};
            ConnectionFactory.getInstance().update(SQLquerys.getQuery(SQLcmd.Account_delete_user), o);
        } catch (SQLException ex) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean validateUser(String username, String token) {
        boolean validate = false;
        Object[] o = {username};
        ResultSet rs;
        try {
            rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.Validation_password), o);

            String pw = "";
            if (rs.next()) {
                pw = rs.getString("Password");
            }

            if (!"".equals(pw)) {
                String chkToken = Controllers.AccountController.encryptPassword(username + "PR" + pw);
                validate = chkToken.equals(token);
            }

            rs.close();
        } catch (SQLException ex) {
            validate = false;
            ex.printStackTrace();
        }
        return validate;
    }

    private UserData getUserObject(String username) {
        UserData ud = null;
        try {
            Object[] objs = {username};
            ResultSet rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.Account_user_data), objs);
            if (rs.next()) {
                ud = new UserData(rs.getInt("ID_User"), rs.getString("Username"), rs.getString("Email"),
                        rs.getString("Name"), rs.getDate("Date_of_Birth"), rs.getDate("Date_of_Registration"),
                        rs.getString("Image_Path"), rs.getString("Account_Type"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ud;
    }

    public static String encryptPassword(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (Exception e) {
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private int getUnwatchedCount(int id_show, int id_user) {
        int count = 0;
        Object[] objs = {id_show, id_user};
        try {
            ResultSet rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.MyShows_count_unwatched), objs);
            if(rs.next())
                count = rs.getInt("Unwatched");
            rs.close();
        } catch (SQLException ex) {
        }
        return count;
    }
    
    private int getWatchableCount(int id_show){
        int count = 0;
        Object[] objs = {id_show};
        try {
            ResultSet rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.MyShows_count_watchable), objs);
            if(rs.next())
                count = rs.getInt("Watchable");
            rs.close();
        } catch (SQLException ex) {
        }
        return count;
    }

    private enum Pages {
        INDEX, PROFILE, SEARCH, SHOW, FROMPAGE, MYSHOWS, NULL;
    }
}
