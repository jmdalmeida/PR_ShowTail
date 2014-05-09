<%@page import="Utils.Episode"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.ResultSet"%>
<%
    if (session.getAttribute("episodes_array") == null) {
        String id_season = request.getParameter("id");
%>
<jsp:forward page="ShowController" >
    <jsp:param name="Process" value="Episodes" />
    <jsp:param name="ID_Season" value="<%=id_season%>" />
</jsp:forward>
<%
    }
    ArrayList<Episode> episodes = (ArrayList<Episode>) session.getAttribute("episodes_array");
%>

<div id="episodes">
    <% 
    int n = 0;
    for (int b = 0; b < episodes.size(); b++) {
            Episode ep = episodes.get(b);
            if (b % 2 != 0) { %>
    <div id="oddepisode">
        <% } else { %>
        <div id="evenepisode">
            <% }%>
            <div id="name">
                <strong><%= ep.getSeasonNumber()%>x<%= ep.getEpisodeNumber()%></strong>
                <br>
                <p><%= ep.getTitle()%></p>
            </div>
            <div id="separator"></div>
            <div id="summary">
                <% n = (ep.getSeasonNumber() * 50) + b;%>
                <div id="short">
                    <p id="texts<%=n%>-short"> 
                        <%= ep.getShortOverview()%>
                        <%if (ep.getOverviewLength() > 199) {%>
                        <a href="#" id="texts-show" onclick="showHide('texts<%=n%>');
                                return false;">
                            Read More >>
                        </a>
                        <%}%>    
                    </p>
                    <div id="texts<%=n%>">
                        <div id="open">
                            <p id="texts<%=n%>-complete">
                                <%= ep.getOverview()%>
                                <a href="#" id="texts-hide" 
                                   onclick="showHide('texts<%= n%>');
                                           return false;"> << Read Less</a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <% }%>
    </div>

    <%
        episodes.clear();
        session.removeAttribute("episodes_array");
    %>