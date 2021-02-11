package ru.agilix.bookstorage.repository.dsl;

public class Create {
    public static BookBuilder Book(String id) {
        return new BookBuilder(id);
    }

    public static CommentBuilder Comment() {
        return new CommentBuilder("-1");
    }

    public static BookBuilder Book() {
        return new BookBuilder("-1");
    }

    public static CommentBuilder Comment(String id) {
        return new CommentBuilder(id);
    }
}
