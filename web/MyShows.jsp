<%@page import="Utils.Data.*" %>
<%@page import="java.util.ArrayList"%>
<%@page import="Controllers.AccountController"%>
<%@include file="WEB-INF/JSP/validation.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% 
if (session.getAttribute("array_shows_followed") == null) {
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
        <title>MY Shows</title>   
    </head>
    <body>
        <div id="wrapper">
            <%@ include file="WEB-INF/JSP/header.jsp" %>
            <div id="content">                    
                <%@include file="WEB-INF/JSP/searchBar.jsp" %>  
                    <div id="SeriesFollowing">
                        <h1>Followed Shows:</h1>
                        <div id="myShow">
                            <% for (Show s : shows) {%>
                            <ul id="show">
                                <li>
                                    <a href="#">
                                        <img alt="<%=s.getTitle()%>" src="<%=s.getImgPath()%>" />
                                    </a><br>
                                </li>
                                <a id="nomeSerie" href="ShowTemplate.jsp?id=<%=s.getId()%>"><%=s.getTitle()%></a>
                                <li id="moreInfo">
                                    <h2>Last Episode</h2><hr>
                                    <p> Nome ep. </p><br>
                                    <h2>Next Episode</h2><hr>
                                    <p> Nome ep. </p><br>
                                    <input id="followButton" type="submit" value="UnTail Show"/>
                                </li>                                    
                            </ul>
                            <% }%>
                        </div>
                    </div>
                </div>
            </div>
            <%@ include file="WEB-INF/JSP/footer.jsp" %>
    </body>
</html>
