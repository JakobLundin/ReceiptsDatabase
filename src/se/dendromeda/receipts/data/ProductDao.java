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
import se.dendromeda.receipts.model.Product;

public class ProductDao implements DataAccess<Product>{

	public static void main(String args[]) {
		ProductDao dao = new ProductDao();
		System.out.println(dao.getAll());
		System.out.println(dao.getById(1));
		Product newProduct = new Product(0, "TestProdukt", null);
		dao.create(newProduct);
		Product prod = dao.getByName("TestProdukt");
		System.out.println(prod);
		prod.setName("TestadProdukt");
		dao.update(prod);
		Product prod2 = dao.getById(prod.getId());
		System.out.println(prod2);
		dao.delete(prod2);
	}
	
	private CategoryDao categoryDao = new CategoryDao();
	
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
	
	private Product extractProduct(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("name");
		Category category = categoryDao.getById(rs.getInt("category"));
		
		Product prod = new Product(id, name, category);
		return prod;
		
	}
	
	@Override
	public List<Product> getAll() {
		List<Product> list = new ArrayList<>();
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM products");
			ResultSet rs = stmt.executeQuery();		
			
			while (rs.next()) {
				list.add(extractProduct(rs));
			}
			System.out.println("Product getAll");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
		return list;
		
	}

	@Override
	public Product getById(int id) {
		Product prod = null;
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM products WHERE id = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();		
			
			if (rs.next()) {
				prod = extractProduct(rs);
			}
			System.out.println("Product get by id: " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
		return prod;
	}

	@Override
	public Product getByName(String name) {
		Product prod = null;
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM products WHERE name = ?");
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();		
			
			if (rs.next()) {
				prod = extractProduct(rs);
			}
			System.out.println("Product get by name: " + name);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
		return prod;
	}

	@Override
	public void update(Product obj) {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("UPDATE products SET name = ?, category = ? WHERE id = ?");
			stmt.setString(1, obj.getName());
			stmt.setInt(2, obj.getCategory().getId());
			stmt.setInt(3, obj.getId());
			stmt.execute();		
			System.out.println("Product update: " + obj);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
	}

	@Override
	public void create(Product obj) {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO products (`name`, `category`) VALUES (?, ?)");
			stmt.setString(1, obj.getName());
			int catId = 0;
			if (obj.getCategory() != null) {
				 obj.getCategory().getId();
			}
			stmt.setInt(2, catId);				
			stmt.execute();
			ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() AS id");
			rs.next();
			obj.setId(rs.getInt("id"));
			System.out.println("Product create: " + obj);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
	}

	@Override
	public void delete(Product obj) {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM products WHERE id = ?");
			stmt.setInt(1, obj.getId());
			stmt.execute();		
			System.out.println("Product delete: " + obj);
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
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM products WHERE id = ?");
			stmt.setInt(1, id);
			stmt.execute();		
			System.out.println("Product delete by id: " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(conn);
		}
	}
	
	
}
