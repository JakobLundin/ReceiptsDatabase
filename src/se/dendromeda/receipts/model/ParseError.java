package se.dendromeda.receipts.model;

public class ParseError {
	int id;
	Receipt receipt;
	String text;
	
	public ParseError(int id, Receipt receipt, String text) {
		this.id = id;
		this.receipt = receipt;
		this.text = text;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
