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
                if (cols.length < 58) continue; // 必要な列数があるか
                String title = cols[2]; // 作品名
                String author = cols[16]; // 姓名
                String category = cols[53]; // カテゴリ
                String excerpt = cols[57]; // 書き出し
                books.add(new Book(title, author, category, excerpt));
            }
        }
    }

    public Book getRandomBook() {
        if (books.isEmpty()) return null;
        return books.get(random.nextInt(books.size()));
    }
} 