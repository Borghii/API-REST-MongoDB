
## 📚 **Mini Project: "Book and Author Manager"**

### 🎯 Objective:

Build a RESTful API where you can:

* Create, read, update, and delete books.
* Associate each book with an author.
* Retrieve all books written by a specific author.
* Search for books by title, genre, or year.

---

## 🧱 Data Structure

### 1. 📘 **Book**

```json
{
  "id": "ObjectId",
  "title": "Clean Code",
  "genre": "Programming",
  "year": 2008,
  "authorId": "ObjectId"
}
```

### 2. 👤 **Author**

```json
{
  "id": "ObjectId",
  "name": "Robert C. Martin",
  "nationality": "USA"
}
```

---

## 🔧 Features 

| Function              | Endpoint                       | Method |
| --------------------- | ------------------------------ | ------ |
| Create book           | `/api/books`                   | POST   |
| Get all books         | `/api/books`                   | GET    |
| Search books by genre | `/api/books?genre=Programming` | GET    |
| Create author         | `/api/authors`                 | POST   |
| Get books by author   | `/api/authors/{id}/books`      | GET    |
| Update book           | `/api/books/{id}`              | PUT    |
| Delete book           | `/api/books/{id}`              | DELETE |

---

## 🛠️ Technologies and Libraries

* `Spring Boot`
* `Spring Web`
* `Spring Data MongoDB`
* `Lombok` (to reduce boilerplate)
* `MongoDB` (via Docker)



