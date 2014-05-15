<%@page import="Utils.Data.*" %>
<%@page import="java.util.ArrayList"%>
<%@page import="Controllers.AccountController"%>
<%@include file="WEB-INF/JSP/validation.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%    if (session.getAttribute("array_shows_followed") == null) {
%>
<jsp:forward page="AccountController" >
    <jsp:param name="action" value="MyShows" />
</jsp:forward>
<% }
    ArrayList<Show> shows = (ArrayList<Show>) session.getAttribute("array_shows_followed");
%>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="CSS/MyShows.css" />
        <link rel="stylesheet" type="text/css" href="CSS/myCSS.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
        <title>My Shows</title>   
    </head>
    <body>
        <div id="wrapper">
            <%@ include file="WEB-INF/JSP/header.jsp" %>
            <div id="content">                    
                <%@include file="WEB-INF/JSP/searchBar.jsp" %>  
                <div id="SeriesFollowing">
                    <h1>Tailed Shows:</h1>
                    <div id="myShow">
                        <% for (Show s : shows) {
                                int limit = 20;
                                String title = s.getTitle().length() < limit ? s.getTitle() : s.getTitle().substring(0, limit - 3) + "...";
                        %>
                        <ul id="show">
                            <li>
                                <a href="#">
                                    <img alt="<%=s.getTitle()%>" src="<%=s.getImgPath()%>" />
                                </a><br>
                            </li>
                            <a id="nomeSerie" href="ShowTemplate.jsp?id=<%=s.getId()%>" title="<%=s.getTitle()%>"><%=title%></a>                               
                        </ul>
                        <% }%>
                    </div>
                    <div id="unwatchedEpisodes">
                        
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="WEB-INF/JSP/footer.jsp" %>
    </body>
</html>
<%
    shows.clear();
    session.removeAttribute("array_shows_followed");
%>