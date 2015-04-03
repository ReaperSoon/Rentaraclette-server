package fr.stevecohen.database.old;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import fr.stevecohen.rpc.ServicesLoader;
import fr.stevecohen.util.Logger;

public class ORM {
	private String request = "";
	private Database mysql = Database.getInstance();
	
	public synchronized ORM custom(String custom_request) {
		request += custom_request;
		return this;
	}

	public synchronized ORM select(String... values) {
		request += "SELECT ";
		for (int i = 0; i < values.length; i++) {
			if (values[i].equals("*"))
				request += "* ";
			else
				request += "`" + values[i] + "` ";
			if (i != values.length - 1)
				request += ", ";
		}
		return this;
	}

	public synchronized ORM clear() {
		request = "";
		return this;
	}

	public synchronized ORM update(String table) {
		request += "UPDATE `" + table + "` ";
		return this;
	}

	public synchronized ORM set(Map<String, Object> map, boolean secureMode) {
		Set<Entry<String, Object>> entrySet = map.entrySet();
		Iterator<Entry<String, Object>> it = entrySet.iterator();
		request += "SET ";
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			request += "`" + entry.getKey().replaceAll("'", "''") + "` = '"
					+ (secureMode ? "?" : entry.getValue().toString().replaceAll("'", "''")) + "'";
			if (it.hasNext())
				request += ", ";
		}
		request += " ";
		return this;
	}

	public synchronized ORM set(String key, String value) {
		request += "SET `" + key + "` = " + "'" + value.replaceAll("'", "''")
				+ "'";
		return this;
	}
	
	public synchronized ORM set(String[] keys, String[] values) {
		request += "SET ";
		for (int i = 0; i < keys.length; i++) {
			if (values[i] != null)
				request += "`" + keys[i] + "`=" + values[i];
			if (i < keys.length-1)
				request += ",";
		}
		return this;
	}

	public synchronized ORM delete_from(String col) {
		request += "DELETE FROM `" + col + "` ";
		return this;
	}

	public synchronized ORM from(String table) {
		request += "FROM `" + table + "` ";
		return this;
	}

	public synchronized ORM from(String table, String col) {
		request += "FROM `" + table + "`" + ".`" + col + "` ";
		return this;
	}

	public synchronized ORM where(String key, String comparator, String value) {
		request += "WHERE `" + key + "` " + comparator + " '" + value + "' ";
		return this;
	}

	public synchronized ORM where(String key, String[] values) {
		request += "WHERE `" + key + "` in (" + StringUtils.join(values, ", ")
				+ ")";
		return this;
	}

	public synchronized ORM replace_into(String table, String... keys) {
		request += "REPLACE INTO `" + table + "` " + "(";
		for (int i = 0; i < keys.length; i++) {
			request += "`" + keys[i] + "`";
			if (i != keys.length - 1)
				request += ", ";
		}
		request += ") ";
		return this;
	}

	public synchronized ORM insert_into(String table, String... keys) {
		for (int i = 0; i < keys.length; i++) {
			if (!keys[i].startsWith("`") && !keys[i].endsWith("`"))
				keys[i] = "`" + keys[i] + "`";
		}
		request += "INSERT INTO `" + table + "` " + "(";
		request += StringUtils.join(keys, ",");
		request += ") ";
		return this;
	}

	public synchronized ORM values(Object... values) {
		request += "VALUES(";
		request += StringUtils.join(values, ",");
		request += ") ";
		return this;
	}

	public ResultSet execQuery(PrepareStatementCallback prepareStatementCallback) throws SQLException {
		if (!mysql.isConnected())
			ServicesLoader.getInstance().setupHibernate();
		ResultSet res = null;
		Statement statement = null;
		if (prepareStatementCallback != null) {
			statement = mysql.createPreparedStatement(request);
			prepareStatementCallback.prepareStatement((PreparedStatement) statement);
			res = ((PreparedStatement) statement).executeQuery();
		}else {
			statement = mysql.createStatement();
			res = statement.executeQuery(request);
		}
		Logger.log(Logger.LOG, request);
		this.clear();
		return res;
	}

	public int execUpdate(PrepareStatementCallback prepareStatementCallback) throws SQLException {
		if (!mysql.isConnected())
			ServicesLoader.getInstance().setupHibernate();
		Logger.log(Logger.LOG, request);
		int id = -1;
		Statement statement = null;
		if (prepareStatementCallback != null) {
			statement = mysql.createPreparedStatement(request);
			prepareStatementCallback.prepareStatement((PreparedStatement) statement);
			((PreparedStatement) statement).executeUpdate();
			ResultSet rs = ((PreparedStatement) statement).getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		}else {
			statement = mysql.createStatement();
			statement.executeUpdate(request);
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		}
		this.clear();
		return id;
	}

	@Override
	public synchronized String toString() {
		return request;
	}
}
