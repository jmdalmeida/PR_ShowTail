<%@page import="JDBC.ConnectionFactory"%>
<%
    String funct = request.getParameter("funct");
    if(funct == null)
        response.sendRedirect("ShowTemplate.jsp");
    
    int id_show = Integer.parseInt(request.getParameter("id_show"));
    int id_user = Integer.parseInt(request.getParameter("id_user"));
    
    ConnectionFactory.getInstance().init();
    if("Follow".equals(funct)){
        Object[] objs = {id_user, id_show};
        ConnectionFactory.getInstance().update("INSERT INTO Following(ID_User, ID_Show) VALUES(?,?);", objs);
    }
    ConnectionFactory.getInstance().close();
%>