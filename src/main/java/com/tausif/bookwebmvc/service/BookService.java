package com.tausif.bookwebmvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tausif.bookwebmvc.entity.Book;
import com.tausif.bookwebmvc.entity.User;
import com.tausif.bookwebmvc.repo.BookRepo;
import com.tausif.bookwebmvc.repo.UserRepo;

@Service
public class BookService {
	@Autowired
	private BookRepo bookRepo;
	
	public boolean saveBook(Book book) {
		bookRepo.save(book);
		return true;
	}

	public List<Book> getAllBooks() {
		return bookRepo.findAll();
	}

	public Book getBook(int id) {
		// TODO Auto-generated method stub
		return bookRepo.findById(id).orElse(null);
	}

	public List<Book> getAllBooks(User u) {
		return bookRepo.findAllByUser(u);
		
//		return bookRepo.getMyBooks(u);
//		return bookRepo.getMyBooks(u.getEmail());
	}

	public boolean deleteBook(int id) {
		if(bookRepo.findById(id).orElse(null)==null) {
			return false;
		}
		bookRepo.deleteById(id);
		return true;
	}

	public List<Book> searchBook(String name) {
//		return bookRepo.findAllByName(name);
		return bookRepo.findAllByNameContains(name);
//		return bookRepo.findAllByNameLike("%"+name+"%");
	}
}
