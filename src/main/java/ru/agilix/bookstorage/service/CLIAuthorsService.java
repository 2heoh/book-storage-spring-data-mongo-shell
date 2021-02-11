package ru.agilix.bookstorage.service;

import org.springframework.stereotype.Service;
import ru.agilix.bookstorage.domain.Author;
import ru.agilix.bookstorage.repository.AuthorRepository;
import ru.agilix.bookstorage.ui.output.AuthorOutputService;

import java.util.List;

@Service
public class CLIAuthorsService implements AuthorsService {
    private final AuthorRepository repository;
    private final AuthorOutputService ui;

    public CLIAuthorsService(AuthorRepository repository, AuthorOutputService ui) {
        this.ui = ui;
        this.repository = repository;
    }

    @Override
    public String showAllAuthors() {
        final var authors = (List<Author>) repository.findAll();

        if (authors.isEmpty())
            return ui.showEmptyAuthorsList();

        return ui.showAuthorsList(authors);
    }

}
