package ru.agilix.bookstorage.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import ru.agilix.bookstorage.domain.Book;
import ru.agilix.bookstorage.domain.Comment;
import ru.agilix.bookstorage.domain.UnwindBook;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public CommentRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<Comment> findById(String id) {
        final var query = new Query().addCriteria(Criteria.where("comments._id").is(id));
        return mongoTemplate.findOne(query, Book.class).getComments().stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<Book> findBook(String id) {
        final var query = new Query().addCriteria(Criteria.where("_id").is(id));
        return Optional.ofNullable(mongoTemplate.findOne(query, Book.class));
    }

    public Optional<Book> findBookByCommentId(String id) {
        final var query = new Query().addCriteria(Criteria.where("comments._id").is(id));
        return Optional.ofNullable(mongoTemplate.findOne(query, Book.class));
    }

    @Override
    public void delete(String commentId) {
        final var query = new Query().addCriteria(Criteria.where("comments._id").is(commentId));
        final var update = new Update().pull("comments", Query.query(Criteria.where("_id").is(commentId)));
        mongoTemplate.updateFirst(query, update, Book.class);
    }

    @Override
    public Comment save(Comment comment) {

        final var bookId = comment.getBook().getId();
        comment.setBook(null);
        comment.setId(new ObjectId().toString());
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(bookId)),
                new Update().push("comments", comment), Book.class
        );


        return findById(comment.getId()).get();
    }

    @Override
    public Comment update(Comment comment) {

        final var book = findBookByCommentId(comment.getId());
        if (book.isPresent()) {

            mongoTemplate.updateFirst(
                    Query.query(Criteria.where("_id").is(book.get().getId()).and("comments._id").is(comment.getId())),
                    new Update()
                            .set("comments.$.text", comment.getText())
                            .set("comments.$.author", comment.getAuthor())
                            .set("comments.$.date", comment.getDate()),
                    Book.class
            );
        }

        return findById(comment.getId()).get();
    }

    @Override
    public List<Comment> findAllByBookId(String bookId) {


        final var aggregation = newAggregation(
                unwind("comments"),
                match(new Criteria("_id").is(bookId)),
                sort(Sort.Direction.DESC, "comments.date"),
                project("comments")
        );

        final var results = mongoTemplate.aggregate(aggregation, "books", UnwindBook.class);

        return results.getMappedResults().stream().map(UnwindBook::getComments).collect(Collectors.toList());
    }
}
