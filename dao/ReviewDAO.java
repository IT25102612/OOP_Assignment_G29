package com.tranquility.dao;

import com.tranquility.models.Review;
import com.tranquility.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReviewDAO {

    public boolean addReview(Review review) {

        boolean status = false;

        try {
            //Establish operation using INSERT query
            Connection con = DBConnection.getConnection();

            //CREATE operation using INSERT query
            String sql = "INSERT INTO reviews(guest_name,room_type,rating,comment) VALUES(?,?,?,?)";
            
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, review.getGuestName());
            pst.setString(2, review.getRoomType());
            pst.setInt(3, review.getRating());
            pst.setString(4, review.getComment());

            status = pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }
}
