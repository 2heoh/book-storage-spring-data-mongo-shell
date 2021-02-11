package ru.agilix.bookstorage.service;

public interface CommentsService {
    String addCommentByBookId(String bookId);

    String deleteComment(String id);

    String updateComment(String id);

    String getCommentsByBookId(String bookId);

}
