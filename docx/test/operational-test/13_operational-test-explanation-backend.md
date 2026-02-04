# 単体テスト(バックエンド)
## 目的
サービス層やユーティリティの動作確認。
DBアクセスや他サービス依存なしでロジックを検証。
## 対象
service/ 内のクラス
ドメインモデルのバリデーション
ユーティリティクラス（もしあるなら）
## ツール
JUnit 5
Mockito（依存オブジェクトのモック）
## ディレクトリ
```
backend/src/test/java/com/zestark/timewatch/service/
backend/src/test/java/com/zestark/timewatch/domain/
```
