import javax.xml.transform.Result;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

	public int createContact(String name, String phone, LocalDate dob,
							 String email, String address, String notes) {
		int id = getNextId();
		String query = "INSERT INTO CONTACTS VALUES(?,?,?,?,?,?,?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, (phone.length() == 0) ? null : phone);
			preparedStatement.setString(4, (email.length() == 0) ? null : email);
			preparedStatement.setDate(5, (dob == null) ? null : Date.valueOf(dob));
			preparedStatement.setString(6, (address.length() == 0) ? null : address);
			preparedStatement.setString(7, (notes.length() == 0) ? null : notes);
			preparedStatement.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return id;
	}

	public String updateContact(int user_id, String name, String phone,
								String email, LocalDate dob, String address, String notes) {
		CallableStatement callSt = null;
		try {
			callSt = connection.prepareCall("{?= call updateExistingContact(?,?,?,?,?,?,?)}");
			callSt.registerOutParameter(1, Types.VARCHAR);
			callSt.setInt(2, user_id);
			callSt.setString(3, name);
			callSt.setString(4, (phone.length() == 0) ? null : phone);
			callSt.setString(5, (email.length() == 0) ? null : email);
			callSt.setDate(6, (dob == null) ? null : Date.valueOf(dob));
			callSt.setString(7, (address.length() == 0) ? null : address);
			callSt.setString(8, (notes.length() == 0) ? null : notes);
			callSt.execute();
			
			return callSt.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}

	public void deleteContact(int user_id) {
		String query = "DELETE FROM CONTACTS WHERE USER_ID = " + user_id;
		try {
			statement.executeUpdate(query);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public boolean ifBelongs(int user_id, String tab) {
		String query = "SELECT COUNT(*) FROM (SELECT * FROM " + tab + " WHERE user_id = " + user_id + ") T";
		try {
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet != null && resultSet.next()) {
				return resultSet.getInt(1) == 1;
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return false;
	}

	public int getNextId() {
		String query = "SELECT MAX(user_id) from Contacts";
		try {
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet != null && resultSet.next()) {
				return resultSet.getInt(1) + 1;
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return 0;
	}

	public void addToTab(String tab, int user_id) {
		String query = "INSERT INTO " + tab + " VALUES (" + user_id + ")";
		try {
			statement.executeUpdate(query);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

	}

}
