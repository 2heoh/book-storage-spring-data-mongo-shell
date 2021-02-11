package ru.agilix.bookstorage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.agilix.bookstorage.domain.Book;
import ru.agilix.bookstorage.domain.Comment;
import ru.agilix.bookstorage.repository.BooksRepository;
import ru.agilix.bookstorage.repository.CommentRepositoryImpl;
import ru.agilix.bookstorage.repository.dsl.Create;
import ru.agilix.bookstorage.ui.output.BookOutputService;
import ru.agilix.bookstorage.ui.output.CommentOutputService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CLICommentsServiceTests {
    @Mock
    private BooksRepository repository;

    private CommentsService service;

    @Mock
    private CommentOutputService output;

    @Mock
    private InputService input;

    @Mock
    private DateService date;

    @Mock
    private BookOutputService bookUi;

    @Mock
    private CommentRepositoryImpl commentRepository;

    @BeforeEach
    void setUp() {
        this.service = new CLICommentsService(commentRepository, output, input, date, bookUi);
    }

    @Test
    void shouldGetNewCommentAndSaveIt() {
        Comment comment = Create.Comment("0")
                .Text("text")
                .Author("somebody")
                .build();
        Book book = Create.Book("1").build();
        given(commentRepository.findBook("1")).willReturn(Optional.of(book));
        given(input.getNewComment("1")).willReturn(comment);

        service.addCommentByBookId("1");

        verify(input, times(1)).getNewComment("1");
        verify(commentRepository, times(1)).save(comment);
    }


    @Test
    void shouldCallDeleteOnDaoAndSuccessMessageWhenCommentExists() {
        final var existingId = "1";
        final var comment = Create.Comment().Id(existingId).build();
        given(commentRepository.findBook(existingId)).willReturn(Optional.of(Create.Book().build()));

        service.deleteComment(existingId);

        verify(commentRepository, times(1)).delete(comment.getId());
        verify(output, times(1)).showCommentDeletedMessage(existingId);
    }

    @Test
    void shouldNotCallDeleteShowErrorMessageWhenCommentNotExists() {
        String nonExistingId = "-1";
        given(commentRepository.findBook(nonExistingId)).willReturn(Optional.empty());

        service.deleteComment(nonExistingId);

        verify(commentRepository, times(0)).delete(anyString());
        verify(output, times(1)).showCommentNotFound(nonExistingId);
    }

    @Test
    void shouldCallGetUpdatedCommentDaoSaveAndShowResultMessage() {
        final var existingCommentId = "1";
        Comment comment = Create.Comment().Id(existingCommentId).build();
        given(input.getUpdatedComment(comment)).willReturn(comment);
        given(commentRepository.findById(existingCommentId)).willReturn(Optional.of(comment));

        service.updateComment(existingCommentId);

        verify(commentRepository, times(1)).findById(existingCommentId);
        verify(input, times(1)).getUpdatedComment(comment);
        verify(commentRepository, times(1)).update(any(Comment.class));
        verify(output, times(1)).showCommentUpdated(any());
    }

    @Test
    void shouldShowCommentIsNotFoundForNonExistingCommentId() {
        final var nonExistingCommentId = "-1";
        Comment comment = Create.Comment().Id(nonExistingCommentId).build();
        given(commentRepository.findById(nonExistingCommentId)).willReturn(Optional.empty());

        service.updateComment(nonExistingCommentId);

        verify(commentRepository, times(0)).update(comment);
        verify(commentRepository, times(0)).save(comment);
        verify(output, times(1)).showCommentNotFound(nonExistingCommentId);
    }

    @Test
    void shouldShowCommentsByBookId() {
        final var comment = Create.Comment().Text("first").build();
        final var book = Create.Book().Comment(comment).build();
        given(commentRepository.findBook("1")).willReturn(java.util.Optional.of(book));
        given(commentRepository.findAllByBookId("1")).willReturn(List.of(comment));

        service.getCommentsByBookId("1");

        verify(commentRepository, times(1)).findAllByBookId("1");
        verify(output, times(1)).showListOfComments(List.of(comment));
    }


}
