package ru.agilix.bookstorage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class Book {
    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @DBRef
    private List<Author> authors;

    @DBRef
    private Genre genre;

    private List<Comment> comments;

    public Book(String title, String description, List<Author> authors, Genre genre, List<Comment> comments) {
        this.title = title;
        this.description = description;
        this.authors = authors;
        this.genre = genre;
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Book('" + id + "','" + title + "','" + description + "',\n" + comments + "\n" + authors + ")";
    }
}
