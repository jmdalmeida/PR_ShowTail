package Controllers;

import JDBC.ConnectionFactory;
import Utils.SQLcmd;
import Utils.SQLquerys;
import Utils.UserData;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        ConnectionFactory.getInstance().init();
        String action = request.getParameter("action");
        String fromPage = "";
        switch (action) {
            case "login":
                String username = request.getParameter("username");
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
                    System.out.println("Login bem sucedido.");
                } else {
                    request.getSession().setAttribute("failedlogin", "Invalid username/password.");
                    System.out.println("Login falhou.");
                }
                toPage = Pages.INDEX;
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

                    toPage = Pages.PROFILE;
                } else {
                    //Tratar o caso de erro na criação do user aqui

                    toPage = Pages.INDEX;
                }
                break;
            case "UpdateUser":
                user = (UserData) session.getAttribute("user");
                username = user.getUsername();
                String nameU = request.getParameter("nomeEdit");
                String emailU = request.getParameter("emailEdit");
                attemptUserUpdate(nameU, emailU, username);
                toPage = Pages.PROFILE;
                break;
            case "DeleteUser":
                user = (UserData) session.getAttribute("user");
                username = user.getUsername();
                attemptDeleteUser(username);
                toPage = Pages.INDEX;
                break;
            case "Validation":
                user = (UserData) session.getAttribute("user");
                username = user.getUsername();
                String token = (String) request.getParameter("token");
                fromPage = (String) request.getParameter("fromPage");
                boolean valid = validateUser(username, token);
                if (valid) {
                    session.setAttribute("valid_user", true);
                    session.setAttribute("user", getUserObject(username));
                } else {
                    session.removeAttribute("user");
                    session.setAttribute("valid_user", false);
                }
                toPage = Pages.FROMPAGE;
                break;
            default:
                break;
        }
        ConnectionFactory.getInstance().close();

        switch (toPage) {
            case INDEX:
                rd = request.getRequestDispatcher("/index.jsp");
                break;
            case PROFILE:
                rd = request.getRequestDispatcher("/profile.jsp");
                break;
            case FROMPAGE:
                rd = request.getRequestDispatcher("/" + fromPage);
                break;
            default:
                rd = request.getRequestDispatcher("/index.jsp");
                break;
        }
        if (rd != null) {
            rd.forward(request, response);
        }
    }

    private boolean attemptLogin(final String username, final String hashedPassword) {
        boolean success = false;
        try {
            Object[] o = {username, hashedPassword};
            ResultSet rs = ConnectionFactory.getInstance().select("SELECT * FROM user WHERE Username = ? AND Password =?;", o);
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
            ConnectionFactory.getInstance().update("INSERT INTO user (Name, Username, Password, Email, Account_Type, Date_of_Birth, Date_of_Registration ,Image_Path) VALUES (?, ?, ?, ?, ?, ?, ?, ?);", o);
        } catch (SQLException ex) {
            success = false;
        }
        return success;
    }

    private void attemptUserUpdate(String name, String email, String username) {
        try {
            Object[] o = {name, email, username};
            ConnectionFactory.getInstance().update("UPDATE user SET Name=?, Email=? where Username=?;", o);
        } catch (SQLException ex) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void attemptDeleteUser(String username) {
        try {
            Object[] o = {username};
            ConnectionFactory.getInstance().update("DELETE FROM user WHERE Username=?;", o);
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
            while (rs.next()) {
                pw = rs.getString("Password");
            }

            if (!"".equals(pw)) {
                String chkToken = Controllers.AccountController.encryptPassword(username + "PR" + pw);
                validate = chkToken.equals(token);
            }
        } catch (SQLException ex) {
            validate = false;
        }
        return validate;
    }

    private UserData getUserObject(String username) {
        UserData ud = null;
        try {
            Object[] objs = {username};
            ResultSet rs = ConnectionFactory.getInstance().select(SQLquerys.getQuery(SQLcmd.Account_UserData), objs);
            if (rs.next()) {
                ud = new UserData(rs.getInt("ID_User"), rs.getString("Username"), rs.getString("Email"),
                        rs.getString("Name"), "", "", "", "", rs.getString("Account_Type"));
            }
            System.out.println("Created user obj: " + ud);
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

    private enum Pages {

        INDEX, PROFILE, SEARCH, SHOW, FROMPAGE;
    }
}
