# 🚀 AWSデプロイガイド

## 📋 前提条件

### 1. AWSアカウントの準備
- AWSアカウントを作成
- IAMユーザーを作成（管理者権限またはElastic Beanstalk権限）
- AWS CLIをインストール

### 2. AWS CLIの設定
```bash
aws configure
```
以下の情報を入力：
- AWS Access Key ID
- AWS Secret Access Key
- Default region: `ap-northeast-1`
- Default output format: `json`

## 🎯 デプロイ方法

### 方法1: Elastic Beanstalk（推奨）

#### 1. プロジェクトの準備
```bash
# プロジェクトディレクトリに移動
cd AozoraBookSelector

# ビルドスクリプトに実行権限を付与
chmod +x build.sh
chmod +x deploy-eb.sh
```

#### 2. WARファイルの作成
```bash
./build.sh
```

#### 3. Elastic Beanstalkへのデプロイ
```bash
./deploy-eb.sh
```

#### 4. 手動デプロイ（GUI）
1. AWS Management Consoleにログイン
2. Elastic Beanstalkサービスに移動
3. 「新しいアプリケーションを作成」をクリック
4. アプリケーション名: `aozora-book-selector`
5. プラットフォーム: `Tomcat`
6. プラットフォームブランチ: `Tomcat 10 Corretto 17`
7. プラットフォームバージョン: 最新版
8. アプリケーションコード: `AozoraBookSelector.war`をアップロード
9. 「アプリケーションを作成」をクリック

### 方法2: EC2 + Tomcat

#### 1. EC2インスタンスの作成
- Amazon Linux 2 AMI
- t3.micro（無料枠）または t3.small
- セキュリティグループでポート80, 443, 8080を開放

#### 2. Tomcatのインストール
```bash
# システムを更新
sudo yum update -y

# Java 17をインストール
sudo yum install java-17-amazon-corretto -y

# Tomcat 10をインストール
sudo yum install tomcat10 -y

# Tomcatを起動
sudo systemctl start tomcat10
sudo systemctl enable tomcat10
```

#### 3. アプリケーションのデプロイ
```bash
# WARファイルをアップロード
sudo cp AozoraBookSelector.war /usr/share/tomcat10/webapps/

# Tomcatを再起動
sudo systemctl restart tomcat10
```

### 方法3: Docker + ECS

#### 1. Dockerイメージのビルド
```bash
docker build -t aozora-book-selector .
```

#### 2. ECRにプッシュ
```bash
aws ecr create-repository --repository-name aozora-book-selector
aws ecr get-login-password --region ap-northeast-1 | docker login --username AWS --password-stdin [ACCOUNT_ID].dkr.ecr.ap-northeast-1.amazonaws.com
docker tag aozora-book-selector:latest [ACCOUNT_ID].dkr.ecr.ap-northeast-1.amazonaws.com/aozora-book-selector:latest
docker push [ACCOUNT_ID].dkr.ecr.ap-northeast-1.amazonaws.com/aozora-book-selector:latest
```

## 🔧 設定とカスタマイズ

### 環境変数の設定
Elastic Beanstalkコンソールで以下を設定：
- `JAVA_HOME`: `/usr/lib/jvm/java-17-amazon-corretto`
- `JVM_OPTS`: `-Xms256m -Xmx512m`

### ドメインの設定
1. Route 53でドメインを購入
2. Elastic Beanstalk環境にカスタムドメインを設定
3. SSL証明書をACMで取得・設定

### ロードバランサーの設定
- Application Load Balancerを使用
- ヘルスチェックパス: `/`
- セッションアフィニティを有効化

## 📊 監視とログ

### CloudWatchの設定
- ログの自動保存
- メトリクスの監視
- アラームの設定

### ログの確認
```bash
# Elastic Beanstalkログの取得
eb logs

# リアルタイムログ
eb logs --all
```

## 🔒 セキュリティ

### セキュリティグループの設定
- ポート80, 443のみ開放
- ソースIPを制限（必要に応じて）

### IAMロールの設定
- Elastic Beanstalk用のIAMロール
- 必要最小限の権限を付与

## 💰 コスト最適化

### 無料枠の活用
- t3.microインスタンス（月750時間無料）
- Elastic Beanstalk（無料枠あり）

### コスト削減のポイント
- 使用していない時はインスタンスを停止
- リザーブドインスタンスの活用
- 適切なインスタンスサイズの選択

## 🚨 トラブルシューティング

### よくある問題

1. **デプロイ失敗**
   - ログを確認: `eb logs`
   - WARファイルの整合性を確認
   - メモリ不足の場合はインスタンスサイズを増加

2. **アプリケーションが起動しない**
   - Tomcatログを確認
   - ポート設定を確認
   - セキュリティグループの設定を確認

3. **パフォーマンスの問題**
   - CloudWatchメトリクスを確認
   - JVMヒープサイズを調整
   - インスタンスサイズを増加

## 📞 サポート

- AWSサポート: 有料サポートプラン
- Elastic Beanstalkドキュメント: https://docs.aws.amazon.com/elasticbeanstalk/
- AWS Developer Forums: https://forums.aws.amazon.com/

---

**デプロイが完了したら、アプリケーションURLにアクセスして動作確認を行ってください！** 🌐✨ 