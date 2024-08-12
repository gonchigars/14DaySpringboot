package com.example.demo.scheduletask;

import com.example.demo.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.repository.BookRepository;

@Component
public class BookCleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(BookCleanupScheduler.class);

    private static final Logger logger = LoggerFactory.getLogger(BookCleanupScheduler.class);
    private final BookRepository bookRepository;

    @Autowired
    public BookCleanupScheduler(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Run this task every 30 seconds
    @Scheduled(cron = "0/30 * * * * ?")
    public void cleanUpOldBooks() {
        LocalDate tenYearsAgo = LocalDate.now().minus(10, ChronoUnit.YEARS);
        long count = bookRepository.findAll().stream()
                .filter(book -> book.getPublishedDate().isBefore(tenYearsAgo))
                .peek(book -> bookRepository.delete(book))
                .count();

        if (count > 0) {
            logger.info("Old books cleaned up successfully. Total deleted: {}", count);
        } else {
            logger.info("No old books found to clean up.");
        }
        try {
            LocalDate tenYearsAgo = LocalDate.now().minus(10, ChronoUnit.YEARS);
            long count = bookRepository.findAll().stream()
                    .filter(book -> book.getPublishedDate().isBefore(tenYearsAgo))
                    .peek(book -> bookRepository.delete(book))
                    .count();

            if (count > 0) {
                logger.info("Old books cleaned. Total removed: {}", count);
            } else {
                logger.info("No old books found.");
            }
        } catch (Exception e) {
            logger.error("Critical error occurred during book cleanup: {}", e.getMessage());
        }
    }
}
