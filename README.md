# AozoraBookSelector

青空文庫からランダムに書籍を選択するJava Webアプリケーションです。

## 機能

- ユーザー登録・ログイン機能
- 青空文庫からのランダム書籍選択
- 書籍情報の表示（タイトル、著者、HTMLファイルURL）
- セッション管理

## 使用技術

- **Java Servlet/JSP** (Jakarta EE)
- **MySQL** (ユーザー認証)
- **青空文庫API** ([goark/aozora-api](https://github.com/goark/aozora-api))

## APIについて

このアプリケーションは以下のAPIを使用しています：

### 主要API: goark/aozora-api
- **URL**: `https://aozora-api.linhvu.dev/books`
- **形式**: RESTful API
- **言語**: Go言語で実装
- **特徴**: 
  - 公式的なAPIサービス
  - 豊富な書籍データ
  - 安定した動作
  - JSON形式のレスポンス

### 代替API
- **ZORAPI**: `https://api.bungomail.com/v0/books?limit=20`
- **作者別API**: `https://aozora-api.linhvu.dev/authors`

## セットアップ

### 1. データベース設定

MySQLでデータベースとテーブルを作成：

```sql
CREATE DATABASE aozora_book_selector;
USE aozora_book_selector;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 2. データベース接続設定

`src/main/java/com/example/util/DatabaseConfig.java` でデータベース接続情報を設定：

```java
public class DatabaseConfig {
    public static final String URL = "jdbc:mysql://localhost:3306/aozora_book_selector";
    public static final String USERNAME = "your_username";
    public static final String PASSWORD = "your_password";
}
```

### 3. プロジェクトの実行

1. Eclipseでプロジェクトをインポート
2. Tomcatサーバーを設定
3. プロジェクトを実行

## 使用方法

1. **ユーザー登録**: `/register.jsp` でアカウントを作成
2. **ログイン**: `/login.jsp` でログイン
3. **書籍選択**: `/book-selector.jsp` でランダム書籍を取得
4. **書籍閲覧**: 表示されたHTMLファイルURLから青空文庫で読書

## ファイル構成

```
src/main/
├── java/com/example/
│   ├── servlet/
│   │   ├── LoginServlet.java
│   │   ├── RegisterServlet.java
│   │   ├── LogoutServlet.java
│   │   ├── UserEditServlet.java
│   │   └── BookSelectorServlet.java
│   ├── model/
│   │   ├── User.java
│   │   └── Book.java
│   └── util/
│       └── DatabaseConfig.java
└── webapp/
    ├── WEB-INF/
    │   └── web.xml
    ├── login.jsp
    ├── register.jsp
    ├── dashboard.jsp
    ├── user-edit.jsp
    ├── book-selector.jsp
    └── book-result.jsp
```

## APIレスポンス形式

### goark/aozora-api
```json
[
  {
    "book_id": 123,
    "title": "作品タイトル",
    "authors": [
      {
        "last_name": "姓",
        "first_name": "名"
      }
    ],
    "html_url": "https://www.aozora.gr.jp/cards/.../files/...html"
  }
]
```

## 注意事項

- APIの利用制限やレート制限に注意してください
- ネットワーク接続が必要です
- APIが利用できない場合は、サンプルデータが表示されます

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。

## 参考リンク

- [青空文庫](https://www.aozora.gr.jp/)
- [goark/aozora-api](https://github.com/goark/aozora-api)
- [ZORAPI](https://api.bungomail.com/) 