package com.example.test;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan("com.example")
public class TestApplication implements CommandLineRunner {
	
	@Autowired
	private JdbcTemplate jt;
	
	public static void main(String[] args) {
		System.out.println("entered");
		SpringApplication.run(TestApplication.class, args);
	}
	
	   @Override
	   public void run(String... arg0) throws Exception {
	      System.out.println("Hello world from Command Line Runner");
	      jt.execute("create table if not exists testing1  (user_id int NOT NULL AUTO_IN"
	      		+ "CREMENT,username varchar(20) NOT NULL UNIQUE,mobilenumber "
	      		+ "varchar(10) NOT NULL UNIQUE, password varchar(100) NOT "
	      		+ "NULL , email varchar(100), role varchar(20) NOT NULL,"
	      		+ "primary key (user_id))");
	      jt.execute("create table if not exists testing2(book_id int NOT NULL AUTO_IN"
		      		+ "CREMENT,name varchar(100) NOT NULL UNIQUE,author "
		      		+ "varchar(50) NOT NULL, genre varchar(50) NOT "
		      		+ "NULL , stock int NOT NULL, price int NOT NULL,"
		      		+ "description text NOT NULL ,imageurl text NOT NULL,"
		      		+ "primary key (book_id))");
	      jt.execute("create table if not exists testing3(cid int NOT NULL AUTO_IN"
		      		+ "CREMENT,user_id int NOT NULL,book_id "
		      		+ "int NOT NULL, quantity int NOT "
		      		+ "NULL , tot_price int NOT NULL, book_name varchar(50) NOT NULL,"
		      		+ "primary key (cid))");
	      jt.execute("create table if not exists testing4(order_id int NOT NULL AUTO_IN"
		      		+ "CREMENT,user_id int NOT NULL,book_id "
		      		+ "int NOT NULL, quantity int NOT "
		      		+ "NULL , book_name varchar(50) NOT NULL, tot_price int NOT NULL,"
		      		+ "primary key (order_id))");
	      
	   }
}


