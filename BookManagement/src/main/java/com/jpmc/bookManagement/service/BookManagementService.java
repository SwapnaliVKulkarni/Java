package com.jpmc.bookManagement.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jpmc.bookManagement.model.Book;

public interface BookManagementService {

	public abstract List<Book> getBooks();

	public abstract Book addBook(Book book);

	public abstract Book updateBook(Long isbn, Book book);

	public abstract void deleteBook(Long isbn);

	public abstract Book getBookByISBN(Long isbn);

	public abstract List<Book> getBooksByAttribute(String attrName, String attrValue);

	public abstract void getBooksBFromCSV(MultipartFile file);

}
