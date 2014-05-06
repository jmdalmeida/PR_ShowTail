<%@page import="JDBC.ConnectionFactory"%>
<%@page import="Shows.Season"%>
<%@page import="java.util.ArrayList"%>
<%@include file="WEB-INF/JSP/validation.jsp" %>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String title = "";
    String status = "";
    String premiered = "";
    String picture = "";
    String overview = "";
    int season_number = 0;
    int id_show = 0;
    int number_episodes = 0;
    int number_ep = 0;
    int idSeason = 0;
    ArrayList<Season> seas = new ArrayList<Season>();
    ConnectionFactory.getInstance().init();
    try {
        int id = Integer.parseInt(request.getParameter("id"));
        Object[] o = {id};
        ResultSet hi = ConnectionFactory.getInstance().select("select * from tv_show where ID_Show = ?;", o);
        while (hi.next()) {
            title = hi.getString("Title");
            id_show = Integer.parseInt(hi.getString("ID_Show"));
            status = hi.getString("Status");
            picture = hi.getString("Image_Path");
            overview = hi.getString("Overview");
            premiered = hi.getString("First_Air_Date");
        }
        Object[] o2 = {id_show};
        ResultSet hi2 = ConnectionFactory.getInstance().select("select * from season where ID_Show = ?;", o2);
        while (hi2.next()) {
            seas.add(new Season(Integer.parseInt(hi2.getString("ID_Season")), Integer.parseInt(hi2.getString("Season_Number"))));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    int[] episode_number = new int[number_ep];
    ConnectionFactory.getInstance().close();
%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="CSS/myCSS.css" />
        <link rel="stylesheet" type="text/css" href="CSS/showTemplateCSS.css" />
        <link rel="stylesheet" type="text/css" href="CSS/perfect-scrollbar.css" />
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script> 
        <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
        <script src="JavaScript/jquery.mousewheel.js"></script>
        <script src="JavaScript/perfect-scrollbar.js"></script>
        <script>function showHide(shID) {
                if (document.getElementById(shID)) {
                    var temp = document.getElementById(shID + '-short').innerHTML;
                    document.getElementById(shID + '-short').innerHTML = document.getElementById(shID + '-complete').innerHTML;
                    document.getElementById(shID + '-complete').innerHTML = temp;
                }
            }
        </script>
        <script type="text/javascript">
            $(document).ready(function($) {
                $('#summariesLi').perfectScrollbar({
                    wheelSpeed: 20,
                    wheelPropagation: false
                });
                $("#middle-middleC").tabs({
                    beforeLoad: function(event, ui) {
                        ui.jqXHR.error(function() {
                            ui.panel.html(
                                    "Couldn't load this tab. We'll try to fix this as soon as possible. " +
                                    "If this wouldn't be a demo.");
                        });
                    }
                });
            });
        </script>
        <title>Show Template</title>
    </head>
    <body>
        <div id="wrapper">
            <%@ include file="WEB-INF/JSP/header.jsp" %>
            <div id="content">
                <%-- SEARCH --%>
                <%@include file="WEB-INF/JSP/searchBar.jsp" %>
                <%-- MiddleLayer --%>
                <div id="middleLayer">
                    <div id="header-middleC">
                        <h1> <%= title%> </h1>
                        <div id="banner">
                            <img src="<%= picture%>" />   
                        </div>
                        <div id="rating">
                            <div id="OverviewBox">
                                <h1>Summary</h1>
                                <ul id="summaries">
                                    <li id="summariesLi"><%= overview%></li>
                                </ul>
                            </div>
                            <div id="ratingBox">
                                <h1>Ratings</h1>
                                <ul id="rates">
                                    <li><span>-.-/10</span></li>
                                    <li id="one"><strong>Premiered: <%= premiered%></strong></li>
                                    <li id="one"><strong>Episodes: <%= number_episodes%></strong></li>
                                    <li id="one"><strong>Status: <%= status%></strong></li>
                                </ul>
                            </div>
                            <a href="#" id="Follow_Unfollow">Follow</a>
                        </div>
                    </div>
                    <div id="middle-middleC">
                        <div id = tab>
                            <h1>Seasons:</h1>
                            <ul id="tabs">
                                <% for (Season s : seas) {%>
                                <li><a id="batman" href="Show.jsp?id=<%= s.getId()%>"><%= s.getNumberSeason()%></a></li> 
                                    <% }%>
                            </ul>
                        </div>
                        <!-- Things will Appear -->
                    </div>
                    <div id="comments">
                        <h1> Comments: </h1>
                        <div id="comments-box">
                            <div id="userF">

                            </div>
                            <div id="postComment">
                                <textarea id="textArea">
                                </textarea>
                            </div>   
                        </div>
                    </div>
                </div>
            </div>
            <%@ include file="WEB-INF/JSP/footer.jsp" %>
        </div>
    </body>
</html>
