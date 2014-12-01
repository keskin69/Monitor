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
	String name = request.getParameter("name");
%>

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
	<label id="rptName" hidden="true"><%=name%></label>
	<%
		out.write("<CENTER><BOLD>" + name
				+ " Detay Raporu</BOLD></CENTER><BR><BR>");
	%>
	<div style="width: 260px; height: 500px; float: left">
		<%
			out.write(PageMaker.getDetail(name));
		%>
	</div>
	<div id="chart_div"
		style="width: 550px; height: 500px; float: left; margin-left: 50px;"></div>
</body>
</html>