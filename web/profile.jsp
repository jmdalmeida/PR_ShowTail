<%@ include file="WEB-INF/JSP/validation.jsp" %>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="JDBC.JDBCConnections"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" class="User.UserData" scope="session"/> 
<% 
JDBCConnections jdbc = new JDBCConnections("localhost", "db_pr", "Filipe", "a12345");
String name = "";
String email = "";
String picture = "";
String dataNasc = "";
String dataRegist = "";

jdbc.init();
try {
    Object[] o = {session.getAttribute("username")};
    ResultSet hi = jdbc.select("select * from user where Username like ?", o);
    while(hi.next()) {
        name = hi.getString("Name");
        email = hi.getString("Email");
        username = hi.getString("Username");
        picture = hi.getString("Image_Path");
        String[] a = hi.getString("Date_of_Birth").split(" ");
        dataNasc = a[0];
        String[] b = hi.getString("Date_of_Registration").split(" ");
        dataRegist = b[0];
    }
} catch(SQLException e) {
    e.printStackTrace();
}
jdbc.close();
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
            $(document).ready(function(){ 
                x = false;
                $("#datas").click(function(){ 
                    if (!x) {
                        $("#informationsDates").slideDown("slow"); 
                        x=true;
                    } else {
                        $("#informationsDates").hide("fast");
                        x=false;
                    }
                });
                
                $("#edit").click(function () {
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
                        
               });
              
               
            });
            function deleteUser() {
                var answer = confirm("Are you sure you want to delete this account?");
                alert(answer);
                if (answer) {
                    return true;
                }else {
                     return false;
                 }
            }  
        </script>
    </head>
    <body>
        <div id="wrapper">
            <%@ include file="WEB-INF/JSP/header.jsp" %>
            <div id="content">
                <div id="UserData">
                    <h1 id="username"><%= username %> Profile</h1><hr><br>
                    <img src= "<%= picture %>" id="profile"/>
                    <div id="informations">
                    <form name="ConfirmEditForm" method="POST" id="editConfirmForm" action="LoginController">
                        <input type="hidden" name="action" value="UpdateUser" />
                        <h1>Nome</h1>
                        <p id="nome"><%= name %></p>
                        <input id="nomeEdit" type="text" name="nomeEdit" value="<%= name %>">
                        <br><hr><br>
                        <h1>Email</h1>
                        <p id="emailU"><%= email %></p>
                        <input id="emailEdit" type="text" name="emailEdit" value="<%= email %>">
                        <br><hr><br>
                        <a href="#" id="datas" >Outros dados...</a>
                        <input type="submit" id="save" value="Save and Exit" >
                    </form>
                        <form id="deleteConfirm" method="POST" action="LoginController" onsubmit="return deleteUser()">
                        <input type="hidden" name="action" value="DeleteUser" />
                        <input type="submit" id="remove" value="Delete Acc" >
                    </form>
                    </div>
                    <div id="informationsDates">
                        <h1>Data de Nascimento</h1>
                        <p><%= dataNasc %></p><br><hr><br>
                        <h1>Data de Registo</h1>
                        <p><%= dataRegist %></p><br><hr><br>
                        
                    </div>
                    <a href="#" id="edit" >Editar</a>    
                </div>
                <div id="SeriesFollowing">
                    <h1>Followed Shows</h1>
                    <div>
                        <u1 id="result">
                                <% for(int i = 0; i < 6; i++) {%>
                                <li>
                                    <a href="#">
                                        <img alt="pixel" src="https://image.tmdb.org/t/p/original/fXBWu8jEvLQlHxvStyJMJCVP7KO.jpg">
                                    </a>
                                    <a id="serie_name" href="#">Name Example</a>
                                    <p>Followes 100000 | Episodes: ..</p>
                                </li> 
                                <%  } %>
                            </u1>
                    </div>
                </div>
                <div id="ConfirmEdit">
                    <a href="" id="close"><img src="images/buttonClose.png" id="btn_close" title="Close Window" alt="Close" /></a>
                    <form name="ConfirmEditForm" method="POST" id="editConfirmForm" action="LoginController">
                        <input type="hidden" name="action" value="UpdateUser" />
                        <span>Are you sure do you want to delete this account?</span><br><br>
                        <input id="Yes" type="submit" value="Yes" />
                        <a href="#" alt="Close">No</a>
                    </form>
                </div>
            </div>
            <%@ include file="WEB-INF/JSP/footer.jsp" %>
        </div>
    </body>
</html>
