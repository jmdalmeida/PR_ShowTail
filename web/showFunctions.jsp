<%
    String funct = request.getParameter("funct");
    if (funct == null) {
        response.sendRedirect("ShowTemplate.jsp");
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
    } else if("Rate".equals(funct)){
        int rate = Integer.parseInt(request.getParameter("rate"));
        %>
<jsp:forward page="ShowController" >
    <jsp:param name="Process" value="Rate" />
    <jsp:param name="ID_User" value="<%=param_id_user%>" />
    <jsp:param name="ID_Show" value="<%=param_id_show%>" />
    <jsp:param name="Rate" value="<%=rate%>" />
</jsp:forward>
<%
    }
%>