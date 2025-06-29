# 💰 AWSコスト最適化ガイド

## 🎯 コスト削減の戦略

### 1. リザーブドインスタンスの活用

#### 1年契約（推奨）
```bash
# リザーブドインスタンスの購入
aws ec2 purchase-reserved-instances-offering \
    --reserved-instances-offering-id [OFFERING_ID] \
    --instance-count 1
```

**割引率**: 約40%
**月額コスト**: $8.47 → $5.08

#### 3年契約（長期運用）
**割引率**: 約60%
**月額コスト**: $8.47 → $3.39

### 2. 自動スケーリングの設定

#### Elastic Beanstalk設定
```yaml
# .ebextensions/02_autoscaling.config
option_settings:
  aws:autoscaling:asg:
    MinSize: 1
    MaxSize: 3
  aws:autoscaling:trigger:
    BreachDuration: 5
    LowerBreachScaleIncrement: -1
    UpperBreachScaleIncrement: 1
    LowerThreshold: 20
    UpperThreshold: 80
```

### 3. 使用時間の最適化

#### 開発環境の自動停止
```bash
# 夜間自動停止スクリプト
aws autoscaling set-desired-capacity \
    --auto-scaling-group-name [ASG_NAME] \
    --desired-capacity 0 \
    --honor-cooldown
```

#### スケジュール設定
- **平日**: 9:00-18:00 稼働
- **夜間**: インスタンス停止
- **休日**: 必要時のみ起動

### 4. データ転送の最適化

#### CloudFrontの活用
```yaml
# .ebextensions/03_cloudfront.config
option_settings:
  aws:elasticbeanstalk:environment:proxy:staticfiles:
    /static: /static
    /images: /images
    /css: /css
    /js: /js
```

**効果**: データ転送コストを最大50%削減

### 5. ストレージの最適化

#### EBS最適化
- **gp3**: 最新の汎用SSD（推奨）
- **io1**: 高パフォーマンス（必要時のみ）
- **削除**: 使用していないボリューム

## 📊 月額コスト比較表

| 構成 | 通常料金 | リザーブド1年 | リザーブド3年 | スポット |
|------|----------|---------------|---------------|----------|
| t3.micro | $25-30 | $15-18 | $10-12 | $2-5 |
| t3.small | $35-40 | $21-24 | $14-16 | $3-8 |
| t3.medium | $50-55 | $30-33 | $20-22 | $5-10 |

## 🎯 推奨構成

### 個人利用・学習目的
```
構成: t3.micro + リザーブド1年契約
月額: $15-18 (約2,100-2,500円)
年間: $180-216 (約25,200-30,240円)
```

### 小規模ビジネス
```
構成: t3.small + リザーブド1年契約
月額: $21-24 (約2,900-3,400円)
年間: $252-288 (約35,280-40,320円)
```

### 中規模サービス
```
構成: t3.medium + リザーブド1年契約 + CloudFront
月額: $30-35 (約4,200-4,900円)
年間: $360-420 (約50,400-58,800円)
```

## 🚨 コスト監視

### CloudWatch Billing
```bash
# コストアラートの設定
aws cloudwatch put-metric-alarm \
    --alarm-name "MonthlyCost" \
    --alarm-description "月額コスト監視" \
    --metric-name EstimatedCharges \
    --namespace AWS/Billing \
    --statistic Maximum \
    --period 86400 \
    --threshold 30 \
    --comparison-operator GreaterThanThreshold
```

### コスト分析ツール
- **AWS Cost Explorer**: 詳細なコスト分析
- **AWS Budgets**: 予算設定とアラート
- **AWS Trusted Advisor**: コスト最適化提案

## 💡 実践的なコスト削減Tips

### 1. 開発環境の分離
```
本番環境: t3.small (常時稼働)
開発環境: t3.micro (スポットインスタンス)
テスト環境: 必要時のみ起動
```

### 2. リソースの定期チェック
```bash
# 未使用リソースの確認
aws ec2 describe-instances --filters "Name=state-name,Values=stopped"
aws ec2 describe-volumes --filters "Name=status,Values=available"
```

### 3. タグ付けによる管理
```bash
# コスト配分のためのタグ付け
aws ec2 create-tags \
    --resources i-1234567890abcdef0 \
    --tags Key=Environment,Value=Production Key=Project,Value=AozoraBookSelector
```

## 📈 スケーリング戦略

### 段階的スケールアップ
1. **開始時**: t3.micro
2. **アクセス増加**: t3.small
3. **本格運用**: t3.medium
4. **高負荷**: t3.large + 複数インスタンス

### 負荷テスト
```bash
# Apache Bench での負荷テスト
ab -n 1000 -c 10 http://your-app-url/
```

## 🎉 まとめ

**最適化後の月額コスト:**
- **個人利用**: $15-18 (約2,100-2,500円)
- **小規模ビジネス**: $21-24 (約2,900-3,400円)
- **中規模サービス**: $30-35 (約4,200-4,900円)

**年間コスト:**
- **個人利用**: $180-216 (約25,200-30,240円)
- **小規模ビジネス**: $252-288 (約35,280-40,320円)
- **中規模サービス**: $360-420 (約50,400-58,800円)

---

**適切な最適化により、コストを最大60%削減可能です！** 💰✨ 