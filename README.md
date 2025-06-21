# AozoraBookSelector

青空文庫関連の書籍選択・管理を行うWebアプリケーションです。

## 概要

- **技術スタック**: Java (Jakarta EE), JSP, MySQL
- **フレームワーク**: Servlet/JSP
- **データベース**: MySQL
- **Webサーバー**: Tomcat (推奨)
- **外部API**: [ZORAPI（ゾラピ）](https://api.bungomail.com/) - 青空文庫非公式API
- **フォールバック機能**: ZORAPIが利用できない場合のWebスクレイピング

## 機能

- ユーザー認証（ログイン・ログアウト）
- 新規ユーザー登録
- セッション管理
- **🎲 青空文庫ランダム選書機能** (新機能)

### ランダム選書機能について

青空文庫の主要な作家（夏目漱石、芥川龍之介、太宰治、森鴎外、宮沢賢治、川端康成、谷崎潤一郎、志賀直哉、有島武郎、武者小路実篤）の作品の中から、ランダムに1冊を選んでおすすめします。

**機能の特徴:**
- 主要な作家の作品からランダム選択
- 作品の詳細情報（説明文）を表示
- 青空文庫の該当ページへの直接リンク
- 美しいモダンなUIデザイン
- 何度でも新しい本を選べる機能
- **ZORAPIによる豊富な作品情報**
  - カテゴリ情報
  - アクセス数
  - 作品ID
  - 書き出し文
- **フォールバック機能**
  - ZORAPIが利用できない場合の自動切り替え
  - Webスクレイピングによる代替データ取得

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

### ランダム選書機能
1. メインページにログイン後、「📖 本を選んでもらう」ボタンをクリック
2. システムが青空文庫からランダムに1冊を選択
3. 選ばれた本の情報（タイトル、作者、説明）を確認
4. 「📖 青空文庫で読む」ボタンで青空文庫の該当ページに移動
5. 「🎲 別の本を選ぶ」ボタンで新しい本を選択

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
│       │       ├── LogoutServlet.java
│       │       ├── AozoraBookSelector.java  ← ランダム選書機能のコア
│       │       └── RandomBookServlet.java   ← ランダム選書のWeb API
│       └── webapp/
│           ├── index.jsp              ← 更新：ランダム選書へのリンク追加
│           ├── randombook.jsp         ← 新規：ランダム選書結果表示ページ
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
- **ランダム選書機能の拡張**
  - ジャンル別選択
  - 作者別選択
  - お気に入り機能
  - 読書記録機能

## 技術的な詳細

### ランダム選書機能の実装

1. **AozoraBookSelector.java**: 青空文庫のデータ取得とランダム選択のロジック
   - [ZORAPI（ゾラピ）](https://api.bungomail.com/)を使用して作品情報を取得
   - JSONレスポンスを解析して作品情報を抽出
   - 主要な作家の作品一覧から選択
   - 24時間のキャッシュ機能
   - **フォールバック機能**: ZORAPI失敗時のWebスクレイピング

2. **RandomBookServlet.java**: Web APIとしての機能提供
   - `/randombook` エンドポイントでアクセス
   - 選ばれた本の情報をJSPに渡す

3. **randombook.jsp**: 美しいUIでの結果表示
   - モダンなCSSデザイン
   - レスポンシブ対応
   - アニメーション効果
   - 作品の詳細情報表示
   - データソースの表示（ZORAPI/WebScraping）

### 使用しているAPI

#### ZORAPI（ゾラピ）
- **URL**: https://api.bungomail.com/
- **説明**: 青空文庫の作品データを検索できる非公式API
- **エンドポイント**: 
  - `/v0/books` - 作品情報API
  - `/v0/persons` - 人物情報API
- **データソース**: 青空文庫の公式データを基にしたスプレッドシート

#### APIの利点
- **構造化されたデータ**: JSON形式で統一されたレスポンス
- **豊富な情報**: 作品ID、カテゴリ、アクセス数、書き出し文など
- **検索機能**: 作家名や作品名での検索
- **安定性**: Webスクレイピングよりも安定したデータ取得
- **利用規約準拠**: 適切なAPI利用

### フォールバック機能

#### 動作仕様
1. **優先順位**: ZORAPI → Webスクレイピング
2. **自動切り替え**: ZORAPIが503エラーなどの障害時に自動でWebスクレイピングに切り替え
3. **データソース表示**: UIでどちらの方法でデータを取得したかを表示
4. **情報の違い**:
   - **ZORAPI**: カテゴリ、アクセス数、書き出し文を含む豊富な情報
   - **Webスクレイピング**: 基本的な作品情報（タイトル、作者、URL）

#### エラーハンドリング
- **503 Service Unavailable**: ZORAPIサーバー障害
- **ネットワークエラー**: 接続タイムアウト、DNS解決失敗
- **JSON解析エラー**: レスポンス形式の変更
- **Webスクレイピングエラー**: 青空文庫サイトの構造変更

### 対応作家一覧

現在、以下の作家の作品から選択されます：
- 夏目漱石
- 芥川龍之介
- 太宰治
- 森鴎外
- 宮沢賢治
- 川端康成
- 谷崎潤一郎
- 志賀直哉
- 有島武郎
- 武者小路実篤

### キャッシュ機能

- **24時間キャッシュ**: 一度取得したデータを24時間保存
- **高速化**: 2回目以降はキャッシュから即座に取得
- **API負荷軽減**: ZORAPIサーバーへのアクセス回数を削減
- **エラー耐性**: ネットワークエラー時のフォールバック

### トラブルシューティング

#### よくある問題と対処法

1. **ZORAPI 503エラー**
   - 原因: ZORAPIサーバーの一時的な障害
   - 対処: 自動的にWebスクレイピングに切り替わります
   - 確認: UIのデータソースバッジで確認可能

2. **Webスクレイピング失敗**
   - 原因: 青空文庫サイトの構造変更
   - 対処: 正規表現パターンの更新が必要
   - 確認: ログでエラーメッセージを確認

3. **キャッシュ問題**
   - 原因: 古いデータがキャッシュされている
   - 対処: `AozoraBookSelector.clearCache()` でキャッシュをクリア
   - 確認: `AozoraBookSelector.showCacheStatus()` でキャッシュ状態を確認

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。 