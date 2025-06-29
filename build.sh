#!/bin/bash

echo "🚀 AozoraBookSelector WARファイルをビルド中..."

# プロジェクトディレクトリに移動
cd "$(dirname "$0")"

# buildディレクトリを作成
mkdir -p build/WEB-INF

# Javaクラスをコンパイル
echo "📦 Javaクラスをコンパイル中..."
mkdir -p build/WEB-INF/classes
javac -cp "src/main/webapp/WEB-INF/lib/*" -d build/WEB-INF/classes src/main/java/com/example/servlet/*.java

# リソースファイルをコピー
echo "📁 リソースファイルをコピー中..."
cp -r src/main/resources/* build/WEB-INF/
cp -r src/main/webapp/* build/
rm -rf build/WEB-INF/classes

# WARファイルを作成
echo "📦 WARファイルを作成中..."
cd build
jar -cf ../AozoraBookSelector.war .

echo "✅ ビルド完了！ AozoraBookSelector.war が作成されました" 