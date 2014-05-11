<%
    String funct = request.getParameter("funct");
    if (funct == null) {
        response.sendRedirect("index.jsp");
    }

    int param_id_user = Integer.parseInt(request.getParameter("id_user"));
    int param_id_show = Integer.parseInt(request.getParameter("id_show"));

    if ("Follow".equals(funct)) {
%>
<jsp:forward page="ShowController" >
    <jsp:param name="Process" value="Follow" />
    <jsp:param name="ID_User" value="<%=param_id_user%>" />
    <jsp:param name="ID_Show" value="<%=param_id_show%>" />
</jsp:forward>
<%
} else if ("Unfollow".equals(funct)) {
%>
<jsp:forward page="ShowController" >
    <jsp:param name="Process" value="Unfollow" />
    <jsp:param name="ID_User" value="<%=param_id_user%>" />
    <jsp:param name="ID_Show" value="<%=param_id_show%>" />
</jsp:forward>
<%
} else if ("Rate".equals(funct)) {
    int rate = Integer.parseInt(request.getParameter("rate"));
%>
<jsp:forward page="ShowController" >
    <jsp:param name="Process" value="Rate" />
    <jsp:param name="ID_User" value="<%=param_id_user%>" />
    <jsp:param name="ID_Show" value="<%=param_id_show%>" />
    <jsp:param name="Rate" value="<%=rate%>" />
</jsp:forward>
<%
} else if ("SetSeenStatus".equals(funct)) {
    int param_id_episode = Integer.parseInt(request.getParameter("id_episode"));
    int param_id_season = Integer.parseInt(request.getParameter("id_season"));
    boolean seenStatus = Boolean.parseBoolean(request.getParameter("seen"));
%>
<jsp:forward page="ShowController" >
    <jsp:param name="Process" value="SeenStatus" />
    <jsp:param name="ID_User" value="<%=param_id_user%>" />
    <jsp:param name="ID_Show" value="<%=param_id_show%>" />
    <jsp:param name="ID_Season" value="<%=param_id_season%>" />
    <jsp:param name="ID_Episode" value="<%=param_id_episode%>" />
    <jsp:param name="SeenStatus" value="<%=seenStatus%>" />
</jsp:forward>
<%
} else if ("Mark".equals(funct)) {
    int param_number_season = Integer.parseInt(request.getParameter("number_season"));
    String action = request.getParameter("action");
%>
<jsp:forward page="ShowController" >
    <jsp:param name="Process" value="Mark" />
    <jsp:param name="ID_User" value="<%=param_id_user%>" />
    <jsp:param name="ID_Show" value="<%=param_id_show%>" />
    <jsp:param name="Number_Season" value="<%=param_number_season%>" />
    <jsp:param name="Action" value="<%=action%>" />
</jsp:forward>
<%
    }
%>