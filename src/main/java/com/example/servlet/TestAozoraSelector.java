package com.example.servlet;

/**
 * 青空文庫ランダム選書機能のテストクラス
 * このクラスは開発・テスト用です。本番環境では削除してください。
 */
public class TestAozoraSelector {
    
    public static void main(String[] args) {
        System.out.println("=== 青空文庫ランダム選書機能 テスト ===");
        System.out.println();
        
        // 複数回テストして動作確認
        for (int i = 1; i <= 5; i++) {
            System.out.println("--- テスト " + i + " ---");
            testRandomBookSelection();
            System.out.println();
            
            // 少し待機（サーバーに負荷をかけないため）
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("=== テスト完了 ===");
    }
    
    private static void testRandomBookSelection() {
        try {
            System.out.println("本を選んでいます...");
            AozoraBookSelector.Book selectedBook = AozoraBookSelector.selectRandomBook();
            
            if (selectedBook != null) {
                System.out.println("✅ 選ばれた本: " + selectedBook.getTitle());
                System.out.println("👤 作者: " + selectedBook.getAuthor());
                System.out.println("🔗 URL: " + selectedBook.getUrl());
                
                if (selectedBook.getDescription() != null && !selectedBook.getDescription().isEmpty()) {
                    System.out.println("📝 説明: " + selectedBook.getDescription());
                } else {
                    System.out.println("📝 説明: 説明文はありません");
                }
            } else {
                System.out.println("❌ 本の選択に失敗しました");
            }
            
        } catch (Exception e) {
            System.err.println("❌ エラーが発生しました: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 