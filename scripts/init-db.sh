#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    -- データベース初期化完了メッセージ
    SELECT 'Database initialized successfully' AS status;

    -- タイムゾーン設定
    SET timezone = 'Asia/Tokyo';

    -- UUID拡張の有効化（マイグレーションファイルでも実行されるが、念のため）
    CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

    -- 初期化完了ログ
    SELECT 'Timezone set to Asia/Tokyo' AS info;
    SELECT 'UUID extension enabled' AS info;
EOSQL

echo "PostgreSQL initialization completed successfully"
