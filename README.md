# zestark-timer

時間の使い方を記録・可視化するタイムトラッキングアプリケーション

## 技術スタック

### バックエンド
- Java 17
- Spring Boot 3.2.2
- PostgreSQL 15
- JPA/Hibernate
- Flyway
- Maven

### フロントエンド
- React 18
- TypeScript 5.3
- Next.js 14
- Tailwind CSS
- Capacitor 5
- Axios

### インフラ
- Docker
- docker-compose

## プロジェクト構成

```
zestark-timer/
├── backend/              # Spring Bootバックエンド
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/zestark/timewatch/
│   │   │   │   ├── domain/         # エンティティ
│   │   │   │   ├── repository/     # JPA Repository
│   │   │   │   ├── service/        # ビジネスロジック
│   │   │   │   ├── controller/     # REST API
│   │   │   │   ├── dto/            # データ転送オブジェクト
│   │   │   │   ├── exception/      # 例外処理
│   │   │   │   └── config/         # 設定
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── db/migration/   # Flywayマイグレーション
│   │   └── test/                   # テストコード
│   └── pom.xml
│
├── frontend/             # Next.jsフロントエンド
│   ├── src/
│   │   ├── app/                    # Next.js App Router
│   │   ├── components/             # Reactコンポーネント
│   │   ├── hooks/                  # カスタムフック
│   │   ├── lib/
│   │   │   ├── api/                # APIクライアント
│   │   │   └── utils/              # ユーティリティ
│   │   └── types/                  # TypeScript型定義
│   ├── package.json
│   └── tsconfig.json
│
├── docker/               # Docker設定
│   ├── docker-compose.yml
│   └── Dockerfile.backend
│
└── docx/                 # 設計ドキュメント
    ├── requirements/
    ├── design/
    └── cording-rule/
```

## セットアップ

### 必要な環境
- Docker Desktop
- Node.js 18以上（フロントエンド開発時）
- Java 17（バックエンド開発時）

### Docker環境起動

```bash
cd docker
docker-compose up -d
```

これにより以下のサービスが起動します：
- PostgreSQL (ポート5432)
- バックエンドAPI (ポート8080)

### データベース接続確認

```bash
docker exec -it zestark-timer-db psql -U timewatch_user -d timewatch -c "\dt"
```

### バックエンドヘルスチェック

```bash
curl http://localhost:8080/actuator/health
```

## 開発

### バックエンド開発

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### フロントエンド開発

```bash
cd frontend
npm install
npm run dev
```

ブラウザで http://localhost:3000 にアクセス

## テスト

### バックエンドテスト

```bash
cd backend
mvn test
mvn jacoco:report  # カバレッジレポート
```

### フロントエンドテスト

```bash
cd frontend
npm run test
npm run test:coverage  # カバレッジレポート
```

## API仕様

### タスクAPI

- `GET /api/tasks` - タスク一覧取得
- `POST /api/tasks` - タスク作成
- `GET /api/tasks/{taskId}` - タスク詳細取得
- `PUT /api/tasks/{taskId}` - タスク更新
- `DELETE /api/tasks/{taskId}` - タスク削除
- `PUT /api/tasks/{taskId}/status` - タスクステータス変更

### セッションAPI

- `POST /api/sessions/start` - タイマー開始
- `POST /api/sessions/stop` - タイマー停止
- `GET /api/sessions/running` - 実行中セッション取得
- `GET /api/sessions/{sessionId}` - セッション詳細取得

詳細は `/docx/design/11_api-design.md` を参照

## 主要機能

### タイマー機能
- OS時刻ベースの計測で高精度（バックグラウンド・スリープ対応）
- データベース制約による排他制御（1タスク1セッションのみ実行中を保証）
- 自動終了機能（tolerance設定による柔軟な制御）

### タスク管理
- タスク作成・更新・削除
- ステータス管理（PENDING, RUNNING, COMPLETED, CANCELLED）
- タスクイベント履歴

### エラーハンドリング
- グローバル例外ハンドラによる統一的なエラーレスポンス
- フロントエンドAPIクライアントでの一元的なエラー処理

## コーディング規約

- バックエンド: Checkstyle（2スペースインデント）
- フロントエンド: ESLint + Prettier
- 命名規則: `NAMING_CONVENTION_CHECKLIST.md` に100%準拠
- 詳細は `/docx/cording-rule/rule.md` を参照

## ライセンス

Proprietary
