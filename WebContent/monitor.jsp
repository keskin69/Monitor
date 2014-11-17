<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="web.PageMaker"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>KMSH/FÜS Monitor Sayfası</title>
</head>
<body>
	<%
		String confFile = request.getParameter("conf");
		String keyList = request.getParameter("key");

		confFile = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/keyList.xml";
		keyList = "key1";
		if (confFile == null) {
			out.write("Provide configuration xml file path with ?conf= parameter");
		} else {
			if (keyList == null) {
				out.write("Provide key list name with ?key= parameter");

			} else {
				PageMaker monPage = new PageMaker(confFile, keyList);
				out.write(monPage.process());
			}
		}
	%>

	<SCRIPT>
		function detail(key) {
			var win = window.open("detail.jsp?key=" + key, '_blank');
			win.focus();
		}
	</SCRIPT>
</body>
</html>
