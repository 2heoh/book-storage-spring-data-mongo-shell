package ru.agilix.bookstorage.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import ru.agilix.bookstorage.domain.Author;
import ru.agilix.bookstorage.domain.Book;
import ru.agilix.bookstorage.domain.Comment;
import ru.agilix.bookstorage.domain.Genre;
import ru.agilix.bookstorage.repository.dsl.Create;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataMongoTest
//@Import(MongoConfiguration.class)
public class BooksRepositoryTest {

    public static final String EXISTING_ID = "b01";
    public static final String NON_EXISTING_ID = "b-1";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BooksRepository repository;

    @Test
    void getAllBooksReturnsListOfBooks() {

        List<Book> books = (List<Book>) repository.findAll();

        assertThat(books)
                .hasSize(2)
                .anyMatch( b -> b.getTitle().equals("Вий"))
                .anyMatch( b -> b.getAuthors().stream().anyMatch( a -> a.getName().equals( "Николай Гоголь")));
    }

    @Test
    @DirtiesContext
    void createBookShouldReturnIt() {

        Book book = Create.Book()
                .Title("title")
                .Description("description")
                .Author(new Author("a01","Author"))
                .Genre(new Genre( "g01","Genre"))
                .build();

        Book inserted = repository.save(book);

        assertThat(inserted.getTitle()).isEqualTo("title");
        assertThat(inserted.getDescription()).isEqualTo("description");
        assertThat(inserted.getAuthors().get(0).getName()).isEqualTo("Author");
    }

    @Test
    void saveBookShouldUpdateItsFields() {
        Author gogol = new Author( "a02","Николай Гоголь");
        Author tolstoy = new Author( "a01","Лев Толстой");
        Genre newGenre = new Genre( "g03", "Science Fiction (Sci-Fi)");
        Book updatedBook = Create.Book(EXISTING_ID)
                            .Author(gogol)
                            .Author(tolstoy)
                            .Title("new title")
                            .Description("new description")
                            .Genre(newGenre)
                            .build();

        repository.save(updatedBook);

        Book savedBook = repository.findById(EXISTING_ID).get();
        assertThat(savedBook.getId()).isEqualTo("b01");
        assertThat(savedBook.getTitle()).isEqualTo("new title");
        assertThat(savedBook.getDescription()).isEqualTo("new description");
        assertThat(savedBook.getAuthors()).size().isEqualTo(2);
        assertThat(savedBook.getAuthors())
                .anyMatch( a -> a.getName().equals(gogol.getName()))
                .anyMatch( a -> a.getName().equals(tolstoy.getName()));
        assertThat(savedBook.getGenre().getId()).isEqualTo(newGenre.getId());
    }


    @Test
    void shouldReturnExistingBook() {
        Book book = repository.findById(EXISTING_ID).get();

        assertThat(book.getTitle()).isEqualTo("Война и мир");
        assertThat(book.getDescription()).startsWith("Войнa и мир");
        assertThat(book.getAuthors()).size().isEqualTo(1);
        assertThat(book.getAuthors()).anyMatch( a -> a.getName().equals("Лев Толстой"));
    }

    @Test
    void shouldRaiseExceptionForNonExistingBook() {
        final var nonExitingBook = repository.findById(NON_EXISTING_ID);
        assertFalse(nonExitingBook.isPresent());
    }

    @Test
    @DirtiesContext
    void shouldDeleteBookByIdAndAllItsComments() {
        final var savedBook = repository.findById(EXISTING_ID);

        repository.delete(savedBook.get());

        assertFalse( repository.findById(EXISTING_ID).isPresent());
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is("c01"));
        assertThat(mongoTemplate.find(query, Comment.class)).isEmpty();
    }

}