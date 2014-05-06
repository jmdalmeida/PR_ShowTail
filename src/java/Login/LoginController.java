
package Login;

import JDBC.JDBCConnections;
import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet(urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {
    
    public static JDBCConnections jdbc = new JDBCConnections("localhost", "db_pr", "Filipe", "a12345");
    private Date now = new Date();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        
        jdbc.init();
        String action = request.getParameter("action");
        switch (action) {
            case "login":
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                boolean remember = request.getParameter("chkRemember") != null;
                String hashedPassword = encryptPassword(password);
                
                System.out.printf("A fazer login com os dados: ('%s', '%s', '%s')\n", username, password, remember);
                
                if(attemptLogin(username, hashedPassword)){
                    String token = encryptPassword(username + "PR" + hashedPassword);
                    session.setAttribute("username", username);
                    
                    Cookie c1 = new Cookie("username", username);
                    Cookie c2 = new Cookie("token", token);
                    c2.setMaxAge(remember ? 12*31*24*60*60 : -1);
                    c1.setMaxAge(remember ? 12*31*24*60*60 : -1);
                    response.addCookie(c1);
                    response.addCookie(c2);
                    System.out.println("Login bem sucedido.");
                } else {
                    request.getSession().setAttribute("failedlogin", "Invalid username/password.");
                    System.out.println("Login falhou.");
                }
                response.sendRedirect("index.jsp");
                break;
            case "signUp":
                String nameR = request.getParameter("name");
                String usernameR = request.getParameter("username");
                String passwordR = request.getParameter("password");
                String emailR = request.getParameter("email");
                String dataNascimentoR = request.getParameter("dataNascimento");
                String hashedPasswordR = encryptPassword(passwordR);
                attemptUserCreation(usernameR, nameR, emailR, dataNascimentoR, hashedPasswordR);
                String tokenR = encryptPassword(usernameR + "PR" + hashedPasswordR);
                session.setAttribute("username", usernameR);
                Cookie c1 = new Cookie("username", usernameR);
                Cookie c2 = new Cookie("token", tokenR);
                c2.setMaxAge(-1);
                c1.setMaxAge(-1);
                response.addCookie(c1);
                response.addCookie(c2);
                response.sendRedirect("profile.jsp");
                break;
            case "UpdateUser":
                username = (String) session.getAttribute("username");
                String nameU = request.getParameter("nomeEdit");
                String emailU = request.getParameter("emailEdit");
                attemptUserUpdate(nameU, emailU, username);
                response.sendRedirect("profile.jsp");
                break;
            case "DeleteUser":
                username = (String) session.getAttribute("username");
                attemptDeleteUser(username);
                response.sendRedirect("index.jsp");
                break;
            default:
                break;
        }
        out.close();
        jdbc.close();
    }
    
    private boolean attemptLogin(final String username, final String hashedPassword) {
        boolean success = false;
        try {
            Object[] o = {username, hashedPassword};
            ResultSet rs = jdbc.select("SELECT * FROM user WHERE Username = ? AND Password =?;", o);
            while(rs.next()){
                success = true;
                break;
            }
        } catch (SQLException ex) {
            success = false;
        }
        return success;
    }
    
    private void attemptUserCreation(String username, String name, String email, String dataNascimento, String encryptedPassword) {
        int i = (int)(Math.random()*11);
        Date dataReg =  new Date(System.currentTimeMillis());
        String imagePath = "UserAvatars/" + i + ".jpg";
        String account = "N";
        try {
            Object [] o ={name, username, encryptedPassword, email, account ,dataNascimento, dataReg, imagePath};
            jdbc.update("INSERT INTO user (Name, Username, Password, Email, Account_Type, Date_of_Birth, Date_of_Registration ,Image_Path) VALUES (?, ?, ?, ?, ?, ?, ?, ?);", o);
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void attemptUserUpdate(String name, String email, String username) {
        try {
            Object [] o = {name, email, username};
            jdbc.update("UPDATE user SET Name=?, Email=? where Username=?;", o);
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void attemptDeleteUser(String username) {
        try {
            Object[] o= {username};
            jdbc.update("DELETE FROM user WHERE Username=?;", o);
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
}
