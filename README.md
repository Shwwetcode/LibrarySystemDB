# 📚 Library Management System (Java + MySQL)

A simple command-line Library Management System built in Java using JDBC and MySQL. This project demonstrates core concepts of:

- Object-Oriented Programming (OOP)
- Java Database Connectivity (JDBC)
- MySQL database integration
- CRUD operations
- Modular Maven project structure

---

## ⚙️ Features

- Add books to the library
- Register members
- Borrow and return books
- View borrow records (with timestamps)
- MySQL-backed persistent storage

---

## 🧰 Technologies Used

- Java 21
- MySQL 8.0+
- JDBC
- Maven
- MySQL Workbench (for DB design)

---

## 🗃️ Database Schema

- `books(id, title, author, isbn, available)`
- `members(id, name, member_id)`
- `borrow_records(id, member_id, isbn, borrow_date, return_date)`

---

## ▶️ Running the Project

1. Clone the repo:
   ```bash
   git clone https://github.com/Shwwetcode/LibrarySystemDB.git
