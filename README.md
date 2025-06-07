# 📝 Risspecct Blog Platform Backend

A Spring Boot-based RESTful API for a role-secured blogging platform with JWT authentication, user role hierarchy, and full CRUD support for posts, comments, and likes.

---

## 🚀 Features

* User registration & login with JWT-based authentication
* Secure password hashing with BCrypt
* Role-based access: `VIEWER`, `AUTHOR`, `MOD`, `ADMIN`
* Users can:

    * Create, edit, delete their posts
    * Comment on and like posts
    * View posts and comments
* Moderators can delete any post or comment
* Admins can manage users (ban, delete, assign roles)
* Global exception handling with detailed responses
* Clean DTO to Entity mapping using MapStruct

---

## 📁 Project Structure

```bash
risspecct-blog-platform-backend/
├── src/main/java/users/rishik/BlogPlatform/
│   ├── Controllers/        # REST API controllers
│   ├── Entities/           # JPA entities
│   ├── Dtos/               # DTOs for request/response
│   ├── Services/           # Business logic services
│   ├── Repositories/       # Spring Data JPA repositories
│   ├── Filters/            # JWT authentication filter
│   ├── Security/           # JWT config, role hierarchy, user principal
│   ├── Exceptions/         # Global and custom exceptions
│   ├── Mappers/            # MapStruct mappers
│   └── Enums/              # Role enum
├── postman/
│   └── blog-platform-api.postman_collection.json
├── src/main/resources/
│   └── application.properties.example  # Configuration example
├── pom.xml
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

## 🧪 API Testing with Postman

A full-featured Postman collection is included:

**File:** `postman/blog-platform-api.postman_collection.json`

### ⚙️ Usage

1. Open Postman
2. Import the `.json` collection
3. Run `POST /users/login` to get a JWT
4. The token is saved to `{{token}}` for use in all authenticated requests
5. Test endpoints organized into folders:

    * Auth
    * Posts
    * Comments
    * Likes
    * Admin
    * Moderator
    * Error Scenarios

---

## ⚙️ Configuration

Use the included `application.properties.example` file:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/blog_platform
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
jwt.secret=your_jwt_secret_key
jwt.expiration=3600000
```

1. Rename it to `application.properties`
2. Fill in your database and JWT settings

---

## 🛡️ Security Overview

* Stateless JWT authentication
* Role hierarchy: `ADMIN > MOD > AUTHOR > VIEWER`
* Access control via `@PreAuthorize`
* BCrypt for secure password hashing
* Custom exception handling for 401, 403, 404, etc.

---

## 📆 Tech Stack

* Java 21
* Spring Boot 3
* Spring Security
* Spring Data JPA (Hibernate)
* MySQL
* JWT
* MapStruct
* Lombok

---

## 📦 Build & Run

### Prerequisites

* Java 21+
* Maven
* MySQL 8+

### Run Locally

```bash
git clone https://github.com/your-username/risspecct-blog-platform-backend.git
cd risspecct-blog-platform-backend
cp src/main/resources/application.properties.example src/main/resources/application.properties
./mvnw spring-boot:run
```

---

## 📝 License

This project is licensed under the MIT License.

---

## 🤝 Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
