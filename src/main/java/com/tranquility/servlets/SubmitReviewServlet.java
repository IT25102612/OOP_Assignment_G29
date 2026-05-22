package com.tranquility.servlets;

import com.tranquility.dao.ReviewDAO;
import com.tranquility.models.Review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/SubmitReviewServlet")
public class SubmitReviewServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String codename = request.getParameter("codename");
        String location = request.getParameter("location");
        String email = request.getParameter("email");
        String reviewText = request.getParameter("reviewText");

        Review review = new Review();

        review.setCodename(codename);
        review.setLocation(location);
        review.setEmail(email);
        review.setReviewText(reviewText);

        ReviewDAO dao = new ReviewDAO();

        boolean status = dao.addReview(review);

        if (status) {

            response.sendRedirect("review-success.jsp");

        } else {

            response.getWriter().println("Review Submission Failed");

        }
    }
}