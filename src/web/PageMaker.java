package web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.KmshLogger;
import tr.com.telekom.kmsh.util.KmshUtil;
import tr.com.telekom.kmsh.util.Table;

public class PageMaker {
	// query db table, generate a page
	private NodeList fileList = null;
	private String base = null;

	public PageMaker(String confFile) {
		if (confFile != null) {
			ConfigReader.file = confFile;
			ConfigReader conf = ConfigReader.getInstance();
			String keyFileName = conf.getProperty("xmlFiles");
			base = conf.getProperty("base");

			File xmlFile = new File(keyFileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
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
	}

	public String getLogItems() {
		String out = "Rapor Zamanı: " + KmshUtil.getCurrentTimeStamp(0);
		out += "<BR><BR>\n<TABLE border=\"1\">";
		out += "<TR><TH>Zaman</TH><TH>Veri</TH><TH>Değer</TH><TH>Detay</TH></TR>";

		for (int i = 0; i < fileList.getLength(); i++) {
			Element eElement = (Element) fileList.item(i);
			String file = base + eElement.getTextContent();

			out += Util.processCommandFile(file);
		}

		out += "</TABLE>";

		return out;
	}

	@SuppressWarnings("unchecked")
	public String getDownTime() {
		String out = "Rapor Zamanı: " + KmshUtil.getCurrentTimeStamp(0);
		out += "<BR><BR>\n<TABLE border=\"1\">";
		out += "<TR><TH>Kapandı</TH><TH>Açıldı</TH><TH>Kapalı Kaldığı Süre (Dakika)</TH></TR>";
		String sql = "select date, value from tblKey where id='cmd3.1' and date >'"
				+ KmshUtil.getCurrentTimeStamp(-90) + "'";

		Table table = H2Util.readAsTable(sql);
		String prevDate = null;
		String newDate = null;
		for (ArrayList<String> row : table) {
			newDate = row.get(0);
			if (row.get(1).contains("stopped")) {
				// stopped
				if (prevDate == null) {
					prevDate = newDate;
				}
			} else {
				// started
				if (prevDate != null) {
					Date pDate = KmshUtil.convertFullDate(prevDate);
					Date nDate = KmshUtil.convertFullDate(newDate);
					double duration = (nDate.getTime() - pDate.getTime())
							/ (1000 * 60);
					out += "<TR><TD>" + prevDate + "</TD><TD>" + newDate
							+ "</TD><TD>" + duration + "</TD></TR>";
					prevDate = null;
				}
			}
		}

		out += "</TABLE>";
		return out;
	}

	public static void main(String args[]) {
		String conf = "/Users/mustafakeskin/Documents/workspace/MonitorLizard/monitor.cfg";

		if (args.length == 1) {
			conf = args[0];
		}

		PageMaker page = new PageMaker(conf);
		KmshLogger.log(1, page.getLogItems());
	}
}
