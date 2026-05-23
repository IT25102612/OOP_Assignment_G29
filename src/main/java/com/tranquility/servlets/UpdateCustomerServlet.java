package com.tranquility.servlets;


import com.tranquility.dao.CustomerDAO;
import com.tranquility.models.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/UpdateCustomerServlet")
public class UpdateCustomerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String customerId = request.getParameter("customerId");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        CustomerDAO dao = new CustomerDAO();

        Customer oldCustomer = dao.getById(customerId);

        if (oldCustomer != null) {

            Customer updatedCustomer = new Customer(
                    customerId,
                    name,
                    oldCustomer.getNic(),
                    phone,
                    email
            );

            dao.updateCustomer(updatedCustomer);

            response.getWriter().println("Customer Updated!");

        } else {

            response.getWriter().println("Customer Not Found!");
        }
    }
}