package CodingChallenge.SampleProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.hsqldb.persist.HsqlProperties;
/*
 * Singlton class to manage database interactions
 * Implements iDBManager
 */
public class DBManager implements iDBManager {
	Connection dbConn = null;
	static DBManager mgr = null;
	private static String connection = ConfigFileReader.p.getProperty("dbconnection", "jdbc:hsqldb:hsql://localhost/testdb");
	private DBManager() throws SQLException, ClassNotFoundException {

		dbConn = GetDBConn();
	}

	public static iDBManager GetManager() throws SQLException, ClassNotFoundException {
		if (mgr == null) {
			mgr = new DBManager();
			mgr.ExecuteSQL(
					"CREATE TABLE IF NOT EXISTS logInfo (EventId VARCHAR(50), Duration bigint, Host varchar(50), Type varchar(50), Alert bit)");

		}
		return mgr;
	}

	@Override
	public Connection GetDBConn() throws ClassNotFoundException, SQLException {

		Class.forName("org.hsqldb.jdbcDriver");
		dbConn = DriverManager.getConnection(connection, "SA", "");  
		if (dbConn != null) {
			System.out.println("Connection created successfully");

		} else {
			System.out.println("Problem with creating connection");
		}

		return dbConn;
	}

	@Override
	public void ExecuteSQL(String sql) throws SQLException, ClassNotFoundException {

		Statement stmp = dbConn.createStatement();
		stmp.executeQuery(sql);

	}
}
