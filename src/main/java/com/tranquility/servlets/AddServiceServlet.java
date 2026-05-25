package com.tranquility.servlets;

import com.tranquility.dao.ServiceDAO;
import com.tranquility.models.Service;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/AddServiceServlet")
public class AddServiceServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException {

        try {

            String name     = req.getParameter("name");
            String priceStr = req.getParameter("price");
            String category = req.getParameter("category");

            if (name == null     || name.trim().isEmpty() ||
                priceStr == null || priceStr.trim().isEmpty() ||
                category == null || category.trim().isEmpty()) {
                JsonHelper.err(res,"Missing required fields: name, price, category.");
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr.trim());
                if (price < 0) {
                    JsonHelper.err(res, "Price cannot be negative.");
                    return;
                }
            } catch (NumberFormatException e) {
                
                JsonHelper.err(res, "Price must be a valid number.");
                return;
            }

            ServiceDAO dao = new ServiceDAO();
            String serviceId = dao.generateServiceId();

            Service newService = new Service(
                serviceId,
                name.trim(),
                price,
                category.trim(),
                true 
            );

            dao.addService(newService);

            System.out.println("Added: " + newService);

            JsonHelper.send(res,"{\"success\":true," + "\"message\":\"Service added.\"," + "\"serviceId\":\"" + serviceId + "\"}");

        } catch (SQLException e) {
            e.printStackTrace();
            JsonHelper.err(res,"Database error: " + e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            JsonHelper.err(res,"Server error: " + e.getMessage());
        }
    }
}