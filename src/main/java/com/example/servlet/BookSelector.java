package com.example.servlet;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class BookSelector {
    private List<Book> books = new ArrayList<>();
    private Random random = new Random();

    public BookSelector(InputStream is) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            boolean isFirst = true;
            while ((line = br.readLine()) != null) {
                if (isFirst) { isFirst = false; continue; } // ヘッダー行スキップ
                String[] cols = line.split(",", -1);
                if (cols.length < 60) continue; // 必要な列数があるか
                String title = cols[1]; // 作品名（2列目）
                String author = cols[15]; // 姓名（16列目）
                String category = cols[59]; // カテゴリ（60列目）
                String excerpt = cols[57]; // 書き出し（58列目）
                String cardUrl = cols[13]; // 図書カードURL（14列目）
                String textUrl = cols[46]; // テキストファイルURL（47列目）
                String htmlUrl = cols[51]; // HTMLファイルURL（52列目）
                books.add(new Book(title, author, category, excerpt, cardUrl, textUrl, htmlUrl));
            }
        }
    }

    public Book getRandomBook() {
        if (books.isEmpty()) return null;
        return books.get(random.nextInt(books.size()));
    }
} 