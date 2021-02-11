package ru.agilix.bookstorage.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.agilix.bookstorage.domain.Author;

@Repository
public interface AuthorRepository extends MongoRepository<Author, String> {
}
