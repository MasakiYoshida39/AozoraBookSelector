#!/bin/bash

echo "🚀 AWS Elastic Beanstalk にデプロイ中..."

# 環境変数の設定（必要に応じて変更）
APP_NAME="aozora-book-selector"
ENVIRONMENT_NAME="aozora-book-selector-prod"
REGION="ap-northeast-1"

# WARファイルをビルド
echo "📦 WARファイルをビルド中..."
chmod +x build.sh
./build.sh

# Elastic Beanstalkアプリケーションが存在しない場合は作成
echo "🔍 アプリケーションの存在確認..."
if ! aws elasticbeanstalk describe-applications --application-names $APP_NAME --region $REGION > /dev/null 2>&1; then
    echo "📝 新しいアプリケーションを作成中..."
    aws elasticbeanstalk create-application --application-name $APP_NAME --region $REGION
fi

# 環境が存在しない場合は作成
echo "🔍 環境の存在確認..."
if ! aws elasticbeanstalk describe-environments --application-name $APP_NAME --environment-names $ENVIRONMENT_NAME --region $REGION > /dev/null 2>&1; then
    echo "🌍 新しい環境を作成中..."
    aws elasticbeanstalk create-environment \
        --application-name $APP_NAME \
        --environment-name $ENVIRONMENT_NAME \
        --solution-stack-name "64bit Amazon Linux 2 v5.8.0 running Tomcat 10 Corretto 17" \
        --region $REGION
fi

# アプリケーションバージョンを作成
VERSION_LABEL="v$(date +%Y%m%d-%H%M%S)"
echo "📦 アプリケーションバージョン $VERSION_LABEL を作成中..."
aws elasticbeanstalk create-application-version \
    --application-name $APP_NAME \
    --version-label $VERSION_LABEL \
    --source-bundle S3Bucket="elasticbeanstalk-$REGION-$(aws sts get-caller-identity --query Account --output text)",S3Key="AozoraBookSelector.war" \
    --region $REGION

# 環境を更新
echo "🔄 環境を更新中..."
aws elasticbeanstalk update-environment \
    --application-name $APP_NAME \
    --environment-name $ENVIRONMENT_NAME \
    --version-label $VERSION_LABEL \
    --region $REGION

echo "✅ デプロイ完了！"
echo "🌐 アプリケーションURL: https://$ENVIRONMENT_NAME.$REGION.elasticbeanstalk.com" 