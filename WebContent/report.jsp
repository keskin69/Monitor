<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="web.PageMaker,tr.com.telekom.kmsh.util.SQLUtil,tr.com.telekom.kmsh.util.ConfigReader,tr.com.telekom.kmsh.util.KmshUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="general.css">
<title>KMSH/FÜS Sistemi Dashboard</title>
<script type="text/javascript" src="chart.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi">
	
</script>
<script type="text/javascript">
	google.load("visualization", "1", {
		packages : [ "corechart" ]
	});

	google.setOnLoadCallback(function() {
		drawChart2D("UpTime %", "UpTime");
	});

	google.setOnLoadCallback(function() {
		drawChart2D("Ortalama Bildirim Çıkma Hızı (Dakika)", "AveBildirim");
	});

	google.setOnLoadCallback(function() {
		drawChart3D("Toplam Bildirim Sayıları", "KMSH80", "KMSH100");
	});

	google.setOnLoadCallback(function() {
		drawChart3D("İletim Sayıları", "DeliveryPending", "DeliveryCompleted");
	});
</script>
</head>
<body>
	<center>
		<b>KMSH/FÜS Dashboard</b>
	</center>

	<%
		String confFile = request.getParameter("conf");

		//confFile = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/monitor.cfg";

		if (confFile == "") {
			out.write("Provide configuration file path with ?conf=parameter");
		}

		ConfigReader.file = confFile;
		String result = KmshUtil.getCurrentTimeStamp(0) + "<BR><BR><div>";
		result += "Status: ";
		result += SQLUtil.readDB("cmd3.1", "value").contains("running") ? "<font color=\"green\">Çalışıyor</font>"
				: "<font color=\"red\">Durmuş</font>";
		result += "<BR>";

		// uptime
		result += "UpTime%: " + SQLUtil.readDB("UpTime", "value") + "<BR>";

		// notification delay
		result += "Ortalama Bildirim Çıkma Zamanı (Dakika): "
				+ SQLUtil.readDB("AveBildirim", "value") + "<BR>";

		result += "<BR></div>";
		out.write(result);

		// Weekly Notif
		out.write(PageMaker.getSummary("KMSH80"));
		out.write(PageMaker.getSummary("KMSH100"));

		// Weekly uptime
		out.write(PageMaker.getSummary("UpTime"));

		// Weekly Notif delay
		out.write(PageMaker.getSummary("AveBildirim"));

		// Weekly Delivery
		out.write(PageMaker.getSummary("DeliveryPending"));
		out.write(PageMaker.getSummary("DeliveryCompleted"));
	%>
	<div id="chart_div_KMSH80"
		style="width: 520px; height: 400px; float: left;"></div>

	<div id="chart_div_UpTime"
		style="width: 520px; height: 400px; float: left;"></div>

	<div id="chart_div_AveBildirim"
		style="width: 520px; height: 400px; float: left;"></div>

	<div id="chart_div_DeliveryPending"
		style="width: 520px; height: 400px; float: left;"></div>


</body>
</html>