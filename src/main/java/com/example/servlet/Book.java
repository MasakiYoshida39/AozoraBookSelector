package com.example.servlet;

public class Book {
    private String title;
    private String author;
    private String category;
    private String excerpt;

    public Book(String title, String author, String category, String excerpt) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.excerpt = excerpt;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getExcerpt() {
        return excerpt;
    }
} 