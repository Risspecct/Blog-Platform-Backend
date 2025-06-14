# 📝 Risspecct Blog Platform Backend

A secure, role-based blogging platform backend built with Spring Boot. Features include JWT authentication, user role hierarchy, and full CRUD operations for posts, comments, and likes.

---

## 🚀 Features

* Register & login with JWT authentication
* Secure password hashing using BCrypt
* Role-based access: `VIEWER`, `AUTHOR`, `MOD`, `ADMIN`
* Users can:

  * Create, update, delete their posts
  * Comment on and like posts
  * View posts, comments, and their own data
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
├── src/main/resources
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

## 🔧 Endpoints Overview

### ⚖️ Auth

* `POST /users/register` - Register a new user
* `POST /users/login` - Login and receive JWT token

### 👤 User

* `GET /users/` - Get current user info
* `PUT /users/` - Update current user
* `DELETE /users/` - Delete own account
* `GET /users/comments` - Get user's comments
* `GET /users/all` - (Admin) Get all users

### 📄 Posts

* `POST /posts` - Add a new post (Author only)
* `GET /posts/{id}` - Get a single post
* `GET /users/me/posts` - Get all posts by current user
* `GET /users/{userId}/posts` - Get all posts by another user
* `PUT /posts/{id}` - Update own post
* `DELETE /posts/{id}` - Delete own post

### 💬 Comments

* `POST /posts/{postId}/comments` - Add a comment
* `GET /posts/{postId}/comments/` - Get all comments for a post
* `GET /posts/{postId}/comments/{id}` - Get a single comment
* `PUT /posts/{postId}/comments/{id}` - Update own comment
* `DELETE /posts/{postId}/comments/{id}` - Delete own comment

### ❤️ Likes

* `POST /posts/{postId}/addLike` - Add a like
* `GET /posts/{postId}/likes` - Count likes for a post
* `DELETE /posts/{postId}/unlike` - Remove a like

### 🛡️ Admin

* `PUT /admin/users/roles/{userId}` - Set user roles
* `PUT /admin/users/ban/{userId}` - Ban/unban user
* `DELETE /admin/users/delete/{userId}` - Delete user

### 🧰 Moderator

* `DELETE /mod/delete/posts/{postId}` - Delete any post
* `DELETE /mod/delete/comments/{commentId}` - Delete any comment

---

## 🛡️ Security Highlights

* Stateless JWT authentication
* Role hierarchy: `ADMIN > MOD > AUTHOR > VIEWER`
* `@PreAuthorize` annotations for fine-grained control
* BCrypt hashing for secure passwords
* Custom exception handling for 401/403/404/500 errors

---

## 🌱 Tech Stack

* Java 21
* Spring Boot 3
* Spring Security
* Spring Data JPA (Hibernate)
* MySQL
* JWT
* MapStruct
* Lombok

---

## 🛆 Setup & Run

### Prerequisites

* Java 21+
* Maven
* MySQL 8+

### Steps

```bash
git clone https://github.com/your-username/risspecct-blog-platform-backend.git
cd risspecct-blog-platform-backend
cp src/main/resources/application.properties.example src/main/resources/application.properties
# configure DB and JWT settings in application.properties
./mvnw spring-boot:run
```

Then open:

* **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* **OpenAPI JSON Spec:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Use the "Authorize" button at the top of Swagger UI to paste your JWT token (`Bearer <token>`) and test secured endpoints.

---

## 🔮 Postman Collection

A full-featured Postman collection is provided in `postman/blog-platform-api.postman_collection.json`

1. Import the file in Postman
2. Run the login endpoint to obtain a JWT token
3. Token is auto-assigned to `{{token}}` for future use

---

##  📃License

This project is licensed under the MIT License.

---

## 🤝 Contributions

Contributions are welcome! For significant changes, please open an issue first to discuss the changes you'd like to make.
