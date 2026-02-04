# 結合テスト(バックエンド)
## 目的
Spring Context 全体、リポジトリ、DBアクセス、Controller経由のAPIを通して動作確認
## 対象
controller/ → service/ → repository/ のフロー
REST API エンドポイント
## ツール
Spring Boot Test (@SpringBootTest)
Testcontainers（PostgreSQLなどをDocker上で立ち上げ）
MockMvc / WebTestClient
## ディレクトリ
```
backend/src/test/java/com/zestark/timewatch/controller/
```