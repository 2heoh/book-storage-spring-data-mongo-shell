package ru.agilix.bookstorage.ui.output;

import lombok.val;
import org.springframework.stereotype.Service;
import ru.agilix.bookstorage.domain.Author;
import ru.agilix.bookstorage.domain.Book;
import ru.agilix.bookstorage.domain.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TextBookOutputService implements BookOutputService {

    @Override
    public String showBooksList(List<Book> books) {

        if (books.isEmpty())
            return new OutputMessage("Books not found.").render();

        val table = new OutputTwoColumnTable("List of books:");
        table.header("id", "title");
        for (Book book : books) {
            table.row(String.valueOf(book.getId()), book.getTitle());
        }
        return table.render();
    }

    @Override
    public String showBookInfo(Book book) {

        final var table = new OutputOneColumnTable(
            String.format("#%s %s", book.getId(), book.getTitle())
        );

        if (!book.getAuthors().isEmpty()) {
            String authors = "by: " + book.getAuthors().stream().map(Author::getName).collect(Collectors.joining(", "));
            table.row(authors);
        }
        if (book.getGenre() != null) {
            table.row("genre: " + book.getGenre().getName());
        }
        if (book.getDescription() == null || book.getDescription().equals("")) {
            table.row("description is not set");
        } else {
            table.row(book.getDescription());
        }
        return table.render();
    }

    @Override
    public String showBookCreatedMessage(Book inserted) {
        val table = new OutputOneColumnTable("Created book:");

        for (String line : List.of(String.format("#%s %s", inserted.getId(), inserted.getTitle()))) {
            table.row(line);
        }
        return table.render();
    }

    @Override
    public String showBookDeletedMessage(String id) {
        return new OutputMessage(String.format("Deleted book: #%s", id)).render();
    }

    @Override
    public String showBookNotFound(String id) {
        return new OutputMessage(String.format("Book not found: #%s", id)).render();
    }



}
