<%@page import="JDBC.ConnectionFactory"%>
<%@page import="Controllers.AccountController"%>
<%@page import="Utils.*" %>
<%@page import="java.util.ArrayList"%>
<%@include file="WEB-INF/JSP/validation.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%    String query = "";
    String pageNumber = "";
    String genre = "";
    String order = "";
    if (session.getAttribute("genres_array") == null || session.getAttribute("shows_array") == null) {
        query = request.getParameter("q");
        pageNumber = request.getParameter("page") != null ? request.getParameter("page") : "1";
        genre = request.getParameter("genre");
        order = request.getParameter("order");

        if (query != null && query != "") {
%>
<jsp:forward page="SearchController" >
    <jsp:param name="SearchFor" value="Query" />
    <jsp:param name="Query" value="<%=query%>" />
    <jsp:param name="Page" value="<%=pageNumber%>" />
</jsp:forward>
<%
} else if (order != null && order != "") {
%>
<jsp:forward page="SearchController" >
    <jsp:param name="SearchFor" value="Order" />
    <jsp:param name="OrderBy" value="<%=order%>" />
    <jsp:param name="Page" value="<%=pageNumber%>" />
</jsp:forward>
<%
} else if (genre != null && genre != "") {
%>
<jsp:forward page="SearchController" >
    <jsp:param name="SearchFor" value="Genre" />
    <jsp:param name="Genre" value="<%=genre%>" />
    <jsp:param name="Page" value="<%=pageNumber%>" />
</jsp:forward>
<%
} else {
%>
<jsp:forward page="SearchController" >
    <jsp:param name="SearchFor" value="Order" />
    <jsp:param name="OrderBy" value="All" />
    <jsp:param name="Page" value="<%=pageNumber%>" />
</jsp:forward>
<%
        }
    }
    ArrayList<Genre> genres = (ArrayList<Genre>) session.getAttribute("genres_array");
    ArrayList<Show> shows = (ArrayList<Show>) session.getAttribute("shows_array");
    int count = (Integer) session.getAttribute("number_pages");
    int resultsPerPage = (Integer) session.getAttribute("results_per_page");
    double numPages = Math.ceil(count / (double) resultsPerPage);
    SearchFilter filter = (SearchFilter)session.getAttribute("filter_used");
%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="CSS/myCSS.css" />
        <link rel="stylesheet" type="text/css" href="CSS/TV-Shows.css" />
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search TV-Show</title>
        <script>
        </script>
    </head>
    <body>
        <div id="wrapper">
            <%@ include file="WEB-INF/JSP/header.jsp" %>
            <div id="content">                    
                <%@include file="WEB-INF/JSP/searchBar.jsp" %>  
                <div id="middleLayer-Title">
                    <div id="series_icon"></div>
                    <div id="series">TV-SHOWS</div>
                    <ul id="menuSeries">
                        <li><a href="TV-Shows.jsp?order=All">All</a></li>
                        <li><a href="TV-Shows.jsp?order=MostFollowed">Most Followed</a></li>
                        <li><a href="TV-Shows.jsp?order=Recommended">Recommended</a></li>
                    </ul>
                </div>
                <div id="middleLayer">
                    <div id="Generos" >
                        <div id="GeneroTitulo">
                            <div id="GeneroTitulo_icon"></div>
                            <span>Genres</span>    
                        </div>
                        <div id="selecaoGeneros">
                            <% for (Genre g : genres) {%>
                            <a href="TV-Shows.jsp?genre=<%=g.getId()%>"><%=g.getGenre()%></a>
                            <% } %>
                        </div>
                    </div>
                    <div id="Results">
                        <div><% if (shows.size() > 0) { %>
                            <ul id="result">
                                <% for (Show s : shows) {%>
                                <li>
                                    <a href="ShowTemplate.jsp?id=<%=s.getId()%>">
                                        <img alt="<%=s.getTitle()%>" src="<%=s.getImgPath()%>">
                                    </a>
                                    <a id="serie_name" href="ShowTemplate.jsp?id=<%=s.getId()%>"><%=s.getTitle()%></a>
                                    <p>Followers <%=s.getFollowers()%> | Episodes <%=s.getEpisodesNumber()%></p>
                                </li> 
                                <%  }%>
                            </ul>
                            <% } else { %>
                            <div style="width: 100%;float: top; margin-top: 10px;">
                                <div style="width: 250px; height: 200px; margin: 0 auto; text-align: center;">
                                    <img src="http://data2.whicdn.com/images/65260945/large.gif" alt="Sorry" width="250" height="200">
                                    I couldn't find a match...
                                </div>
                            </div>
                            <% }%>
                        </div>
                        <div id="number_page">
                            <% for (int i = 0; i < (int) (numPages); i++) {%>
                            <a href="TV-Shows.jsp?<%=filter.getFilterUsed()%>=<%=filter.getFilterValue()%>&page=<%=(i+1)%>"><%=i + 1%></a>
                            <% } %>
                        </div>
                    </div>
                </div>  
            </div>
        </div>    
        <%@ include file="WEB-INF/JSP/footer.jsp" %>  
    </body>
</html>

<%
    genres.clear();
    shows.clear();
    session.removeAttribute("genres_array");
    session.removeAttribute("shows_array");
    session.removeAttribute("number_pages");
    session.removeAttribute("results_per_page");
%>
