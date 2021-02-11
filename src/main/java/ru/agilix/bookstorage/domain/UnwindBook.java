package ru.agilix.bookstorage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class UnwindBook {

    private String id;

    private String title;

    private String description;

    private Comment comments;
}
