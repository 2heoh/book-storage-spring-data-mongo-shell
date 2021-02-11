package ru.agilix.bookstorage.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.agilix.bookstorage.domain.Author;
import ru.agilix.bookstorage.domain.Book;
import ru.agilix.bookstorage.domain.Comment;
import ru.agilix.bookstorage.domain.Genre;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "sergey.lobin", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }


    @ChangeSet(order = "002", id = "insertAuthorUnknown", author = "sergey.lobin")
    public void insertAuthorUnknown(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("authors");
        var doc = new Document().append("name", "Unknown");
        myCollection.insertOne(doc);
    }


    @ChangeSet(order = "005", id = "insertGenreUnknown", author = "sergey.lobin")
    public void insertGenreUnknown(MongockTemplate mongockTemplate) {
        mongockTemplate.save(new Genre("g00","Unknown"), "genres");
    }

    @ChangeSet(order = "006", id = "insertGenreAction", author = "sergey.lobin")
    public void insertGenreAction(MongockTemplate mongockTemplate) {
        mongockTemplate.save(new Genre("g02","Action"), "genres");
    }

    @ChangeSet(order = "008", id = "insertGenreSciFi", author = "sergey.lobin")
    public void insertGenreSciFi(MongockTemplate mongockTemplate) {
        mongockTemplate.save(new Genre("g03","Science Fiction (Sci-Fi)"), "genres");
    }

    @ChangeSet(order = "009", id = "insertBookWarAndPeace", author = "sergey.lobin")
    public void insertBookWarAndPeace(MongockTemplate mongockTemplate) throws ParseException {

        final var author = mongockTemplate.save(new Author("a01","Лев Толстой"), "authors");

        final var first = new Comment("c01", "first", "john doe", getDate("2021-02-01 10:00:00"));
        final var second = new Comment("c02", "second", "john doe", getDate("2021-02-01 10:02:00"));

        final var classics = mongockTemplate.insert(new Genre("g01", "Classics"), "genres");

        final var book = mongockTemplate.save(new Book("b01", "Война и мир", "Войнa и мир — роман-эпопея Льва Николаевича Толстого, описывающий русское общество в " +
                "эпоху войн против Наполеона в 1805—1812 годах. Эпилог романа доводит повествование до 1820 года."
                , List.of(author), classics, List.of(first, second)), "books");

    }

    private Timestamp getDate(String inputDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse(inputDate);
        long time = date.getTime();
        return new Timestamp(time);
    }

    @ChangeSet(order = "010", id = "insertBookViy", author = "sergey.lobin")
    public void insertBookViy(MongockTemplate mongockTemplate) {
        final var third = new Comment("third", "john doe");
        final var author = mongockTemplate.save(new Author("a02","Николай Гоголь"), "authors");
        mongockTemplate.save(new Book("Вий", "", List.of(author), null, List.of(third)), "books");
    }

}
