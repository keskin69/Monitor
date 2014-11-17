package web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import tr.com.telekom.kmsh.config.Key;

public class H2Reader {
	public static String readDB(ArrayList<Key> keyList) {
		Connection conn = null;
		String out = "";

		try {
			Class.forName("org.h2.Driver");

			conn = DriverManager.getConnection(
					"jdbc:h2:tcp://localhost/~/kmsh", "sa", "");
			Statement stat = conn.createStatement();

			for (Key key : keyList) {
				String sql = "select * from tblKey where key='" + key.name
						+ "' order by date desc";

				ResultSet rs = stat.executeQuery(sql);

				// read only one line
				if (rs.next()) {
					out += rs.getString("date") + ";";
					out += rs.getString("key") + ";";
					out += rs.getString("value");

					if (!out.endsWith("\n")) {
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

		try {
			Class.forName("org.h2.Driver");

			conn = DriverManager.getConnection(
					"jdbc:h2:tcp://localhost/~/kmsh", "sa", "");
			Statement stat = conn.createStatement();

			String sql = "select * from tblKey where key='" + key
					+ "' order by date desc";

			ResultSet rs = stat.executeQuery(sql);
			int cnt = 0;
			while (rs.next() && cnt < 50) {
				cnt++;
				out += rs.getString("date") + ";";
				out += rs.getString("key") + ";";
				out += rs.getString("value");

				if (!out.endsWith("\n")) {
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
