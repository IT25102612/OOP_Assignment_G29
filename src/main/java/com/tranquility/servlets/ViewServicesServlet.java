package com.tranquility.servlets;

import com.tranquility.dao.ServiceDAO;
import com.tranquility.models.Service;
import com.tranquility.utils.JsonHelper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * ViewServicesServlet.java
 * URL:    GET /ViewServicesServlet
 * Access: Public (guests and admin)
 *
 * Called from:
 *   meal-options.html → to load meal cards dynamically
 *   admin pages       → to see all services
 *   booking-confirmation.html → to show linked services
 *
 * Parameters (all optional):
 *   category      → filter by category e.g. "Meals"
 *   reservationId → get services linked to a specific booking
 *   (none)        → return all available services
 *
 * Example calls from your JavaScript:
 *   fetch('/ViewServicesServlet?category=Meals')
 *   fetch('/ViewServicesServlet?reservationId=RES001')
 *   fetch('/ViewServicesServlet')
 *
 * Response:
 *   {
 *     "success": true,
 *     "services": [
 *       { "serviceId": "SVC001", "name": "Breakfast",
 *         "price": 35.0, "category": "Meals",
 *         "available": true },
 *       ...
 *     ]
 *   }
 */
@WebServlet("/ViewServicesServlet")
public class ViewServicesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res)
            throws IOException {

        try {
            ServiceDAO dao           = new ServiceDAO();
            String     category      = req.getParameter("category");
            String     reservationId = req.getParameter("reservationId");

            List<Service> services;

            // Decide which query to run based on parameters
            if (reservationId != null &&
                !reservationId.trim().isEmpty()) {

                // Guest wants services on their specific booking
                services = dao.getServicesByReservation(
                    reservationId.trim());

            } else if (category != null &&
                       !category.trim().isEmpty()) {

                // Filter by category e.g. Meals, Wellness
                services = dao.getByCategory(category.trim());

            } else {

                // No filter — return everything available
                services = dao.getAllServices();
            }

            // Build JSON array of services
            StringBuilder sb = new StringBuilder();
            sb.append("{\"success\":true,\"services\":[");

            for (int i = 0; i < services.size(); i++) {
                sb.append(services.get(i).toJson());
                if (i < services.size() - 1) sb.append(",");
            }

            sb.append("]}");
            JsonHelper.send(res, sb.toString());

        } catch (Exception e) {
            JsonHelper.err(res, "Server error: " + e.getMessage());
        }
    }
}