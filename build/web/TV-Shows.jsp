<%@page import="JDBC.ConnectionFactory"%>
<%@page import="Controllers.LoginController"%>
<%@page import="Utils.*" %>
<%@page import="java.util.ArrayList"%>
<%@ include file="WEB-INF/JSP/validation.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    ResultSet rs = null;
    String query = request.getParameter("q");
    String pageNumber = request.getParameter("page");
    String genre = request.getParameter("genre");
    String order = request.getParameter("order");

    ConnectionFactory.getInstance().init();

    //LOAD GENRES
    ArrayList<Genre> genres = new ArrayList<Genre>();
    rs = ConnectionFactory.getInstance().select("SELECT DISTINCT ID_Genre, Genre FROM SearchGenresView ORDER BY Genre ASC;", null);
    while (rs.next()) {
        genres.add(new Genre(rs.getInt("ID_Genre"), rs.getString("Genre")));
    }

    //PROCESS SEARCH
    ArrayList<Show> shows = new ArrayList<Show>();
    if (query != null && query != "") {
        Object[] objs = {new String("%" + query + "%")};
        rs = ConnectionFactory.getInstance().select("SELECT * FROM SearchView WHERE Title LIKE ? ORDER BY Followers DESC;", objs);
    } else if (order != null && order != "") {
        if(order.equals("All")) {
            rs = ConnectionFactory.getInstance().select("SELECT * FROM SearchView ORDER BY Title ASC;", null);
        }
        else if(order.equals("MostFollowed")) {
            rs = ConnectionFactory.getInstance().select("SELECT * FROM SearchView ORDER BY Followers DESC;", null);
        }
        else if(order.equals("Recommended")) {
            rs = ConnectionFactory.getInstance().select("SELECT * FROM SearchView ORDER BY Rating DESC", null);
        }
        else {
            rs = ConnectionFactory.getInstance().select("SELECT * FROM SearchView ORDER BY Followers DESC;", null);
        }
    } else if (genre != null && genre != "") {
        Object[] objs = {Integer.parseInt(genre)};
        rs = ConnectionFactory.getInstance().select("SELECT * FROM SearchView sv, SearchGenresView sgv WHERE sv.ID_Show = sgv.ID_Show AND sgv.ID_Genre = ?", objs);
    } else {
        rs = ConnectionFactory.getInstance().select("SELECT * FROM SearchView ORDER BY Followers DESC;", null);
    }
    if (rs != null) {
        while (rs.next()) {
            shows.add(new Show(rs.getInt("ID_Show"), rs.getInt("Followers"), rs.getInt("Episodes"), 
                    rs.getString("Title"), rs.getString("Image_Path"), "", "", "", 0.0));
        }
    }
    ConnectionFactory.getInstance().close();
%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="CSS/myCSS.css" />
        <link rel="stylesheet" type="text/css" href="CSS/TV-Shows.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search TV-Show</title>
    </head>
    <body>
        <div id="wrapper">
            <%@ include file="WEB-INF/JSP/header.jsp" %>
            <div id="content">                    
                <%@include file="WEB-INF/JSP/searchBar.jsp" %>  
                <div id="middleLayer-Title">
                    <div id="series_icon"></div>
                    <div id="series">TV-SHOWS</div>
                    <u1 id="menuSeries">
                        <li><a href="TV-Shows.jsp?order=All">All</a></li>
                        <li><a href="TV-Shows.jsp?order=MostFollowed">Most Followed</a></li>
                        <li><a href="TV-Shows.jsp?order=Recommended">Recommended</a></li>
                    </u1>
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
                                    <img src="http://data2.whicdn.com/images/65260945/large.gif" alt="Sorry" width="250px" height="200px">
                                    I couldn't find a match...
                                </div>
                            </div>
                            <% }%>
                        </div>
                    </div>
                </div>  
            </div>
        </div>    
        <%@ include file="WEB-INF/JSP/footer.jsp" %>  
    </body>
</html>