package ru.agilix.bookstorage.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
//@Import(MongoConfiguration.class)
class AuthorRepositoryTest {

    @Autowired
    AuthorRepository repository;

    @Test
    void shouldGetAll() {

        final var authors = repository.findAll();

        assertThat(authors)
                .anyMatch( a -> a.getName().equals("Unknown"))
                .anyMatch( a -> a.getName().equals("Лев Толстой"))
                .anyMatch( a -> a.getName().equals("Николай Гоголь"))
                .size().isEqualTo(3);
    }
}