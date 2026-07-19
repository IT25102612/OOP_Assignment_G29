package com.tranquility.servlets;

import com.tranquility.dao.AdminDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;



@WebServlet("/AdminLoginServlet")
public class AdminLoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String adminId = request.getParameter("adminId");
        String password = request.getParameter("password");

        AdminDAO dao = new AdminDAO();

        boolean isValid = dao.validateLogin(adminId, password);

        if (isValid) {

            HttpSession session = request.getSession();
            session.setAttribute("adminId", adminId);

            response.sendRedirect("admin-dashboard.jsp");

        } else {

            response.getWriter().println("Invalid Admin Login");

        }
    }
}