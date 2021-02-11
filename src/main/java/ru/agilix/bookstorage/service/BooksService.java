package ru.agilix.bookstorage.service;

public interface BooksService {
    String createBook();

    String retrieveBook(String id);

    String updateBook(String id);

    String retrieveAllBooks();

    String deleteBook(String id);

}
