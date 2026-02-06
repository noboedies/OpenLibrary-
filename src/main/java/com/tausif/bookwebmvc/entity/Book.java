package com.tausif.bookwebmvc.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor

//or

@Data
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(length = 20)
	private String name;
	private int price;
	@Column(columnDefinition = "longblob", nullable = true)
	private byte[]  coverImage;
	@Column(columnDefinition = "longblob", nullable = true)
	private byte[] content;
	
//	@OneToOne(cascade = CascadeType.ALL)
	@OneToOne
	private User user;
}
