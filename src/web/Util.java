package web;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tr.com.telekom.kmsh.config.GroupCommandConfig;
import tr.com.telekom.kmsh.config.PeriodicCommandConfig;
import tr.com.telekom.kmsh.manager.CommandManager;
import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.KmshUtil;
import tr.com.telekom.kmsh.util.Table;

public class Util {
	private static final SimpleDateFormat dIn = new SimpleDateFormat(
			"yyyy-MMM-d");
	private static final SimpleDateFormat dOut = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static String getSummary(String id) {
		String str = H2Util.getSummary(id, 14).getHTML(id);
		return "<div hidden=true>" + str + "</div>";
	}

	public static String getDetail(String name) {
		String max = ConfigReader.getInstance().getProperty("MAX_VALUE");
		String sql = "select date, value from tblKey where name='" + name
				+ "' order by date desc limit " + max;
		String out = H2Util.readAsTable(sql).getHTML(name);

		return out;
	}

	public static String getTimeMap(String cmdName) {
		Object obj = CommandManager.execute(cmdName);
		String out = "<TABLE id=\"" + cmdName
				+ "\">\n<TR><TD></TD><TD></TD></TR>\n";
		if (obj instanceof String) {
			String str = (String) obj;
			String yearStr[] = str.split("year=");

			for (int i = 1; i < yearStr.length; i++) {
				String y = yearStr[i];
				int idx = y.indexOf(" ");
				int year = 2000 + new Integer(y.substring(0, idx)).intValue();
				y = y.substring(idx + 3, y.length());

				String monthStr[] = y.split("}");
				for (int j = 0; j < monthStr.length - 1; j++) {
					String m = monthStr[j];
					idx = m.indexOf("{");
					String month = m.substring(0, idx).trim();

					m = m.substring(idx + 1, m.length());

					String dayStr[] = m.split(" ");
					for (int k = 1; k < dayStr.length; k++) {
						String d = dayStr[k].split("=")[0];
						d = d.replace("d", "");
						String v = dayStr[k].split("=")[1];

						String dateStr = "";
						try {
							Date date = dIn.parse(year + "-" + month + "-" + d);

							dateStr = dOut.format(date.getTime());
							out += "<TR><TD>" + dateStr + "</TD><TD>" + v
									+ "</TD></TR>\n";
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// System.out.println(dateStr + ":" + v);
					}
				}

			}
		}

		out += "</TABLE>";
		return "<div hidden=true>" + out + "</div>";
	}

	public static String processCommandFile(String file) {
		String out = "";
		File xmlFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("group");

			for (int i = 0; i < nList.getLength(); i++) {
				GroupCommandConfig keyConf = new GroupCommandConfig();
				keyConf.parseXML(nList.item(i));

				Table table = null;
				for (PeriodicCommandConfig cmd : keyConf.commandList) {
					String sql = "select date, value from tblKey where id='"
							+ cmd.id + "' order by date desc";

					table = H2Util.readAsTable(sql);

					if (table.size() > 1) {
						@SuppressWarnings("unchecked")
						ArrayList<String> row = table.get(1);
						out += "<TR>";

						String date = row.get(0);
						String val = row.get(1);

						out += "<TD>" + date + "</TD><TD>" + cmd.name
								+ "</TD><TD>" + val + "</TD>";

						if (val != null && KmshUtil.isNumeric(val)) {
							out += "<TD><CENTER><INPUT TYPE=\"BUTTON\" VALUE=\"?\" ONCLICK=\"detail('"
									+ cmd.name + "')\"><CENTER></TD>";
						} else {
							out += "<TD></TD>";
						}

						out += "</TR>\n";
					}
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return out;
	}
}
