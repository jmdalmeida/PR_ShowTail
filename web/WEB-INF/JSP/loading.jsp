<div id="loading" style="display: none; position: fixed; left: 0; top: 0; width: 100%; height: 100%; 
     background-color: black; overflow: hidden; z-index: 999;
     opacity:0.5; filter:alpha(opacity=50);">
    <div id="loadingPlaceholder" style="width: 41px; height: 41px; background-color: white; position: fixed; 
         top: 49%; left: 49%; opacity:1; filter:alpha(opacity=100); z-index: 1000;">
        <img src="../../PR/images/loading.gif" alt="Loading..." style="position:absolute; left: 5px; top: 5px;" />
    </div>
</div>

<script type="text/javascript">
    function loadingState(loading) {
        if (loading === true) {
            $("#loading").show();
        } else {
            setTimeout(function() {
                $("#loading").hide();
            }, 500);
        }
    }
</script>