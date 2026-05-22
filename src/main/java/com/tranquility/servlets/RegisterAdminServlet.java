package com.tranquility.servlets;

import com.tranquility.dao.AdminDAO;
import com.tranquility.models.Admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/registerAdmin")
public class RegisterAdminServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Admin admin = new Admin(fullName, email, username, password);

        AdminDAO dao = new AdminDAO();

        if (dao.registerAdmin(admin)) {
            response.sendRedirect("admin-main.html");
        } else {
            response.getWriter().println("Admin Registration Failed");
        }
    }
}