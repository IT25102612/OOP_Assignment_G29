package com.tranquility.servlets;

import com.tranquility.dao.CustomerDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/DeleteCustomerServlet")
public class DeleteCustomerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String customerId = request.getParameter("customerId");

        CustomerDAO dao = new CustomerDAO();

        boolean deleted = dao.deleteCustomer(customerId);

        if (deleted) {

            response.getWriter().println("Customer Deleted!");

        } else {

            response.getWriter().println("Customer Not Found!");
        }
    }
}