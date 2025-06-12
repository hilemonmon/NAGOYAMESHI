
# NAGOYAMESHI

**名古屋市のB級グルメに特化した飲食店レビュー & 予約サービス**

---

## 概要

**NAGOYAMESHI** は、名古屋のB級グルメを楽しむための飲食店レビュー＆予約サービスです。  
無料会員／有料会員／管理者それぞれに異なる機能が提供され、名古屋を訪れる旅行者や地元のユーザーに向けた飲食体験の最適化を図ります。

---

## 主な機能

- 店舗検索（カテゴリ・予算・評価）
- レビュー閲覧／投稿（有料会員）
- お気に入り登録（有料会員）
- 店舗予約（有料会員）
- 管理者による店舗・会員・レビュー・集計管理
- Stripeを用いた有料会員（月額300円）管理

---

## 技術スタック

| 区分         | 使用技術                                                                 |
|--------------|--------------------------------------------------------------------------|
| 言語・環境     | Java 17+, Maven 3.8+, MySQL 5.7+                                          |
| バックエンド   | Spring Boot, Spring Security, JPA (Hibernate), HikariCP                 |
| フロントエンド | Thymeleaf（サーバーサイドレンダリング）                                 |
| 決済／認証     | Stripe Java SDK, Spring Security                                          |
| テスト        | JUnit, Spring Boot Test                                                  |
| その他        | Lombok, PlantUML（ユースケース・画面遷移図）, dbdiagram.io（ER図）        |

---

## ディレクトリ構成（Monorepo）

```
NAGOYAMESHI/
├── src/
│   ├── main/
│   │   ├── java/            # Javaソースコード
│   │   ├── resources/       # Thymeleafテンプレート、設定ファイル
│   ├── test/                # 単体・結合テスト
├── pom.xml                  # Maven依存管理
└── README.md
```

---

## セットアップ手順（ローカル環境）

1. Java 17+ / Maven 3.8+ / MySQL 5.7+ を用意
2. `application.properties` または `application.yml` を設定
3. MySQLにDB作成: `CREATE DATABASE nagoyameshi;`
4. 以下のコマンドで起動:

```bash
mvn clean spring-boot:run
```

---

## デプロイ予定

- Heroku / AWS（Elastic Beanstalkなど） / Java対応VPS を想定

---

## セキュリティ・非機能要件

- Spring Securityによるページ別アクセス制御
- CSRF対策実装済み
- Webサーバーの冗長構成（想定）
- スムーズな検索・絞り込みパフォーマンス
- 本番とステージング環境の切り分け設計

---

## ER図・ユースケース図・画面遷移図

- [ユースケース図](uml/usecase.png)
- [ユーザー向け画面遷移図](uml/screenflow.png)
- [管理者向け画面遷移図](uml/admin_screenflow.png)
- [ER図（dbdiagram DSL）](#6-er図（エンティティ・リレーションシップ図）)

---

## ライセンス

本プロジェクトは [MIT License](LICENSE) のもとで提供されます。

---

## 開発者

- 本プロジェクトは個人開発プロジェクトです。
- 問い合わせ・貢献・不具合報告は Issue または Pull Request にてお知らせください。
