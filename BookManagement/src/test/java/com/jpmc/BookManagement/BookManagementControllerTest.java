package com.jpmc.BookManagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.bookManagement.controller.BookManagementController;
import com.jpmc.bookManagement.model.Book;
import com.jpmc.bookManagement.repository.BookRepository;
import com.jpmc.bookManagement.service.BookManagementService;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookManagementController.class)
class BookManagementControllerTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	@MockBean
	BookManagementService bookManagementService;
	@MockBean
	BookRepository bookRepository;

	Book RECORD_1 = new Book(1l, "BookName1", "bookAuthor1", new String[] { "tag1", "tag12", "tag13" });
	Book RECORD_2 = new Book(2l, "BookName2", "bookAuthor2", new String[] { "tag2", "tag21" });
	Book RECORD_3 = new Book(3l, "BookName3", "bookAuthor3", new String[] { "tag3", "tag31", "tag32" });

	@Test
	public void getAllBooksTest() throws Exception {
		List<Book> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));

		Mockito.when(bookRepository.findAll()).thenReturn(records);
		Mockito.when(bookManagementService.getBooks()).thenReturn(records);

		mockMvc.perform(MockMvcRequestBuilders.get("/books").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[1].title", is("BookName2")));
	}

	@Test
	public void getBookByISBNTest() throws Exception {
		Long isbn = 1l;

		Mockito.when(bookManagementService.getBookByISBN(isbn)).thenReturn(RECORD_1);
		Mockito.when(bookRepository.findById(isbn)).thenReturn(Optional.of(RECORD_1));

		mockMvc.perform(MockMvcRequestBuilders.get("/books/" + isbn).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.title", is("BookName1")));

	}

	@Test
	public void addBookTest() {

		Mockito.when(bookManagementService.addBook(Mockito.any(Book.class))).thenReturn(RECORD_1);
		Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(RECORD_1);

		MockHttpServletRequestBuilder builder;
		try {
			builder = MockMvcRequestBuilders.post("/books").contentType(MediaType.APPLICATION_JSON_VALUE)
					.accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
					.content(this.mapper.writeValueAsBytes(RECORD_1));

			mockMvc.perform(builder).andExpect(status().isCreated()).andExpect(jsonPath("$", notNullValue()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
