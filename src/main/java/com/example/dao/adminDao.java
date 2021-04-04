package com.example.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.entities.book;
import com.example.entities.order;


@Component
public class adminDao {
	
	@Autowired
	private JdbcTemplate jt;
	
	public adminDao(JdbcTemplate jt) {
		this.jt = jt;
	}
	
	org.springframework.jdbc.core.RowMapper<book> rm = (rs,rowNum)->{
		book b = new book();
		b.setBook_id(rs.getInt("book_id"));
		b.setName(rs.getString("name"));
		b.setAuthor(rs.getString("author"));
		b.setGenre(rs.getString("genre"));
		b.setStock(rs.getString("stock"));
		b.setPrice(rs.getString("price"));
		b.setDescription(rs.getString("description"));
		b.setImageurl(rs.getString("imageurl"));
		return b;
	};
	
	org.springframework.jdbc.core.RowMapper<order> ro = (rs,rowNum)->{
		order o = new order(rs.getInt("order_id"),rs.getInt("user_id"),rs.getInt("book_id"),rs.getInt("quantity"),rs.getInt("price"),rs.getString("book_name"));
		return o;
	};
	
	public boolean find(String name)
	{
		String sql = "select * from books where name = '"+name+"'";
		List<book> l = jt.query(sql,rm);
		if(l.size()==0)
			return true;
		else 
			return false;
	}
	
	public List<book> getProducts()
	{
		String sql = "select * from books";
		return jt.query(sql,rm);
	}
	
	public Map<String,String> productSave(book bk)
	{
		Map<String,String> mp = new HashMap<>();
		if(find(bk.getName())) {
			try {
				String sql = "insert into books values ('"+bk.getBook_id()+"','"+bk.getName()+"','"+bk.getAuthor()+"','"+bk.getGenre()+"','"+bk.getStock()+"','"+bk.getPrice()+"','"+bk.getDescription()+"','"+bk.getImageurl()+"')";
				jt.update(sql);
				mp.put("status","success");
			}catch(Exception e) {
			mp.put("status","failed");
			}
		}else
			mp.put("status","book already exists");
		return mp;
	}
	
	public Map<String,String> deleteById(int id) {
		Map<String,String> mp = new HashMap<>();
		String sql = "delete from books where book_id = '"+id+"'";
		try {
			jt.update(sql);
			mp.put("status","success");
		}catch(Exception e) {
			System.out.println(e);
			mp.put("status","failed");
		}
		return mp;
	}
	
	public book getById(int id)
	{
		String sql = "select * from books where book_id = '"+id+"'";
		List<book> l = jt.query(sql,rm);
		return l.get(0);
	}
	
	public Map<String,String> postById(book b,int id)
	{
		Map<String,String> mp = new HashMap<>();
			try {
				String sql = "update books set name = '"+b.getName()+"',"
						+ " author = '"+b.getAuthor()+"', genre = "
								+ "'"+b.getGenre()+"',stock = '"+b.getStock()+"',"
										+ "price = '"+b.getPrice()+"',"
												+ "description='"+b.getDescription()+"',"
														+ "imageurl='"+b.getImageurl()+"' where book_id = '"+id+"'";
				jt.execute(sql);
				mp.put("status","success");
			}catch(Exception e) {
				System.out.println(e);
				mp.put("status","failed");
			}
		return mp;
	}
	
	public List<order> getOrders()
	{
		String sql = "select * from orders";
		return jt.query(sql, ro);
	}
}
