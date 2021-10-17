package com.jpmc.bookManagement.model;

import javax.persistence.*;

import com.opencsv.bean.CsvBindByName;

@Entity(name = "BOOK")
public class Book {

	@Id
	@Column(name = "ISBN")
	@CsvBindByName
	private Long ISBN;

	@Column(name = "TITLE", nullable = false)
	@CsvBindByName
	private String title;

	@Column(name = "AUTHOR", nullable = false)
	@CsvBindByName
	private String author;

	@Column(name = "TAGS", nullable = true)
	@CsvBindByName
	@ElementCollection
	@OrderColumn
	private String[] tags;

	public Book() {
	}

	public Book(Long isbn, String title, String author) {
		this.ISBN = isbn;
		this.title = title;
		this.author = author;
	}

	public Book(Long isbn, String title, String author, String[] tags) {
		this.ISBN = isbn;
		this.title = title;
		this.author = author;
		this.tags = tags;
	}

	public Long getISBN() {
		return ISBN;
	}

	public void setISBN(long iSBN) {
		ISBN = iSBN;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}
}
