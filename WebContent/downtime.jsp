<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="web.PageMaker"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="general.css">
<title>KMSH/FÜS Çalışma Raporu</title>
</head>
<body>
	<%
		String confFile = request.getParameter("conf");

		//confFile = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/monitor.cfg";

		if (confFile == "") {
			out.write("Provide configuration file path with ?conf= parameter");
		} else {
			PageMaker monPage = new PageMaker(confFile);
			out.write(monPage.getDownTime());
		}
	%>
</body>
</html>
