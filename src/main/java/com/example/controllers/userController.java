package com.example.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dao.userDao;
import com.example.entities.AuthenticationResponse;
import com.example.entities.book;
import com.example.entities.cart;
import com.example.entities.order;
import com.example.entities.user;
import com.example.security.JwtUtil;
import com.example.security.MyUserDetailsService;


@RestController
@CrossOrigin(origins="http://localhost:3000")
public class userController {
	
	private static userDao ud;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;
	
	public userController(userDao ud)
	{
		this.ud=ud;
	}
	

	@PostMapping("/login")
	
	public Map<String,String> validate(@RequestBody user us)
	{
		System.out.println("login");
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(us.getUsername(), us.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new BadCredentialsException("Incorrect username or password", e);
		}


		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(us.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);
		System.out.println(userDetails.getAuthorities().toArray()[0].toString());
		String role = userDetails.getAuthorities().toArray()[0].toString();
		Map<String,String> mp = new HashMap<>();
		user u = ud.getUser(userDetails.getUsername());
		mp.put("token",jwt);
		if(role.equals("admin"))
			mp.put("admin","true");
		mp.put("status","true");
		mp.put("user",userDetails.getUsername());
		mp.put("id",""+u.getUser_id());
		return mp;
	}


	@PostMapping("/signup")
	public Map<String,String> addUser(@RequestBody user us)
	{
		System.out.println("signup");
		return ud.postUser(us);
	}
	

	@GetMapping("/home")
	public ResponseEntity< List<book> > getProducts()
	{
		System.out.println("home");
		HttpHeaders hh = new HttpHeaders();
		hh.set("Access-Control-Allow-Origin", "*");
			List<book> l = ud.getProducts();
	return new ResponseEntity<>(l,hh,HttpStatus.OK);
		
	}
	
	@PostMapping("/home/{id}")
	public Map<String,String> toCart(@PathVariable("id") String id, @RequestParam("quantity") String quantity)
	{
		System.out.println("home/id");
		Map<String,String> mp = new HashMap<>();
			try {
				return ud.addToCart(Integer.parseInt(id), Integer.parseInt(quantity));
			}catch(Exception e)
			{
				System.out.println(e);
				mp.put("status","failed");
			}
		return mp;
	}
	
	@GetMapping("/cart/{id}")
	public ResponseEntity< List<cart> > getCart(@PathVariable("id") String id)
	{

			List<cart> l = ud.getCartItems();
			return new ResponseEntity<>(l,HttpStatus.OK);
	}
	
	@PostMapping("/cart/delete")
	public Map<String,String> delCart(@RequestParam("bid") String bid)
	{
		return ud.deleteFromCart(Integer.parseInt(bid));
	}
	
	@PostMapping("/saveOrder")
	public Map<String,String> saveOrder(@RequestBody List<order> orders,@RequestHeader("id") int id)
	{
		Map<String,String> mp = new HashMap<>();
		try {
			for(order o : orders)
			{
				ud.saveOrder(o,0,id);
			}
			mp.put("status","success");
		}catch(Exception e)
		{
			System.out.println(e);
			mp.put("status","failed");
		}
		return mp;
	}
	
	@GetMapping("/orders")
	public ResponseEntity< List<order> > getOrders(@RequestHeader("id") int id)
	{
		List<order> l = ud.getOrders(id);
		return new ResponseEntity<>(l,HttpStatus.OK);
	}
	
	@PostMapping("/placeOrder")
	public Map<String,String> directOrder(@RequestBody order o,@RequestHeader("id") int id)
	{
		Map<String,String> mp = new HashMap<>();
		try {
			ud.saveOrder(o,1,id);
			mp.put("status","success");
		}catch(Exception e)
		{
			System.out.println(e);  
			mp.put("status","failed");
		}
		return mp;
	}
}
