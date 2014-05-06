<%@page import="JDBC.ConnectionFactory"%>
<%
    String funct = request.getParameter("funct");
    if(funct == null)
        response.sendRedirect("ShowTemplate.jsp");
    
    int id_show = Integer.parseInt(request.getParameter("id_show"));
    int id_user = Integer.parseInt(request.getParameter("id_user"));
    
    ConnectionFactory.getInstance().init();
    boolean success = true;
    if("Follow".equals(funct)){
        Object[] objs = {id_user, id_show};
        System.out.println("Attempting to insert Follower(Show: " + id_show + "; User: " + id_user);
        long id = ConnectionFactory.getInstance().insertAndReturnId("INSERT INTO Following(ID_User, ID_Show) VALUES(?,?);", objs);
        if(id == -1)
            success = false;
    }
    ConnectionFactory.getInstance().close();
    out.print(success);
%>