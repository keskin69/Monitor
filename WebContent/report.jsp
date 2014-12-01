<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="tr.com.telekom.kmsh.util.H2Util, tr.com.telekom.kmsh.util.ConfigReader, tr.com.telekom.kmsh.util.KmshUtil"%>
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
		drawChart();
	});
</script>
</head>
<body>
	<center>
		<b>KMSH/FÜS Durum Raporu</b>
	</center>

	<%
		String confFile = request.getParameter("conf");

		confFile = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/monitor.cfg";

		if (confFile == null) {
			out.write("Provide configuration file path with ?conf= parameter");
		} else {
			ConfigReader.file = confFile;
			String result = KmshUtil.getCurrentTimeStamp(0)
					+ "<BR><BR><div>";
			result += "Status: ";
			result += H2Util.readDB("cmd3.1", "value").contains("running") ? "<font color=\"green\">Çalışıyor</font>"
					: "<font color=\"red\">Durmuş</font>";
			result += "<BR>";

			// uptime
			result += "UpTime%: " + H2Util.readDB("UpTime", "value")
					+ "<BR>";

			// notification delay
			result += "Ortalama Bildirim Çıkma Zamanı (Dakika): "
					+ H2Util.readDB("AveBildirim", "value") + "<BR>";

			// cdr delay
			result += "Ortalama CDR İşleme Zamanı (Dakika): "
					+ H2Util.readDB("AveCDRIsleme", "value") + "<BR>";

			result += "</div>";
			out.write(result);
		}
	%>

	<!-- Weekly UpTime -->
	<%
		String result = "<div hidden=true>"
				+ H2Util.getWeeklySummary("UpTime").getHTML("UpTime");
		out.write(result);

		out.write("<label id=\"rptName\" hidden=\"true\">" + "UpTime"
				+ "</label></div>");
	%>

	<div id="chart_div_UpTime"
		style="width: 500px; height: 400px; float: left;"></div>

	<!-- Weekly Notif Delay -->
	<%
		result = "<div hidden=true>"
				+ H2Util.getWeeklySummary("AveBildirim").getHTML("UpTime");
		out.write(result);

		out.write("<label id=\"rptName\" hidden=\"true\">" + "AveBildirim"
				+ "</label></div>");
	%>

	<div id="chart_div_AveBildirim"
		style="width: 500px; height: 400px; float: left;"></div>
</body>
</html>