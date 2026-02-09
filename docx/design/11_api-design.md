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
## Session操作API（統一）
```
| メソッド | パス                  | 内容         |
| ---- | ------------------- | ------------ |
| POST | /sessions/start     | 計測開始       |
| POST | /sessions/pause     | 一時停止        |
| POST | /sessions/resume    | 再開          |
| POST | /sessions/end       | 手動終了        |
| POST | /sessions/auto-end  | 自動終了        |
| GET  | /sessions/{id}      | セッション詳細取得 |
| GET  | /tasks/{id}/sessions| タスクのセッション一覧 |
```
### リクエスト例（start）
```
POST /sessions/start
{ "task_id": "UUID", "planned_duration_minutes": 30 }
```
### レスポンス例（共通）
```
200 OK
{ "session_id": "UUID", "status": "running" }
```
## エクスポートAPI
```
| メソッド | パス                         | 内容                    |
| ---- | ---------------------------- | --------------------- |
| GET  | /export/sessions             | CSVエクスポート（期間指定）   |
```
クエリ:
```
GET /export/sessions?from=YYYY-MM-DD&to=YYYY-MM-DD
```
