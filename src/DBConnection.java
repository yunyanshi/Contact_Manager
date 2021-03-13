import javax.xml.transform.Result;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class DBConnection {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/mysql";
	private static final String USER = "root";
	private static final String PASSWORD = "Syy19930305";
	Connection connection;
	Statement statement;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public DBConnection() {
		try {
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			statement = connection.createStatement();
			System.out.println("Connected Successfully");
		} catch (SQLException err) {
			System.out.println(err.getMessage());
		}
	}

	public ResultSet getContactListResultSet(String tab) {
		String query;
		if (tab.equals("contact")) {
			query = "SELECT user_id, name FROM Contacts order BY name";
		} else {
			query = "SELECT user_id, name FROM Contacts Where user_id in " + "(SELECT user_id FROM " + tab
					+ ") order BY name";
		}

		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(query);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return resultSet;
	}

	public ResultSet getContactInfoResultSet(int user_id) {
		String query = "SELECT * FROM Contacts WHERE user_id = " + user_id;
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(query);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return resultSet;
	}

	
	public String addNewContacts(String name, String phone_number, String email, String dob, String address,
			String notes) {

		CallableStatement callSt = null;
		try {
			callSt = connection.prepareCall("{?= call addNewContacts(?,?,?,?,?,?)}");
			callSt.registerOutParameter(1, Types.VARCHAR);
			callSt.setString(2, name);
			callSt.setString(3, phone_number);
			callSt.setString(4, email);
			java.util.Date date = sdf.parse(dob);
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			callSt.setDate(5, sqlDate);
			callSt.setString(6, address);
			callSt.setString(7, notes);
			callSt.execute();
			connection.close();
			callSt.close();
			return callSt.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (callSt != null)
					callSt.close();
				if (connection != null)
					connection.close();
			} catch (Exception ex) {
			}
		}
		return null;
	}

	public String updateExistContact(String user_id, String name, String phone_number, String email, String dob,
			String address, String notes) {

		CallableStatement callSt = null;
		try {
			callSt = connection.prepareCall("{?= call updateExistingContact(?,?,?,?,?,?,?)}");
			callSt.registerOutParameter(1, Types.VARCHAR);
			callSt.setInt(2, Integer.parseInt(user_id));
			callSt.setString(3, name);
			callSt.setString(4, phone_number);
			callSt.setString(5, email);
			java.util.Date date = sdf.parse(dob);
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			callSt.setDate(6, sqlDate);
			callSt.setString(7, address);
			callSt.setString(8, notes);
			callSt.execute();
			connection.close();
			callSt.close();
			return callSt.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (callSt != null)
					callSt.close();
				if (connection != null)
					connection.close();
			} catch (Exception ex) {
			}
		}
		return null;
	}
	
	public void deleteCurrentContact(String user_id) {
		String query = "DELETE FROM contacts WHERE user_id = " + Integer.parseInt(user_id);
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(query);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}	
	}

}
