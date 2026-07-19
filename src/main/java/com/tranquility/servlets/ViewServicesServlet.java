package com.tranquility.servlets;

import com.tranquility.dao.ServiceDAO;
import com.tranquility.models.Service;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.tranquility.utils.JsonHelper;

@WebServlet("/ViewServicesServlet")
public class ViewServicesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException {

        try {

            ServiceDAO dao = new ServiceDAO();
            String category = req.getParameter("category");
            String reservationId = req.getParameter("reservationId");

            List<Service> services;

            if (reservationId != null && !reservationId.trim().isEmpty()) {
                services = dao.getServicesByReservation(reservationId.trim());
            } else if (category != null && !category.trim().isEmpty()) {
                services = dao.getByCategory(category.trim());
            } else {
                services = dao.getAllServices();
            }

            if (services.isEmpty()) {
                JsonHelper.send(res, "{\"success\":true,\"services\":[]}");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("{\"success\":true,\"services\":[");

            for (int i = 0; i < services.size(); i++) {

                Service s = services.get(i);

                sb.append(s.toJson());

                if (i < services.size() - 1) {
                    sb.append(",");
                }
            }

            sb.append("]}");
            JsonHelper.send(res, sb.toString());

        } catch (SQLException e) {

            e.printStackTrace();
            JsonHelper.err(res, "Database error: " + e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();
            JsonHelper.err(res, "Server error: " + e.getMessage());
        }
    }
}