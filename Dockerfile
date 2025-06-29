FROM tomcat:10.1-jdk17

# 作業ディレクトリを設定
WORKDIR /usr/local/tomcat

# WARファイルをコピー（後で作成）
COPY AozoraBookSelector.war /usr/local/tomcat/webapps/

# ポート8080を公開
EXPOSE 8080

# Tomcatを起動
CMD ["catalina.sh", "run"] 