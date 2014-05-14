<%@page import="Utils.Data.ShowMovieDB"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
<%@include file="WEB-INF/JSP/validation.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    ArrayList<ShowMovieDB> shows = (ArrayList<ShowMovieDB>) session.getAttribute("array_request_shows");
    boolean hasToShow = shows != null;

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Request Show</title>
        <link rel="stylesheet" type="text/css" href="CSS/myCSS.css" />
        <link rel="stylesheet" type="text/css" href="CSS/request.css" />
    </head>
    <body>
        <div id="wrapper">
            <%@ include file="WEB-INF/JSP/header.jsp" %>
            <div id="content">
                <div class="formRequest">
                    <div class="search">
                        <h1>Missing a show? Look for it here...</h1>
                        <form action="MovieDBController" method="POST">
                            <input type="hidden" name="action" value="SelectRequest" />
                            <input type="text" name="query" placeholder="Look for show..." />
                            <input type="submit" value="Search" />
                        </form>
                    </div>
                    <div class="moviedb">
                        <a href="https://www.themoviedb.org/" target="_blank">
                            <span>powered by</span>
                            <img src="images/themoviedb.png" alt="TheMovieDB"/>
                        </a>
                    </div>
                </div>
                <% if (hasToShow) { %>
                <div class="results">
                    <% for (ShowMovieDB s : shows) {%>
                    <img src="<%=s.getImgPath()%>" alt="<%=s.getTitle()%>" />
                    <span><%=s.getTitle()%></span>
                    <% if (!s.exists()) {%>
                    <form action="MovieDBController" method="POST">
                        <input type="hidden" name="action" value="GatherUserRequest" />
                        <input type="hidden" name="moviedbID" value="<%=s.getId()%>" />
                        <input type="submit" value="Add this show" />
                    </form>
                    <% } else { %>
                    <span>Show already added to SHOWTAIL</span>
                    <% }
                            } %>
                </div>
                <% }%>
            </div>
        </div>
        <%@ include file="WEB-INF/JSP/footer.jsp" %>  
    </body>
</html>

<%
    if (shows != null) {
        shows.clear();
    }
    session.removeAttribute("array_request_shows");
%>
