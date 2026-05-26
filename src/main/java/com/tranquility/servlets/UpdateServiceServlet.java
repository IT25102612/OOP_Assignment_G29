package com.tranquility.servlets;

import com.tranquility.dao.ServiceDAO;
import com.tranquility.models.Service;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import com.tranquility.utils.JsonHelper;

@WebServlet("/UpdateServiceServlet")
public class UpdateServiceServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        try {
            String serviceId = req.getParameter("serviceId");

            if (serviceId == null || serviceId.trim().isEmpty()) {
                JsonHelper.err(res, "serviceId is required.");
                return;
            }

            ServiceDAO dao = new ServiceDAO();
            Service found = dao.getByServiceId(serviceId.trim());

            if (found == null) {
                JsonHelper.err(res, "Service not found.");
                return;
            }

            String s;

            if ((s = req.getParameter("name")) != null && !s.trim().isEmpty()) {
                found.setName(s.trim());
            }

            if ((s = req.getParameter("price")) != null && !s.trim().isEmpty()) {
                try {
                    found.setPrice(Double.parseDouble(s.trim()));
                } catch (NumberFormatException e) {
                    JsonHelper.err(res, "Price must be a number.");
                    return;
                }
            }

            if ((s = req.getParameter("category")) != null && !s.trim().isEmpty()) {
                found.setCategory(s.trim());
            }

            if ((s = req.getParameter("available")) != null && !s.trim().isEmpty()) {
                found.setAvailable(Boolean.parseBoolean(s.trim()));
            }

            dao.updateService(found);
            JsonHelper.ok(res, "Service updated.");

        } catch (SQLException e) {
            e.printStackTrace();
            JsonHelper.err(res, "Database error: " + e.getMessage());

        } catch (NumberFormatException e) {
            JsonHelper.err(res,  "Price must be a valid number.");

        } catch (Exception e) {
            e.printStackTrace();
            JsonHelper.err(res, "Server error: " + e.getMessage());
        }
    }
}