<%@page import="Utils.Show"%>
<%@page import="JDBC.ConnectionFactory"%>
<%@page import="Utils.Season"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="WEB-INF/JSP/validation.jsp" %>
<%
    Show show = null;
    ArrayList<Season> seas = new ArrayList<Season>();
    ConnectionFactory.getInstance().init();
    try {
        int id = Integer.parseInt(request.getParameter("id"));
        Object[] o = {id};
        ResultSet hi = ConnectionFactory.getInstance().select("select * from tv_show t, SearchView sv where t.ID_Show = ? and t.ID_Show = sv.ID_Show;", o);
        if (hi.next()) {
            show = new Show(Integer.parseInt(hi.getString("ID_Show")), 0, hi.getInt("Episodes"), hi.getString("Title"),
                            hi.getString("Image_Path"), hi.getString("Overview"), hi.getString("Status"), hi.getString("First_Air_Date"), hi.getDouble("Rating"));

            Object[] o2 = {show.getId()};
            ResultSet hi2 = ConnectionFactory.getInstance().select("select * from season where ID_Show = ?;", o2);
            while (hi2.next()) {
                seas.add(new Season(Integer.parseInt(hi2.getString("ID_Season")), Integer.parseInt(hi2.getString("Season_Number"))));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    ConnectionFactory.getInstance().close();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="CSS/myCSS.css" />
        <link rel="stylesheet" type="text/css" href="CSS/showTemplateCSS.css" />
        <link rel="stylesheet" type="text/css" href="CSS/perfect-scrollbar.css" />
        <link rel="stylesheet" href="CSS/jQuery/jQueryUI.css">
        <link rel="stylesheet" href="CSS/jQuery/jQueryUI_theme.css">
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
                    wheelPropagation: false,
                    suppressScrollX: true
                });
                $("#middle-middleC").tabs({
                    collapsible: true,
                    selected: -1,
                    beforeLoad: function(event, ui) {
                        ui.jqXHR.error(function() {
                            ui.panel.html(
                                    "Couldn't load this tab. We'll try to fix this as soon as possible. ");
                        });
                    }
                });
                $("#followButton").click(function() {
                    $.post("showFunctions.jsp",
                    {funct: "Follow", id_show: <%=show.getId()%>, id_user: 1},
                    function(data, status) {
                        alert("Data: " + data + "\nStatus: " + status);
                    });
                });
            });
        </script>
        <title>Show Template</title>
    </head>
    <body>
        <%@ include file="WEB-INF/JSP/header.jsp" %>
        <div id="wrapper">
            <div id="content">
                <%-- SEARCH --%>
                <%@include file="WEB-INF/JSP/searchBar.jsp" %>
                <%-- MiddleLayer --%>
                <div id="middleLayer">
                    <div id="header-middleC">
                        <h1> <%= show.getTitle()%> </h1>
                        <div id="banner">
                            <img src="<%= show.getImgPath()%>" />   
                        </div>
                        <div id="rating">
                            <div id="OverviewBox">
                                <h2>Overview:</h2>
                                <ul id="summaries">
                                    <li id="summariesLi"><%= show.getOverview()%></li>
                                </ul>
                            </div>
                            <div id="ratingBox">
                                <ul id="rates">
                                    <li id="one">Rating: <%=show.getRating()%>/10</li>
                                    <li id="one">Premiered: <%= show.getPremierDate()%></li>
                                    <li id="one">Episodes: <%= show.getEpisodesNumber()%></li>
                                    <li id="one">Status: <%= show.getStatus()%></li>
                                    <li id="one">
                                        <input id="followButton" type="submit" value="+ Follow" />
                                        <a id="imdbButton" href="http://www.imdb.com/find?q=<%= show.getTitle()%>&s=all" target="_blank" ></a>
                                        <div id="trailer">
                                            <a id="trailerText" href="https://www.youtube.com/results?search_query=Trailer <%= show.getTitle()%>" target="_blank" >Trailer</a>
                                            <a id="trailerImg" href="https://www.youtube.com/results?search_query=Trailer <%= show.getTitle()%>" target="_blank"></a>
                                        </div>
                                    </li>
                                    <li id="two"><%for(int i = 0; i<10; i++) { %>
                                        <a href="" id="rater<%= i %>"></a> <%}%></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div id="middle-middleC">
                        <h1>Seasons:</h1>
                        <div id="tab">
                            <ul id="tabs">
                                <% for (Season s : seas) {%>
                                <li><a href="Show.jsp?id=<%= s.getId()%>"><%= s.getNumberSeason()%></a></li> 
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
        </div>
        <%@ include file="WEB-INF/JSP/footer.jsp" %>
    </body>
</html>
