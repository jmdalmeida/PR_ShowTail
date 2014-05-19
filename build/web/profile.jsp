<%@page import="Utils.Data.Show"%>
<%@page import="Utils.Data.UserData"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@ include file="WEB-INF/JSP/validation.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    UserData ud = null;
    if (session.getAttribute("user") != null) {
        ud = (UserData) session.getAttribute("user");
    } else {
        response.sendRedirect("index.jsp");
    }
    if (session.getAttribute("array_shows_followed") == null) {
%>
<jsp:forward page="AccountController" >
    <jsp:param name="action" value="Profile" />
</jsp:forward>
<%
    }

    ArrayList<Show> shows = (ArrayList<Show>) session.getAttribute("array_shows_followed");
%>    
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="CSS/myCSS.css" />
        <link rel="stylesheet" type="text/css" href="CSS/ProfileCSS.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js">
        </script>
        <title> Profile </title>
        <script>
            $(document).ready(function() {
                x = false;
                $("#datas").click(function() {
                    if (!x) {
                        $("#informationsDates").slideDown("slow");
                        x = true;
                    } else {
                        $("#informationsDates").hide("fast");
                        x = false;
                    }
                });

                $("#edit").click(function() {
                    $("#datas").hide("fast");
                    $("#nomeEdit").show("fast");
                    $("#emailEdit").show("fast");
                    $("#save").show("fast");
                    $("#nome").hide("fast");
                    $("#emailU").hide("fast");
                    $("#edit").hide("fast");
                    $("#remove").show("fast");
                });

                $("#save").click(function() {
                    $("#datas").show("fast");
                    $("#nomeEdit").hide("fast");
                    $("#emailEdit").hide("fast");
                    $("#save").hide("fast");
                    $("#nome").show("fast");
                    $("#emailU").show("fast");
                    $("#edit").show("fast");
                    $("#remove").hide("fast");

                });


            });
            function deleteUser() {
                var answer = confirm("Are you sure you want to delete this account?");
                alert(answer);
                if (answer) {
                    return true;
                } else {
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <%@ include file="header.jsp" %>
            <div id="content">
                <div id="UserData">
                    <h1 id="username"><%= username%>'s Profile</h1><hr><br>
                    <img src= "<%=ud.getPathImagem()%>" id="profile"/>
                    <div id="informations">
                        <form name="ConfirmEditForm" method="POST" id="editConfirmForm" action="AccountController">
                            <input type="hidden" name="action" value="UpdateUser" />
                            <h1>Nome</h1>
                            <p id="nome"><%=ud.getName()%></p>
                            <input id="nomeEdit" type="text" name="nomeEdit" value="<%= ud.getName()%>">
                            <br><hr><br>
                            <h1>Email</h1>
                            <p id="emailU"><%=ud.getEmail()%></p>
                            <input id="emailEdit" type="text" name="emailEdit" value="<%= ud.getEmail()%>">
                            <br><hr><br>
                            <a href="#" id="datas" >Outros dados...</a>
                            <input type="submit" id="save" value="Save and Exit" >
                        </form>
                        <form id="deleteConfirm" method="POST" action="AccountController" onsubmit="return deleteUser()">
                            <input type="hidden" name="action" value="DeleteUser" />
                            <input type="submit" id="remove" value="Delete Acc" >
                        </form>
                    </div>
                    <div id="informationsDates">
                        <h1>Data de Nascimento</h1>
                        <p><%= ud.getDataNascimento()%></p><br><hr><br>
                        <h1>Data de Registo</h1>
                        <p><%= ud.getDataRegisto()%></p><br><hr><br>

                    </div>
                    <a href="#" id="edit" >Editar</a>    
                </div>
                <% if (shows.size() > 0) { %>
                <div id="SeriesFollowing">
                    <h1>Recently Tailed Shows:</h1>
                    <div>
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
                    </div>
                </div>
                <% } %>
                <% if (ud.isAdmin()) { %>
                <div id="formAdmin">
                    <h1>Administration Panel:</h1>
                    <ul>
                        <li>
                            <form method="POST" action="MovieDBController">
                                <input id="inputID" name="moviedbID" type="text" placeholder="Input TV Show id"/>
                                <input type="submit" value="Gather show" />
                                <input type="hidden" name="action" value="GatherShow" />
                            </form>
                        </li>
                        <li>
                            <form method="POST" action="MovieDBController">
                                <input type="submit" value="Gather popular shows" />
                                <input type="hidden" name="action" value="GatherPopularShows" />
                            </form>
                        </li>
                        <li>
                            <form method="POST" action="MovieDBController">
                                <input type="submit" value="Gather genres" />
                                <input type="hidden" name="action" value="GatherGenres" />
                            </form>
                        </li>
                        <li>
                            <form method="POST" action="MovieDBController">
                                <input id="deleteUser" name="moviedbID" type="text" placeholder="Delete User"/>
                                <input type="submit" value="Delete" />
                                <input type="hidden" name="action" value="DeleteUser" />
                            </form>
                        </li>
                    </ul>
                </div>
                <% } %>
                <div id="ConfirmEdit">
                    <a href="" id="close"><img src="images/buttonClose.png" id="btn_close" title="Close Window" alt="Close" /></a>
                    <form name="ConfirmEditForm" method="POST" id="editConfirmForm" action="AccountController">
                        <input type="hidden" name="action" value="UpdateUser" />
                        <span>Are you sure do you want to delete this account?</span><br><br>
                        <input id="Yes" type="submit" value="Yes" />
                        <a href="#">No</a>
                    </form>
                </div>
            </div>
            <%@ include file="WEB-INF/JSP/footer.jsp" %>
        </div>
    </body>
</html>

<%
    shows.clear();
    session.removeAttribute("array_shows_followed");
%>