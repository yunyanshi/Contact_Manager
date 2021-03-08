import sun.security.util.Password;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.HashMap;

public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mysql";
    private static final String USER = "root";
    private static final String PASSWORD = "Syy19930305";
    Connection connection;
    Statement statement;

    public DBConnection() {
        try {
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            statement = connection.createStatement();
            System.out.println("Connected Successfully");
        } catch(SQLException err) {
            System.out.println(err.getMessage());
        }
    }

    public ResultSet getContactListResultSet() {
        HashMap<Integer, String> map = new HashMap<>();
        String query = "SELECT user_id, name FROM Contacts order BY name";
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


}
