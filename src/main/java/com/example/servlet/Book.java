package com.example.servlet;

public class Book {
    private String title;
    private String author;
    private String category;
    private String excerpt;
    private String cardUrl;
    private String textUrl;
    private String htmlUrl;

    public Book(String title, String author, String category, String excerpt, String cardUrl, String textUrl, String htmlUrl) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.excerpt = excerpt;
        this.cardUrl = cardUrl;
        this.textUrl = textUrl;
        this.htmlUrl = htmlUrl;
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

    public String getCardUrl() {
        return cardUrl;
    }

    public String getTextUrl() {
        return textUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }
} 