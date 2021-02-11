package ru.agilix.bookstorage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    private String id;

    private String text;

    private String author;

    private Date date;

    private Book book;

    public Comment(String id, String text, String author, Date date) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.date = date;
    }

    public Comment(String text, String author) {
        this.text = text;
        this.author = author;
    }

    public Comment(String id, String text, String author) {
        this.id = id;
        this.text = text;
        this.author = author;
    }

    @Override
    public String toString() {
        return String.format("Comment(%s,'%s','%s','%s', %s)", id, text, author, date, book);
    }
}
