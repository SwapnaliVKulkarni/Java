package com.jpmc.bookManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BookManagementExceptionController {

	@ExceptionHandler(value = BookNotfoundException.class)
	public ResponseEntity<Object> exception(BookNotfoundException exception) {
		return new ResponseEntity<>("requested book not found", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = BookAlreadyPresent.class)
	public ResponseEntity<Object> exception(BookAlreadyPresent exception) {
		return new ResponseEntity<>("Book with input ISBN is already added/present", HttpStatus.ALREADY_REPORTED);
	}

	@ExceptionHandler(value = InputFileNotFound.class)
	public ResponseEntity<Object> exception(InputFileNotFound exception) {
		return new ResponseEntity<>("Input file not found", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = InputFileProcessingFailed.class)
	public ResponseEntity<Object> exception(InputFileProcessingFailed exception) {
		return new ResponseEntity<>("input file processing failed", HttpStatus.BAD_REQUEST);
	}

}
