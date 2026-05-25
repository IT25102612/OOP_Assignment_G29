package com.tranquility.servlets;

import com.tranquility.dao.ReviewDAO;
import com.tranquility.models.Review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/ViewReviewsServlet")
public class ViewReviewsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String all = request.getParameter("all");
        
        ReviewDAO dao = new ReviewDAO();

        // READ operation       
        List<Review> reviewList;

        if (all != null && all.equals("true")) {
            reviewList = dao.getAllReviews();
        } else {
            reviewList = dao.getApprovedReviews();
        }

        request.setAttribute("reviewList", reviewList);

        request.getRequestDispatcher("reviews.jsp")
                .forward(request, response);
    }
}
