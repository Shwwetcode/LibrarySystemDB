package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowRecord {

    public static void borrowBook(String memberId, String isbn) {
        String sql = "INSERT INTO borrow_records (member_id, isbn) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, memberId);
            stmt.setString(2, isbn);
            stmt.executeUpdate();
            System.out.println("✅ Book borrowed successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Failed to borrow book.");
            e.printStackTrace();
        }
    }

    public static void returnBook(String memberId, String isbn) {
        String sql = "UPDATE borrow_records SET return_date = CURRENT_TIMESTAMP WHERE member_id = ? AND isbn = ? AND return_date IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, memberId);
            stmt.setString(2, isbn);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Book returned successfully!");
            } else {
                System.out.println("⚠️ No active borrow record found.");
            }

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