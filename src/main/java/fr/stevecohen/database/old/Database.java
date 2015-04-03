package fr.stevecohen.database.old;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContextEvent;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import fr.stevecohen.util.Logger;

public class Database implements javax.servlet.ServletContextListener {

	private Connection 				connection = null;
	private PreparedStatement 		statement = null;
	private int						connexionFailed = 0;
	private static Database 		instance = null;

	/* Update this to change the database */
	private String		addr;
	private String		port;
	private String		bdd;
	private String		user;
	private String		pass;

	public Database() throws Exception {
		if (instance != null)
			throw (new Exception("MySQL is already instanciated, please use MySQL.getInstance()"));
	}

	public static Database getInstance() {
		if (instance == null)
			try {
				instance = new Database();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return instance;
	}
	
	public boolean initialize() {
		JSONTokener tokener;
		try {
			tokener = new JSONTokener(this.getClass().getClassLoader().getResourceAsStream("config.json"));
			JSONObject root = new JSONObject(tokener);
			JSONObject MYSQLconf = root.getJSONObject("MYSQL");
			addr = MYSQLconf.getString("HOST");
			port = MYSQLconf.getString("PORT");
			bdd = MYSQLconf.getString("DATABASE");
			user = MYSQLconf.getString("USER");
			pass = MYSQLconf.getString("PASSWORD");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return connect();
	}

	public boolean connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Logger.log(Logger.INFO, "Connecting to jdbc:mysql://" + addr + ":" + port + "/" + bdd + "..." + ((connexionFailed > 0) ? (" (try " + connexionFailed + ")") : ""));
			connection = DriverManager.getConnection(
					"jdbc:mysql://" + addr + ":" + port + "/" + bdd + "?autoReconnect=true",
					user, pass);
			if (!this.isConnected()) {
				this.connexionFailed++;
				return false;
			}
				
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			this.connexionFailed++;
			return false;
		} catch (SQLException e) {
			//quietly exception
			this.connexionFailed++;
			return false;
		}
		return true;
	}
	
	public boolean isConnected() {
		boolean valid = false;
		try {
			valid = connection.isValid(1000);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return valid;
	}

	public boolean closeSession() {
		try {
			connection.close();
			if (connection.isClosed()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Database.instance = this;
		if (initialize()) {
			Logger.log(Logger.INFO, "Connected successfully");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		closeSession();
	}

	public PreparedStatement createPreparedStatement(String request) throws SQLException {
		return connection.prepareStatement(request);
	}

	public Statement createStatement() throws SQLException {
		return connection.createStatement();
	}

	public int getConnexionFailed() {
		return connexionFailed;
	}

	public void setConnexionFailed(int connexionFailed) {
		this.connexionFailed = connexionFailed;
	}
}

