package com.tranquility.servlets;

import com.tranquility.dao.ReviewDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/DeleteReviewServlet")
public class DeleteReviewServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get ID of review to delete         
        int reviewId = Integer.parseInt(
                request.getParameter("reviewId")
        );

        ReviewDAO dao = new ReviewDAO();

        // DELETE operation called here        
        boolean status = dao.deleteReview(reviewId);

        if (status) {
            response.sendRedirect("ViewReviewsServlet?all=true");
        } else {
            response.getWriter().println("Delete Failed");
        }
    }
}
