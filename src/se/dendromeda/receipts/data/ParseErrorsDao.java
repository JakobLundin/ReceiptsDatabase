package se.dendromeda.receipts.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import se.dendromeda.receipts.model.ParseError;

public class ParseErrorsDao {
	
	private ReceiptDao recDao = new ReceiptDao();
	
	private Connection connect() throws SQLException{
		Connection conn = null;
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", "root");
	    connectionProps.put("password", "root");

	    conn = DriverManager.getConnection(
	    		"jdbc:mysql://localhost:3306/receipt_database",
	    				connectionProps);

	    System.out.println("Connected to database");
	    return conn;
	}
	
	private void disconnect(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
				System.out.println("Disconnected from database");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void add(ParseError error){
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO parse_errors (receipt, text) VALUES (?, ?)");
			stmt.setInt(1, error.getReceipt().getId());
			stmt.setString(2, error.getText());
			stmt.execute();
			System.out.println("ParseError add: " + error);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
	}
	
	public List<ParseError> getAll() {
		List<ParseError> list = new ArrayList<>();
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM parse_errors");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ParseError error = new ParseError(rs.getInt("id"), recDao.getById(rs.getInt("receipt")), rs.getString("text"));
				list.add(error);
			}
			System.out.println("ParseError getAll");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
		return list;
	}
}
