package com.tausif.bookwebmvc.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.tausif.bookwebmvc.entity.Book;
import com.tausif.bookwebmvc.entity.User;
import com.tausif.bookwebmvc.service.BookService;
import com.tausif.bookwebmvc.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;



@Controller
public class BookController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private BookService bookService;
	
//	@RequestMapping("/")
	@RequestMapping(value = {"/","/home"})
	public String home() {
//		return "index.html";
		return "index";  //.html is optional
	}
	
	@GetMapping("/login-signup")
	public String loginSignup() {
		return "login-signup";
	}
	@GetMapping("/UserHome")
	public String userHome(HttpSession session,Model m) {
		User u=(User)session.getAttribute("user");
		if(u==null) {
			m.addAttribute("msg","Please Login First!");
			return "login-signup";
		}
		return "UserHome";
	}
	@GetMapping("/Logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "login-signup";
	}
	
	@PostMapping("/Login")
	public String login(@RequestParam String email,@RequestParam String password,HttpSession session,Model m) {
		User u=userService.checkLogin(email,password);
		if(u==null) {
			m.addAttribute("msg","Wrong Credentials!");
			return "login-signup";
		}else {
			session.setAttribute("user", u);
			return "UserHome";
		}
	}
	
	@PostMapping("/Register")
	public String register(@ModelAttribute User u,Model m) {
		boolean result=userService.saveUser(u);
		if(result) {
			m.addAttribute("msg","Registered Successfully!");
		}else {
			m.addAttribute("msg","Email Id Already Exist!");
		}
		return "login-signup";
	}
	
//	Why can't we get image or pdf (as byte[]) directly inside a @ModelAttribute Book?
/*
Spring's @ModelAttribute mechanism binds form fields to Java object properties only when the field values are plain text (like String, int, etc.).
But in our case:
	-We’re uploading files, not plain text only.
	-Spring doesn't automatically convert a file upload into a byte[] for a property like private byte[] image; in our Book class.
	-MultipartFile is not the same as byte[].
*/
	@PostMapping("/AddBook")
	public String addBook(@ModelAttribute Book b,@RequestPart MultipartFile ctn,@RequestPart MultipartFile cImage,HttpSession session,Model m) throws IOException {
		byte[] c=ctn.getBytes();
		byte[] ci=cImage.getBytes();
		if(c.length==0) {
			c=null;
		}
		if(ci.length==0) {
			ci=null;
		}
		b.setCoverImage(ci);
		b.setContent(c);
		User u=(User)session.getAttribute("user");
		b.setUser(u);
		boolean result=bookService.saveBook(b);
		if(result) {
			m.addAttribute("msg","Added Successfully!");
		}else {
			m.addAttribute("msg","Book name Already Exist!");
		}
		return "UserHome";
	}
	
	@GetMapping("/AllBooks")
	public String allBooks(Model m) {
		List<Book> books=bookService.getAllBooks();
		m.addAttribute("books",books);
		return "AllBooks";
	}
	
	@GetMapping("/MyBooks")
	public String myBooks(HttpSession session,Model m) {
		User u=(User)session.getAttribute("user");
		List<Book> books=bookService.getAllBooks(u);
		m.addAttribute("books",books);
		return "MyBooks";
	}
	
	@PostMapping("/SearchBook")
	public String searchBook(String name,Model m) {
		List<Book> books=bookService.searchBook(name);
		m.addAttribute("books",books);
		return "index";
	}
	
	@PostMapping("/deleteBook")
	public String deleteBook(@RequestParam int id,HttpSession session,Model m) throws IOException {
		boolean deleteResult=bookService.deleteBook(id);
		m.addAttribute("deleteResult",deleteResult);
		
		User u=(User)session.getAttribute("user");
		List<Book> books=bookService.getAllBooks(u);
		m.addAttribute("books",books);
		return "MyBooks";
	}
	
	@GetMapping("/getCoverImage")
	public void getCoverImage(@RequestParam int id, HttpServletResponse response) throws IOException {
		Book book=bookService.getBook(id);
		if(book!=null) {
			byte[] image=book.getCoverImage();
			if(image==null || image.length==0) {
				InputStream is = this.getClass().getClassLoader().getResourceAsStream("static/book.png");
				image=is.readAllBytes();
			}
			response.getOutputStream().write(image);
		}
	}
	@GetMapping("/viewPDF")
	public void viewPDF(@RequestParam int id, HttpServletResponse response) throws IOException {
		Book book=bookService.getBook(id);
		if(book!=null) {
			byte[] content=book.getContent();
			response.getOutputStream().write(content);
		}
	}
	@GetMapping("/downloadPDF")
	public void downloadPDF(@RequestParam int id, HttpServletResponse response) throws IOException {
		Book book=bookService.getBook(id);
		if(book!=null) {
			byte[] content=book.getContent();
			response.setHeader("Content-Disposition","attachment; filename=" + book.getName()+".pdf" );
			response.getOutputStream().write(content);
		}
	}
}
