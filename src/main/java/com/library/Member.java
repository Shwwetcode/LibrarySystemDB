package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Member {
    private String name;
    private String memberId;

    public Member(String name, String memberId) {
        this.name = name;
        this.memberId = memberId;
    }
    public static void listAllMembers() {
    String sql = "SELECT * FROM members";
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        System.out.println("\n👥 All Members:");
        while (rs.next()) {
            System.out.printf("Name: %s | Member ID: %s%n",
                rs.getString("name"),
                rs.getString("member_id"));
        }
    } catch (SQLException e) {
        System.out.println("❌ Failed to fetch members");
        e.printStackTrace();
    }
}

    public void saveToDatabase() {
    String checkSql = "SELECT COUNT(*) FROM members WHERE member_id = ?";
    String insertSql = "INSERT INTO members (name, member_id) VALUES (?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

        checkStmt.setString(1, memberId);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            System.out.println("⚠️ Member with ID '" + memberId + "' already exists.");
            return;
        }

        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            insertStmt.setString(1, name);
            insertStmt.setString(2, memberId);
            insertStmt.executeUpdate();
            System.out.println("✅ Member registered: " + name);
        }

    } catch (SQLException e) {
        System.out.println("❌ Failed to register member: " + name);
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