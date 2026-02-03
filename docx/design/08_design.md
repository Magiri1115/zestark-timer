# 詳細設計書
## 概要
本システムは、ユーザーがタスク単位で作業時間を計測・管理する
タイムトラッキングシステムである。
タスクの状態遷移および作業時間の整合性を重視し、
状態管理・履歴管理・時間制約を明示的に扱う設計とする。
## 技術選定
| 区分       | 技術                    |
| -------- | --------------------- |
| フロントエンド  | React（Capacitor対応）    |
| バックエンド   | Java / Spring Boot    |
| API      | REST（JSON）            |
| DB       | PostgreSQL        |
| ORM      | JPA（Hibernate）        |
| 認証       | Spring Security + JWT |
| マイグレーション | Flyway                |
## システム構成
```
[ Capacitor App ]
   └─ React UI
        ↓ REST API
[ Spring Boot Backend ]
        ↓ JPA
[ PostgreSQL ]
```
## ドメイン設計
### エンティティ一覧
| エンティティ      | 説明        |
| ----------- | --------- |
| User        | 利用者       |
| Task        | 作業単位      |
| TaskSession | 作業計測セッション |
| TaskEvent   | 状態遷移履歴    |
### エンティティ関係
```
User 1 ── * Task 1 ── * TaskSession 1 ── * TaskEvent
```
## 業務ロジック設計
### 計測開始処理
#### 前提条件
対象Taskが存在する
実行中のTaskSessionが存在しない
#### 処理内容
TaskSessionを新規作成（start_time = now）
Taskのstatusを RUNNING に更新
TaskEventに START を記録
※ トランザクション内で実行
※ 排他制約により二重起動を防止
### 計測停止処理
#### 前提条件
実行中TaskSessionが存在する
#### 処理内容
end_timeを更新
Taskのstatusを COMPLETED に更新
TaskEventに STOP を記録
### トランザクション設計
計測開始／停止は 必ず単一トランザクション
DB制約違反時はロールバック
整合性違反は例外として扱う
## ファイル構成
```
/
├─ backend/                        # Java / Spring Boot
│  ├─ src/
│  │  ├─ main/java/com/zestark/timewatch/
│  │  │  ├─ controller/            # API エンドポイント
│  │  │  ├─ service/               # ビジネスロジック
│  │  │  ├─ domain/                # Entity / ドメインモデル
│  │  │  ├─ repository/            # DBアクセス
│  │  │  └─ config/                # Security, Bean 定義
│  │  ├─ main/resources/
│  │  │  ├─ db/migration/          # Flyway SQL マイグレーション
│  │  │  └─ application.yml        # 通常設定
│  │  └─ test/java/com/zestark/timewatch/
│  │     ├─ controller/            # Controller 結合テスト
│  │     ├─ service/               # Service 単体テスト
│  │     ├─ repository/            # Repository 結合テスト
│  │     └─ config/                # Test用設定
│  └─ test/resources/
│     └─ application-test.yml       # テスト用プロファイル
│
├─ frontend/                        # React + TypeScript + Capacitor
│  ├─ src/
│  │  ├─ app/
│  │  │  ├─ layout.tsx             # 共通レイアウト
│  │  │  ├─ page.tsx               # Topページ
│  │  │  └─ tasks/                 # タスク関連画面
│  │  ├─ components/               # UIコンポーネント
│  │  ├─ lib/                      # APIクライアント / 共通ユーティリティ
│  │  ├─ types/                    # 型定義
│  │  └─ __tests__/                # 単体テスト
│  │     ├─ components/
│  │     ├─ lib/
│  │     └─ pages/
│  ├─ cypress/                      # E2Eテスト
│  │  ├─ e2e/
│  │  └─ support/
│  ├─ jest.config.js                # Jest設定
│  ├─ tsconfig.test.json            # テスト用TS設定
│  └─ capacitor.config.ts           # Capacitor設定
│
├─ docker/                          # Docker構成
│  ├─ docker-compose.yml            # DB / Backend / Frontend 起動
│  └─ Dockerfile.backend            # Backend用コンテナ
│
├─ scripts/                         # 補助スクリプト
│  ├─ init-db.sh                    # DB初期化
│  └─ seed-data.sh                  # シードデータ
│
├─ docs/                             # 設計書
│  ├─ requirement.md
│  ├─ basic-design.md
│  └─ detail-design.md
│
└─ README.md                         # プロジェクト概要、環境構築手順

```
### ファイル/フォルダと記述内容表
| パス                                              | 役割              | 記述内容（端的）                         |
| ----------------------------------------------- | --------------- | -------------------------------- |
| backend/src/main/java/.../controller/           | API入口           | REST API、リクエスト/レスポンスの定義          |
| backend/src/main/java/.../service/              | 業務ロジック          | Task/TaskSession の状態遷移、計測開始・停止処理 |
| backend/src/main/java/.../domain/               | ドメインモデル         | Entity 定義、バリデーション、整合性制約          |
| backend/src/main/java/.../repository/           | DBアクセス          | JPA Repository、CRUDメソッド          |
| backend/src/main/java/.../config/               | 設定/Bean         | Security 設定、Bean 定義              |
| backend/src/main/resources/db/migration/        | DBスキーマ          | Flyway SQL マイグレーション              |
| backend/src/main/resources/application.yml      | 設定              | DB接続、セキュリティ、ポート、環境変数             |
| backend/src/test/java/.../controller/           | Controller結合テスト | REST API エンドツーエンドテスト             |
| backend/src/test/java/.../service/              | Service単体テスト    | ビジネスロジックの単体テスト                   |
| backend/src/test/java/.../repository/           | Repository結合テスト | DBアクセステスト                        |
| backend/src/test/java/.../config/               | テスト設定           | MockやTest用Bean定義                 |
| backend/src/test/resources/application-test.yml | テスト用設定          | テスト用DB接続、環境変数                    |
| frontend/src/app/                               | ページ/ルート         | 各画面（タスク一覧、詳細）の UI とロジック          |
| frontend/src/components/                        | UI部品            | ボタン、モーダル、TaskTimer コンポーネント       |
| frontend/src/lib/                               | ユーティリティ         | API呼び出し、日付計算、共通関数                |
| frontend/src/types/                             | 型定義             | Task / TaskSession / TaskEvent 型 |
| frontend/src/**tests**/components/              | UIコンポーネント単体テスト  | Jest + React Testing Library     |
| frontend/src/**tests**/lib/                     | ユーティリティ単体テスト    | APIクライアント、日付計算関数                 |
| frontend/src/**tests**/pages/                   | ページ単体テスト        | ページ表示やロジックテスト                    |
| frontend/cypress/                               | E2Eテスト          | ページ遷移・API連携などの統合テスト              |
| frontend/jest.config.js                         | テスト設定           | Jest設定                           |
| frontend/tsconfig.test.json                     | TSテスト設定         | TypeScript用テストコンパイル設定            |
| frontend/capacitor.config.ts                    | Capacitor設定     | iOS/Androidビルド設定                 |
| docker/docker-compose.yml                       | 環境構築            | DB/バックエンド/フロントをまとめて起動            |
| docker/Dockerfile.backend                       | バックエンドコンテナ      | Spring Boot アプリビルド・起動            |
| scripts/                                        | 補助スクリプト         | DB初期化、シードデータ、マイグレーション呼び出し        |
| docs/                                           | 設計書             | 要件定義・基本設計・詳細設計                   |
| README.md                                       | プロジェクト概要        | 環境構築手順、開発手順、ビルド方法                |

## 環境構築
### 前提
1. Git, Docker, Docker Compose がインストール済み
2. Node.js はフロント開発用にインストール済み
3. PostgreSQL は Docker 内で立ち上げる想定
### 手順
1. リポジトリをクローン
```
git clone https://github.com/your-org/timewatch.git
cd timewatch
```
2. サブモジュールや依存ライブラリを取得（必要なら）
```
git submodule update --init --recursive
```
3. Docker コンテナのビルド
```
docker-compose build
```
4. DB とバックエンドを含めたコンテナ起動
```
docker-compose up -d
```
5. バックエンドのマイグレーション適用
```
docker-compose exec backend bash -c "java -jar app.jar migrate"
```
6. フロントエンド依存ライブラリのインストール
```
cd frontend
npm install
```
7. フロント開発サーバー起動（Capacitor対応）
```
npm run dev
```
8. Docker 状態確認
```
docker ps
```
### モバイルビルド(capacitor)
```
npx cap sync
npx cap open android
npx cap open ios
```