<%@page import="JDBC.ConnectionFactory"%>
<%@page import="java.sql.ResultSet"%>
<%
    boolean loggedin = false;
    String username = "";
    String token = "";

    if (request.getCookies() != null) {
        for (Cookie c : request.getCookies()) {
            if ("username".equals(c.getName())) {
                username = c.getValue();
            }
            if ("token".equals(c.getName())) {
                token = c.getValue();
            }
        }
    }
    if (username != "" && token != "") {
        ConnectionFactory.getInstance().init();
        Object[] o = {username};
        ResultSet rs = ConnectionFactory.getInstance().select("SELECT Password FROM user WHERE Username = ?", o);
        String pw = "";
        while (rs.next()) {
            pw = rs.getString("Password");
        }

        if (pw != "") {
            String chkToken = Controllers.LoginController.encryptPassword(username + "PR" + pw);
            if (chkToken.equals(token)) {
                loggedin = true;
                session.setAttribute("username", username);
            } else {
                session.invalidate();
            }
        }

        ConnectionFactory.getInstance().close();
    }
%>
