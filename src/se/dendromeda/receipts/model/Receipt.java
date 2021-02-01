package se.dendromeda.receipts.model;

public class Receipt {
	private int id;
	private String date;
	
	public Receipt(int id, String date) {
		this.id = id;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
}
