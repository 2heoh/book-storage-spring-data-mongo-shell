package ru.agilix.bookstorage.service;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class CurrentDateService implements DateService {
    @Override
    public Date getCurrentDate() {
        return new Timestamp(System.currentTimeMillis());
    }
}
