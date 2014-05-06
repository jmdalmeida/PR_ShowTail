<%@ include file="WEB-INF/JSP/validation.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="CSS/myCSS.css" />
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width">
        <title>Showtail - Manage Your TV Shows</title>
    </head>
    <body>
        <div id="wrapper">
            <%@ include file="WEB-INF/JSP/header.jsp" %>
            <div id="content">
                <%-- SEARCH --%>
                <%@include file="WEB-INF/JSP/searchBar.jsp" %>
                <%-- SLIDER --%>
                <br><span id="popularshows">Popular Shows</span>
                <div id="slider">
                    <br>
                    <ul id="slides">
                        <input type="radio" name="RButton" id="img1" checked />
                        <li id="slideImage">
                            <div id="slide">
                                <img src="http://ib.huluim.com/show_key_art/11430?size=1600x600&region=US" />
                            </div>
                            <div id="navButtons">
                                <label for="img6" id="prev">&#x2039;</label>
                                <label for="img2" id="next">&#x203a;</label>
                            </div>
                        </li>
                        <input type="radio" name="RButton" id="img2" />
                        <li id="slideImage">
                            <div id="slide">
                                <img src="http://images8.alphacoders.com/431/431311.jpg" />
                            </div>
                            <div id="navButtons">
                                <label for="img1" id="prev">&#x2039;</label>
                                <label for="img3" id="next">&#x203a;</label>
                            </div>
                        </li>
                        <input type="radio" name="RButton" id="img3" />
                        <li id="slideImage">
                            <div id="slide">
                                <img src="http://www.theyoungfolks.com/wp-content/uploads/2014/02/the-walking-dead-s1.jpg" />
                            </div>
                            <div id="navButtons">
                                <label for="img2" id="prev">&#x2039;</label>
                                <label for="img4" id="next">&#x203a;</label>
                            </div>
                        </li>
                        <input type="radio" name="RButton" id="img4" />
                        <li id="slideImage">
                            <div id="slide">
                                <img src="http://www.butler.edu/admission/student-perspectives/blogs/katie/files/2012/12/68194.jpg" />
                            </div>
                            <div id="navButtons">
                                <label for="img3" id="prev">&#x2039;</label>
                                <label for="img5" id="next">&#x203a;</label>
                            </div>
                        </li>
                        <input type="radio" name="RButton" id="img5" />
                        <li id="slideImage">
                            <div id="slide">
                                <img src="http://3.bp.blogspot.com/-Q5uaIzHEDDI/UPczBjzUXFI/AAAAAAAAPDg/iLnVFMHhT4w/s1600/HIMYM.jpg" />
                            </div>
                            <div id="navButtons">
                                <label for="img4" id="prev">&#x2039;</label>
                                <label for="img6" id="next">&#x203a;</label>
                            </div>
                        </li>
                        <input type="radio" name="RButton" id="img6" />
                        <li id="slideImage">
                            <div id="slide">
                                <img src="http://i1017.photobucket.com/albums/af293/bluerose72795/Non-Wrestling%20Wallpapers/SupernaturalTheEndBegins-1920x1080.jpg" />
                            </div>
                            <div id="navButtons">
                                <label for="img5" id="prev">&#x2039;</label>
                                <label for="img1" id="next">&#x203a;</label>
                            </div>
                        </li>
                        <li id="navDots">
                            <label for="img1" id="navDot" id="ID1"></label>
                            <label for="img2" id="navDot" id="ID2"></label>
                            <label for="img3" id="navDot" id="ID3"></label>
                            <label for="img4" id="navDot" id="ID4"></label>
                            <label for="img5" id="navDot" id="ID5"></label>
                            <label for="img6" id="navDot" id="ID6"></label>
                        </li>
                    </ul>
                </div>
            </div>
            <%@ include file="WEB-INF/JSP/footer.jsp" %>
        </div>
    </body>
</html>
