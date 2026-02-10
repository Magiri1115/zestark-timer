# Docker 起動コマンド

プロジェクトの各サービス（フロントエンド、バックエンド、データベース）を起動するためのコマンドガイドです。

## 全サービスの起動

プロジェクトのルートディレクトリで以下のコマンドを実行してください。

```powershell
docker compose -f docker/docker-compose.yml up -d
```

### ビルドを含めた起動（コード変更時など）

```powershell
docker compose -f docker/docker-compose.yml up -d --build
```

## サービスの停止

```powershell
docker compose -f docker/docker-compose.yml down
```

## ログの確認

### フロントエンド
```powershell
docker logs -f zestark-timer-frontend
```

### バックエンド
```powershell
docker logs -f zestark-timer-backend
```

## アクセスURL

- **フロントエンド**: [http://localhost:3000](http://localhost:3000)
- **バックエンド (API)**: [http://localhost:8080/api](http://localhost:8080/api)
- **バックエンド (ヘルスチェック)**: [http://localhost:8080/api/actuator/health](http://localhost:8080/api/actuator/health)
