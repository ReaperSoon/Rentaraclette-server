package fr.stevecohen.database.old;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseUtil {

	private static DatabaseUtil instance = null;

	private DatabaseUtil() {}

	public static DatabaseUtil getInstance() {
		if (instance == null)
			instance = new DatabaseUtil();
		return instance;
	}

	public List<HashMap<String, Object>> getResultFrom(ResultSet rs) throws SQLException{
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		while (rs.next()){
			HashMap<String, Object> row = new HashMap<String, Object>(columns);
			for(int i=1; i<=columns; ++i){           
				row.put(md.getColumnName(i),rs.getObject(i));
			}
			list.add(row);
		}
		return list;
	}
	
	public HashMap<String, Object> getUniqueResultFrom(ResultSet rs) throws SQLException {
		if (!rs.next())
			return null;
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		HashMap<String, Object> result = new HashMap<String, Object>(columns);
		for(int i=1; i<=columns; ++i){           
			result.put(md.getColumnName(i), rs.getObject(i));
		}
		return result;
	}
}
