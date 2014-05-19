<%
    String funct = request.getParameter("funct");
    if (funct != null) {
    String username = request.getParameter("username");
    String email = request.getParameter("email");
    
    if("Validate".equals(funct)) {
%>
<jsp:forward page="AccountController" >
    <jsp:param name="action" value="Validate" />
    <jsp:param name="usernameV" value="<%=username%>" />
    <jsp:param name="emailV" value="<%=email%>" />
</jsp:forward>
<%}}%>
