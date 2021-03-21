import java.sql.*;
import java.time.LocalDate;

public class DBConnection {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/mysql";
	private static final String USER = "root";
	private static final String PASSWORD = "Syy19930305";
	private Connection connection;
	private Statement statement;

	DBConnection() {
		try {
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			statement = connection.createStatement();
			System.out.println("Connected Successfully");
		} catch (SQLException err) {
			System.out.println(err.getMessage());
		}
	}

	ResultSet getContactListResultSet(String tab) {
		String query;
		if (tab.equals("Contacts")) {
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

	ResultSet getContactInfoResultSet(int user_id) {
		String query = "SELECT * FROM Contacts WHERE user_id = " + user_id;
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(query);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return resultSet;
	}

	int createContact(String name, String phone, LocalDate dob,
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

	void editContact(int user_id, String name, String phone,
					 String email, LocalDate dob, String address, String notes) {
		String query = "UPDATE CONTACTS SET name = ?, phone_number = ?, email = ?, " +
				"birthday = ?, address = ?, notes = ? WHERE user_id = ? ";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, (phone.length() == 0) ? null : phone);
			preparedStatement.setString(3, (email.length() == 0) ? null : email);
			preparedStatement.setDate(4, (dob == null) ? null : Date.valueOf(dob));
			preparedStatement.setString(5, (address.length() == 0) ? null : address);
			preparedStatement.setString(6, (notes.length() == 0) ? null : notes);
			preparedStatement.setInt(7, user_id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	void deleteContact(int user_id) {
		String query = "DELETE FROM CONTACTS WHERE USER_ID = " + user_id;
		try {
			statement.executeUpdate(query);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	boolean ifBelongs(int user_id, String tab) {
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

	int getNextId() {
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

	void addTag(String tab, int user_id) {
		String query = "INSERT INTO " + tab + " VALUES (" + user_id + ")";
		try {
			statement.executeUpdate(query);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

	}

	void deleteTag(String tab, int user_id) {
		String query = "DELETE FROM " + tab + " WHERE user_id = " + user_id;
		try {
			statement.executeUpdate(query);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}
}
