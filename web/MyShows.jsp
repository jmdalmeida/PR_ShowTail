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
    ArrayList<MyShow> shows = (ArrayList<MyShow>) session.getAttribute("array_shows_followed");
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
                <div id="SeriesFollowing">
                    <h1>Tailed Shows:</h1>
                    <div id="myShow">
                        <%
                            int totalWatched = 0;
                            int totalToWatch = 0;
                            for (MyShow s : shows) {
                                int limit = 20;
                                String title = s.getTitle().length() < limit ? s.getTitle() : s.getTitle().substring(0, limit - 3) + "...";
                                int tpx = 138;
                                int episodesWatched = s.getTotalEpisodes() - s.getEpisodesUnwatched();
                                double watchedPercent = episodesWatched / (double) s.getTotalEpisodes();
                                int npx = (int) Math.ceil(tpx * watchedPercent);

                                totalWatched += s.getTotalEpisodes() - s.getEpisodesUnwatched();
                                totalToWatch += s.getTotalEpisodes();
                        %>
                        <ul id="show">
                            <li>
                                <a href="#" title="<%=(int) (watchedPercent * 100)%>% watched">
                                    <img alt="<%=s.getTitle()%>" src="<%=s.getImgPath()%>" style="border: 1px white solid;" />
                                    <div style="width: 5px; height: <%=tpx%>px; position: relative; left: -6px; background-color: black; border: 1px white solid;"></div>
                                    <div style="width: 5px; height: <%=npx%>px; position: relative; left: -6px; bottom: <%=npx%>; background-color: #345d79; border: 1px white solid;"></div>
                                </a><br>
                            </li>
                            <a id="nomeSerie" href="ShowTemplate.jsp?id=<%=s.getId()%>" title="<%=s.getTitle()%>"><%=title%></a><br/>
                            <span class="unwatched"><%=s.getEpisodesUnwatched()%> unwatched</span>
                        </ul>
                        <% }
                            double watchedPercent = totalWatched / (double) totalToWatch;
                            int npx = (int) Math.ceil(800 * watchedPercent);
                        %>
                        <div class="totalPercentage">
                            <div class="totalP" style="position: relative; width: 100%; height: 100%; background-color: black; opacity: 0.7;"></div>
                            <div class="parcP" style="position: relative; width: <%=npx%>px; height: 100%; background-color: #345d79; top: -31px; left:-1px; border: 1px solid white;">
                                <% if (watchedPercent > 0.05) {%>
                                <span class="percentPlacing italicText whiteText"><%=Math.round(watchedPercent * 100)%>%</span>
                                <% }%>
                            </div>
                            <span class="italicText totalsPlacing whiteText"><%=totalWatched%> out of <%=totalToWatch%> episodes watched</span>
                        </div>
                    </div>
                    <div id="unwatchedEpisodes" class="unwatchedEps">
                        <div class="season">
                            <div class="episode">

                            </div>
                        </div>
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