# 命名規則チェックリスト（完全版）

## 目的
このドキュメントは、命名規則チェック担当エージェントが使用する詳細なチェックリストです。
**すべてのコードは、このチェックリストに基づいて検証され、不適切な命名は即座に修正されます。**

---

## 1. 変数名チェックリスト

### 1.1 基本ルール

| チェック項目 | NG例 | OK例 | 理由 |
|------------|------|------|------|
| **一文字変数の禁止** | `int x;` | `int taskCount;` | 意味が不明 |
| **略語の禁止** | `String res;` | `String response;` | 略語は理解を妨げる |
| **型名の繰り返し禁止** | `String str;` | `String taskName;` | 型情報は型宣言で分かる |
| **連番の禁止** | `List list1, list2;` | `List activeTasks, completedTasks;` | 区別がつかない |
| **曖昧な名前の禁止** | `Object data;` | `Task currentTask;` | 何のデータか不明 |
| **temp/tmpの禁止** | `int temp;` | `int previousValue;` | 一時的な意味しかない |

### 1.2 具体的なNG例とOK例

#### プリミティブ型変数

```java
// ❌ NG例
int n;
int num;
int count;
long id;
boolean flag;
double val;

// ✅ OK例
int activeTaskCount;
int totalSessionDuration;
int remainingSeconds;
long taskSessionId;
boolean isTimerRunning;
double averageCompletionTime;
```

#### String型変数

```java
// ❌ NG例
String str;
String s;
String name;
String text;
String msg;

// ✅ OK例
String taskName;
String taskDescription;
String userName;
String errorMessage;
String formattedDuration;
```

#### コレクション型変数

```java
// ❌ NG例
List list;
List data;
List items;
Map map;
Set set;

// ✅ OK例
List<Task> activeTasks;
List<TaskSession> runningSessions;
Map<Long, User> userIdMap;
Set<String> uniqueTagNames;
```

#### DTO/Entity型変数

```java
// ❌ NG例
Task t;
Task task;  // 単に "task" だけでは不十分な場合が多い
TaskSession session;
User u;

// ✅ OK例
Task currentTask;
Task selectedTask;
TaskSession activeSession;
TaskSession previousSession;
User taskOwner;
User authenticatedUser;
```

### 1.3 boolean変数の特別ルール

boolean変数は必ず以下のプレフィックスを使用：

| プレフィックス | 意味 | 例 |
|--------------|------|-----|
| `is` | 状態を表す | `isRunning`, `isCompleted`, `isValid` |
| `has` | 所有を表す | `hasPermission`, `hasError`, `hasTasks` |
| `can` | 能力を表す | `canEdit`, `canDelete`, `canStart` |
| `should` | 推奨を表す | `shouldRetry`, `shouldNotify`, `shouldSave` |

```java
// ❌ NG例
boolean running;
boolean error;
boolean permission;
boolean valid;

// ✅ OK例
boolean isTimerRunning;
boolean hasActiveSession;
boolean canStartNewTask;
boolean shouldAutoStopTimer;
```

### 1.4 時間関連変数の特別ルール

時間を扱う変数は単位を明記：

```java
// ❌ NG例
long time;
int duration;
long start;
long end;

// ✅ OK例
long startTimeInMillis;
long endTimeInMillis;
int durationInSeconds;
int elapsedTimeInMinutes;
LocalDateTime sessionStartTime;
LocalDateTime sessionEndTime;
```

---

## 2. 関数名チェックリスト

### 2.1 基本ルール

| チェック項目 | NG例 | OK例 | 理由 |
|------------|------|------|------|
| **動詞で始める** | `task()` | `createTask()` | 動作が明確 |
| **何をするか明記** | `process()` | `processTaskCompletion()` | 処理内容が分かる |
| **戻り値を示す** | `get()` | `getActiveTaskList()` | 何を取得するか明確 |
| **略語の禁止** | `proc()` | `processPayment()` | 理解しやすい |

### 2.2 メソッド種別ごとの命名規則

#### CRUD操作

```java
// ❌ NG例
Task create(String name);
void update(Task t);
void delete(Long id);
Task get(Long id);

// ✅ OK例
Task createTask(String taskName, String description);
void updateTaskName(Long taskId, String newTaskName);
void deleteTaskById(Long taskId);
Task findTaskById(Long taskId);
List<Task> findAllTasksByUserId(Long userId);
```

#### ビジネスロジック

```java
// ❌ NG例
void start(Long id);
void stop();
void process();
boolean check();

// ✅ OK例
void startTaskSession(Long taskId);
void stopCurrentTaskSession(Long sessionId);
void processTaskCompletion(Long taskId);
boolean isTaskSessionRunning(Long taskId);
```

#### バリデーション

```java
// ❌ NG例
boolean validate(String s);
void check(Task t);

// ✅ OK例
boolean validateTaskName(String taskName);
void validateTaskSessionNotRunning(Long taskId);
boolean isValidTaskDescription(String description);
```

#### 計算・変換

```java
// ❌ NG例
String format(long time);
int calc(int a, int b);
double convert(long val);

// ✅ OK例
String formatElapsedTime(long elapsedSeconds);
int calculateTotalDuration(List<TaskSession> sessions);
double convertSecondsToHours(long seconds);
```

#### イベントハンドラ（フロントエンド）

```typescript
// ❌ NG例
function onClick();
function onSubmit();
function handle();

// ✅ OK例
function handleStartButtonClick(): void;
function handleTaskFormSubmit(event: FormEvent): void;
function handleTimerStopRequest(taskId: number): void;
```

### 2.3 関数名の動詞一覧

| 動詞 | 用途 | 例 |
|------|------|-----|
| `create` | 新規作成 | `createTask`, `createTaskSession` |
| `update` | 更新 | `updateTaskName`, `updateSessionEndTime` |
| `delete` | 削除 | `deleteTaskById`, `deleteExpiredSessions` |
| `find` | 検索・取得 | `findTaskById`, `findActiveTasks` |
| `get` | 取得（単一） | `getCurrentSession`, `getElapsedTime` |
| `fetch` | 外部から取得 | `fetchTasksFromApi`, `fetchUserProfile` |
| `calculate` | 計算 | `calculateDuration`, `calculateAverage` |
| `validate` | 検証 | `validateTaskName`, `validateInput` |
| `format` | フォーマット | `formatDuration`, `formatTimestamp` |
| `start` | 開始 | `startTaskSession`, `startTimer` |
| `stop` | 停止 | `stopTaskSession`, `stopTimer` |
| `process` | 処理 | `processCompletion`, `processEvent` |
| `handle` | イベント処理 | `handleClick`, `handleSubmit` |
| `is/has/can` | 判定 | `isRunning`, `hasPermission`, `canEdit` |

---

## 3. クラス名チェックリスト

### 3.1 基本ルール

| チェック項目 | NG例 | OK例 | 理由 |
|------------|------|------|------|
| **PascalCase使用** | `taskService` | `TaskService` | Java/TS標準 |
| **単一責任を示す** | `Manager` | `TaskSessionManager` | 責任が明確 |
| **曖昧な名前禁止** | `Data`, `Info` | `TaskData`, `UserInfo` | 具体的に |

### 3.2 レイヤー別の命名規則

#### Controller層

```java
// ✅ OK例
@RestController
public class TaskController { }

@RestController
public class TaskSessionController { }

@RestController
public class UserAuthenticationController { }
```

#### Service層

```java
// ✅ OK例
@Service
public class TaskService { }

@Service
public class TaskSessionService { }

@Service
public class TaskEventService { }

@Service
public class UserAuthenticationService { }
```

#### Repository層

```java
// ✅ OK例
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> { }

@Repository
public interface TaskSessionRepository extends JpaRepository<TaskSession, Long> { }
```

#### Domain/Entity層

```java
// ✅ OK例
@Entity
public class Task { }

@Entity
public class TaskSession { }

@Entity
public class TaskEvent { }

@Entity
public class User { }
```

#### Exception層

```java
// ✅ OK例
public class TaskNotFoundException extends RuntimeException { }
public class TaskSessionAlreadyRunningException extends RuntimeException { }
public class InvalidTaskNameException extends RuntimeException { }
```

#### DTO層

```java
// ✅ OK例
public class TaskCreateRequest { }
public class TaskUpdateRequest { }
public class TaskResponse { }
public class TaskSessionStartRequest { }
```

#### Utility層

```java
// ❌ NG例
public class Utils { }
public class Helper { }

// ✅ OK例
public class DateTimeUtils { }
public class DurationFormatter { }
public class TaskValidationHelper { }
```

### 3.3 フロントエンドコンポーネント名

```typescript
// ✅ OK例（React）
export const TaskTimer: React.FC = () => { };
export const TaskList: React.FC = () => { };
export const TaskDetailModal: React.FC = () => { };
export const TimerStartButton: React.FC = () => { };
export const TaskFormInput: React.FC = () => { };
```

---

## 4. 定数名チェックリスト

### 4.1 基本ルール

定数は `UPPER_SNAKE_CASE` で命名

```java
// ✅ OK例
public static final int MAX_TASK_NAME_LENGTH = 100;
public static final int MIN_TASK_NAME_LENGTH = 1;
public static final int DEFAULT_TIMER_INTERVAL_SECONDS = 60;
public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
public static final long MAX_SESSION_DURATION_HOURS = 24;
```

```typescript
// ✅ OK例（TypeScript）
export const MAX_TASK_NAME_LENGTH = 100;
export const MIN_TASK_NAME_LENGTH = 1;
export const API_BASE_URL = "https://api.example.com";
export const DEFAULT_PAGE_SIZE = 20;
```

---

## 5. ファイル名チェックリスト

### 5.1 バックエンド（Java）

ファイル名はクラス名と完全一致：

```
TaskService.java
TaskController.java
TaskRepository.java
Task.java
TaskNotFoundException.java
```

### 5.2 フロントエンド（TypeScript/React）

```
TaskTimer.tsx
TaskList.tsx
TaskDetailModal.tsx
apiClient.ts
dateUtils.ts
types.ts
```

---

## 6. 特殊なケースの命名規則

### 6.1 ループ変数

```java
// ❌ NG例
for(int i = 0; i < tasks.size(); i++) {
    Task t = tasks.get(i);
}

// ✅ OK例（拡張for文）
for(Task task : tasks) {
    // task を使用
}

// ✅ OK例（インデックス必要な場合）
for(int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
    Task currentTask = tasks.get(taskIndex);
}
```

### 6.2 ラムダ式・Stream API

```java
// ❌ NG例
tasks.stream().map(t -> t.getName())

// ✅ OK例
tasks.stream()
    .map(task -> task.getName())
    .filter(taskName -> taskName.startsWith("重要"))
    .collect(Collectors.toList());
```

### 6.3 例外ハンドリング

```java
// ❌ NG例
try {
    // ...
} catch(Exception e) {
    // ...
}

// ✅ OK例
try {
    startTaskSession(taskId);
} catch(TaskNotFoundException taskNotFoundError) {
    log.error("Task not found: {}", taskId, taskNotFoundError);
} catch(TaskSessionAlreadyRunningException sessionRunningError) {
    log.warn("Session already running for task: {}", taskId, sessionRunningError);
}
```

---

## 7. 自動チェックツールの設定

### 7.1 ESLint設定（フロントエンド）

```json
{
  "rules": {
    "id-length": ["error", { "min": 4, "exceptions": ["id", "i", "x", "y"] }],
    "id-match": ["error", "^[a-z][a-zA-Z0-9]*$"],
    "camelcase": ["error", { "properties": "always" }]
  }
}
```

### 7.2 Checkstyle設定（バックエンド）

```xml
<module name="LocalVariableName">
  <property name="format" value="^[a-z][a-zA-Z0-9]*$"/>
  <property name="allowOneCharVarInForLoop" value="false"/>
</module>
<module name="MethodName">
  <property name="format" value="^[a-z][a-zA-Z0-9]*$"/>
</module>
```

---

## 8. チェック実行フロー

### コミット前チェック

1. **全変数名チェック**
   - 3文字以下の変数名を検出（id, url除く）
   - 型名と同じ変数名を検出
   - 連番付き変数名を検出

2. **全関数名チェック**
   - 動詞で始まっているか
   - 処理内容が明確か
   - 略語が使われていないか

3. **全クラス名チェック**
   - PascalCaseか
   - 単一責任が名前から分かるか

4. **boolean変数チェック**
   - `is/has/can/should` で始まっているか

5. **時間関連変数チェック**
   - 単位が明記されているか

### チェック結果の対応

| 重要度 | 対応 |
|-------|------|
| **Critical** | コミット不可、即座に修正必須 |
| **Warning** | 修正推奨、理由があればスキップ可 |
| **Info** | より良い命名の提案 |

---

## 9. まとめ：命名の黄金ルール

1. **意味が明確であること**
   - 変数名だけで何を表すか完全に理解できる

2. **略語を使わないこと**
   - 広く認知されたもの（id, url, api）以外は禁止

3. **一文字変数を使わないこと**
   - 数学的な座標など極めて限定的な場合のみ許可

4. **型名を繰り返さないこと**
   - 型情報は型宣言で分かる

5. **連番を使わないこと**
   - 区別できる意味のある名前を使用

6. **booleanは疑問形にすること**
   - `is/has/can/should` で始める

7. **関数は動詞で始めること**
   - 何をするか明確に

8. **時間には単位を明記すること**
   - `InSeconds`, `InMinutes`, `InMillis`

---

**このチェックリストに従うことで、コードの可読性が飛躍的に向上し、保守性が大幅に改善されます。**
