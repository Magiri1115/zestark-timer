#!/bin/bash

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "=== Zestark Timer 起動 ==="

# バックエンド起動（Docker）
echo "[1/2] バックエンド起動中..."
cd "$PROJECT_DIR/docker"
docker compose up -d

echo ""
echo "[2/2] フロントエンド起動中..."
cd "$PROJECT_DIR/frontend"

# 初回のみ npm install
if [ ! -d "node_modules" ]; then
  echo "  npm install 実行中..."
  npm install
fi

echo ""
echo "=== 起動完了 ==="
echo "  バックエンド: http://localhost:8080"
echo "  フロントエンド: http://localhost:3000"
echo ""
echo "  停止: ./stop.sh"
echo ""

# フロントエンド起動（フォアグラウンド）
./node_modules/.bin/next dev
