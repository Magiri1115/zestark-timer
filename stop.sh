#!/bin/bash

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "=== Zestark Timer 停止 ==="

# バックエンド停止
echo "[1/1] バックエンド停止中..."
cd "$PROJECT_DIR/docker"
docker compose down

echo ""
echo "=== 停止完了 ==="
