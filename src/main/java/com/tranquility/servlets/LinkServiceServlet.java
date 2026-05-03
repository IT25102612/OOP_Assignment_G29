package com.tranquility.servlets;

import com.tranquility.dao.ServiceDAO;
import com.tranquility.utils.JsonHelper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * LinkServiceServlet.java
 * URL:    POST /LinkServiceServlet
 * Access: Called after a reservation is confirmed
 *
 * Called from:
 *   meal-options.html → proceedToPayment() stores the meal choice,
 *   then payment-gateway.html → processPayment() calls this servlet
 *   after the reservation is created to link the chosen services.
 *
 * Parameters:
 *   reservationId  — e.g. "RES001"
 *   serviceId      — e.g. "SVC001"
 *
 * Response:
 *   { "success": true,  "message": "Service linked." }
 *   { "success": false, "message": "Already linked." }
 */
@WebServlet("/LinkServiceServlet")
public class LinkServiceServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res)
            throws IOException {

        try {
            String reservationId = req.getParameter("reservationId");
            String serviceId     = req.getParameter("serviceId");

            if (reservationId == null ||
                reservationId.trim().isEmpty() ||
                serviceId == null ||
                serviceId.trim().isEmpty()) {

                JsonHelper.err(res,
                    "reservationId and serviceId are required.");
                return;
            }

            ServiceDAO dao = new ServiceDAO();
            boolean linked = dao.linkServiceToReservation(
                reservationId.trim(), serviceId.trim());

            if (linked) {
                JsonHelper.ok(res, "Service linked to reservation.");
            } else {
                JsonHelper.err(res,
                    "Service already linked to this reservation.");
            }

        } catch (Exception e) {
            JsonHelper.err(res, "Server error: " + e.getMessage());
        }
    }
}