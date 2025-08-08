package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean available;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = true;
    }

    public void saveToDatabase() {
        String checkSql = "SELECT COUNT(*) FROM books WHERE isbn = ?";
        String insertSql = "INSERT INTO books (title, author, isbn, available) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, isbn);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                System.out.println("⚠️ Book with ISBN '" + isbn + "' already exists.");
                return;
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, title);
                insertStmt.setString(2, author);
                insertStmt.setString(3, isbn);
                insertStmt.setBoolean(4, available);
                insertStmt.executeUpdate();
                System.out.println("✅ Book saved to database: " + title);
            }

        } catch (SQLException e) {
            System.out.println("❌ Failed to save book: " + title);
            e.printStackTrace();
        }
    }

    // Getters (if needed)
}