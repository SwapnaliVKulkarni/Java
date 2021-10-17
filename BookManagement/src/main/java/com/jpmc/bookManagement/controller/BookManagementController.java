package com.jpmc.bookManagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jpmc.bookManagement.model.Book;
import com.jpmc.bookManagement.service.BookManagementService;

@RestController
@RequestMapping("/books")
public class BookManagementController {
	@Autowired
	BookManagementService bookManagementService;

	/**
	 * @param find book by isbn
	 * @return book details
	 */
	@GetMapping("/{isbn}")
	public ResponseEntity<Book> getBookByISBN(@PathVariable(value = "isbn") Long isbn) {
		return new ResponseEntity<Book>(bookManagementService.getBookByISBN(isbn), HttpStatus.OK);
	}

	/**
	 * @return List of Books
	 */
	@GetMapping
	public ResponseEntity<List<Book>> getAllBooks() {
		return new ResponseEntity<List<Book>>(bookManagementService.getBooks(), HttpStatus.OK);
	}

	/**
	 * @param book object attributes in JSON Format
	 * @return book object created
	 */
	@PostMapping
	public ResponseEntity<Book> addBook(@RequestBody Book book) {
		return new ResponseEntity<Book>(bookManagementService.addBook(book), HttpStatus.CREATED);
	}

	/**
	 * @param update  book by its isbn
	 * @param updated book object
	 * @return
	 */
	@PutMapping("/{isbn}")
	public ResponseEntity<Book> updateBook(@PathVariable(value = "isbn") Long isbn, @RequestBody Book book) {
		return new ResponseEntity<Book>(bookManagementService.updateBook(isbn, book), HttpStatus.OK);
	}

	// @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	/**
	 * @param isbn of book which will be deleted
	 * @return deletion status
	 */
	@DeleteMapping("/{isbn}")
	public ResponseEntity<Object> deleteBook(@PathVariable(value = "isbn") Long isbn) {
		bookManagementService.deleteBook(isbn);
		return new ResponseEntity<>("Book is deleted successsfully", HttpStatus.OK);
	}

	/**
	 * @param map of attributes to be searched for getting book list
	 * @return list of books matching search criteria
	 */
	@GetMapping("/get")
	public ResponseEntity<List<Book>> getBooksByAttribute(@RequestParam String attrName,
			@RequestParam String attrValue) {
		return new ResponseEntity<List<Book>>(bookManagementService.getBooksByAttribute(attrName, attrValue),
				HttpStatus.OK);
	}

	/**
	 * @param csv file with book attribute values to be imported
	 * @return import status
	 * 
	 */
	@PostMapping("/upload-csv-file")
	public ResponseEntity<String> uploadCSVFile(@RequestParam("file") MultipartFile file) {
		bookManagementService.getBooksBFromCSV(file);
		return new ResponseEntity<String>("File successfully imported", HttpStatus.OK);

	}

}
