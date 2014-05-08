<%@page import="JDBC.ConnectionFactory"%>
<%@page import="Utils.Episode"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.ResultSet"%>
<%
    if(session.getAttribute("episodes_array") != null){
        
    }
    
    ArrayList<Episode> episodes;
    
    
    String idSeason = request.getParameter("id");
    int season_number = 0;
    String episode_title = "";
    String episode_overview = "";
    String short_episode_overview = "";
    int number_ep = 0;
    int n = 0;
    ArrayList<String> arrayEpisodeTitle = new ArrayList<String>();
    ArrayList<String> arrayEpisodeOverview = new ArrayList<String>();
    ConnectionFactory.getInstance().init();
    try {
        Object[] o2 = {idSeason};
        ResultSet hi2 = ConnectionFactory.getInstance().select("select * from season where ID_Season = ?;", o2);
        while (hi2.next()) {
            season_number = Integer.parseInt(hi2.getString("Season_Number"));
        }
        ResultSet hi3 = ConnectionFactory.getInstance().select("select * from episode where ID_Season = ?;", o2);
        while (hi3.next()) {
            number_ep = Integer.parseInt(hi3.getString("Episode_Number"));
            episode_title = hi3.getString("Title");
            episode_overview = hi3.getString("Overview");
            arrayEpisodeTitle.add(episode_title);
            arrayEpisodeOverview.add(episode_overview);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    int[] episode_number = new int[number_ep];
    ConnectionFactory.getInstance().close();
%>

<div id="episodes">
    <% for (int b = 0; b < episode_number.length; b++) {
            boolean less = false;
            if (arrayEpisodeOverview.get(b).length() < 200) {
                short_episode_overview = arrayEpisodeOverview.get(b);
                less = true;
            } else {
                short_episode_overview = arrayEpisodeOverview.get(b).substring(0, 199) + "...";
        }
        if (b % 2 != 0) { %>
    <div id="oddepisode">
        <% } else { %>
        <div id="evenepisode">  
            <% }%>
            <div id="name">
                <strong><%= season_number%>x<%= b + 1%></strong>
                <br>
                <p><%= arrayEpisodeTitle.get(b)%></p>
            </div>
            <div id="separator"></div>
            <div id="summary">
                <% n = (season_number * 50) + b;%>
                <div id="short">
                    <p id="texts<%=n%>-short"> 
                        <%= short_episode_overview%>
                        <%if (!less) {%>
                        <a href="#" id="texts-show" onclick="showHide('texts<%=n%>');
                                return false;">
                            Read More >>
                        </a>
                        <%}%>    
                    </p>
                    <div id="texts<%=n%>">
                        <div id="open">
                            <p id="texts<%=n%>-complete">
                                <%= arrayEpisodeOverview.get(b)%>
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
