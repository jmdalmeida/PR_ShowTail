<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" class="User.UserData" scope="session"/> 
<jsp:setProperty name="user" property="*"/>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Save Info</title>
    </head>
    <body>
        <% response.sendRedirect("index.jsp"); %>       
    </body>
</html>
