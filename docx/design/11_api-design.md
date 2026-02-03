# API設計
## Task API
```
| メソッド | パス          | 内容      |
| ---- | ----------- | ------- |
| GET  | /tasks      | タスク一覧取得 |
| POST | /tasks      | タスク作成   |
| GET  | /tasks/{id} | タスク詳細   |
| PUT  | /tasks/{id} | タスク更新   |
```
## TaskSession API
```
| メソッド | パス                | 内容   |
| ---- | ----------------- | ---- |
| POST | /tasks/{id}/start | 計測開始 |
| POST | /tasks/{id}/stop  | 計測停止 |
```