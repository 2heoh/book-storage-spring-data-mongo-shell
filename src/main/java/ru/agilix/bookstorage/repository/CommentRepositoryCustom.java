package ru.agilix.bookstorage.repository;

import ru.agilix.bookstorage.domain.Book;
import ru.agilix.bookstorage.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepositoryCustom {
    Optional<Book> findBook(String bookId);

    Optional<Comment> findById(String id);

    void delete(String commentId);

    Comment save(Comment comment);

    Comment update(Comment comment);

    List<Comment> findAllByBookId(String bookId);
}
