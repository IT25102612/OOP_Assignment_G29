package com.tranquility.servlets;

import com.tranquility.dao.ReviewDAO;
import com.tranquility.models.Review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/UpdateReviewServlet")
public class UpdateReviewServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int reviewId = Integer.parseInt(request.getParameter("reviewId"));

        String adminReply = request.getParameter("adminReply");

        boolean approved = Boolean.parseBoolean(
                request.getParameter("approved")
        );

        Review review = new Review();

        review.setReviewId(reviewId);
        review.setAdminReply(adminReply);
        review.setApproved(approved);

        ReviewDAO dao = new ReviewDAO();

        boolean status = dao.updateReview(review);

        if (status) {
            response.sendRedirect("ViewReviewsServlet?all=true");
        } else {
            response.getWriter().println("Review Update Failed");
        }
    }
}