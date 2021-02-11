package ru.agilix.bookstorage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.agilix.bookstorage.domain.Book;
import ru.agilix.bookstorage.repository.AuthorRepository;
import ru.agilix.bookstorage.repository.BooksRepository;
import ru.agilix.bookstorage.repository.GenreRepository;
import ru.agilix.bookstorage.repository.dsl.Create;
import ru.agilix.bookstorage.ui.output.BookOutputService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CLIBooksServiceTest {

    private BooksService service;

    @Mock
    private BooksRepository booksRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private BookOutputService output;

    @Mock
    private InputService input;


    @BeforeEach
    void setUp() {
        this.service = new CLIBooksService(booksRepository, genreRepository, authorRepository, output, input);
    }

    @Test
    void shouldCreateNewBook() {
        Book book = Create.Book("1").Title("title").build();
        given(input.getNewBook(any(), any())).willReturn(book);
        given(booksRepository.save(book)).willReturn(book);

        service.createBook();

        verify(booksRepository, times(1)).save(book);
        verify(output, times(1)).showBookCreatedMessage(book);
    }

    @Test
    void shouldShowExistingBookById() {
        Book book = Create.Book("1").Title("title").Description("description").build();
        given(booksRepository.findById("1")).willReturn(java.util.Optional.ofNullable(book));

        service.retrieveBook("1");

        verify(booksRepository, times(1)).findById("1");
        verify(output, times(1)).showBookInfo(book);
    }

    @Test
    void shouldNotShowNonExistingBook() {
        given(booksRepository.findById("-1")).willReturn(Optional.empty());

        service.retrieveBook("-1");

        verify(booksRepository, times(1)).findById("-1");
        verify(output, times(0)).showBookInfo(any());
        verify(output, times(1)).showBookNotFound("-1");
    }


    @Test
    void shouldUpdateBook() {
        given(booksRepository.findById(anyString())).willReturn(
            Optional.ofNullable(Create.Book().Id("1").Title("title").build())
        );
        Book updated = Create.Book("1").Title("new title").build();
        given(input.getUpdatedBookInfo(any(), any(), any())).willReturn(updated);
        given(output.showBookInfo(any())).willReturn(updated.toString());

        String result = service.updateBook("1");

        verify(booksRepository).save(any());
        assertThat(result).contains("new title");
    }

    @Test
    void shouldDeleteBookWhenBookExists() {
        Book bible = Create.Book().Title("bible").build();
        given(booksRepository.findById("1")).willReturn(java.util.Optional.of(bible));

        service.deleteBook("1");

        verify(booksRepository, times(1)).delete(bible);
        verify(output, times(1)).showBookDeletedMessage("1");
        verify(output, times(0)).showBookNotFound(anyString());
    }

    @Test
    void deletingNonExistingBookDisplaysBookNotFoundMessage() {
        given(booksRepository.findById("-1")).willReturn(Optional.empty());

        service.deleteBook("-1");

        verify(output, times(1)).showBookNotFound("-1");
    }


}