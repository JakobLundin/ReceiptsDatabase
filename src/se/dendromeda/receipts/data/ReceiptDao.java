package se.dendromeda.receipts.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import se.dendromeda.receipts.model.Receipt;

public class ReceiptDao implements DataAccess<Receipt> {
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
	
	private Receipt extractReceipt(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String date = rs.getString("date");
		
		Receipt rec = new Receipt(id, date);
		return rec;
		
	}
	
	@Override
	public List<Receipt> getAll() {
		List<Receipt> list = new ArrayList<>();
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM receipts");
			ResultSet rs = stmt.executeQuery();		
			
			while (rs.next()) {
				list.add(extractReceipt(rs));
			}
			System.out.println("Receipt getAll");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
		return list;
		
	}

	@Override
	public Receipt getById(int id) {
		Receipt rec = null;
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM receipts WHERE id = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();		
			
			if (rs.next()) {
				rec = extractReceipt(rs);
			}
			System.out.println("Receipt get by id: " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
		return rec;
	}

	@Override
	public Receipt getByName(String name) {
		Receipt rec = null;
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM receipts WHERE name = ?");
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();		
			
			if (rs.next()) {
				rec = extractReceipt(rs);
			}
			System.out.println("Receipt get by name: " + name);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
		return rec;
	}

	@Override
	public void update(Receipt obj) {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("UPDATE receipts SET date = ? WHERE id = ?");
			stmt.setString(1, obj.getDate());
			stmt.setInt(2, obj.getId());
			stmt.execute();		
			System.out.println("Receipt update: " + obj);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
	}

	@Override
	public void create(Receipt obj) {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO receipts (`date`) VALUES (?)");
			stmt.setString(1, obj.getDate());
			stmt.execute();
			ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() AS id");
			rs.next();
			obj.setId(rs.getInt("id"));
			System.out.println("Receipt create: " + obj);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
	}

	@Override
	public void delete(Receipt obj) {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM receipts WHERE id = ?");
			stmt.setInt(1, obj.getId());
			stmt.execute();		
			System.out.println("Receipt delete: " + obj);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
	}

	@Override
	public void deleteById(int id) {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM receipts WHERE id = ?");
			stmt.setInt(1, id);
			stmt.execute();		
			System.out.println("Receipt delete by id: " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
	}
}
