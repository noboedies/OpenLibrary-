package com.tausif.bookwebmvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tausif.bookwebmvc.entity.User;
import com.tausif.bookwebmvc.repo.UserRepo;

@Service
public class UserService {
	@Autowired
	private UserRepo userRepo;
	
	public boolean saveUser(User user) {
		User u=userRepo.findById(user.getEmail()).orElse(null);
		if(u==null) {
			userRepo.save(user);
			return true;
		}else {
			return false;
		}
	}

	public User checkLogin(String email, String password) {
		User u=userRepo.findById(email).orElse(null);
		if(u!=null && password.equals(u.getPassword())) {
			return u;
		}else {
			return null;
		}
		
	}
}