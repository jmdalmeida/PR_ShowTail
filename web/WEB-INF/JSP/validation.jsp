<%@page import="JDBC.ConnectionFactory"%>
<%@page import="java.sql.ResultSet"%>
<%
    String actualPage = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length());
    if ("/".equals(actualPage)) {
        actualPage += "index.jsp";
    }
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
        if (session.getAttribute("valid_user") != null) {
            loggedin = (boolean) session.getAttribute("valid_user");
            session.removeAttribute("valid_user");
        } else {
%>
<jsp:forward page="AccountController" >
    <jsp:param name="action" value="Validation" />
    <jsp:param name="username" value="<%=username%>" />
    <jsp:param name="token" value="<%=token%>" />
    <jsp:param name="fromPage" value="<%=actualPage%>" />
</jsp:forward>
<%
        }
    }
%>
