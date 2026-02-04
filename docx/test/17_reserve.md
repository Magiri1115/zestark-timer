# 必要なもの
CI/CD 用のテスト自動化
GitHub Actions / GitLab CI で backend test, frontend test, e2e test を自動実行
テスト用モックサーバ
API連携の外部依存がある場合は MSW（Mock Service Worker）でモック
## 環境ごとの設定
・.env.test / application-test.yml など
・カバレッジレポート
Jest + Coveralls
Jacoco (バックエンド)
・Lint / 型チェック
TS + ESLint / Prettier
Java + Checkstyle / SpotBugs