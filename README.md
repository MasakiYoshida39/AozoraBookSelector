# AozoraBookSelector

青空文庫関連の書籍選択・管理を行うWebアプリケーションです。

## 概要

- **技術スタック**: Java (Jakarta EE), JSP, MySQL
- **フレームワーク**: Servlet/JSP
- **データベース**: MySQL
- **Webサーバー**: Tomcat (推奨)

## 機能

- ユーザー認証（ログイン・ログアウト）
- 新規ユーザー登録
- セッション管理

## セットアップ手順

### 1. 必要な環境

- Java 17以上
- MySQL 8.0以上
- Tomcat 10以上
- Eclipse IDE (推奨)

### 2. データベース設定

#### データベース作成
```sql
CREATE DATABASE aozora_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE aozora_db;
```

#### ユーザーテーブル作成
```sql
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL
);
```

#### テストユーザー追加（オプション）
```sql
INSERT INTO users (username, password) VALUES ('user', 'pass');
```

### 3. JDBCドライバ配置

1. [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) をダウンロード
2. `mysql-connector-j-8.0.xx.jar` を `src/main/webapp/WEB-INF/lib/` に配置

### 4. データベース接続設定

`src/main/java/com/example/servlet/DBConfig.java` でDB接続情報（ユーザー名・パスワード・データベース名）を一元管理しています。

```java
public class DBConfig {
    public static final String JDBC_URL = "jdbc:mysql://localhost:3306/aozora_db?useSSL=false&serverTimezone=UTC";
    public static final String DB_USER = "root"; // MySQLユーザー名
    public static final String DB_PASSWORD = ""; // MySQLパスワード（空の場合は空文字）
}
```

**MySQLのユーザー名やパスワード、データベース名を変更したい場合は、このファイルのみ編集してください。**

### 5. プロジェクト実行

1. Eclipseでプロジェクトを開く
2. Tomcatサーバーを設定・起動
3. ブラウザで `http://localhost:8080/AozoraBookSelector/` にアクセス

## 使用方法

### ログイン
1. ログイン画面でユーザー名・パスワードを入力
2. 「ログイン」ボタンをクリック
3. 認証成功時はメインページに遷移

### 新規ユーザー登録
1. ログイン画面の「新規ユーザー登録」リンクをクリック
2. ユーザー名・パスワードを入力
3. 「登録」ボタンをクリック
4. 登録成功時はログイン画面に戻る

## プロジェクト構成

```
AozoraBookSelector/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/servlet/
│       │       ├── DBConfig.java      ← DB接続情報はここで一元管理
│       │       ├── LoginServlet.java
│       │       ├── RegisterServlet.java
│       │       ├── EditServlet.java
│       │       └── LogoutServlet.java
│       └── webapp/
│           ├── index.jsp
│           ├── login.jsp
│           ├── register.jsp
│           ├── edit.jsp
│           └── WEB-INF/
│               └── web.xml
└── README.md
```

## 注意事項

- パスワードは平文で保存されています（実運用ではハッシュ化を推奨）
- データベース接続情報はハードコーディングされています
- セキュリティ機能は最小限です

## トラブルシューティング

### よくあるエラー

1. **Access denied for user 'root'@'localhost'**
   - MySQLのユーザー名・パスワードが正しく設定されているか確認

2. **No database selected**
   - JDBC接続URLにデータベース名が含まれているか確認

3. **ClassNotFoundException: com.mysql.cj.jdbc.Driver**
   - JDBCドライバが正しく配置されているか確認

## 今後の拡張予定

- ログアウト機能
- パスワードハッシュ化
- 書籍管理機能
- 検索機能

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。 