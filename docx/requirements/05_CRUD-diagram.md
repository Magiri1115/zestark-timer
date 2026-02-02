# CRUD-diagram
## 共通認識
### 主要エンティティ
Task：論理的タスク（夜ごはん、SNS）
TaskSession：開始〜停止〜再開を含む実行単位
TaskEvent：開始/停止/再開/自動終了などのイベントログ
### 状態（TaskSession）
idle（未開始）
running
paused
ended
### CRUD記号
C：Create
R：Read
U：Update
D：Delete
–：操作なし
## CRUDマトリックス
### イベント別エンティティ
| 現在状態    | イベント | Task | TaskSession    | TaskEvent |
| ------- | ---- | ---- | -------------- | --------- |
| idle    | 開始   | R    | **C**（running） | **C**     |
| running | 停止   | R    | **U**（paused）  | **C**     |
| paused  | 再開   | R    | **U**（running） | **C**     |
| running | 自動終了 | R    | **U**（ended）   | **C**     |
| running | 手動終了 | R    | **U**（ended）   | **C**     |
| paused  | 手動終了 | R    | **U**（ended）   | **C**     |
