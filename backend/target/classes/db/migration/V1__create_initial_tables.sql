-- UUID拡張の有効化
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Usersテーブル
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Tasksテーブル
CREATE TABLE tasks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    task_name TEXT NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_task_status CHECK (status IN ('PENDING', 'RUNNING', 'COMPLETED', 'CANCELLED'))
);

-- Task Sessionsテーブル
CREATE TABLE task_sessions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    task_id UUID NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_time_order CHECK (end_time IS NULL OR start_time < end_time)
);

-- 排他制約: 1つのタスクに対して同時に実行中のセッションは1つのみ
-- end_timeがNULLの場合のみ（実行中のセッション）、task_idがユニークである必要がある
CREATE UNIQUE INDEX ux_task_running ON task_sessions (task_id) WHERE end_time IS NULL;

-- Task Eventsテーブル
CREATE TABLE task_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    task_id UUID NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
    event_type VARCHAR(50) NOT NULL,
    occurred_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_event_type CHECK (event_type IN ('START', 'STOP', 'PAUSE', 'RESUME', 'COMPLETE', 'CANCEL'))
);

-- インデックス作成（パフォーマンス最適化）
CREATE INDEX idx_tasks_user_id ON tasks(user_id);
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_task_sessions_task_id ON task_sessions(task_id);
CREATE INDEX idx_task_sessions_start_time ON task_sessions(start_time);
CREATE INDEX idx_task_events_task_id ON task_events(task_id);
CREATE INDEX idx_task_events_occurred_at ON task_events(occurred_at DESC);

-- コメント追加（ドキュメント化）
COMMENT ON TABLE users IS 'ユーザー情報テーブル';
COMMENT ON TABLE tasks IS 'タスク情報テーブル';
COMMENT ON TABLE task_sessions IS 'タスクセッション（計測記録）テーブル';
COMMENT ON TABLE task_events IS 'タスクイベント履歴テーブル';

COMMENT ON COLUMN tasks.task_name IS 'タスク名（必須）';
COMMENT ON COLUMN tasks.status IS 'タスク状態: PENDING（待機）, RUNNING（実行中）, COMPLETED（完了）, CANCELLED（キャンセル）';
COMMENT ON COLUMN task_sessions.start_time IS 'セッション開始時刻';
COMMENT ON COLUMN task_sessions.end_time IS 'セッション終了時刻（NULL=実行中）';
COMMENT ON CONSTRAINT chk_time_order ON task_sessions IS '終了時刻は開始時刻より後でなければならない';
COMMENT ON INDEX ux_task_running IS '1タスクにつき実行中セッションは1つのみ（排他制約）';
