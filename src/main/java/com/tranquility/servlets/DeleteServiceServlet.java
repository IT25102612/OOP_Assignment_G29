package com.tranquility.servlets;

import com.tranquility.dao.ServiceDAO;
import com.tranquility.utils.JsonHelper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * DeleteServiceServlet.java
 * URL:    POST /DeleteServiceServlet
 * Access: Admin only
 *
 * Called from: admin service management page
 *
 * Parameters:
 *   serviceId — the ID of the service to delete e.g. "SVC003"
 *
 * Response:
 *   { "success": true,  "message": "Service deleted." }
 *   { "success": false, "message": "Service not found." }
 *
 * NOTE: This permanently deletes the service from the database.
 * If you only want to hide it from guests, use UpdateServiceServlet
 * with available=false instead.
 */
@WebServlet("/DeleteServiceServlet")
public class DeleteServiceServlet extends HttpServlet {

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

            ServiceDAO dao     = new ServiceDAO();
            boolean    deleted = dao.deleteService(serviceId.trim());

            if (deleted) {
                JsonHelper.ok(res, "Service deleted.");
            } else {
                JsonHelper.err(res, "Service not found.");
            }

        } catch (Exception e) {
            JsonHelper.err(res, "Server error: " + e.getMessage());
        }
    }
}