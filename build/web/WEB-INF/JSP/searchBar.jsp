<%
    String val = request.getParameter("q") != null ? request.getParameter("q") : "";
%>
<div id="search">
    <form id="searchBar" action="" onsubmit="Search(); return false;" style="width: 500px; margin: 0 auto; padding-top: 20px;">
        <input autofocus id="searchBarText" type="text" placeHolder="Search TV-Shows" style="width: 400px; padding: 10px;" value="<%=val%>">
        <input type="submit" value="Search" style="padding: 10px;">
    </form>
</div>
<script>
    function Search() {
        var elem = document.getElementById("searchBarText");
        document.location = "TV-Shows.jsp?q=" + elem.value;
    }
</script>
