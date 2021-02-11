package ru.agilix.bookstorage.ui.output;

import ru.agilix.bookstorage.domain.Comment;

import java.util.List;

public interface CommentOutputService {

    String showCommentInfo(Comment comment);

    String showCommentDeletedMessage(String id);

    String showCommentNotFound(String id);

    String showCommentUpdated(Comment comment);

    String showListOfComments(List<Comment> comments);

}
