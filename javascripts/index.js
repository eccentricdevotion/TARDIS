$(document).ready(function(e) {
    $("#values").load("http://thenosefairy.duckdns.org/mc-data.php",{damage: false});
	$("#filter").keyup(function(e) {
		var data = {
			filter: $("#filter").val(),
			damage: $("#damage").prop('checked')
		}
        $("#values").load("http://thenosefairy.duckdns.org/mc-data.php",data);
    });
	$("#damage").click(function(e) {
        var data = {
			filter: $("#filter").val(),
			damage: $("#damage").prop('checked')
		}
        $("#values").load("http://thenosefairy.duckdns.org/mc-data.php",data);
    });
	$(document).on("mouseover", ".data", function(e) {
			id = $(this).attr("mid");
			data = $(this).attr("mdata");
			$("#infobox").load("http://thenosefairy.duckdns.org/mc-info.php",{id:id,data:data});
			var position = $("section").position();
			var dx = position.left-20;
			y = ((event.pageY)-position.top) + "px";
			x = ((event.pageX)-dx) + "px";
			$("#infobox").css({"position":"absolute","top":y,"left":x}).show();
		}
	).mousemove(function(event) {
        $("#infobox")
        .css('top', ((event.pageY)-position.top) + 'px')
        .css('left', ((event.pageX)-dx) + 'px');
    });
	$(document).on("mouseout", ".data", function(e) {
		$("#infobox").hide();
	});
});