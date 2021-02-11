package ru.agilix.bookstorage.service;

import org.springframework.stereotype.Service;
import ru.agilix.bookstorage.repository.CommentRepositoryImpl;
import ru.agilix.bookstorage.ui.output.BookOutputService;
import ru.agilix.bookstorage.ui.output.CommentOutputService;

@Service
public class CLICommentsService implements CommentsService {
    private final CommentRepositoryImpl commentRepository;

    private final CommentOutputService ui;
    private final InputService input;
    private final DateService date;
    private final BookOutputService bookUi;

    public CLICommentsService(CommentRepositoryImpl commentRepository, CommentOutputService ui, InputService input, DateService date, BookOutputService bookUi) {
        this.commentRepository = commentRepository;
        this.ui = ui;
        this.input = input;
        this.date = date;
        this.bookUi = bookUi;
    }

    @Override
    public String addCommentByBookId(String bookId) {
        final var book = commentRepository.findBook(bookId);
        if (book.isPresent()) {
            final var newComment = input.getNewComment(book.get().getId());
            newComment.setBook(book.get());
            newComment.setDate(date.getCurrentDate());
            final var comment = commentRepository.save(newComment);
            return ui.showCommentInfo(comment);
        } else {
            return bookUi.showBookNotFound(bookId);
        }

    }

    @Override
    public String deleteComment(String id) {
        final var book = commentRepository.findBook(id);
        if (book.isPresent()) {
            commentRepository.delete(id);
            return ui.showCommentDeletedMessage(id);
        } else {
            return ui.showCommentNotFound(id);
        }
    }

    @Override
    public String updateComment(String id) {
        final var comment = commentRepository.findById(id);
        if(comment.isPresent()) {
            final var updatedComment = input.getUpdatedComment(comment.get());
            updatedComment.setDate(date.getCurrentDate());
            final var savedComment = commentRepository.update(updatedComment);
            return ui.showCommentUpdated(savedComment);
        } else {
            return ui.showCommentNotFound(id);
        }
    }

    @Override
    public String getCommentsByBookId(String bookId) {

        if (commentRepository.findBook(bookId).isEmpty()) {
            return bookUi.showBookNotFound(bookId);
        }

        final var comments = commentRepository.findAllByBookId(bookId);
        return ui.showListOfComments(comments);

    }

}
