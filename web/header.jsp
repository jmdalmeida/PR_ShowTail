<% 
    String failedlogin = (String)session.getAttribute("failedlogin");
    if(failedlogin != null)
        session.removeAttribute("failedlogin");
    else
        failedlogin = "";
%>
<script>
    function checkValidation() {
        var username = document.getElementById("username").value;
        var email = document.getElementById("email").value;
        $.post("accountFunctions.jsp",
        {funct: "Validate", username:username, email:email},
        function(data, status) {
            var res = data.split(";");
            if(res[0]==="true") {
                alert("username ja existe");
            }
            else if(res[1]==="true") {
                alert("email ja existe");
            }else {
                document.forms["SignUpForm"].submit();
            }
        });
    }
</script>
<div id="header">
    <div id="header_wrap">
        <div id="header_title">
            <h1>
                <a href="index.jsp"> 
                    <span id="logo"><strong>SHOW</strong>TAIL</span>
                    <span id="slogan">Manage Your TV Shows</span>
                </a>
            </h1>
        </div>
        <div id="menu">
            <ul id="menus">
                <li><a href="TV-Shows.jsp">TV-Shows</a></li>
                    <% if(!loggedin){ %>
                    <li>
                    <a id="Reg" href="#signUp-box">Sign Up</a>
                    </li>
                    <% } %>
            </ul>
        </div>
        <% if(!loggedin){ %>
        <div id="header_login">
            <form name="LoginForm" method="POST" action="AccountController">
                <input type="hidden" name="action" value="login" />
                <table>
                    <tr>
                        <td><input type="text" name="username" placeholder="Username"/></td>
                        <td><input type="password" name="password" placeholder="Password"/></td>
                        <td><input type="submit" value="Login" id="loginButton"/></td>
                    </tr>
                    <tr>
                        <td><input id="chkRemember" name="chkRemember" type="checkbox" value="Remember me" /><span>Remember me?</span></td>
                        <td><span id="loginerror"><%= failedlogin %></span></td>
                    </tr>
                </table>
                <input type="hidden" name="action" value="login"/>
            </form>
        </div>
        <% } else { %>
        <div id="header_user">
            <ul id="user_menu">
                <li><a href="profile.jsp">Profile</a></li>
                <li><a href="MyShows.jsp">My Shows</a></li>
                <li><a href="logout.jsp">Logout</a></li>
            </ul>
        </div>
        <% } %>
    </div>
</div>
<div id="signUp-box">
    <a href="" id="close"><img src="images/buttonClose.png" id="btn_close" title="Close Window" alt="Close" /></a>
    <form name="SignUpForm" onsubmit="checkValidation(); return false;" method="POST" id="signInForm" action="AccountController" autocomplete="off">
        <input type="hidden" name="action" value="signUp" />
        <fieldset id="textbox">
            <label id="NameL">
                <span>Name</span>
                <input id="name" name="name" value="" type="text" autocomplete="off" placeholder="Name" required>
            </label>
            <label id="UsernameL">
                <span>Username</span>
                <input id="username" name="username" value="" type="text" autocomplete="off" placeholder="Username" required>
            </label>
            <label id="PasswordL">
                <span>Password</span>
                <input id="password" name="password" value="" type="password" autocomplete="off" placeholder="Password" required>
            </label>
            <label id="EmailL">
                <span>Email</span>
                <input id="email" name="email" value="" type="email" autocomplete="off" placeholder="Email">
            </label>
            <label id="DataNascL">
                <span>Date Of Birth</span>
                <input id="dtaNascimento" name="dataNascimento" type="date" required>
            </label>
            <input type="submit" value="Sign In" id="button" />     
        </fieldset>
    </form>
</div>
<script>
    $(document).ready(function() {
        $('a#Reg').click(function() {
            var loginBox = $(this).attr('href');
            $(loginBox).fadeIn(300);
            var popMargTop = ($(loginBox).height() + 24) / 2; 
            var popMargLeft = ($(loginBox).width() + 24) / 2; 
            $(loginBox).css({ 
                'margin-top' : -popMargTop,
                'margin-left' : -popMargLeft
            });
            $('body').append('<div id="mask"></div>');
            $('#mask').fadeIn(400);   
            return false;
        });
        
        $('a#close, #mask').live('click', function() { 
            $('#mask , #login-box').fadeOut(400 , function() {
                $('#mask').remove();  
            }); 
            return false;
        });
    });
</script>
