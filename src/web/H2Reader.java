package web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import tr.com.telekom.kmsh.config.Key;
import tr.com.telekom.kmsh.util.ConfigReader;

public class H2Reader {

	public static String readDB(ArrayList<Key> keyList) {
		Connection conn = null;
		String out = "";
		ConfigReader conf = ConfigReader.getInstance();
		String DELIM = conf.getProperty("DELIM");

		try {
			Class.forName(conf.getProperty("driver"));

			conn = DriverManager.getConnection(
					conf.getProperty("sqlConnection"),
					conf.getProperty("dbUser"), conf.getProperty("dbPassword"));
			Statement stat = conn.createStatement();

			for (Key key : keyList) {
				String sql = "select * from tblKey where key='" + key.name
						+ "' order by date desc";

				ResultSet rs = stat.executeQuery(sql);

				// read only one line
				if (rs.next()) {
					String d = rs.getString("date");
					String k = rs.getString("key");
					String v = rs.getString("value");

					out += d.trim() + DELIM + k.replace("\n", "").trim()
							+ DELIM + v.trim();
					if (!v.endsWith("\n")) {
						out += "\n";
					}
				}
			}

			conn.close();
			stat.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return out;
	}

	public static String readAll(String key) {
		Connection conn = null;
		String out = "";
		ConfigReader conf = ConfigReader.getInstance();
		String DELIM = conf.getProperty("DELIM");

		try {
			Class.forName(conf.getProperty("driver"));

			conn = DriverManager.getConnection(
					conf.getProperty("sqlConnection"),
					conf.getProperty("dbUser"), conf.getProperty("dbPassword"));
			Statement stat = conn.createStatement();

			String sql = "select * from tblKey where key='" + key
					+ "' order by date desc";

			ResultSet rs = stat.executeQuery(sql);
			int cnt = 0;
			while (rs.next() && cnt < conf.getInt("MAX_VALUE")) {
				cnt++;
				String d = rs.getString("date");
				String k = rs.getString("key");
				String v = rs.getString("value");

				out += d.trim() + DELIM + k.replace("\n", "").trim() + DELIM
						+ v.trim();
				if (!v.endsWith("\n")) {
					out += "\n";
				}
			}

			conn.close();
			stat.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return out;
	}
}
