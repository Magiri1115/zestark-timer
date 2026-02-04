## データベース設計
### tasksテーブル
```
CREATE TABLE tasks (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL,
  name TEXT NOT NULL,
  status TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```
### task_sessionsテーブル
```
CREATE TABLE task_sessions (
  id UUID PRIMARY KEY,
  task_id UUID NOT NULL REFERENCES tasks(id),
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT now()
);
```
**CHECK制約**
```
ALTER TABLE task_sessions
ADD CONSTRAINT chk_time_order
CHECK (end_time IS NULL OR start_time < end_time);
```
**排他制約**
```
CREATE UNIQUE INDEX ux_task_running
ON task_sessions (task_id)
WHERE end_time IS NULL;
```
### task_eventsテーブル
```
CREATE TABLE task_events (
  id UUID PRIMARY KEY,
  task_id UUID NOT NULL REFERENCES tasks(id),
  event_type TEXT NOT NULL,
  occurred_at TIMESTAMP NOT NULL DEFAULT now()
);
```
## 運用イメージ
開発者がローカルでマイグレーションを実行
```
docker-compose exec backend bash -c "flyway migrate"
```