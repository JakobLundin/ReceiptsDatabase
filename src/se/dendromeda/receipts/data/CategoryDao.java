package se.dendromeda.receipts.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import se.dendromeda.receipts.model.Category;

public class CategoryDao implements DataAccess<Category> {
	public static void main(String args[]) {
		
		
		
	}
	
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
	
	private Category extractCategory(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("name");
		
		Category cat = new Category(id, name);
		return cat;
		
	}
	
	@Override
	public List<Category> getAll() {
		List<Category> list = new ArrayList<>();
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM categories");
			ResultSet rs = stmt.executeQuery();		
			
			while (rs.next()) {
				list.add(extractCategory(rs));
			}
			System.out.println("Category getAll");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
		return list;
		
	}

	@Override
	public Category getById(int id) {
		Category cat = null;
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM categories WHERE id = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();		
			
			if (rs.next()) {
				cat = extractCategory(rs);
			}
			System.out.println("Category get by id: " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
		return cat;
	}

	@Override
	public Category getByName(String name) {
		Category cat = null;
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM categories WHERE name = ?");
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();		
			
			if (rs.next()) {
				cat = extractCategory(rs);
			}
			System.out.println("Category get by name: " + name);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
		return cat;
	}

	@Override
	public void update(Category obj) {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("UPDATE categories SET name = ? WHERE id = ?");
			stmt.setString(1, obj.getName());
			stmt.setInt(2, obj.getId());
			stmt.execute();		
			System.out.println("Category update: " + obj);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
	}

	@Override
	public void create(Category obj) {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO categories (`name`) VALUES (?)");
			stmt.setString(1, obj.getName());
			stmt.execute();
			ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() AS id");
			rs.next();
			obj.setId(rs.getInt("id"));
			System.out.println("Category create: " + obj);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
	}

	@Override
	public void delete(Category obj) {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM categories WHERE id = ?");
			stmt.setInt(1, obj.getId());
			stmt.execute();		
			System.out.println("Category delete: " + obj);
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
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM categories WHERE id = ?");
			stmt.setInt(1, id);
			stmt.execute();		
			System.out.println("Category delete by id: " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
	}
}
