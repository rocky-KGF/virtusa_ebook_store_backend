package com.example.entities;

public class order {
	private int order_id;
	private int user_id;
	private int book_id;
	private int quantity;
	private int price;
	private String book_name;
	public order(int order_id,int user_id, int book_id, int quantity,int price,String book_name) {
		super();
		this.order_id= order_id;
		this.user_id = user_id;
		this.book_id = book_id;
		this.quantity = quantity;
		this.price=price;
		this.book_name = book_name;
	}
	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getBook_id() {
		return book_id;
	}
	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
}
