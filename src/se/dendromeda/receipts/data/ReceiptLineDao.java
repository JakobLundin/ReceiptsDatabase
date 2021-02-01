package se.dendromeda.receipts.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import se.dendromeda.receipts.model.Product;
import se.dendromeda.receipts.model.Receipt;
import se.dendromeda.receipts.model.ReceiptLine;
import se.dendromeda.receipts.model.ReceiptLine;

public class ReceiptLineDao implements DataAccess<ReceiptLine>{
	
	ReceiptDao receiptDao = new ReceiptDao();
	ProductDao productDao = new ProductDao();
	
	
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
	
	private ReceiptLine extractReceiptLine(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String text = rs.getString("text");
		Receipt rec = receiptDao.getById(rs.getInt("receipt"));
		Product prod = productDao.getById(rs.getInt("product"));
		double price = rs.getDouble("price");
		ReceiptLine line = new ReceiptLine(id, rec, prod, text, price);
		return line;
		
	}
	
	@Override
	public List<ReceiptLine> getAll() {
		List<ReceiptLine> list = new ArrayList<>();
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM receipt_lines");
			ResultSet rs = stmt.executeQuery();		
			
			while (rs.next()) {
				list.add(extractReceiptLine(rs));
			}
			System.out.println("Receipt Line getAll");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
		return list;
		
	}

	@Override
	public ReceiptLine getById(int id) {
		ReceiptLine cat = null;
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM receipt_lines WHERE id = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();		
			
			if (rs.next()) {
				cat = extractReceiptLine(rs);
			}
			System.out.println("Receipt Line get by id: " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
		return cat;
	}

	@Override
	public ReceiptLine getByName(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(ReceiptLine obj) {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("UPDATE receipt_lines SET receipt = ?, product = ?, text = ?, price = ? WHERE id = ?");
			stmt.setInt(1, obj.getReceipt().getId());
			stmt.setInt(2, obj.getProduct().getId());
			stmt.setString(3, obj.getText());
			stmt.setDouble(4, obj.getPrice());
			stmt.setInt(5, obj.getId());
			stmt.execute();		
			System.out.println("Receipt Line update: " + obj);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
	}

	@Override
	public void create(ReceiptLine obj) {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO receipt_lines (receipt, product, text, price) VALUES (?, ?, ?, ?)");
			stmt.setInt(1, obj.getReceipt().getId());
			stmt.setInt(2, obj.getProduct().getId());
			stmt.setString(3, obj.getText());
			stmt.setDouble(4, obj.getPrice());
			stmt.execute();
			ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() AS id");
			rs.next();
			obj.setId(rs.getInt("id"));
			System.out.println("Receipt Line create: " + obj);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
	}

	@Override
	public void delete(ReceiptLine obj) {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM receipt_lines WHERE id = ?");
			stmt.setInt(1, obj.getId());
			stmt.execute();		
			System.out.println("Receipt Line delete: " + obj);
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
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM receipt_lines WHERE id = ?");
			stmt.setInt(1, id);
			stmt.execute();		
			System.out.println("Receipt Line delete by id: " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
	}
}
