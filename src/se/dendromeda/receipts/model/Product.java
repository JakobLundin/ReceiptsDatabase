package se.dendromeda.receipts.model;

import java.util.ArrayList;
import java.util.List;

public class Product {
		
	int id;
	String name;
	Category category;
	
	public Product(int id, String name, Category category) {
		this.id = id;
		this.name = name;
		this.category = category;
	}
	
	public String toString() {
		return String.format("[%d] %s", id, name);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	
	
}
