<%@page import="Utils.Data.IndexShow"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="WEB-INF/JSP/validation.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%    if (session.getAttribute("array_popular_shows") == null) {
%>
<jsp:forward page="ShowController" >
    <jsp:param name="Process" value="PopularShows" />
</jsp:forward>
<% }
    ArrayList<IndexShow> shows = (ArrayList<IndexShow>) session.getAttribute("array_popular_shows");
%>

<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="CSS/myCSS.css" />
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width">
        <title>Showtail - Manage Your TV Shows</title>
        <script>
            $(document).ready(function() {
                timer();
            });

            var len = <%=shows.size()%>
            var flag = false;
            var currI = 0;

            function timer() {
                if (flag)
                    $("#next" + currI).click();
                if (currI === len)
                    currI = 0;
                setTimeout(function() {
                    flag = true;
                    timer();
                }, 5000);
            }

        </script>
    </head>
    <body>
        <%@ include file="header.jsp" %>
        <div id="wrapper">
            <div id="content">
                <%-- SEARCH --%>
                <%@include file="WEB-INF/JSP/searchBar.jsp" %>
                <%-- SLIDER --%>
                <% if (!shows.isEmpty()) { %>
                <br><span id="popularshows">Popular Shows</span>
                <div id="slider">
                    <br>
                    <ul id="slides">
                        <% for (int i = 0; i < shows.size(); i++) {
                                IndexShow show = shows.get(i);
                                int indexBefore = ((i - 1) < 0) ? shows.size() - 1 : i - 1;
                                int indexAfter = ((i + 1) >= shows.size()) ? 0 : i + 1;
                        %>
                        <input type="radio" name="RButton" id="img<%=i + 1%>" <% if (i == 0) { %>checked<% }%> />
                        <li id="slideImage">
                            <div id="slide">
                                <img src="<%=show.getBackdropImg()%>" />
                                <div id="showName<%=i + 1%>" class="showNames"><span><%=show.getName()%></span></div>
                            </div>
                            <div id="navButtons">
                                <label for="img<%=indexBefore + 1%>" id="prev" onclick="currI = <%=indexBefore%>;">&#x2039;</label>
                                <label for="img<%=indexAfter + 1%>" class="next" id="next<%=i%>" onclick="currI = <%=indexAfter%>;">&#x203a;</label>
                            </div>
                        </li>
                        <% } %>
                        <li id="navDots">
                            <% for (int i = 0; i < shows.size(); i++) {
                                    IndexShow is = shows.get(i);
                            %>
                            <label for="img<%=i + 1%>" title="<%=is.getName()%>" id="navDot" onclick="currI = <%=i%>;"></label>
                            <% } %>
                        </li>
                    </ul>
                </div>
                <% } %>
            </div>
        </div>
        <%@ include file="WEB-INF/JSP/footer.jsp" %>
    </body>
</html>
<%
    if (session.getAttribute("array_popular_shows") != null) {
        shows.clear();
        session.removeAttribute("array_popular_shows");
    }
%>