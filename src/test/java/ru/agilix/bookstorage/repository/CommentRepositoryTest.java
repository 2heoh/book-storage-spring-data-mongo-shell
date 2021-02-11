package ru.agilix.bookstorage.repository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.agilix.bookstorage.domain.Comment;
import ru.agilix.bookstorage.repository.dsl.Create;

import java.text.ParseException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(CommentRepositoryImpl.class)
class CommentRepositoryTest {

    public static final String EXISTING_BOOK_ID = "b01";
    public static final String NON_EXISTING_BOOK_ID = "b-1";
    public static final String EXISTING_COMMENT_ID = "c01";

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private CommentRepositoryCustom commentRepository;

    private Comment first;
    private Comment second;

    @BeforeEach
    void setUp() throws ParseException {

        first = Create.Comment("c01")
                .Text("first")
                .Author("Gomer Simpson")
                .Date("2021-01-19 10:00:00")
                .build();

        second = Create
                .Comment("c02")
                .Text("second")
                .Author("Gomer Simpson")
                .Date("2021-01-19 10:01:00")
                .build();

    }

    @Test
    void shouldFindCommentById() {
        final var comment = commentRepository.findById("c01");
        assertThat(comment).isNotEmpty();
    }

    @Test
    void shouldFindBook() {
        final var book = commentRepository.findBook("b01");

        assertThat(book).isNotEmpty();
    }

    @Test
    void shouldGetAllCommentsByBookId() {

        final var book = booksRepository.findById(EXISTING_BOOK_ID).get();

        assertThat(book.getComments())
                .hasSize(2)
                .anyMatch(c -> c.getText().equals(first.getText()))
                .anyMatch(c -> c.getText().equals(second.getText()));
    }


    @Test
    void shouldReturnCommentsInReverseDateOrder() {
        List<Comment> expectedOrder = List.of(second, first);
        for (Comment c : expectedOrder){
            c.setBook(booksRepository.findById(EXISTING_BOOK_ID).get());
        }

        final var comments = commentRepository.findAllByBookId(EXISTING_BOOK_ID);

        assertThat(comments.get(0).getText()).isEqualTo("second");
        assertThat(comments.get(1).getText()).isEqualTo("first");
    }

    @Test
    @DirtiesContext
    void shouldInsertNewComment() throws ParseException {

        final var book = booksRepository.findById(EXISTING_BOOK_ID).get();
        Comment comment = Create.Comment()
                .Text("text")
                .Author("somebody")
                .Book(book)
                .Date("2020-02-11 14:00:00")
                .build();

        final var saved = commentRepository.save(comment);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getText()).isEqualTo("text");
        assertThat(saved.getAuthor()).isEqualTo("somebody");
        assertThat(saved.getDate()).isNotNull();
    }


    @Test
    @DirtiesContext
    void shouldDeleteComment() {
        commentRepository.delete(EXISTING_COMMENT_ID);

        assertThat(commentRepository.findBook(EXISTING_COMMENT_ID)).isEmpty();
    }

    @Test
    @DirtiesContext
    void shouldUpdateExistingComment() {
        Comment comment = commentRepository.findById(EXISTING_COMMENT_ID).get();

        comment.setAuthor("new author");
        comment.setText("new text");

        commentRepository.update(comment);

        Comment saved = commentRepository.findById(EXISTING_COMMENT_ID).get();
        assertThat(saved.getId()).isEqualTo(EXISTING_COMMENT_ID);
        assertThat(saved.getText()).isEqualTo("new text");
        assertThat(saved.getAuthor()).isEqualTo("new author");
    }

}