# 画面遷移図
## 画面一覧
| 画面             | 説明                        |
| -------------- | ------------------------- |
| Task一覧         | ユーザーのタスク一覧を表示、作成・削除・開始ボタン |
| Task詳細         | 選択したタスクの詳細、進行中セッション表示     |
| TaskSession計測中 | 計測中画面、停止ボタン表示             |
| TaskEvent履歴    | タスクに紐づくイベント履歴を表示          |
## 遷移フロー
```
[Task一覧]
    │ 新規作成
    ▼
[Task詳細] ← 選択
    │ 計測開始
    ▼
[TaskSession計測中]
    │ 計測停止
    ▼
[Task詳細] 
    │ 履歴表示
    ▼
[TaskEvent履歴]
```
### 遷移条件
1. Task一覧 → Task詳細 ：タスク選択
2. Task詳細 → 計測中画面 ：「開始ボタン」押下
3. 計測中 → Task詳細 ：「停止ボタン」押下
4. Task詳細 → TaskEvent履歴 ：「履歴表示ボタン」押下
**ポイント：計測中は他タスクの開始ボタンを押せない（排他制御）**
## 状態遷移図(Task / TaskSession)
Task.status
```
PENDING ──開始──▶ RUNNING ──停止──▶ COMPLETED
```
TaskSession
```
NULL ──start_time設定──▶ RUNNING ──end_time設定──▶ COMPLETED
```
**TaskEventで START / STOP を記録**