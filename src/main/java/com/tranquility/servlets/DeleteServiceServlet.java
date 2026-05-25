package com.tranquility.servlets;

import com.tranquility.dao.ServiceDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/DeleteServiceServlet")
public class DeleteServiceServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        try {
            String serviceId = req.getParameter("serviceId");

            if (serviceId == null || serviceId.trim().isEmpty()) {
                JsonHelper.err(res, "serviceId is required.");
                return;
            }

            ServiceDAO dao = new ServiceDAO();
            boolean deleted = dao.deleteService(serviceId.trim());

            if (deleted) {
                JsonHelper.ok(res, "Service deleted.");
            } else {
                JsonHelper.err(res, "Service not found.");
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