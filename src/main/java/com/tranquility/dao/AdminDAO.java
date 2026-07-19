package com.tranquility.dao;

import com.tranquility.models.Admin;
import com.tranquility.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    public boolean registerAdmin(Admin admin) {

        boolean status = false;

        try {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO admins(full_name,email,username,password) VALUES(?,?,?,?)";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, admin.getFullName());
            pst.setString(2, admin.getEmail());
            pst.setString(3, admin.getUsername());
            pst.setString(4, admin.getPassword());

            status = pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public boolean validateLogin(String adminId, String password) {
        String sql = "SELECT * FROM admin_users WHERE admin_id = ? AND password_hash = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, adminId);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}