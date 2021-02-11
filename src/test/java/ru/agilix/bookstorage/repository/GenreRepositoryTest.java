package ru.agilix.bookstorage.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.agilix.bookstorage.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
//@Import(MongoConfiguration.class)
class GenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    @Test
    void shouldGetAll() {

        List<Genre> genres = repository.findAll();

        assertThat(genres)
                .anyMatch(g ->g.getName().equals( "Unknown"))
                .anyMatch(g ->g.getName().equals( "Action"))
                .anyMatch(g ->g.getName().equals("Classics"))
                .anyMatch(g ->g.getName().equals("Science Fiction (Sci-Fi)"))
                .size().isEqualTo(4);
    }
}