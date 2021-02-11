package ru.agilix.bookstorage.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.agilix.bookstorage.domain.Book;

public interface BooksRepository extends MongoRepository<Book, String> {
}
