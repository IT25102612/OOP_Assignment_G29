//package
package com.tranquility.servlets;
//handle customer data
import com.tranquility.dao.CustomerDAO;
//represent customer data
import com.tranquility.models.Customer;
//web request handling
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
//handle the errors
import java.io.IOException;

@WebServlet("/RegisterCustomerServlet")
public class RegisterCustomerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String nic = request.getParameter("nic");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        CustomerDAO dao = new CustomerDAO();

        String customerId = dao.generateCustomerId();

        Customer customer = new Customer(customerId, name, nic, phone, email);

        dao.addCustomer(customer);

        response.getWriter().println("Customer Registered Successfully!");
        response.getWriter().println("Customer ID: " + customerId);
    }
}