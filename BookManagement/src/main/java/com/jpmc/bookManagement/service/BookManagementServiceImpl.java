package com.jpmc.bookManagement.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jpmc.bookManagement.exception.*;
import com.jpmc.bookManagement.model.Book;
import com.jpmc.bookManagement.repository.BookRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class BookManagementServiceImpl implements BookManagementService {
	@Autowired
	BookRepository bookRepository;

	@Override
	public List<Book> getBooks() {
		return bookRepository.findAll();
	}

	@Override
	public Book addBook(Book book) {
		Long inputISBN = book.getISBN();
		Optional<Book> bookPresent = bookRepository.findById(inputISBN);
		if (!bookPresent.isPresent()) {
			return bookRepository.save(book);
		} else {
			throw new BookAlreadyPresent();
		}
	}

	@Override
	public Book updateBook(Long isbn, Book bookUpdates) {
		Optional<Book> bookPresent = bookRepository.findById(isbn);
		if (bookPresent.isPresent()) {
			Book book = bookPresent.get();
			if (bookUpdates.getAuthor() != null)
				book.setAuthor(bookUpdates.getAuthor());
			if (bookUpdates.getTags() != null) {
				String[] allTags = Stream.concat(Arrays.stream(book.getTags()), Arrays.stream(bookUpdates.getTags()))
						.toArray(String[]::new);
				book.setTags(allTags);
			}
			if (bookUpdates.getTitle() != null)
				book.setTitle(bookUpdates.getTitle());
			return bookRepository.save(book);
		} else {
			throw new BookNotfoundException();
		}
	}

	@Override
	public void deleteBook(Long isbn) {
		Optional<Book> bookPresent = bookRepository.findById(isbn);
		if (bookPresent.isPresent()) {
			bookRepository.delete(bookPresent.get());
		} else {
			throw new BookNotfoundException();
		}
	}

	@Override
	public Book getBookByISBN(Long isbn) {
		Optional<Book> bookPresent = bookRepository.findById(isbn);
		if (bookPresent.isPresent()) {
			Book book = bookPresent.get();
			return book;
		} else {
			throw new BookNotfoundException();
		}
	}

	@Override
	public List<Book> getBooksByAttribute(String attrName, String attrValue) {
		List<Book> books = new ArrayList<>();
		if (attrName.equalsIgnoreCase("isbn")) {
			Book book = bookRepository.getById(Long.parseLong(attrValue));
			books.add(book);
		} else if (attrName.equalsIgnoreCase("title")) {
			books = bookRepository.findByTitle(attrValue);

		} else if (attrName.equalsIgnoreCase("author")) {
			books = bookRepository.findByAuthor(attrValue);

		} else if (attrName.equalsIgnoreCase("tags")) {
			String[] tags = attrValue.split(";");
			books = bookRepository.findByTagsIn(tags);
		}
		return books;
	}

	@Override
	public void getBooksBFromCSV(MultipartFile file) {
		// validate file
		if (file.isEmpty()) {
			throw new InputFileNotFound();
		} else {

			// parse CSV file to create a list of `Books` objects
			try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

				// create csv bean reader
				CsvToBean<Book> csvToBean = new CsvToBeanBuilder(reader).withType(Book.class)
						.withIgnoreLeadingWhiteSpace(true).build();

				// convert `CsvToBean` object to list of books
				List<Book> books = csvToBean.parse();

				// TODO: save books in DB?
				bookRepository.saveAll(books);

			} catch (Exception ex) {
				throw new InputFileProcessingFailed();
			}
		}

	}

}
