package ru.agilix.bookstorage.service;

import org.springframework.stereotype.Service;
import ru.agilix.bookstorage.domain.Genre;
import ru.agilix.bookstorage.repository.GenreRepository;
import ru.agilix.bookstorage.ui.output.GenreOutputService;

import java.util.List;

@Service
public class CLIGenresService implements GenresService {
    private final GenreOutputService ui;
    private final GenreRepository repository;

    public CLIGenresService(GenreOutputService ui, GenreRepository repository) {
        this.ui = ui;
        this.repository = repository;
    }

    @Override
    public String showAllGenres() {
        return ui.showGenreList((List<Genre>) repository.findAll());
    }
}
