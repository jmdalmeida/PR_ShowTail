<%@page import="Utils.UserData"%>
<%@page import="Utils.Show"%>
<%@page import="Utils.Season"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="WEB-INF/JSP/validation.jsp" %>
<%    String id_show = request.getParameter("id");
    if (id_show == null || id_show == "") {
        //response.sendRedirect("index.jsp");
    }
    if (session.getAttribute("obj_show") == null || session.getAttribute("seasons_array") == null) {
%>
<jsp:forward page="ShowController" >
    <jsp:param name="Process" value="Show" />
    <jsp:param name="ID_Show" value="<%=id_show%>" />
</jsp:forward>
<%
    }
    int id_user = 0;
    boolean following = false;
    int rate = 0;
    if (loggedin) {
        id_user = ((UserData) session.getAttribute("user")).getId();
        following = (Boolean) session.getAttribute("following");
        rate = (Integer) session.getAttribute("rate");
    }
    Show show = (Show) session.getAttribute("obj_show");
    ArrayList<Season> seasons = (ArrayList<Season>) session.getAttribute("seasons_array");
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
        <script type="text/javascript">
            function showHide(shID) {
                if (document.getElementById(shID)) {
                    var temp = document.getElementById(shID + '-short').innerHTML;
                    document.getElementById(shID + '-short').innerHTML = document.getElementById(shID + '-complete').innerHTML;
                    document.getElementById(shID + '-complete').innerHTML = temp;
                }
            }

            $(document).ready(function($) {
                $('#summariesLi').perfectScrollbar({
                    wheelSpeed: 20,
                    wheelPropagation: false,
                    suppressScrollX: true
                });
                initTabs();
                buttonState(<%=following%>);
                setRateStars(<%=rate%>);
            });

            function buttonState(following) {
                var elem = document.getElementById("followButton");
                if (following === true) {
                    elem.value = "- Unfollow";
                    elem.style.backgroundColor = "#FF0000";
                } else {
                    elem.value = "+ Follow";
                    elem.style.backgroundColor = "#339900";
                }
            <% if (loggedin) { %>
                elem.onclick = function() {
                    buttonAction(following);
                };
            <% } else { %>
                elem.disabled = "disabled";
                elem.style.backgroundColor = "#CCCCCC";
            <% }%>
                resetTabs();
            }

            function initTabs() {
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
            }

            function resetTabs() {
                $("#middle-middleC").tabs("destroy");
                initTabs();
            }

            function buttonAction(following) {
                if (following !== true) {
                    $.post("showFunctions.jsp",
                            {funct: "Follow", id_show: <%=show.getId()%>, id_user: <%=id_user%>},
                    function(data, status) {
                        buttonState(true);
                    });
                } else {
                    $.post("showFunctions.jsp",
                            {funct: "Unfollow", id_show: <%=show.getId()%>, id_user: <%=id_user%>},
                    function(data, status) {
                        buttonState(false);
                    });
                }
            }

            function checkSeenStatus(elem, id_season, id_episode) {
                $.post("showFunctions.jsp",
                        {funct: "SetSeenStatus", id_show: <%=show.getId()%>, id_user: <%=id_user%>,
                            id_season: id_season, id_episode: id_episode, seen: elem.checked},
                function(data, status) {

                });
            }

            function rateShow(rate) {
                if (rate < 1 || rate > 10)
                    return;
                $.post("showFunctions.jsp",
                        {funct: "Rate", id_show: <%=show.getId()%>, id_user: <%=id_user%>, rate: rate},
                function(data, status) {
                    document.getElementById("ratingPH").innerHTML = data;
                    setRateStars(rate);
                });
            }

            function setRateStars(rate) {
                for (var i = 1; i <= 10; i++) {
                    var elem = document.getElementById("star" + i);
                    elem.className = "star";
                }
                for (var i = 1; i <= rate; i++) {
                    var elem = document.getElementById("star" + i);
                    elem.className = "starActive";
                }
            }

            function processCombobox(elem, id_season) {
                var value = elem.value;
                switch (value) {
                    case "seasonSeen":
                    case "seasonUnseen":
                    case "showSeen":
                    case "showUnseen":
                        $.post("showFunctions.jsp",
                                {funct: "Mark", id_show: <%=show.getId()%>, id_season: id_season, id_user: <%=id_user%>},
                        function(data, status) {
                            resetTabs();
                        });
                        break;
                    default:
                        break;
                }
            }
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
                                    <li id="one">Rating: <span id="ratingPH"><%=show.getRating()%></span>/10</li>
                                    <li id="one">Premiered: <%= show.getPremierDate()%></li>
                                    <li id="one">Episodes: <%= show.getEpisodesNumber()%></li>
                                    <li id="one">Status: <%= show.getStatus()%></li>
                                    <li id="one">
                                        <input id="followButton" type="submit"/>
                                        <a id="imdbButton" href="http://www.imdb.com/find?q=<%= show.getTitle()%>&s=all" target="_blank" ></a>
                                        <div id="trailer">
                                            <a id="trailerText" href="https://www.youtube.com/results?search_query=Trailer Show <%= show.getTitle()%>" target="_blank" >Trailer</a>
                                            <a id="trailerImg" href="https://www.youtube.com/results?search_query=Trailer Show <%= show.getTitle()%>" target="_blank"></a>
                                        </div>
                                    </li>
                                    <li id="two"> 
                                        <div class="rating">
                                            <span>Rate Show:&nbsp;&nbsp;</span>
                                            <div <% if (loggedin) { %>class="star"<% } %>>
                                                <%for (int i = 10; i > 0; i--) {%>
                                                <span id="star<%=i%>" <% if (loggedin) {%>onclick="rateShow(<%=i%>)"<% } %>>☆</span> <%}%>
                                            </div>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div id="middle-middleC">
                        <h1>Seasons:</h1>
                        <% if (loggedin) { %>
                        <select onchange="processCombobox(this)">
                            <option value="title">Mark season or show as seen/unseen</option>
                            <option value="seasonSeen">Mark season as seen</option>
                            <option value="seasonUnseen">Mark season as unseen</option>
                            <option value="separator">---------------------</option>
                            <option value="showSeen">Mark show as seen</option>
                            <option value="showUnseen">Mark show as unseen</option>
                        </select>
                        <% } %>
                        <div id="tab">
                            <ul id="tabs">
                                <% for (Season s : seasons) {%>
                                <li><a href="Show.jsp?id_show=<%=id_show%>&id_season=<%= s.getId()%>&following=<%=following%>"><%= s.getNumberSeason()%></a></li> 
                                    <% }%>
                            </ul>
                        </div>
                        <!-- Things will Appear -->
                    </div>
                    <div id="comments">
                        <h1>Comments:</h1>
                        <div id="comments-box">
                            <div id="postComment">
                                <textarea id="textArea" rows="6" cols="50" maxlength="254"></textarea>
                                <input type="button" value="Post Comment"/>
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
    show = null;
    seasons.clear();
    session.removeAttribute("obj_show");
    session.removeAttribute("seasons_array");
%>