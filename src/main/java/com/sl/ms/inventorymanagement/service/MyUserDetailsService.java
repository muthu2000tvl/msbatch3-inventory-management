package com.sl.ms.inventorymanagement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sl.ms.inventorymanagement.product.ProductController;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	private static final Logger log = LoggerFactory.getLogger(MyUserDetailsService.class.getName());

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    	
    	log.info("Entering loadUserByUsername");	
    	
        return new User("foo", "foo",
                new ArrayList<>());
    }
}