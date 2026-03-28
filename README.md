# 📘 EnglishVocab — Backend API

> **RESTful API** cho ứng dụng học tiếng Anh tích hợp flashcard SRS, luyện tập bài học, theo dõi tiến độ và quản lý nội dung.

[![Java](https://img.shields.io/badge/Java-17-orange?logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen?logo=spring)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql)](https://www.mysql.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📑 Mục lục

- [Tổng quan](#-tổng-quan)
- [Tính năng](#-tính-năng)
- [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
- [Cơ sở dữ liệu](#-cơ-sở-dữ-liệu)
- [Cài đặt & Chạy](#-cài-đặt--chạy)
- [Biến môi trường](#-biến-môi-trường)
- [API Overview](#-api-overview)
- [Kiến trúc dự án](#-kiến-trúc-dự-án)
- [Bảo mật](#-bảo-mật)

---

## 🌟 Tổng quan

EnglishVocab Backend là một **REST API** được xây dựng bằng **Spring Boot 3**, cung cấp đầy đủ các API cho:

- Người dùng (Student, Teacher, Admin)
- Hệ thống nội dung học tập (Category → Topic → Lesson)
- Hệ thống bài tập và chấm điểm
- Flashcard với thuật toán ôn tập thông minh **SM-2 (Spaced Repetition)**
- Theo dõi tiến độ học tập và **chuỗi học liên tiếp (Streak)**
- Dashboard thống kê người dùng và Admin

---

## ✨ Tính năng

### 👤 Người dùng
- Đăng ký / đăng nhập bằng Email & Password
- Refresh token / Logout (stateless JWT + refresh token lưu DB)
- Cập nhật profile, đổi mật khẩu, upload avatar (Cloudinary)
- Hệ thống **Membership** (FREE / PREMIUM)

### 📚 Nội dung học tập
- Phân cấp 3 tầng: **Category → Topic → Lesson**
- Quản lý từ vựng trong từng Lesson (LessonWord)
- Bài đọc hiểu (Reading Passage)
- Bài tập trắc nghiệm (Exercise → Question → Option)
- Hỗ trợ sắp xếp thứ tự, publish/unpublish

### 📝 Bài tập & Luyện tập
- Start attempt → Submit attempt → Xem kết quả
- Tự động tính điểm và đánh dấu pass/fail
- Lịch sử tất cả các lần làm bài

### 📖 Tiến độ học
- Theo dõi trạng thái: `NOT_STARTED` → `IN_PROGRESS` → `COMPLETED`
- Tiến độ học theo % (progressPercent, không thể giảm)
- Thống kê tiến độ theo từng Topic

### 🧠 Flashcard & SRS
- Tạo/quản lý Deck + Card cá nhân hoặc công khai
- Thuật toán **SM-2** tính lịch ôn tập tối ưu
- Xem danh sách thẻ cần ôn hôm nay (due cards)

### 🔥 Streak & Dashboard
- Tính **current streak** và **longest streak** theo ngày hoạt động
- Dashboard: tổng số lesson hoàn thành, số topic xong
- Stats: tỷ lệ hoàn thành, điểm trung bình, số từ đã học

### 🔧 Admin
- Thống kê toàn hệ thống (users, lessons, exercises, attempts)
- Biểu đồ tăng trưởng người dùng theo ngày
- Nội dung phổ biến (bài học nhiều lượt xem, bài tập nhiều lần làm)
- Quản lý tài khoản người dùng

---

## 🛠 Công nghệ sử dụng

| Công nghệ | Phiên bản | Mục đích |
|-----------|-----------|---------|
| **Java** | 17 | Ngôn ngữ lập trình |
| **Spring Boot** | 3.4.3 | Framework chính |
| **Spring Security** | 6.x | Bảo mật & phân quyền |
| **Spring Data JPA** | 3.x | ORM / Database layer |
| **MySQL** | 8.0 | Cơ sở dữ liệu |
| **Flyway** | — | Database migration |
| **JJWT** | 0.11.5 | JSON Web Token |
| **MapStruct** | 1.5.5 | DTO mapping |
| **Lombok** | — | Giảm boilerplate code |
| **Cloudinary** | 2.0.0 | Upload & lưu trữ ảnh |
| **SpringDoc OpenAPI** | 2.8.4 | Swagger UI tự động |
| **Spring Dotenv** | 5.1.0 | Đọc biến môi trường từ `.env` |
| **Apache Commons CSV** | 1.10.0 | Xử lý file CSV |

---

## 🗄 Cơ sở dữ liệu

### Sơ đồ quan hệ chính

```
User
 ├── UserStreak          (1-1)  streak / chuỗi học
 ├── UserLessonProgress  (1-n)  tiến độ từng bài
 ├── UserExerciseAttempt (1-n)  lịch sử làm bài
 ├── UserAnswerLog       (1-n)  từng câu trả lời
 ├── FlashcardDeck       (1-n)  bộ flashcard cá nhân
 └── UserFlashcardSrs    (1-n)  lịch SRS từng thẻ

Category
 └── Topic
      └── Lesson
           ├── LessonWord       (1-n)  từ vựng
           ├── ReadingPassage   (1-1)  bài đọc
           └── Exercise
                └── Question
                     └── QuestionOption

FlashcardDeck
 └── Flashcard
      └── UserFlashcardSrs
```

### Migration
Database schema được quản lý tự động bởi **Flyway**:
```
src/main/resources/db/migration/
├── V1__init_schema.sql
├── V2__add_users.sql
└── ...
```

---

## 🚀 Cài đặt & Chạy

### Yêu cầu
- Java 17+
- Maven 3.8+
- MySQL 8.0+

### 1. Clone project
```bash
git clone https://github.com/WangJung14/EnglishVocab.git
cd EnglishVocab/englishgrammar
```

### 2. Tạo file `.env.properties`
```bash
cp .env.example .env.properties
```
Rồi điền các biến (xem mục [Biến môi trường](#-biến-môi-trường))

### 3. Tạo database MySQL
```sql
CREATE DATABASE english_vocab CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 4. Chạy ứng dụng
```bash
mvn spring-boot:run
```

### 5. Kiểm tra
```
API:     http://localhost:8080/api/v1
Swagger: http://localhost:8080/api/v1/swagger-ui/index.html
```

### Build JAR
```bash
mvn clean package -DskipTests
java -jar target/englishgrammar-0.0.1-SNAPSHOT.jar
```

---

## 🔑 Biến môi trường

Tạo file `.env.properties` ở root của project (`englishgrammar/`):

```properties
# Database
DB_NAME=english_vocab
DB_USERNAME=root
DB_PASSWORD=your_password

# JWT
JWT_SECRET=your_super_secret_key_here_minimum_32_chars
JWT_EXPIRATION=86400000

# Cloudinary (upload ảnh)
CLOUDINARY_URL=cloudinary://api_key:api_secret@cloud_name
```

> ⚠️ **Không commit** file `.env.properties` lên Git. File này đã được thêm vào `.gitignore`.

---

## 📡 API Overview

> **Base URL:** `http://localhost:8080/api/v1`  
> **Swagger UI:** `http://localhost:8080/api/v1/swagger-ui/index.html`

| Module | Prefix | Auth |
|--------|--------|------|
| Authentication | `/auth` | Public |
| User & Profile | `/users/me` | Bearer Token |
| Content (Public) | `/categories`, `/topics`, `/lessons` | Public |
| Content (Teacher) | `/teacher/lessons`, `/teacher/topics` | TEACHER / ADMIN |
| Exercise | `/exercises` | Bearer Token |
| Lesson Progress | `/lessons/{id}/progress` | Bearer Token |
| Flashcard Deck | `/flashcard-decks` | Bearer Token |
| SRS Review | `/srs` | Bearer Token |
| Dashboard | `/users/me/dashboard` | Bearer Token |
| User Stats | `/users/me/stats` | Bearer Token |
| Admin | `/admin/**` | ADMIN |

### Auth Flow
```
POST /auth/login
  → { accessToken, refreshToken }
  → Gửi kèm mọi request: Authorization: Bearer {accessToken}
  → Hết hạn? POST /auth/refresh-token?token={refreshToken}
```

### Response format (tất cả API)
```json
{
  "code": 1000,
  "message": "success",
  "result": { ... }
}
```

---

## 🏗 Kiến trúc dự án

```
src/main/java/trung/supper/englishgrammar/
│
├── controller/          ← REST Controllers (I/O layer)
├── services/
│   ├── IXxxService.java ← Service interfaces (DIP)
│   └── impl/            ← Service implementations
├── repositorys/         ← Spring Data JPA repositories
├── models/              ← JPA Entities
├── dto/
│   ├── request/         ← Request DTOs (input)
│   └── response/        ← Response DTOs (output)
├── mapper/              ← MapStruct mappers (Entity ↔ DTO)
├── sercurity/           ← JWT filter, SecurityConfig, CustomUserDetails
├── enums/               ← Các enum (Role, Level, Status...)
├── exception/           ← Global exception handler
└── utils/               ← Tiện ích (SlugUtils, ...)
```

### Nguyên tắc thiết kế
- **Clean Architecture**: Controller → Service → Repository (3 tầng rõ ràng)
- **SOLID Principles**: DIP (constructor injection), SRP (mỗi class 1 trách nhiệm), OCP (Strategy pattern cho lesson types)
- **DTO Pattern**: Không bao giờ expose Entity trực tiếp ra API
- **No N+1**: Dùng `JOIN FETCH` và aggregate queries thay vì load từng entity
- **Stateless JWT**: Access token ngắn hạn + Refresh token lưu DB

---

## 🔒 Bảo mật

| Cơ chế | Chi tiết |
|--------|---------|
| **JWT Authentication** | Access token (configurable expiry) |
| **Refresh Token** | Lưu DB, rotate mỗi lần refresh |
| **Role-Based Access** | `STUDENT`, `TEACHER`, `ADMIN` |
| **Method Security** | `@PreAuthorize("hasRole('ADMIN')")` |
| **Password Hashing** | BCrypt |
| **CORS** | Cấu hình trong `SecurityConfig` |

### Phân quyền API

| Role | Quyền |
|------|-------|
| **PUBLIC** | Đọc categories, topics, lessons |
| **STUDENT** | Profile, progress, exercises, flashcards, SRS |
| **TEACHER** | + Tạo/sửa/xóa lessons, exercises, questions |
| **ADMIN** | + Mọi quyền, quản lý users, xem admin stats |

---

## 📄 License

MIT License — Xem file [LICENSE](LICENSE) để biết thêm chi tiết.

---

## 👤 Author

**lilmeomeo14**  
GitHub: [@WangJung14](https://github.com/WangJung14)
