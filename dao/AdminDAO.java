package com.tranquility.dao;

import com.tranquility.models.Admin;
import com.tranquility.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AdminDAO {

    public boolean registerAdmin(Admin admin) {

        boolean status = false;

        try {
            Connection con = DBConnection.getConnection();
            
            // CREATE operation for admin registration
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
}
