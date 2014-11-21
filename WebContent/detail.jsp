<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="web.PageMaker"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Details</title>
<link rel="stylesheet" type="text/css" href="./general.css">

<%
	String id = request.getParameter("id");
%>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
	google.load("visualization", "1", {
		packages : [ "corechart" ]
	});

	google.setOnLoadCallback(drawChart);

	function drawChart(str) {
		var data = new google.visualization.DataTable();
		data.addColumn('datetime', 'Date');
		data.addColumn('number', 'Value');

		var table = document.getElementById("tblValues");
		for (var i = 0, row; row = table.rows[i]; i++) {
			if (i > 0) {
				var dt = row.cells[0].innerHTML;
				var vl = row.cells[1].innerHTML;

				var year = parseInt(dt.substring(0, 4));
				var month = parseInt(dt.substring(5, 7));
				var day = parseInt(dt.substring(8, 10));
				var hour = parseInt(dt.substring(11, 13));
				var min = parseInt(dt.substring(14, 16));
				var sec = parseInt(dt.substring(17, 20));

				data.addRow([ new Date(year, month, day, hour, min, sec),
						parseInt(vl) ]);
			}
		}

		var options = {
			title : 'Değerler',
			hAxis : {
				title : 'Zaman',
			}
		};

		var chart = new google.visualization.ColumnChart(document
				.getElementById('chart_div'));

		chart.draw(data, options);
	}
</script>

</head>
<body>
	<%
		out.write("<CENTER><BOLD>" + id
				+ " Detay Raporu</BOLD></CENTER><BR><BR>");
		//out.write("Rapor Zamanı: " + KmshUtil.getCurrentTimeStamp());
	%>
	<div style="width: 270px; height: 500px; float: left">
		<%
			out.write(PageMaker.getDetail(id));
		%>
	</div>
	<div id="chart_div"
		style="width: 600px; height: 500px; float: left; margin-left: 50px;"></div>
</body>
</html>