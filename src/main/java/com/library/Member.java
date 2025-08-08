package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Member {
    private String name;
    private String memberId;

    public Member(String name, String memberId) {
        this.name = name;
        this.memberId = memberId;
    }

    public void saveToDatabase() {
        String sql = "INSERT INTO members (name, member_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, memberId);
            stmt.executeUpdate();

            System.out.println("✅ Member saved to database: " + name);
        } catch (SQLException e) {
            System.out.println("❌ Failed to save member: " + name);
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public String getMemberId() {
        return memberId;
    }
}