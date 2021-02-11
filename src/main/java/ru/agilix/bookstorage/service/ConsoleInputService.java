package ru.agilix.bookstorage.service;

import lombok.val;
import org.springframework.stereotype.Service;
import ru.agilix.bookstorage.domain.Author;
import ru.agilix.bookstorage.domain.Book;
import ru.agilix.bookstorage.domain.Comment;
import ru.agilix.bookstorage.domain.Genre;
import ru.agilix.bookstorage.ui.UpdateService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsoleInputService implements InputService {
    private final UpdateService updateService;

    public ConsoleInputService(UpdateService updateService) {
        this.updateService = updateService;
    }

    @Override
    public Book getUpdatedBookInfo(Book existingBook, List<Author> authors, List<Genre> genres) {
        String newTitle = updateService.getNewValueFor("title", existingBook.getTitle());
        String newDescription = updateService.getNewValueFor("description", existingBook.getDescription());
        val newAuthors = updateService.getNewAuthor(authors);
        Genre newGenre = updateService.getNewGenre(genres);
        return new Book(existingBook.getId(), newTitle, newDescription, newAuthors, newGenre, existingBook.getComments());
    }

    @Override
    public Book getNewBook(List<Author> authors, List<Genre> genres) {
        String newTitle = updateService.getNewValueFor("title", "");
        String newDescription = updateService.getNewValueFor("description", "");
        List<Author> newAuthors = updateService.getNewAuthor(authors);
        Genre newGenre = updateService.getNewGenre(genres);
        return new Book(newTitle, newDescription, newAuthors, newGenre, new ArrayList<>());
    }

    @Override
    public Comment getNewComment(String bookId) {
        String author = updateService.getNewValueFor("author", "");
        String text = updateService.getNewValueFor("text", "");
        return new Comment(text, author);
    }

    @Override
    public Comment getUpdatedComment(Comment comment) {
        String author = updateService.getNewValueFor("author", comment.getAuthor());
        comment.setAuthor(author);
        String text = updateService.getNewValueFor("text", comment.getText());
        comment.setText(text);
        return comment;
    }

}
