package se.dendromeda.receipts.model;
import java.util.ArrayList;
import java.util.List;

import se.dendromeda.receipts.data.ProductDao;
import se.dendromeda.receipts.util.StringHandler;

public class ReceiptLine {
	
	
	
	int id;
	Receipt receipt;
	Product product;
	String text;
	double price;
	
	public ReceiptLine(Receipt receipt, String input) {
		this.receipt = receipt;
		String[] splitString = input.split(" ");
		
		String priceString = splitString[splitString.length-1].replace(',', '.');
		price = Double.parseDouble(priceString);
		if (!priceString.contains(".")) {
			price = price/100;
		}
		text = splitString[0];
		for (int i = 1; i < splitString.length -1 ; i++) {
			text += " " + splitString[i];
		}
		
	}
	
	public ReceiptLine(int id, Receipt receipt, Product product, String text, double price) {
		this.id = id;
		this.receipt = receipt;
		this.product = product;
		this.text = text;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Receipt getReceipt() {
		return receipt;
	}

	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}

	public String toString() {
		return String.format("%s - %f (%s)", text, price, product);
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}

	public String getText() {
		return text;
	}

	public void setText(String name) {
		this.text = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Product getProduct() {
		return product;
	}
	
	
	
}
