package com.example.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ZORAPIの状況を調査するためのテストクラス
 */
public class AozoraApiTester {
    
    private static final String ZORAPI_BASE_URL = "https://api.bungomail.com/v0";
    private static final String BOOKS_ENDPOINT = ZORAPI_BASE_URL + "/books";
    private static final String PERSONS_ENDPOINT = ZORAPI_BASE_URL + "/persons";
    
    public static void main(String[] args) {
        System.out.println("=== ZORAPI 障害調査 ===");
        System.out.println();
        
        // 1. 基本的な接続テスト
        testBasicConnection();
        
        // 2. エンドポイント別テスト
        testEndpoints();
        
        // 3. レスポンスヘッダー分析
        analyzeResponseHeaders();
        
        // 4. 代替URLのテスト
        testAlternativeUrls();
        
        System.out.println("\n=== 調査完了 ===");
    }
    
    /**
     * 基本的な接続テスト
     */
    private static void testBasicConnection() {
        System.out.println("--- 基本接続テスト ---");
        
        try {
            URL url = new URL(ZORAPI_BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "AozoraApiTester/1.0");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            int responseCode = connection.getResponseCode();
            System.out.println("ベースURL レスポンスコード: " + responseCode);
            
            if (responseCode == 200) {
                System.out.println("✅ ベースURLは正常に応答しています");
            } else {
                System.out.println("❌ ベースURLでエラー: " + responseCode);
            }
            
        } catch (Exception e) {
            System.err.println("❌ 接続エラー: " + e.getMessage());
        }
    }
    
    /**
     * エンドポイント別テスト
     */
    private static void testEndpoints() {
        System.out.println("\n--- エンドポイント別テスト ---");
        
        String[] endpoints = {
            BOOKS_ENDPOINT,
            BOOKS_ENDPOINT + "?limit=1",
            PERSONS_ENDPOINT,
            PERSONS_ENDPOINT + "?limit=1"
        };
        
        for (String endpoint : endpoints) {
            testEndpoint(endpoint);
        }
    }
    
    /**
     * 特定のエンドポイントをテスト
     */
    private static void testEndpoint(String endpoint) {
        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "AozoraApiTester/1.0");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            
            System.out.println("エンドポイント: " + endpoint);
            System.out.println("  レスポンスコード: " + responseCode + " " + responseMessage);
            
            if (responseCode == 200) {
                System.out.println("  ✅ 正常");
            } else if (responseCode == 503) {
                System.out.println("  ❌ Service Unavailable - サーバー障害の可能性");
            } else if (responseCode == 429) {
                System.out.println("  ⚠️ Too Many Requests - レート制限の可能性");
            } else if (responseCode == 403) {
                System.out.println("  ❌ Forbidden - アクセス拒否の可能性");
            } else {
                System.out.println("  ❌ その他のエラー");
            }
            
            // レスポンスヘッダーを確認
            String retryAfter = connection.getHeaderField("Retry-After");
            if (retryAfter != null) {
                System.out.println("  📅 Retry-After: " + retryAfter + " 秒後");
            }
            
        } catch (Exception e) {
            System.err.println("❌ エンドポイントテストエラー (" + endpoint + "): " + e.getMessage());
        }
    }
    
    /**
     * レスポンスヘッダーを分析
     */
    private static void analyzeResponseHeaders() {
        System.out.println("\n--- レスポンスヘッダー分析 ---");
        
        try {
            URL url = new URL(BOOKS_ENDPOINT + "?limit=1");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "AozoraApiTester/1.0");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            int responseCode = connection.getResponseCode();
            System.out.println("レスポンスコード: " + responseCode);
            
            // 重要なヘッダーを確認
            String[] importantHeaders = {
                "Server", "X-Powered-By", "X-RateLimit-Limit", "X-RateLimit-Remaining",
                "X-RateLimit-Reset", "Retry-After", "Cache-Control", "Content-Type"
            };
            
            for (String headerName : importantHeaders) {
                String headerValue = connection.getHeaderField(headerName);
                if (headerValue != null) {
                    System.out.println(headerName + ": " + headerValue);
                }
            }
            
            // レスポンスボディを確認（エラーの場合）
            if (responseCode != 200) {
                System.out.println("\n--- エラーレスポンス ---");
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), "UTF-8"))) {
                    String line;
                    StringBuilder errorBody = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        errorBody.append(line).append("\n");
                    }
                    System.out.println(errorBody.toString());
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ ヘッダー分析エラー: " + e.getMessage());
        }
    }
    
    /**
     * 代替URLのテスト
     */
    private static void testAlternativeUrls() {
        System.out.println("\n--- 代替URLテスト ---");
        
        String[] alternativeUrls = {
            "https://api.bungomail.com/",
            "https://api.bungomail.com/v0/",
            "https://api.bungomail.com/v1/",
            "https://bungomail.com/api/",
            "https://www.bungomail.com/api/"
        };
        
        for (String urlString : alternativeUrls) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "AozoraApiTester/1.0");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                
                int responseCode = connection.getResponseCode();
                System.out.println(urlString + ": " + responseCode);
                
            } catch (Exception e) {
                System.out.println(urlString + ": 接続エラー");
            }
        }
    }
    
    /**
     * 青空文庫の直接アクセステスト
     */
    private static void testAozoraDirect() {
        System.out.println("\n--- 青空文庫直接アクセステスト ---");
        
        try {
            URL url = new URL("https://www.aozora.gr.jp/index_pages/person148.html");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            int responseCode = connection.getResponseCode();
            System.out.println("青空文庫直接アクセス: " + responseCode);
            
            if (responseCode == 200) {
                System.out.println("✅ 青空文庫は正常にアクセス可能");
            } else {
                System.out.println("❌ 青空文庫でエラー: " + responseCode);
            }
            
        } catch (Exception e) {
            System.err.println("❌ 青空文庫アクセスエラー: " + e.getMessage());
        }
    }
} 