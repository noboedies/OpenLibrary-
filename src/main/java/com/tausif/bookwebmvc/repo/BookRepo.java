package com.tausif.bookwebmvc.repo;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Repository;

import com.tausif.bookwebmvc.entity.Book;
import com.tausif.bookwebmvc.entity.User;

@Repository
public interface BookRepo extends JpaRepository<Book,Integer>{

	List<Book> findAllByUser(User u);
	
	@Query("select b from Book b where b.user = :user")
	List<Book> getMyBooks(@Param("user") User u);
	
	@Query("select b from Book b where b.user.email = :email")
	List<Book> getMyBooks(@Param("email") String email);

	
	
	
	List<Book> findAllByName(String name);

	List<Book> findAllByNameContains(String name);

	List<Book> findAllByNameLike(String name);
	
}
