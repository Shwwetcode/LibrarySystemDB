package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowRecord {

    public static void borrowBook(String memberId, String isbn) {
    String checkMemberSql = "SELECT COUNT(*) FROM members WHERE member_id = ?";
    String checkBookSql = "SELECT available FROM books WHERE isbn = ?";
    String borrowSql = "INSERT INTO borrow_records (member_id, isbn) VALUES (?)";
    String updateBookSql = "UPDATE books SET available = false WHERE isbn = ?";

    try (Connection conn = DatabaseConnection.getConnection()) {
        // Check if member exists
        try (PreparedStatement checkMemberStmt = conn.prepareStatement(checkMemberSql)) {
            checkMemberStmt.setString(1, memberId);
            ResultSet memberRs = checkMemberStmt.executeQuery();
            if (memberRs.next() && memberRs.getInt(1) == 0) {
                System.out.println("❌ Member ID not found.");
                return;
            }
        }

        // Check if book exists and is available
        boolean isAvailable = false;
        try (PreparedStatement checkBookStmt = conn.prepareStatement(checkBookSql)) {
            checkBookStmt.setString(1, isbn);
            ResultSet bookRs = checkBookStmt.executeQuery();
            if (bookRs.next()) {
                isAvailable = bookRs.getBoolean("available");
            } else {
                System.out.println("❌ Book ISBN not found.");
                return;
            }
        }

        if (!isAvailable) {
            System.out.println("⚠️ Book is already borrowed.");
            return;
        }

        // Insert borrow record
        try (PreparedStatement borrowStmt = conn.prepareStatement("INSERT INTO borrow_records (member_id, isbn) VALUES (?, ?)")) {
            borrowStmt.setString(1, memberId);
            borrowStmt.setString(2, isbn);
            borrowStmt.executeUpdate();
        }

        // Mark book as unavailable
        try (PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql)) {
            updateBookStmt.setString(1, isbn);
            updateBookStmt.executeUpdate();
        }

        System.out.println("✅ Book borrowed successfully.");

    } catch (SQLException e) {
        System.out.println("❌ Failed to borrow book.");
        e.printStackTrace();
    }
}

    public static void returnBook(String memberId, String isbn) {
    String checkSql = "SELECT id FROM borrow_records WHERE member_id = ? AND isbn = ? AND return_date IS NULL";
    String updateBorrowSql = "UPDATE borrow_records SET return_date = CURRENT_TIMESTAMP WHERE id = ?";
    String updateBookSql = "UPDATE books SET available = TRUE WHERE isbn = ?";

    try (Connection conn = DatabaseConnection.getConnection()) {
        int borrowId = -1;

        // Check if a borrow record exists
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, memberId);
            checkStmt.setString(2, isbn);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                borrowId = rs.getInt("id");
            } else {
                System.out.println("⚠️ No active borrow record found for this member and book.");
                return;
            }
        }

        // Update return_date
        try (PreparedStatement updateStmt = conn.prepareStatement(updateBorrowSql)) {
            updateStmt.setInt(1, borrowId);
            updateStmt.executeUpdate();
        }

        // Mark book as available
        try (PreparedStatement bookStmt = conn.prepareStatement(updateBookSql)) {
            bookStmt.setString(1, isbn);
            bookStmt.executeUpdate();
        }

        System.out.println("✅ Book returned successfully.");

    } catch (SQLException e) {
        System.out.println("❌ Failed to return book.");
        e.printStackTrace();
    }
}
    public static void viewAllRecords() {
    String sql = "SELECT * FROM borrow_records";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        System.out.println("\n📚 Borrow Records:");
        while (rs.next()) {
            int id = rs.getInt("id");
            String memberId = rs.getString("member_id");
            String isbn = rs.getString("isbn");
            String borrowDate = rs.getString("borrow_date");
            String returnDate = rs.getString("return_date");

            System.out.printf("ID: %d | Member ID: %s | ISBN: %s | Borrowed: %s | Returned: %s\n",
                    id, memberId, isbn, borrowDate,
                    returnDate == null ? "Not returned yet" : returnDate);
        }
    } catch (SQLException e) {
        System.out.println("❌ Failed to fetch borrow records");
        e.printStackTrace();
    }
}
}