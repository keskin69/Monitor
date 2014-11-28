package web;

import java.io.File;

import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.H2Reader;
import tr.com.telekom.kmsh.util.KmshLogger;
import tr.com.telekom.kmsh.util.KmshUtil;
import tr.com.telekom.kmsh.util.Table;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tr.com.telekom.kmsh.config.GroupCommandConfig;
import tr.com.telekom.kmsh.config.PeriodicCommandConfig;

public class PageMaker {
	// query db table, generate a page
	private NodeList fileList = null;
	private String base = null;

	public PageMaker(String confFile) {
		ConfigReader.file = confFile;
		ConfigReader conf = ConfigReader.getInstance();
		String keyFileName = conf.getProperty("xmlFiles");
		base = conf.getProperty("base");

		File xmlFile = new File(keyFileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(base + xmlFile);
			doc.getDocumentElement().normalize();
			fileList = doc.getElementsByTagName("commandsFile");
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
	}

	public String process() {
		String out = "Rapor Zamanı: " + KmshUtil.getCurrentTimeStamp();
		out += "<BR><BR>\n<TABLE border=\"1\">";
		out += "<TR><TH>Zaman</TH><TH>Veri</TH><TH>Değer</TH><TH>Detay</TH></TR>";

		for (int i = 0; i < fileList.getLength(); i++) {
			Element eElement = (Element) fileList.item(i);
			String file = base + eElement.getTextContent();

			out += processCommandFile(file);
		}

		out += "</TABLE>";

		return out;
	}

	public String processCommandFile(String file) {
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

					table = H2Reader.readAsTable(sql);

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
									+ cmd.id + "')\"><CENTER></TD>";
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

	public static String getDetail(String id) {
		String out = "";
		String sql = "select date, value from tblKey where id='" + id
				+ "' order by date desc;";
		String values = H2Reader.readAsTable(sql).getString();

		out += "<TABLE id=\"tblValues\" border=\"1\">";
		out += "<thead><TR><TH>Zaman</TH><TH>Değer</TH></TR></thead><tbody>";

		int cnt = 0;
		for (String row : values.split("\n")) {
			if (cnt > ConfigReader.getInstance().getInt("MAX_VALUE")) {
				break;
			}
			out += "<TR>";

			for (String col : row.split(";")) {
				out += "<TD>" + col + "</TD>\n";
			}

			out += "</TR>";

		}

		out += "</tbody></TABLE>";

		return out;
	}

	public static void main(String args[]) {
		String conf = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/monitor.cfg";

		if (args.length == 1) {
			conf = args[0];
		}

		PageMaker page = new PageMaker(conf);
		KmshLogger.log(1, page.process());
	}
}
