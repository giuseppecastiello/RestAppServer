import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	public static final String JDBCDriver = "com.mysql.cj.jdbc.Driver";
	public static final String JDBCURL = "jdbc:mysql://localhost:3306/DBRestApp?"
			//+ "user=bepis&password=OopRestApp"
			+ "user=root&password=nbicocchi"
			+ "&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=CET";
	protected Statement statement;
	protected Connection connection;

	public DBManager(String JDBCDriver, String JDBCURL) throws ClassNotFoundException, SQLException {
		Class.forName(JDBCDriver);
		connection = DriverManager.getConnection(JDBCURL);
		statement = connection.createStatement();
		statement.setQueryTimeout(30); 
	}
	
		public ResultSet executeQuery(String query) throws SQLException {
			return statement.executeQuery(query);
		}

		public int executeUpdate(String query) throws SQLException {
			return statement.executeUpdate(query);
		}

		public void close() throws SQLException {
			if (connection != null) {
				statement.close();
				connection.close();
			}
		}
		/*
	public static void main(String args[]) throws ClassNotFoundException, SQLException{
		DBManager db = new DBManager(DBManager.JDBCDriver,DBManager.JDBCURL);
		int r = db.executeUpdate("INSERT INTO prodotto VALUES (4,'CocaCola',10,3,'Bibita');");
		System.out.println(r);
		/*while (rs.next()) {
			System.out.println(rs.getString("nome") + " " + rs.getString("cognome"));
		 *
		db.close();
	}
		 */
	}