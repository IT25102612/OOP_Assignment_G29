package com.tranquility.servlets;

import com.tranquility.dao.ServiceDAO;
import com.tranquility.models.Service;
import com.tranquility.utils.JsonHelper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * UpdateServiceServlet.java
 * URL:    POST /UpdateServiceServlet
 * Access: Admin only
 *
 * Called from: admin service management page
 *
 * Parameters:
 *   serviceId  (required) — which service to update
 *   name       (optional) — new name
 *   price      (optional) — new price
 *   category   (optional) — new category
 *   available  (optional) — "true" or "false"
 *
 * Response:
 *   { "success": true,  "message": "Service updated." }
 *   { "success": false, "message": "Service not found." }
 */
@WebServlet("/UpdateServiceServlet")
public class UpdateServiceServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res)
            throws IOException {

        try {
            String serviceId = req.getParameter("serviceId");

            if (serviceId == null || serviceId.trim().isEmpty()) {
                JsonHelper.err(res, "serviceId is required.");
                return;
            }

            // Load the existing service first
            ServiceDAO dao   = new ServiceDAO();
            Service    found = dao.getByServiceId(serviceId.trim());

            if (found == null) {
                JsonHelper.err(res, "Service not found.");
                return;
            }

            // Only update fields that were actually sent by frontend
            String s;

            if ((s = req.getParameter("name")) != null
                && !s.trim().isEmpty()) {
                found.setName(s.trim());
            }

            if ((s = req.getParameter("price")) != null
                && !s.trim().isEmpty()) {
                try {
                    found.setPrice(Double.parseDouble(s.trim()));
                } catch (NumberFormatException e) {
                    JsonHelper.err(res, "Price must be a number.");
                    return;
                }
            }

            if ((s = req.getParameter("category")) != null
                && !s.trim().isEmpty()) {
                found.setCategory(s.trim());
            }

            if ((s = req.getParameter("available")) != null
                && !s.trim().isEmpty()) {
                found.setAvailable(Boolean.parseBoolean(s.trim()));
            }

            // Save changes to database
            dao.updateService(found);
            JsonHelper.ok(res, "Service updated.");

        } catch (Exception e) {
            JsonHelper.err(res, "Server error: " + e.getMessage());
        }
    }
}