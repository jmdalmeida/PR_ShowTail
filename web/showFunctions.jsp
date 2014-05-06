<%@page import="JDBC.ConnectionFactory"%>
<%
    String funct = request.getParameter("funct");
    if(funct == null)
        response.sendRedirect("ShowTemplate.jsp");
    
    String id_show = request.getParameter("id_show");
    String id_user = request.getParameter("id_user");
    
    ConnectionFactory.getInstance().init();
    boolean success = true;
    if("Follow".equals(funct)){
        Object[] objs = {id_show, id_user};
        long id = ConnectionFactory.getInstance().insertAndReturnId("INSERT INTO Following(ID_User, ID_Show) VALUES(?,?);", objs);
        if(id == -1)
            success = false;
    }
    ConnectionFactory.getInstance().close();
    out.print(success);
%>