<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="web.Util,tr.com.telekom.kmsh.util.H2Util,tr.com.telekom.kmsh.util.ConfigReader,tr.com.telekom.kmsh.util.KmshUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>KMSH/FÜS Sistemi Dashboard</title>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="general.css">
<script type="text/javascript" src="chart.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
	google.load("visualization", "1", {
		packages : [ "corechart" ]
	});

	google.setOnLoadCallback(function() {
		drawChart2D("UpTime %", "UpTime");
	});

	google.setOnLoadCallback(function() {
		drawChart2D("Anlık İşlenen CDR Sayısı", "cmdTimeMap");
	});

	google.setOnLoadCallback(function() {
		drawChart3D("Ortalama Bildirim Çıkma Hızı (Dakika)", "AveBildirim",
				"AveDosya");
	});

	google.setOnLoadCallback(function() {
		drawChart3D("Toplam Bildirim Sayıları", "KMSH80", "KMSH100");
	});

	google.setOnLoadCallback(function() {
		drawChart3D("İletim Sayıları", "DeliveryPending", "DeliveryCompleted");
	});

	google.setOnLoadCallback(function() {
		drawChart2D("İşlenen CDR Dosyası", "ToplamDosya");
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

		if (confFile == null) {
			out.write("Provide configuration file path with ?conf=parameter");
		} else {
			ConfigReader.file = confFile;
			String result = KmshUtil.getCurrentTimeStamp(0)
					+ "<BR><BR><div>";
			result += "Status: ";
			result += H2Util.readDB("cmd3.1", "value").contains("running") ? "<font color=\"green\">Çalışıyor</font>"
					: "<font color=\"red\">Çalışmıyor</font>";
			result += "<BR>";

			// uptime
			result += "Çalışma Zamanı %: "
					+ H2Util.readDB("UpTime", "value") + "<BR>";

			// notification delay
			result += "Ortalama Bildirim Çıkma Zamanı (Dakika): "
					+ H2Util.readDB("AveBildirim", "value") + "<BR>";

			result += "Ortalama CDR Transfer Zamanı (Dakika): "
					+ H2Util.readDB("AveDosya", "value") + "<BR>";

			// CDR waitings in the queue
			result += "Queue'da İşlenmeyi Bekleyen CDR sayısı:"
					+ H2Util.readDB("cmd1.1", "value") + "<BR>";

			int pending = 0;
			try {
				pending = new Integer(H2Util.readDB("cmd2.1", "value"))
						.intValue();
			} catch (Exception ex) {

			}

			result += (pending > 10) ? "<font color=\"red\">"
					: "<font color=\"green\">";
			result += "İşlenmeyi Bekleyen CDR Dosyasi:" + pending
					+ "<BR></font>";

			result += "<BR></div>";
			out.write(result);

			// 10 day summary graphs
			// Notif
			out.write(Util.getSummary("KMSH80"));
			out.write(Util.getSummary("KMSH100"));

			// CDR File
			out.write(Util.getSummary("AveDosya"));
			out.write(Util.getSummary("ToplamDosya"));

			// uptime
			out.write(Util.getSummary("UpTime"));

			// Notif delay
			out.write(Util.getSummary("AveBildirim"));

			// Delivery
			out.write(Util.getSummary("DeliveryPending"));
			out.write(Util.getSummary("DeliveryCompleted"));

			// TimeMap Distribution
			out.write(Util.getTimeMap("cmdTimeMap"));
		}
	%>


	<div id="chart_div_KMSH80"
		style="width: 650px; height: 400px; float: left;"></div>

	<div id="chart_div_UpTime"
		style="width: 650px; height: 400px; float: left;"></div>

	<div id="chart_div_AveBildirim"
		style="width: 650px; height: 400px; float: left;"></div>

	<div id="chart_div_DeliveryPending"
		style="width: 650px; height: 400px; float: left;"></div>

	<div id="chart_div_ToplamDosya"
		style="width: 650px; height: 400px; float: left;"></div>

	<div id="chart_div_cmdTimeMap"
		style="width: 650px; height: 400px; float: left;"></div>
</body>
</html>