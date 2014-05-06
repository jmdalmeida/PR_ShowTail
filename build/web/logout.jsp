<% 
    Cookie c1 = new Cookie("username", null);
    Cookie c2 = new Cookie("token", null);
    c1.setMaxAge(0);
    c2.setMaxAge(0);
    response.addCookie(c1);
    response.addCookie(c2);
    
    session.setAttribute("username", null);
    session.invalidate();
    response.sendRedirect("index.jsp");
%>
