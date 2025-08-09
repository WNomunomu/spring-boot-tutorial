# ユーザー機能と認証認可機能の実装計画

このドキュメントは、現在のタスク管理アプリケーションにユーザー機能を追加し、将来的に認証認可機能を実装するための詳細な手順をGitHub issueとして整理したものです。

## フェーズ1: 基本的なユーザー機能実装

### Issue #1: Userエンティティとリポジトリの作成
**Priority:** High  
**Labels:** enhancement, backend, phase1

#### 目的
ユーザー情報を管理するためのUserエンティティとUserRepositoryを作成する

#### 実装内容
- [ ] `User` エンティティクラスの作成 (`entity/User.kt`)
  - id (Primary Key)
  - username (unique, not null)
  - email (unique, not null)
  - createdAt, updatedAt (タイムスタンプ)
- [ ] `UserRepository` インターフェースの作成 (`repository/UserRepository.kt`)
  - JpaRepositoryを継承
  - findByUsername, findByEmailメソッドの定義
- [ ] データベーステーブルの作成確認

#### 受け入れ基準
- Userエンティティが正常にデータベースに保存できること
- ユニーク制約が正しく動作すること

---

### Issue #2: TaskとUserの関連付け
**Priority:** High  
**Labels:** enhancement, backend, phase1

#### 目的
TaskエンティティにUser参照を追加し、ユーザーごとにタスクを管理できるようにする

#### 実装内容
- [ ] `Task` エンティティの修正
  - userIdフィールドの追加
  - `@ManyToOne` アノテーションでUserとの関連付け
- [ ] `TaskRepository` の修正
  - findByUserIdメソッドの追加
  - findByUserIdOrderByCreatedAtDescメソッドの追加
- [ ] データベーススキーマの更新確認

#### 受け入れ基準
- Taskが特定のユーザーに関連付けられること
- ユーザーIDでタスクの絞り込みができること

---

### Issue #3: UserServiceとUserControllerの実装
**Priority:** High  
**Labels:** enhancement, backend, api, phase1

#### 目的
ユーザー管理のためのサービス層とAPI層を実装する

#### 実装内容
- [ ] `UserService` クラスの作成 (`service/UserService.kt`)
  - createUser, getAllUsers, getUserById, updateUser, deleteUserメソッド
  - ユーザー名・メールアドレスの重複チェック
- [ ] `UserController` クラスの作成 (`controller/UserController.kt`)
  - GET /api/users (全ユーザー取得)
  - GET /api/users/{id} (特定ユーザー取得)
  - POST /api/users (ユーザー作成)
  - PUT /api/users/{id} (ユーザー更新)
  - DELETE /api/users/{id} (ユーザー削除)
- [ ] ユーザー作成・更新用のDTOクラス作成

#### 受け入れ基準
- 全てのAPI endpointsが正常に動作すること
- バリデーションが適切に動作すること

---

### Issue #4: TaskServiceとTaskControllerのユーザー対応
**Priority:** High  
**Labels:** enhancement, backend, api, phase1

#### 目的
既存のタスク管理機能をユーザー別に分離する

#### 実装内容
- [ ] `TaskService` の修正
  - 全てのメソッドにuserIdパラメータを追加
  - ユーザーが存在しない場合のエラーハンドリング
  - タスクの所有者チェック機能
- [ ] `TaskController` の修正
  - GET /api/users/{userId}/tasks (特定ユーザーのタスク一覧)
  - POST /api/users/{userId}/tasks (特定ユーザーのタスク作成)
  - PUT/DELETE操作での所有者確認
- [ ] TaskCreateRequest, TaskUpdateRequestの修正

#### 受け入れ基準
- ユーザーは自分のタスクのみアクセスできること
- 他のユーザーのタスクへのアクセスが制限されること

---

## フェーズ2: 認証認可機能実装の準備

### Issue #5: Spring Security依存関係の追加
**Priority:** Medium  
**Labels:** enhancement, security, phase2

#### 目的
認証認可機能実装のためのライブラリを追加する

#### 実装内容
- [ ] `build.gradle.kts` の修正
  - spring-boot-starter-securityのコメントアウト解除
  - JWT関連ライブラリ（jjwt）のコメントアウト解除
  - BCryptPasswordEncoderの依存関係追加
- [ ] セキュリティ設定の初期化
  - 基本的なSecurityConfigクラスの作成
  - 現在は全てのエンドポイントを許可

#### 受け入れ基準
- アプリケーションが正常に起動すること
- 既存の機能が引き続き動作すること

---

### Issue #6: パスワードハッシュ機能の実装
**Priority:** Medium  
**Labels:** enhancement, security, phase2

#### 目的
ユーザーのパスワードを安全に保存するためのハッシュ機能を実装する

#### 実装内容
- [ ] `User` エンティティにpasswordフィールド追加
- [ ] `PasswordEncoder` Beanの設定
- [ ] `UserService` の修正
  - ユーザー作成時のパスワードハッシュ化
  - パスワード更新機能
- [ ] パスワードバリデーション機能

#### 受け入れ基準
- パスワードが平文で保存されないこと
- パスワードの強度チェックが動作すること

---

## フェーズ3: 本格的な認証認可機能

### Issue #7: JWT認証の実装
**Priority:** Low  
**Labels:** enhancement, security, authentication, phase3

#### 目的
JWT（JSON Web Token）を使用した認証システムを実装する

#### 実装内容
- [ ] JWT Utilityクラスの作成
  - トークン生成、検証、解析機能
- [ ] AuthenticationServiceの実装
  - ログイン機能（JWT発行）
  - トークン検証機能
- [ ] JWTAuthenticationFilterの実装
- [ ] AuthControllerの作成
  - POST /api/auth/login
  - POST /api/auth/register

#### 受け入れ基準
- ユーザーがログインできること
- 有効なJWTトークンが発行されること

---

### Issue #8: エンドポイント保護の実装
**Priority:** Low  
**Labels:** enhancement, security, authorization, phase3

#### 目的
認証されたユーザーのみがAPIにアクセスできるように制限する

#### 実装内容
- [ ] SecurityConfigの詳細設定
  - 認証が必要なエンドポイントの指定
  - 認証不要なエンドポイント（登録、ログイン）の指定
- [ ] UserDetailsServiceの実装
- [ ] タスクアクセス時の所有者確認強化
- [ ] エラーハンドリングの改善

#### 受け入れ基準
- 未認証ユーザーが保護されたエンドポイントにアクセスできないこと
- 認証されたユーザーは自分のリソースのみアクセス可能なこと

---

### Issue #9: ロールベース認可の実装
**Priority:** Low  
**Labels:** enhancement, security, authorization, phase3

#### 目的
管理者・一般ユーザーなどの役割に基づいたアクセス制御を実装する

#### 実装内容
- [ ] Roleエンティティまたは列挙型の作成
- [ ] UserエンティティにroleField追加
- [ ] `@PreAuthorize` アノテーションの活用
- [ ] 管理者専用API endpoints
  - GET /api/admin/users（全ユーザー管理）
  - GET /api/admin/tasks（全タスク管理）

#### 受け入れ基準
- 一般ユーザーが管理者機能にアクセスできないこと
- 管理者が全てのリソースにアクセスできること

---

## フェーズ4: セキュリティ強化とテスト

### Issue #10: セキュリティテストの実装
**Priority:** Medium  
**Labels:** testing, security, phase4

#### 目的
セキュリティ機能の動作を保証するテストを作成する

#### 実装内容
- [ ] 認証テスト
  - 正常ログインテスト
  - 無効な認証情報でのテスト
- [ ] 認可テスト
  - 所有者以外のリソースアクセステスト
  - ロール別アクセス制御テスト
- [ ] JWT関連テスト
  - トークン有効性テスト
  - 期限切れトークンテスト

#### 受け入れ基準
- 全セキュリティテストが通過すること

---

### Issue #11: セキュリティ設定の最適化
**Priority:** Low  
**Labels:** enhancement, security, optimization, phase4

#### 目的
本番環境に向けたセキュリティ設定の最適化

#### 実装内容
- [ ] CORS設定の追加
- [ ] レート制限の実装
- [ ] セキュリティヘッダーの設定
- [ ] 本番用セキュリティプロファイルの作成

#### 受け入れ基準
- セキュリティベストプラクティスに準拠していること

---

## 技術的考慮事項

### データベース設計
```sql
-- Users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tasks table (updated)
ALTER TABLE tasks ADD COLUMN user_id BIGINT NOT NULL;
ALTER TABLE tasks ADD FOREIGN KEY (user_id) REFERENCES users(id);
```

### API エンドポイント設計
```
# 認証
POST /api/auth/register
POST /api/auth/login

# ユーザー管理
GET /api/users
GET /api/users/{id}
POST /api/users
PUT /api/users/{id}
DELETE /api/users/{id}

# タスク管理（ユーザー別）
GET /api/users/{userId}/tasks
POST /api/users/{userId}/tasks
PUT /api/tasks/{taskId}
DELETE /api/tasks/{taskId}

# 管理者専用
GET /api/admin/users
GET /api/admin/tasks
```

### 実装順序の推奨
1. **フェーズ1**: 基本的なユーザー機能（Issue #1-4）
2. **フェーズ2**: セキュリティ準備（Issue #5-6）
3. **フェーズ3**: 認証認可実装（Issue #7-9）
4. **フェーズ4**: テスト・最適化（Issue #10-11）

各フェーズ完了後にテストと動作確認を実施することを推奨します。