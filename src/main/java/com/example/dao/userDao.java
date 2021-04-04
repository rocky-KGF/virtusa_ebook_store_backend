package com.example.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.entities.book;
import com.example.entities.cart;
import com.example.entities.order;
import com.example.entities.user;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class userDao {
	
	@Autowired
	private static JdbcTemplate jt;
	
	public userDao(JdbcTemplate jt)
	{
		this.jt=jt;
	}
	
	RowMapper<user> ru = (rs,rowNum)->{
		user u = new user(rs.getInt("user_id"),rs.getString("username"),rs.getString("mobilenumber"),rs.getString("password"),rs.getString("role"));
		return u;
	};
	
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
	
	org.springframework.jdbc.core.RowMapper<cart> rc = (rs,rowNum)->{
		cart c = new cart(rs.getInt("user_id"),rs.getInt("book_id"),
				rs.getInt("quantity"),rs.getInt("tot_price"),rs.getString("book_name"));
		return c;
	};
	
	org.springframework.jdbc.core.RowMapper<order> ro = (rs,rowNum)->{
		order o = new order(rs.getInt("order_id"),rs.getInt("user_id"),rs.getInt("book_id"),rs.getInt("quantity"),rs.getInt("price"),rs.getString("book_name"));
		return o;
	};
	
	public user getUser(String u)
	{
		String sql = "select * from users where username = '"+u+"'";
		List<user> l = jt.query(sql,ru);
		if(l.get(0).getUsername()==null)
			return null;
		else
			return l.get(0);
	}
	
	public Map<String,String> postUser(user u)
	{
		Map<String,String> mp = new HashMap<>();
		u.setRole("user");
		String sql = "insert into users(username,mobilenumber,password,email,role) values('"+u.getUsername()+"','"+u.getMobilenumber()+"','"+u.getPassword()+"','"+u.getEmail()+"','"+u.getRole()+"')";
		try {
			jt.update(sql);
			mp.put("status","success");
		}catch(Exception e) {
			System.out.println(e);
			mp.put("status","failed");
		}
		return mp;
	}
	
	public List<book> getProducts()
	{
		String sql = "select * from books";
		return jt.query(sql,rm);
	}
	
	public Map<String,String> addToCart(int id,int quantity)
	{
		Map<String,String> mp = new HashMap<>();
		try {
			UserDetails uu = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
			user u = getUser(uu.getUsername());
			List<book> ll = jt.query("select * from books where book_id='"+id+"'",rm);
			book b = ll.get(0);
			int tot_price = Integer.parseInt(b.getPrice()) * quantity;
			List<cart> lll = jt.query("select * from cart where "
					+ "user_id='"+u.getUser_id()+"' and "
							+ "book_id='"+id+"'", rc);
			String sql="";
			if(lll.size()==0)
			{
				 sql = "insert into cart(user_id,book_id,quantity,tot_price,book_name) values('"+u.getUser_id()+"','"+id+"','"+quantity+"','"+tot_price+"','"+b.getName()+"')";
			}else
			{
				 sql = "update cart set quantity='"+quantity+"', tot_price='"+tot_price+"' where "
						+ "user_id = '"+u.getUser_id()+"' and book_id='"+id+"'";
			}
			jt.update(sql);
			jt.update("update books set stock = stock-"+quantity+" where book_id='"+id+"'");
			mp.put("status","success");
		}catch(Exception e) {
			System.out.println(e);
			mp.put("status","failed");
		}
		return mp;
	}
	
	public List<cart> getCartItems()
	{
		UserDetails uu = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
		user u = getUser(uu.getUsername());
		String sql = "select * from cart where user_id='"+u.getUser_id()+"'";
		List<cart> l = jt.query(sql, rc);
		return l;
	}
	
	public Map<String,String> deleteFromCart(int bid)
	{
		UserDetails uu = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
		user u = getUser(uu.getUsername());
		Map<String,String> mp = new HashMap<>();
			try {
				List<cart> lll = jt.query("select * from cart where "
						+ "user_id='"+u.getUser_id()+"' and "
								+ "book_id='"+bid+"'", rc);
				int t = lll.get(0).getQuantity();
				System.out.println(u.getUser_id() + " " + lll.size()+" "+t);
				jt.update("update books set stock= stock+'"+t+"' where"
						+ " book_id = '"+bid+"'");
				jt.update("delete from cart where user_id='"+u.getUser_id()+"'"
						+ " and book_id='"+bid+"'");
				mp.put("status","success");
			}catch(Exception e) {
				System.out.println(e);
				mp.put("status","failed");
			}
		return mp;
	}
	
	public void saveOrder(order o,int flag,int id)
	{
		try {
			String sql = "insert into orders(user_id,book_id,quantity,book_name,price) values("
					+ "'"+id+"','"+o.getBook_id()+"','"+o.getQuantity()+"','"+o.getBook_name()+"','"+o.getPrice()+"')";
			jt.execute(sql);
			if(flag==0)
			{
				sql="delete from cart where user_id='"+id+"'";
				jt.execute(sql);
			}else {
				sql = "update books set stock=stock-'"+o.getQuantity()+"'"
						+ "where book_id='"+o.getBook_id()+"'";
				jt.execute(sql);
			}
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public List<order> getOrders(int id)
	{
		return jt.query("select * from orders where user_id='"+id+"'", ro);
	}
	
}
