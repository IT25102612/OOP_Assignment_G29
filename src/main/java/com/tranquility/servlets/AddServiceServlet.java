package com.tranquility.servlets;

import com.tranquility.dao.ServiceDAO;
import com.tranquility.models.Service;
import com.tranquility.utils.JsonHelper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * AddServiceServlet.java
 * URL:    POST /AddServiceServlet
 * Access: Admin only
 *
 * Called from: any admin page that manages services
 *
 * Parameters received from frontend:
 *   name     (String)  — e.g. "Breakfast"
 *   price    (double)  — e.g. 35.00
 *   category (String)  — e.g. "Meals"
 *
 * Response sent back to frontend:
 *   SUCCESS: { "success": true,
 *              "message": "Service added.",
 *              "serviceId": "SVC008" }
 *   FAILURE: { "success": false,
 *              "message": "Missing required fields." }
 */
@WebServlet("/AddServiceServlet")
public class AddServiceServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res)
            throws IOException {

        try {
            // 1. Read parameters from the frontend request
            String name     = req.getParameter("name");
            String priceStr = req.getParameter("price");
            String category = req.getParameter("category");

            // 2. Validate — make sure nothing is empty
            if (name == null     || name.trim().isEmpty() ||
                priceStr == null || priceStr.trim().isEmpty() ||
                category == null || category.trim().isEmpty()) {

                JsonHelper.err(res, "Missing required fields.");
                return;
            }

            // 3. Parse price to a number
            double price;
            try {
                price = Double.parseDouble(priceStr.trim());
            } catch (NumberFormatException e) {
                JsonHelper.err(res, "Price must be a number.");
                return;
            }

            // 4. Generate the next service ID
            ServiceDAO dao       = new ServiceDAO();
            String     serviceId = dao.generateServiceId();

            // 5. Build the Service object
            Service newService = new Service(
                serviceId,
                name.trim(),
                price,
                category.trim(),
                true  // new services are available by default
            );

            // 6. Save to database
            dao.addService(newService);

            // 7. Send success response back to frontend
            JsonHelper.send(res,
                "{\"success\":true,"
              + "\"message\":\"Service added.\","
              + "\"serviceId\":\"" + serviceId + "\"}");

        } catch (Exception e) {
            JsonHelper.err(res, "Server error: " + e.getMessage());
        }
    }
}