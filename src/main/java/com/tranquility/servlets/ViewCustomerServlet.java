package com.tranquility.servlets;

import com.tranquility.dao.CustomerDAO;
import com.tranquility.models.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/ViewCustomerServlet")
public class ViewCustomerServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String customerId = request.getParameter("customerId");

        CustomerDAO dao = new CustomerDAO();

        Customer customer = dao.getById(customerId);

        if (customer != null) {

            response.getWriter().println(customer);

        } else {

            response.getWriter().println("Customer not found");
        }
    }
}