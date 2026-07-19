package com.tranquility.servlets;

import com.tranquility.utils.JsonHelper;

import com.tranquility.dao.RoomDAO;
import com.tranquility.utils.JsonHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/UpdateRoomServlet")
public class UpdateRoomServlet extends HttpServlet {

    private final RoomDAO roomDAO = new RoomDAO();

    @Override
    public void init() throws ServletException {
        try {
            roomDAO.loadAllRoomsIntoBST();
        } catch (SQLException e) {
            throw new ServletException("Failed to load rooms into BST: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String roomId = request.getParameter("roomId");
        if (roomId == null || roomId.trim().isEmpty()) {
            JsonHelper.err(response, "roomId is required.");
            return;
        }

        String suiteName   = request.getParameter("suiteName");
        String priceStr    = request.getParameter("pricePerNight");
        String bedsStr     = request.getParameter("beds");
        String bathsStr    = request.getParameter("baths");
        String bedType     = request.getParameter("bedType");
        String status      = request.getParameter("status");
        String balcony     = request.getParameter("balcony");
        String privatePool = request.getParameter("privatePool");
        String jointRooms  = request.getParameter("jointRooms");

        try {
            boolean updated = roomDAO.updateRoom(
                    roomId, suiteName, priceStr, bedsStr, bathsStr,
                    bedType, status, balcony, privatePool, jointRooms
            );
            if (updated) {
                JsonHelper.ok(response, "Room " + roomId + " updated successfully.");
            } else {
                JsonHelper.err(response, "Room " + roomId + " not found.");
            }
        } catch (SQLException e) {
            JsonHelper.err(response, "Database error: " + e.getMessage());
        }
    }
}