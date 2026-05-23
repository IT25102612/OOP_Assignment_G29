package com.tranquility.servlets;

import com.tranquility.dao.RoomDAO;
import com.tranquility.models.Room;
import com.tranquility.utils.JsonHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/AddRoomServlet")
public class AddRoomServlet extends HttpServlet {

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

        String roomId      = request.getParameter("roomId");
        String suiteName   = request.getParameter("suiteName");
        String priceStr    = request.getParameter("pricePerNight");
        String bedsStr     = request.getParameter("beds");
        String bathsStr    = request.getParameter("baths");
        String bedType     = request.getParameter("bedType");
        String status      = request.getParameter("status");
        String building    = request.getParameter("building");
        String floorStr    = request.getParameter("floor");
        String roomNumStr  = request.getParameter("roomNumber");
        String country     = request.getParameter("country");
        String city        = request.getParameter("city");
        String balcony     = request.getParameter("balcony");
        String privatePool = request.getParameter("privatePool");
        String jointRooms  = request.getParameter("jointRooms");

        if (isEmpty(roomId) || isEmpty(suiteName) || isEmpty(priceStr) ||
                isEmpty(bedsStr) || isEmpty(bathsStr)  || isEmpty(country)  ||
                isEmpty(city)    || isEmpty(building)  || isEmpty(floorStr) || isEmpty(roomNumStr)) {
            JsonHelper.err(response, "Missing required fields.");
            return;
        }

        double pricePerNight;
        int beds, baths, floor, roomNum;
        try {
            pricePerNight = Double.parseDouble(priceStr);
            beds          = Integer.parseInt(bedsStr);
            baths         = Integer.parseInt(bathsStr);
            floor         = Integer.parseInt(floorStr);
            roomNum       = Integer.parseInt(roomNumStr);
        } catch (NumberFormatException e) {
            JsonHelper.err(response, "Invalid number value in one of the fields.");
            return;
        }

        if (isEmpty(bedType))     bedType     = "Standard";
        if (isEmpty(status))      status      = "available";
        if (isEmpty(balcony))     balcony     = "no";
        if (isEmpty(privatePool)) privatePool = "no";
        if (isEmpty(jointRooms))  jointRooms  = "no";

        Room room = new Room(roomId, suiteName, pricePerNight, beds, baths, bedType, status,
                building, floor, roomNum, country, city,
                balcony, privatePool, jointRooms);
        try {
            roomDAO.addRoom(room);
            JsonHelper.ok(response, "Room " + roomId + " added successfully.");
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                JsonHelper.err(response, "Room ID " + roomId + " already exists.");
            } else {
                JsonHelper.err(response, "Database error: " + e.getMessage());
            }
        }
    }

    private boolean isEmpty(String s) { return s == null || s.trim().isEmpty(); }
}