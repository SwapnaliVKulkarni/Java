package com.jpmc.bookManagement.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jpmc.bookManagement.model.Book;

@Repository
@Transactional
public interface BookRepository extends JpaRepository<Book, Long> {

	List<Book> findByTitle(String title);

	List<Book> findByAuthor(String author);

	List<Book> findByTags(String tag);

	List<Book> findByTagsIn(String[] tags);

}
