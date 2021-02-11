package ru.agilix.bookstorage.ui.output;

import lombok.val;
import org.springframework.stereotype.Service;
import ru.agilix.bookstorage.domain.Comment;

import java.util.List;

@Service
public class TextCommentOutputService implements CommentOutputService {

    @Override
    public String showCommentInfo(Comment comment) {
        val table = new OutputTwoColumnTable("New comment #" + comment.getId());
        table.row(
                String.format("%s said at %s", comment.getAuthor(), comment.getDate()),
                comment.getText()
        );
        return table.render();
    }

    @Override
    public String showCommentDeletedMessage(String id) {
        return new OutputMessage(String.format("Comment #%s successfully deleted", id)).render();
    }

    @Override
    public String showCommentNotFound(String id) {
        return new OutputMessage(String.format("Comment #%s is not found", id)).render();
    }

    @Override
    public String showCommentUpdated(Comment comment) {
        val table = new OutputTwoColumnTable(
                String.format("Comment #%s successfully updated:", comment.getId())
        );
        table.row(
                String.format("%s said at %s", comment.getAuthor(), comment.getDate()),
                comment.getText()
        );
        return table.render();
    }

    @Override
    public String showListOfComments(List<Comment> comments) {

        String result = new OutputMessage("Comments:").render() + "\n";

        for (Comment comment : comments) {

            result += new OutputMessage(String.format(
                    "#%s '%s' said at %s: %s",
                    comment.getId(),
                    comment.getAuthor(),
                    comment.getDate(),
                    comment.getText())).render() + "\n";
        }

        return result;
    }

}
