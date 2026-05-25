package com.tranquility.servlets;

import com.tranquility.dao.ServiceDAO;
import com.tranquility.utils.JsonHelper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/LinkServiceServlet")
public class LinkServiceServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        try {
            String reservationId = req.getParameter("reservationId");
            String serviceId     = req.getParameter("serviceId");

            if (reservationId == null ||
                reservationId.trim().isEmpty() ||
                serviceId == null ||
                serviceId.trim().isEmpty()) {

                JsonHelper.err(res, "reservationId and serviceId are required.");
                return;
            }

            ServiceDAO dao = new ServiceDAO();
            boolean linked = dao.linkServiceToReservation(reservationId.trim(), serviceId.trim());

            if (linked) {
                JsonHelper.ok(res, "Service linked to reservation.");
            } else {
                JsonHelper.err(res, "Service already linked to this reservation.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JsonHelper.err(res, "Database error: " + e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            JsonHelper.err(res, "Server error: " + e.getMessage());
        }
    }
}