# 📝 Risspecct Blog Platform Backend

![Java](https://img.shields.io/badge/Java-21-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-6DB33F.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)
![Build](https://img.shields.io/badge/Status-Active-success)

A secure, role-based blogging platform backend built with Spring Boot. Features include JWT authentication, user role hierarchy, and full CRUD operations for posts, comments, and likes.

---

## 🚀 Features

* Register & login with JWT authentication
* Secure password hashing using BCrypt
* Role-based access: `VIEWER`, `AUTHOR`, `MOD`, `ADMIN`
* Users can:

  * Create, update, delete their posts
  * Comment on and like posts
  * View posts, comments, and own data
* Moderators can delete any post or comment
* Admins can manage users (ban, delete, assign roles)
* Global exception handling with descriptive error messages
* Clean DTO ↔ Entity mapping using MapStruct
* **Interactive Swagger UI for API testing and documentation**

---

## 📁 Project Structure

```
risspecct-blog-platform-backend/
├── Controllers/
├── Entities/
├── Dtos/
├── Services/
├── Repositories/
├── Filters/
├── Security/
├── Exceptions/
├── Mappers/
├── Enums/
├── postman/
├── src/main/resources/
```

---

## 🔐 Roles & Permissions

| Role   | Permissions                                     |
| ------ | ----------------------------------------------- |
| VIEWER | View posts, comment, like                       |
| AUTHOR | All of the above + create/edit/delete own posts |
| MOD    | All of the above + delete any post/comment      |
| ADMIN  | Full access + manage users and assign roles     |

---

## 📌 API Endpoint Overview

### ⚖️ Auth (`/users`)

* `POST /register` – Register new user
* `POST /login` – Authenticate and get JWT

### 👤 User (`/users`)

* `GET /` – View own profile
* `PUT /` – Update own profile
* `DELETE /` – Delete own account
* `GET /comments` – Get own comments
* `GET /all` – (Admin) View all users

### 📄 Posts (`/posts`)

* `POST /` – (Author) Add post
* `GET /{id}` – View specific post
* `PUT /{id}` – (Author) Update own post
* `DELETE /{id}` – (Author) Delete own post
* `GET /users/me/posts` – View own posts
* `GET /users/{userId}/posts` – View posts by user

### 💬 Comments (`/posts/{postId}/comments`)

* `POST /` – Add comment
* `GET /` – Get all comments on a post
* `GET /{id}` – Get single comment
* `PUT /{id}` – Update own comment
* `DELETE /{id}` – Delete own comment

### ❤️ Likes (`/posts/{postId}`)

* `POST /like` – Like a post
* `GET /likes` – Get like count
* `DELETE /like` – Remove like

### 🛡️ Admin (`/admin`)

* `PUT /users/roles/{userId}` – Assign roles
* `PUT /users/ban/{userId}` – Ban/unban user
* `DELETE /users/delete/{userId}` – Delete user

### 🩰 Moderator (`/mod`)

* `DELETE /delete/posts/{postId}` – Delete any post
* `DELETE /delete/comments/{commentId}` – Delete any comment

---

## 🛡️ Security Highlights

* Stateless JWT authentication
* Role hierarchy: `ADMIN > MOD > AUTHOR > VIEWER`
* `@PreAuthorize` annotations on secured routes
* Custom exception handler for clean error responses

---

## 🌱 Tech Stack

* Java 21
* Spring Boot 3.4+
* Spring Security
* Spring Data JPA (Hibernate)
* MySQL 8+
* JWT (jjwt)
* MapStruct
* Lombok

---

## 📚 API Documentation

* **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
* Shortcut: Access via `/docs` redirect

Use the “Authorize” button and provide `Bearer <JWT>` to test secured endpoints.

---

## 🛠️ Setup & Run

### Prerequisites

* Java 21+
* Maven
* MySQL 8+

### Steps

```bash
git clone https://github.com/your-username/risspecct-blog-platform-backend.git
cd risspecct-blog-platform-backend
cp src/main/resources/application.properties.example src/main/resources/application.properties
# edit DB and JWT settings in application.properties
./mvnw spring-boot:run
```

---

## 🔮 Postman Collection

Located at: `postman/blog-platform-api.postman_collection.json`

1. Import into Postman
2. Use `/users/login` to retrieve a JWT
3. JWT auto-assigned to `{{token}}` for all requests

---

## 📃 License

This project is licensed under the [MIT License](LICENSE).

---

## 🤝 Contributions

Pull requests are welcome. For major changes, please open an issue first to discuss what you’d like to change.
