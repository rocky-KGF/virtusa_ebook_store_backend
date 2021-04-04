package com.example.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dao.adminDao;
import com.example.dao.userDao;
import com.example.entities.book;
import com.example.entities.order;


@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class adminController {

	private static adminDao ad;
	
	public adminController(adminDao jd) {
		this.ad=jd;
	}
	
	@GetMapping
	public ResponseEntity< List<book> > find()
	{
		System.out.println("entered1");

			List<book> l = ad.getProducts();
			return new ResponseEntity<>(l,HttpStatus.OK);
		
	}
	
	@PostMapping("/addProduct")
	public Map<String,String> addProduct(@RequestBody book bk)
	{

			System.out.println("addproduct");
			return ad.productSave(bk);
		
	
	}
	
	@GetMapping("/delete/{id}")
	public Map<String,String> deleteById(@PathVariable("id") String id)
	{

			System.out.println("deletebyid");
			return ad.deleteById(Integer.parseInt(id));

	}
	
	@GetMapping("/productEdit/{id}")
	public ResponseEntity<book> getEditById(@PathVariable("id") String id)
	{
		System.out.println("geteditbyid");

			book b = ad.getById(Integer.parseInt(id));
			return new ResponseEntity<book>(b,HttpStatus.OK);

	}
	
	@PostMapping("/productEdit/{id}")
	public Map<String,String> postEditById(@RequestBody book bk,@PathVariable("id") String id)
	{
		System.out.println("posteditbyid");
		return ad.postById(bk, Integer.parseInt(id));
	}
	
	@GetMapping("/orders")
	public ResponseEntity< List<order> > getOrders()
	{
		System.out.println("getorders");

			List<order> l = ad.getOrders();
			return new ResponseEntity<>(l,HttpStatus.OK);
	}
}
