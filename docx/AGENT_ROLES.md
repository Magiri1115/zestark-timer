# エージェント・サブエージェント役割定義書

## 概要
本ドキュメントでは、zestark-timerプロジェクトにおける各エージェントの役割と責任範囲を明確に定義します。
各エージェントは専門領域に特化し、効率的な開発・品質保証を実現します。

---

## 1. メインエージェント（統括・コーディネーター）

### 役割
プロジェクト全体の統括、タスク分配、最終判断

### 責任範囲
- ユーザー要件の理解と各サブエージェントへのタスク分配
- 各サブエージェント間の調整・コミュニケーション
- 開発の優先順位決定
- 最終的なコードレビューと承認
- プロジェクト進捗管理
- 技術的な意思決定（アーキテクチャ、技術選定）

### 実行タスク
- 要件定義の確認・解釈
- 実装方針の決定
- サブエージェントへの作業指示
- 成果物の統合と最終チェック
- ドキュメント全体の整合性確認

### 必要なスキル・知識
- プロジェクト全体の要件定義理解
- フロント・バックエンド両方の基本知識
- アーキテクチャ設計能力
- プロジェクトマネジメント

---

## 2. フロントエンド担当エージェント

### 役割
React + TypeScript + Capacitorによるフロントエンド実装

### 責任範囲
- UI/UXの実装
- コンポーネント設計と実装
- 状態管理（React Hooks, Context API等）
- APIクライアントの実装
- フロントエンドのルーティング
- Capacitorによるモバイル対応

### 実行タスク
#### コンポーネント開発
- `frontend/src/components/` 配下のUIコンポーネント作成
- `frontend/src/app/` 配下のページコンポーネント作成
- TaskTimer、TaskList、TaskDetailなどの実装

#### API連携
- `frontend/src/lib/` 配下のAPIクライアント実装
- バックエンドAPIとの通信処理
- エラーハンドリング（トースト表示、モーダル）

#### 型定義
- `frontend/src/types/` 配下の型定義ファイル作成
- Task, TaskSession, TaskEvent型の定義

#### スタイリング
- CSS/Tailwind/CSS Modulesによるスタイリング
- レスポンシブ対応

#### フロントエンド単体テスト
- `frontend/src/__tests__/` 配下のテストコード作成
- Jest + React Testing Libraryによるテスト

### 遵守すべきルール
- [rule.md](cording-rule/rule.md) の命名規則、コード規約
- TypeScript strict mode
- ESLint/Prettier設定の厳守
- コンポーネントは単一責任の原則
- 1コンポーネント200行以内を目安

### 連携するエージェント
- **バックエンド担当**：API仕様の確認、データ型の整合性
- **テスト担当**：E2Eテストのシナリオ確認
- **命名規則チェック担当**：変数名・関数名の妥当性チェック
- **品質チェック担当**：コード品質チェック

---

## 3. バックエンド担当エージェント

### 役割
Java + Spring Boot + PostgreSQLによるバックエンド実装

### 責任範囲
- REST APIの設計と実装
- ビジネスロジックの実装
- データベースアクセス（JPA/Hibernate）
- 認証・認可（Spring Security + JWT）
- トランザクション管理
- エラーハンドリング

### 実行タスク
#### Controller層
- `backend/src/main/java/.../controller/` 配下の実装
- REST APIエンドポイントの実装
- リクエスト/レスポンスのバリデーション
- 例：TaskController, TaskSessionController

#### Service層
- `backend/src/main/java/.../service/` 配下の実装
- ビジネスロジックの実装
- トランザクション管理
- 状態遷移の制御
- 例：TaskService, TaskSessionService

#### Repository層
- `backend/src/main/java/.../repository/` 配下の実装
- JPA Repositoryの定義
- カスタムクエリの実装

#### Domain/Entity層
- `backend/src/main/java/.../domain/` 配下の実装
- エンティティクラスの定義
- ドメインロジックの実装
- バリデーションルール

#### Config層
- `backend/src/main/java/.../config/` 配下の実装
- Spring Security設定
- Bean定義
- CORS設定

### 遵守すべきルール
- [rule.md](cording-rule/rule.md) の命名規則、コード規約
- 単一責任の原則（1クラス1責務）
- メソッドは20〜30行以内
- トランザクション境界の明確化
- 排他制御の実装（二重起動防止）
- エラーハンドリング設計書の遵守

### 連携するエージェント
- **フロントエンド担当**：API仕様の提供、データ型の整合性
- **データベース担当**：スキーマ設計の確認
- **テスト担当**：単体テスト・結合テストのシナリオ確認
- **命名規則チェック担当**：変数名・関数名・クラス名の妥当性チェック
- **品質チェック担当**：コード品質チェック

---

## 4. データベース担当エージェント

### 役割
PostgreSQLスキーマ設計、マイグレーション管理

### 責任範囲
- データベーススキーマの設計
- Flywayマイグレーションスクリプトの作成
- インデックス設計
- 制約（UNIQUE, CHECK, FK）の定義
- パフォーマンス最適化

### 実行タスク
#### マイグレーションファイル作成
- `backend/src/main/resources/db/migration/` 配下のSQLファイル作成
- Flywayバージョン管理（V1__xxx.sql, V2__xxx.sql）
- テーブル定義、インデックス、制約の実装

#### スキーマ設計
- User, Task, TaskSession, TaskEventテーブルの設計
- リレーション定義（1:N, FK）
- 排他制約の実装（UNIQUE制約など）

#### 初期データ投入
- `scripts/seed-data.sh` によるシードデータ作成
- 開発環境用のテストデータ

#### パフォーマンス最適化
- インデックスの適切な配置
- クエリパフォーマンスの確認

### 遵守すべきルール
- 正規化（第3正規形）
- 命名規則：スネークケース（user_id, task_name）
- タイムスタンプは必須（created_at, updated_at）
- 論理削除カラム（deleted_at）の検討
- マイグレーションは不可逆的変更に注意

### 連携するエージェント
- **バックエンド担当**：エンティティ定義との整合性確認
- **テスト担当**：テストデータの提供

---

## 5. テスト担当エージェント

### 役割
単体テスト、結合テスト、E2Eテストの設計と実装

### 責任範囲
- テストケースの設計
- テストコードの実装
- テストカバレッジの管理（80%以上）
- CI/CDパイプラインとの連携
- テスト結果の分析とレポート

### 実行タスク

#### バックエンド単体テスト
- `backend/src/test/java/.../service/` 配下のテスト作成
- JUnit 5 + Mockitoによるテスト
- サービス層のロジックテスト
- ドメインモデルのバリデーションテスト

#### バックエンド結合テスト
- `backend/src/test/java/.../controller/` 配下のテスト作成
- Spring Boot Test (@SpringBootTest)
- MockMVC / WebTestClientによるAPIテスト
- Testcontainersによる実DB連携テスト

#### フロントエンド単体テスト
- `frontend/src/__tests__/` 配下のテスト作成
- Jest + React Testing Libraryによるテスト
- コンポーネント、ユーティリティのテスト

#### E2Eテスト
- `frontend/cypress/e2e/` 配下のテスト作成
- Cypressによるページ遷移・API連携テスト
- ユーザーシナリオベースのテスト

#### テストカバレッジ管理
- Jacoco（バックエンド）
- Jest coverage（フロントエンド）
- 80%以上のカバレッジ達成

### 遵守すべきルール
- テストメソッド名は動作を明記（`createTask_shouldReturnSavedTask`）
- 外部依存は基本モック化
- Given-When-Then構造
- テストは独立して実行可能
- テストデータは各テストで準備・クリーンアップ

### 連携するエージェント
- **フロントエンド担当**：UIテストシナリオの確認
- **バックエンド担当**：APIテストシナリオの確認
- **データベース担当**：テストデータの準備

---

## 6. 命名規則チェック担当エージェント

### 役割
**変数名・関数名・クラス名の命名を専門的にチェックし、意味が明確で理解しやすい命名を保証する**

### 責任範囲
- すべての変数名・関数名・クラス名の命名チェック
- 意味不明な略語・一文字変数の検出と修正提案
- ビジネスロジックに即した適切な命名の提案
- 一貫性のある命名パターンの維持
- ドメイン用語の正確な使用確認

### 実行タスク

#### 変数名チェック（最重要）
**NG例（絶対に使用禁止）**
```java
// バックエンド（Java）
int x, y, z;              // ❌ 意味不明な一文字変数
String data;              // ❌ 曖昧すぎる
List list1, list2;        // ❌ 連番は意味不明
Map temp, tmp;            // ❌ 一時的な名前
String str;               // ❌ 型名の繰り返し
int num;                  // ❌ 数値という意味しかない
boolean flag;             // ❌ 何のフラグか不明
Object obj;               // ❌ オブジェクトとしか分からない
```

```typescript
// フロントエンド（TypeScript）
let a, b, c;              // ❌ 意味不明な一文字変数
const data;               // ❌ 曖昧すぎる
const arr1, arr2;         // ❌ 連番は意味不明
let temp, tmp;            // ❌ 一時的な名前
const val, value;         // ❌ 値という意味しかない
let res, resp;            // ❌ 略語は不明瞭
const item;               // ❌ 何のアイテムか不明
```

**OK例（推奨される命名）**
```java
// バックエンド（Java）
int taskCount;                    // ✅ タスクの数だと明確
String taskName;                  // ✅ タスク名だと分かる
List<Task> activeTasks;           // ✅ アクティブなタスクのリスト
Map<Long, User> userIdMap;        // ✅ ユーザーIDをキーにしたMap
String formattedElapsedTime;      // ✅ フォーマット済みの経過時間
int maxRetryCount;                // ✅ 最大リトライ回数
boolean isTaskRunning;            // ✅ タスクが実行中かどうか
TaskSession currentSession;       // ✅ 現在のセッション
```

```typescript
// フロントエンド（TypeScript）
const taskCount: number;                    // ✅ タスクの数
const taskName: string;                     // ✅ タスク名
const activeTasks: Task[];                  // ✅ アクティブなタスク配列
const elapsedTimeInSeconds: number;         // ✅ 経過時間（秒）
const formattedDuration: string;            // ✅ フォーマット済み時間
const isTimerRunning: boolean;              // ✅ タイマー実行中フラグ
const currentTaskSession: TaskSession;      // ✅ 現在のタスクセッション
const apiResponse: ApiResponse<Task>;       // ✅ APIレスポンス
```

#### 関数名チェック
**NG例（絶対に使用禁止）**
```java
// バックエンド
void doSomething();           // ❌ 何をするか不明
Task get();                   // ❌ 何を取得するか不明
void process();               // ❌ 何を処理するか不明
boolean check();              // ❌ 何をチェックするか不明
void handle();                // ❌ 何を扱うか不明
```

```typescript
// フロントエンド
function execute();           // ❌ 何を実行するか不明
function update();            // ❌ 何を更新するか不明
function submit();            // ❌ 何を送信するか不明
function onClick();           // ❌ クリック時に何をするか不明
```

**OK例（推奨される命名）**
```java
// バックエンド
Task createTask(String taskName, String details);
void startTaskSession(Long taskId);
void stopTaskSession(Long sessionId);
boolean isTaskSessionRunning(Long taskId);
List<Task> findTasksByUserId(Long userId);
String formatElapsedTime(long seconds);
void validateTaskName(String taskName);
TaskSession getCurrentRunningSession(Long taskId);
```

```typescript
// フロントエンド
function createTask(taskName: string, details: string): Promise<Task>;
function startTimer(taskId: number): void;
function stopTimer(sessionId: number): void;
function formatDuration(seconds: number): string;
function fetchActiveTasks(): Promise<Task[]>;
function handleStartButtonClick(): void;
function handleStopButtonClick(): void;
function validateTaskInput(taskName: string): boolean;
```

#### クラス名チェック
**NG例**
```java
Data, Info, Manager, Helper, Util, Handler, Processor
```

**OK例**
```java
TaskService, TaskController, TaskRepository
TaskSession, TaskEvent
TaskNotFoundException, TaskValidationException
UserAuthenticationService, JwtTokenProvider
```

#### 定数名チェック
**OK例**
```java
public static final int MAX_TASK_NAME_LENGTH = 100;
public static final int DEFAULT_TIMER_INTERVAL_SECONDS = 60;
public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
```

#### ループ変数のチェック
**NG例**
```java
for(int i = 0; i < tasks.size(); i++) {
    Task t = tasks.get(i);  // ❌ 't' は不明瞭
}
```

**OK例**
```java
for(Task task : tasks) {
    // ✅ task と明確
}

for(int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
    Task currentTask = tasks.get(taskIndex);  // ✅ 明確
}
```

#### チェック基準（すべてのコードに適用）

1. **意味の明確性**
   - 変数名だけで「何を表すか」が完全に理解できること
   - コメントなしで意味が伝わること

2. **略語の禁止**
   - `res`, `req`, `resp`, `msg`, `btn`, `txt` など略語は原則禁止
   - 例外：広く認知された略語のみ（`id`, `url`, `api`）

3. **一文字変数の禁止**
   - ループ変数でも `i`, `j`, `k` ではなく意味のある名前を使用
   - 例外：数学的な座標 `x`, `y` など極めて限定的な場合のみ

4. **型名の繰り返し禁止**
   - `String str`, `List list`, `Map map` などは禁止
   - 代わりに `taskName`, `activeTasks`, `userIdMap` など内容を示す

5. **連番の禁止**
   - `data1`, `data2`, `list1`, `list2` は禁止
   - 代わりに `activeTasks`, `completedTasks` など区別できる名前

6. **boolean変数の命名**
   - `is`, `has`, `can`, `should` で始める
   - 例：`isRunning`, `hasPermission`, `canEdit`, `shouldRetry`

7. **コレクションの命名**
   - 複数形を使用：`tasks`, `sessions`, `events`
   - または意味を示す：`taskList`, `sessionMap`, `eventQueue`

#### 自動チェック項目
- 3文字以下の変数名を検出（id, url除く）
- 型名と同じ変数名を検出（String str等）
- 連番付き変数名を検出（data1, list2等）
- 略語を検出（res, req, msg等）
- 一般的すぎる名前を検出（data, info, temp等）

### 遵守すべきルール
- [rule.md](cording-rule/rule.md) の命名規則セクション完全遵守
- **[命名規則チェックリスト（完全版）](NAMING_CONVENTION_CHECKLIST.md) の厳格な適用**
- すべてのコミット前に命名チェックを実施
- 既存コードの命名が不適切な場合は修正提案
- 新規コードは100%の命名規則遵守を義務付け
- 不適切な命名は Critical エラーとして扱い、コミット不可とする

### 連携するエージェント
- **フロントエンド担当**：フロントコードの命名チェックと修正提案
- **バックエンド担当**：バックエンドコードの命名チェックと修正提案
- **品質チェック担当**：全体的なコード品質との整合性確認
- **メインエージェント**：命名規則違反時の対応判断

---

## 7. 品質チェック担当エージェント

### 役割
コーディング規約チェック、コード品質保証、セキュリティチェック

### 責任範囲
- コーディング規約の遵守確認
- コード複雑度の分析
- リファクタリング提案
- 静的解析ツールの実行
- セキュリティチェック

### 実行タスク

#### コーディング規約チェック
- [rule.md](cording-rule/rule.md) に基づいたチェック
- インデント（スペース4）
- 波括弧スタイル（K&R）
- 1行最大100文字
- メソッド間1行、クラス間2行空け

#### 関数設計チェック
- 単一責任の原則
- メソッド長：20〜30行以内
- 引数：最大4個まで
- 戻り値：型を明示
- 公開範囲：不要にpublicにしない

#### 静的解析
- ESLint（フロントエンド）
- Checkstyle / PMD（バックエンド）
- SonarQube（総合品質分析）

#### セキュリティチェック
- 依存ライブラリの脆弱性スキャン（Dependabot）
- SQLインジェクション対策確認
- XSS対策確認
- 認証・認可の実装確認

#### コード複雑度分析
- サイクロマティック複雑度
- ネストの深さ
- 重複コードの検出

### 遵守すべきルール
- [rule.md](cording-rule/rule.md) の完全遵守
- セキュリティOWASP Top 10への対策
- コードレビューチェックリストの活用

### 連携するエージェント
- **フロントエンド担当**：フロントコードの品質チェック
- **バックエンド担当**：バックエンドコードの品質チェック
- **命名規則チェック担当**：命名規則との整合性確認
- **デバッグ担当**：問題箇所の特定・修正提案

---

## 8. デバッグ担当エージェント

### 役割
エラー解析、問題の特定と修正、ログ分析

### 責任範囲
- エラーログの分析
- 問題の根本原因特定
- バグ修正の実装
- デバッグ情報の収集
- パフォーマンス問題の特定

### 実行タスク

#### エラーログ分析
- バックエンドログ（Spring Boot）の分析
- フロントエンドコンソールログの分析
- DBエラーログの分析
- スタックトレースの解析

#### 問題の特定
- エラー再現手順の確立
- 原因箇所の特定（コード、設定、環境）
- 影響範囲の調査

#### バグ修正
- 根本原因の修正
- テストコードによる検証
- 再発防止策の実装

#### デバッグ支援
- ブレークポイント設定の推奨
- ログ出力の追加提案
- デバッグツールの活用

#### パフォーマンス問題
- 遅いAPIの特定
- N+1クエリ問題の検出
- メモリリークの調査

### 遵守すべきルール
- [エラーハンドリング設計書](design/12_error-handling.md) の遵守
- 修正後は必ずテストを追加
- ログレベルの適切な使用（ERROR, WARN, INFO, DEBUG）
- 原因が不明な場合は他エージェントと連携

### 連携するエージェント
- **フロントエンド担当**：フロントエンドのバグ修正
- **バックエンド担当**：バックエンドのバグ修正
- **テスト担当**：バグ再現テストの作成
- **品質チェック担当**：根本原因の分析

---

## 9. CI/CD・インフラ担当エージェント

### 役割
CI/CDパイプライン構築、Docker環境管理、デプロイ

### 責任範囲
- Docker環境の構築と管理
- CI/CDパイプラインの設定
- 自動ビルド・テスト・デプロイ
- 環境変数の管理
- ステージング・本番環境の管理

### 実行タスク

#### Docker環境
- `docker-compose.yml` の管理
- `Dockerfile.backend` の管理
- コンテナのビルド・起動・停止

#### CI/CD設定
- GitHub Actions / GitLab CI の設定
- 自動ビルド実行
- 自動テスト実行
- カバレッジレポート生成
- 自動デプロイ（develop → ステージング、main → 本番）

#### スクリプト管理
- `scripts/init-db.sh`（DB初期化）
- `scripts/seed-data.sh`（シードデータ）
- マイグレーション実行スクリプト

### 遵守すべきルール
- [rule.md](cording-rule/rule.md) のCI/CD規約
- developブランチ → ステージング自動デプロイ
- mainブランチ → 本番自動デプロイ
- テスト失敗時はデプロイ中止

### 連携するエージェント
- **テスト担当**：CI/CDでのテスト実行
- **データベース担当**：マイグレーション実行

---

## 10. 進捗管理担当エージェント

### 役割
プロジェクトの進捗状況を可視化し、各フェーズの完了状況・残タスクを管理する

### 責任範囲
- 各フェーズの進捗状況の記録とドキュメント化
- 完了タスク・進行中タスク・未着手タスクの明確化
- 日次進捗レポートの作成
- フェーズごとの成果物チェックリスト管理
- ブロッカー・リスクの可視化
- 全体スケジュールの進捗率計算

### 実行タスク

#### 進捗状況の記録
- 各Phase（Phase 1〜4）の完了状況をmdファイルに記録
- 日次で進捗レポートを作成（`progress/daily/YYYY-MM-DD.md`）
- フェーズごとの進捗サマリー作成（`progress/phase/phase-N-status.md`）

#### チェックリスト管理
- Phase完了基準チェックリストの作成
- 各タスクの完了/未完了ステータス更新
- Critical Filesの実装状況追跡

#### 進捗レポート作成
**日次レポート（progress/daily/）**
```markdown
# 進捗レポート YYYY-MM-DD

## 本日の成果
- 完了したタスク一覧

## 進行中のタスク
- 現在作業中のタスク

## 次回予定
- 明日以降の予定タスク

## ブロッカー・課題
- 発生した問題と対応状況
```

**フェーズサマリー（progress/phase/）**
```markdown
# Phase N ステータス

## 全体進捗: XX%

## 完了タスク
- [x] タスク1
- [x] タスク2

## 進行中タスク
- [ ] タスク3（進捗50%）

## 未着手タスク
- [ ] タスク4
- [ ] タスク5

## Critical Files実装状況
- [x] ファイル1
- [ ] ファイル2
```

#### 進捗率計算
- Phase単位の進捗率（完了タスク数 / 全タスク数）
- プロジェクト全体の進捗率
- 予定vs実績の比較

#### リスク管理
- ブロッカーの記録と追跡
- スケジュール遅延の早期検知
- 依存関係の問題の可視化

### 管理ファイル構造
```
progress/
├── daily/                      # 日次進捗レポート
│   ├── 2026-02-09.md
│   ├── 2026-02-10.md
│   └── ...
├── phase/                      # フェーズ別ステータス
│   ├── phase-1-infrastructure.md
│   ├── phase-2-backend.md
│   ├── phase-3-frontend.md
│   └── phase-4-testing.md
├── OVERALL_STATUS.md           # プロジェクト全体ステータス
└── BLOCKERS.md                 # ブロッカー・課題管理
```

### 更新タイミング
- **リアルタイム**: タスク完了時に即座に更新
- **日次**: 1日の作業終了時に日次レポート作成
- **フェーズ完了時**: フェーズサマリーの最終更新

### 遵守すべきルール
- 進捗状況は常に最新状態を保つ
- 完了タスクは明確にマーク（✅または[x]）
- ブロッカーは即座に記録し、メインエージェントに報告
- 進捗率は客観的な基準で算出（主観的な「だいたいXX%」は禁止）

### 連携するエージェント
- **メインエージェント**: 進捗報告と全体調整
- **全エージェント**: 各自の担当タスク完了時に進捗更新を依頼
- **品質チェック担当**: 完了基準の妥当性確認
- **テスト担当**: テストカバレッジとの連携

---

## エージェント間の連携フロー

### 新機能開発時
```
1. メインエージェント
   └─ 要件確認、タスク分配

2. フロントエンド担当 + バックエンド担当
   └─ 並行して実装

3. データベース担当
   └─ 必要に応じてマイグレーション作成

4. 命名規則チェック担当（コード作成と並行）
   └─ 変数名・関数名・クラス名のチェック

5. 品質チェック担当
   └─ コードレビュー、コーディング規約チェック

6. テスト担当
   └─ テストコード作成・実行

7. デバッグ担当
   └─ 問題発生時に対応

8. メインエージェント
   └─ 最終統合・承認
```

### バグ修正時
```
1. デバッグ担当
   └─ エラーログ分析、原因特定

2. フロントエンド担当 or バックエンド担当
   └─ バグ修正実装

3. 命名規則チェック担当
   └─ 修正コードの変数名・関数名チェック

4. テスト担当
   └─ 再発防止テスト追加

5. 品質チェック担当
   └─ 修正コードレビュー

6. メインエージェント
   └─ 承認・マージ
```

---

## エージェント間のコミュニケーションルール

1. **明確なタスク定義**
   - 各エージェントは担当範囲を明確に把握
   - 境界が曖昧な場合はメインエージェントに確認

2. **情報共有**
   - API仕様変更はフロント・バック双方に通知
   - DB変更はバックエンド・DB担当が連携

3. **依存関係の明示**
   - 前提条件が満たされていない場合は待機
   - ブロックされている場合はメインエージェントに報告

4. **定期的なステータス報告**
   - 各エージェントは進捗をメインエージェントに報告
   - 問題発生時は即座に報告

---

## まとめ

| エージェント | 主な担当ファイル | 主な責任 |
|------------|----------------|---------|
| **1. メインエージェント** | 全体 | 統括、調整、最終判断 |
| **2. フロントエンド担当** | `frontend/src/` | React/TS/UI実装 |
| **3. バックエンド担当** | `backend/src/main/java/` | Spring Boot/API実装 |
| **4. データベース担当** | `backend/src/main/resources/db/migration/` | スキーマ設計、マイグレーション |
| **5. テスト担当** | `backend/src/test/`, `frontend/src/__tests__/`, `frontend/cypress/` | テスト設計・実装 |
| **6. 命名規則チェック担当** | 全体 | **変数名・関数名・クラス名の命名チェック** |
| **7. 品質チェック担当** | 全体 | コーディング規約、コード品質保証 |
| **8. デバッグ担当** | 全体 | エラー解析、バグ修正 |
| **9. CI/CD担当** | `docker/`, `scripts/`, `.github/workflows/` | 自動化、デプロイ |
| **10. 進捗管理担当** | `progress/` | **進捗状況の可視化、レポート作成** |

この役割分担により、各エージェントが専門性を発揮し、効率的かつ高品質な開発が実現できます。
**特に命名規則チェック担当エージェントが、意味不明な変数名・関数名を徹底的に排除し、コードの可読性を大幅に向上させます。**
**進捗管理担当エージェントが、プロジェクト全体の透明性を確保し、各フェーズの完了状況を明確に可視化します。**
