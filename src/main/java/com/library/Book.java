package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    public static void listAllBooks() {
    String sql = "SELECT * FROM books";
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        System.out.println("\n📚 All Books:");
        while (rs.next()) {
            System.out.printf("Title: %s | Author: %s | ISBN: %s | Available: %b%n",
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("isbn"),
                rs.getBoolean("available"));
        }
    } catch (SQLException e) {
        System.out.println("❌ Failed to fetch books");
        e.printStackTrace();
    }
}
    public void saveToDatabase() {
    String checkSql = "SELECT COUNT(*) FROM books WHERE isbn = ?";
    String insertSql = "INSERT INTO books (title, author, isbn, available) VALUES (?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

        checkStmt.setString(1, isbn);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
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
    public void updateAvailabilityInDB(boolean availability) {
    String sql = "UPDATE books SET available = ? WHERE isbn = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setBoolean(1, availability);
        stmt.setString(2, this.isbn);
        stmt.executeUpdate();
        System.out.println("✅ Updated availability of book: " + title);
    } catch (SQLException e) {
        System.out.println("❌ Failed to update availability for: " + title);
        e.printStackTrace();
    }
}
public static void searchBooks(String keyword) {
    String sql = "SELECT * FROM books WHERE title LIKE ? OR isbn LIKE ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        String queryKeyword = "%" + keyword + "%";
        stmt.setString(1, queryKeyword);
        stmt.setString(2, queryKeyword);

        ResultSet rs = stmt.executeQuery();

        System.out.println("\n🔍 Search Results:");
        boolean found = false;
        while (rs.next()) {
            System.out.printf("Title: %s | Author: %s | ISBN: %s | Available: %s\n",
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("isbn"),
                rs.getBoolean("available") ? "Yes" : "No"
            );
            found = true;
        }

        if (!found) {
            System.out.println("❌ No books found matching the keyword.");
        }

    } catch (SQLException e) {
        System.out.println("❌ Failed to search books.");
        e.printStackTrace();
    }
}
    // Getters (if needed)
}