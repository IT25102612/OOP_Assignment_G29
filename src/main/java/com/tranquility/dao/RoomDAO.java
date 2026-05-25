package com.tranquility.dao;


import com.tranquility.models.Room;

import com.tranquility.structures.RoomBST;
import com.tranquility.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    private static final RoomBST bst = new RoomBST();

    // Called once at startup — loads all rooms from MySQL into the BST
    public void loadAllRoomsIntoBST() throws SQLException {
        String sql = "SELECT * FROM rooms";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) bst.insert(mapRow(rs));
        }
    }

    // CREATE — add new room to MySQL + BST
    public void addRoom(Room room) throws SQLException {
        String sql = "INSERT INTO rooms "
                + "(room_id, suite_name, price_per_night, beds, baths, bed_type, status, "
                + " building, floor, room_number, country, city, balcony, private_pool, joint_rooms) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,  room.getRoomId());
            ps.setString(2,  room.getSuiteName());
            ps.setDouble(3,  room.getPricePerNight());
            ps.setInt(4,     room.getBeds());
            ps.setInt(5,     room.getBaths());
            ps.setString(6,  room.getBedType());
            ps.setString(7,  room.getStatus());
            ps.setString(8,  room.getBuilding());
            ps.setInt(9,     room.getFloor());
            ps.setInt(10,    room.getRoomNumber());
            ps.setString(11, room.getCountry());
            ps.setString(12, room.getCity());
            ps.setString(13, room.getBalcony());
            ps.setString(14, room.getPrivatePool());
            ps.setString(15, room.getJointRooms());
            ps.executeUpdate();
        }
        bst.insert(room);
    }

    // READ — get one room by ID (uses BST, no DB query needed)
    public Room getRoomById(String roomId) {
        return bst.search(roomId);
    }

    // READ — get all rooms sorted by roomId
    public List<Room> getAllRoomsSorted() {
        return bst.inOrder();
    }

    // READ — get only available rooms
    public List<Room> getAvailableRooms() {
        return bst.getAvailable();
    }

    // READ — filtered search from available-rooms.html
    public List<Room> getFilteredRooms(String country, String city,
                                        String beds, String baths,
                                        String balcony, String pool,
                                        String joint, String suiteType,
                                        String checkin, String checkout) throws SQLException {
        List<Room> result   = new ArrayList<>();
        List<String> booked = new ArrayList<>();

        if (notEmpty(checkin) && notEmpty(checkout)) {
            booked = getBookedRoomIds(checkin, checkout);
        }

        for (Room r : bst.getAvailable()) {
            if (notEmpty(country)   && !country.equalsIgnoreCase(r.getCountry()))    continue;
            if (notEmpty(city)      && !city.equalsIgnoreCase(r.getCity()))          continue;
            if (notEmpty(beds)      && Integer.parseInt(beds)  != r.getBeds())       continue;
            if (notEmpty(baths)     && Integer.parseInt(baths) != r.getBaths())      continue;
            if (notEmpty(balcony)   && !balcony.equalsIgnoreCase(r.getBalcony()))    continue;
            if (notEmpty(pool)      && !pool.equalsIgnoreCase(r.getPrivatePool()))   continue;
            if (notEmpty(joint)     && !joint.equalsIgnoreCase(r.getJointRooms()))   continue;
            if (notEmpty(suiteType) && !suiteType.equalsIgnoreCase(r.getSuiteName()))continue;
            if (booked.contains(r.getRoomId()))                                       continue;
            result.add(r);
        }
        return result;
    }

    private List<String> getBookedRoomIds(String checkin, String checkout) throws SQLException {
        List<String> booked = new ArrayList<>();
        String sql = "SELECT DISTINCT room_id FROM reservations "
                + "WHERE status NOT IN ('cancelled') "
                + "AND checkin_date < ? AND checkout_date > ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, checkout);
            ps.setString(2, checkin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) booked.add(rs.getString("room_id"));
        }
        return booked;
    }

    // UPDATE — change room details in MySQL + BST
    public boolean updateRoom(String roomId, String suiteName, String pricePerNight,
                              String beds, String baths, String bedType,
                              String status, String balcony, String pool, String joint)
            throws SQLException {

        Room existing = bst.search(roomId);
        if (existing == null) return false;

        if (notEmpty(suiteName))     existing.setSuiteName(suiteName);
        if (notEmpty(pricePerNight)) existing.setPricePerNight(Double.parseDouble(pricePerNight));
        if (notEmpty(beds))          existing.setBeds(Integer.parseInt(beds));
        if (notEmpty(baths))         existing.setBaths(Integer.parseInt(baths));
        if (notEmpty(bedType))       existing.setBedType(bedType);
        if (notEmpty(status))        existing.setStatus(status);
        if (notEmpty(balcony))       existing.setBalcony(balcony);
        if (notEmpty(pool))          existing.setPrivatePool(pool);
        if (notEmpty(joint))         existing.setJointRooms(joint);

        String sql = "UPDATE rooms SET "
                + "suite_name=?, price_per_night=?, beds=?, baths=?, bed_type=?, status=?, "
                + "balcony=?, private_pool=?, joint_rooms=? WHERE room_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,  existing.getSuiteName());
            ps.setDouble(2,  existing.getPricePerNight());
            ps.setInt(3,     existing.getBeds());
            ps.setInt(4,     existing.getBaths());
            ps.setString(5,  existing.getBedType());
            ps.setString(6,  existing.getStatus());
            ps.setString(7,  existing.getBalcony());
            ps.setString(8,  existing.getPrivatePool());
            ps.setString(9,  existing.getJointRooms());
            ps.setString(10, roomId);
            ps.executeUpdate();
        }
        bst.update(existing);
        return true;
    }

    // DELETE — remove from MySQL + BST
    public boolean deleteRoom(String roomId) throws SQLException {
        if (bst.search(roomId) == null) return false;
        String sql = "DELETE FROM rooms WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomId);
            ps.executeUpdate();
        }
        bst.delete(roomId);
        return true;
    }

    // Map a MySQL row to a Room object
    private Room mapRow(ResultSet rs) throws SQLException {
        Room r = new Room();
        r.setRoomId(       rs.getString("room_id"));
        r.setSuiteName(    rs.getString("suite_name"));
        r.setPricePerNight(rs.getDouble("price_per_night"));
        r.setBeds(         rs.getInt("beds"));
        r.setBaths(        rs.getInt("baths"));
        r.setBedType(      rs.getString("bed_type"));
        r.setStatus(       rs.getString("status"));
        r.setBuilding(     rs.getString("building"));
        r.setFloor(        rs.getInt("floor"));
        r.setRoomNumber(   rs.getInt("room_number"));
        r.setCountry(      rs.getString("country"));
        r.setCity(         rs.getString("city"));
        r.setBalcony(      rs.getString("balcony"));
        r.setPrivatePool(  rs.getString("private_pool"));
        r.setJointRooms(   rs.getString("joint_rooms"));
        return r;
    }

    private boolean notEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
