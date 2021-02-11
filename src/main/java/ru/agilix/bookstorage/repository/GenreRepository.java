package ru.agilix.bookstorage.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.agilix.bookstorage.domain.Genre;

public interface GenreRepository extends MongoRepository<Genre, String> {

}
