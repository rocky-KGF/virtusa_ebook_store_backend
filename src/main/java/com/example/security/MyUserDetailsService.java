package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.dao.userDao;
import com.example.entities.user;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	userDao ud;
	
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        
    	user us = ud.getUser(s);
        if(us==null)
        	throw new UsernameNotFoundException(s);
        UserDetails user = User.withUsername(us.getUsername()).password(us.getPassword()).authorities(us.getRole()).build();
        return user;
    }
}
