package com.tranquility.servlets;

import com.tranquility.dao.ReviewDAO;
import com.tranquility.models.Review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/addReview")
public class AddReviewServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String guestName = request.getParameter("guestName");
        String roomType = request.getParameter("roomType");
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comment = request.getParameter("comment");

        Review review = new Review(guestName, roomType, rating, comment);

        ReviewDAO dao = new ReviewDAO();

        if (dao.addReview(review)) {
            response.sendRedirect("booking-confirmation.html");
        } else {
            response.getWriter().println("Review Submission Failed");
        }
    }
}