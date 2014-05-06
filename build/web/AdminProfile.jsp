<%@ include file="WEB-INF/JSP/validation.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="CSS/myCSS.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Administrator Profile</title>
    </head>
    <body>
        <div id="wrapper">
            <%@ include file="WEB-INF/JSP/header.jsp" %>
            <div id="content">
                <div id="formAdmin">
                    <form method="POST" action="AdminController">
                        <input id="inputID" name="moviedbID" type="text" placeholder="Input TV Show id"/>
                        <input type="submit" value="Gather show" />
                        <input type="hidden" name="action" value="GatherShow" />
                    </form>
                    <form method="POST" action="AdminController">
                        <input type="submit" value="Gather popular shows" />
                        <input type="hidden" name="action" value="GatherPopularShows" />
                    </form>
                    <form method="POST" action="AdminController">
                        <input type="submit" value="Gather genres" />
                        <input type="hidden" name="action" value="GatherGenres" />
                    </form>
                </div>
            </div>
            <%@ include file="WEB-INF/JSP/footer.jsp" %>
        </div>
    </body>
</html>
