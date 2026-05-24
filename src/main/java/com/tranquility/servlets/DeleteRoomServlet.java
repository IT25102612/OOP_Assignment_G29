// Component 01 - Room Inventory Management - IT25102616
package com.tranquility.servlets;

import com.tranquility.dao.RoomDAO;
import com.tranquility.utils.JsonHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/DeleteRoomServlet")
public class DeleteRoomServlet extends HttpServlet {

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

        try {
            boolean deleted = roomDAO.deleteRoom(roomId.trim());
            if (deleted) {
                JsonHelper.ok(response, "Room " + roomId + " deleted successfully.");
            } else {
                JsonHelper.err(response, "Room " + roomId + " not found.");
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("foreign key constraint")) {
                JsonHelper.err(response, "Cannot delete — this room has existing reservations. Cancel them first.");
            } else {
                JsonHelper.err(response, "Database error: " + e.getMessage());
            }
        }
    }
}