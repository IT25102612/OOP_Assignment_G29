package com.tranquility.structures;

import com.tranquility.models.Room;
import java.util.ArrayList;
import java.util.List;

public class RoomBST {

    private static class Node {
        Room room;
        Node left;
        Node right;

        Node(Room room) {
            this.room  = room;
            this.left  = null;
            this.right = null;
        }
    }

    private Node root;

    public RoomBST() {
        this.root = null;
    }

    // INSERT — adds a new room into the tree
    public void insert(Room room) {
        root = insertRec(root, room);
    }

    private Node insertRec(Node node, Room room) {
        if (node == null) return new Node(room);
        int cmp = room.getRoomId().compareTo(node.room.getRoomId());
        if      (cmp < 0) node.left  = insertRec(node.left,  room);
        else if (cmp > 0) node.right = insertRec(node.right, room);
        else              node.room  = room; // same ID = replace
        return node;
    }

    // SEARCH — find one room by its ID
    public Room search(String roomId) {
        return searchRec(root, roomId);
    }

    private Room searchRec(Node node, String roomId) {
        if (node == null) return null;
        int cmp = roomId.compareTo(node.room.getRoomId());
        if (cmp == 0) return node.room;
        if (cmp < 0)  return searchRec(node.left,  roomId);
        return             searchRec(node.right, roomId);
    }

    // IN-ORDER — returns ALL rooms sorted by roomId
    public List<Room> inOrder() {
        List<Room> result = new ArrayList<>();
        inOrderRec(root, result);
        return result;
    }

    private void inOrderRec(Node node, List<Room> result) {
        if (node == null) return;
        inOrderRec(node.left,  result);
        result.add(node.room);
        inOrderRec(node.right, result);
    }

    // GET AVAILABLE — only rooms with status "available"
    public List<Room> getAvailable() {
        List<Room> result = new ArrayList<>();
        for (Room r : inOrder()) {
            if ("available".equalsIgnoreCase(r.getStatus())) result.add(r);
        }
        return result;
    }

    // UPDATE — replace a room's data by ID
    public boolean update(Room updatedRoom) {
        Node node = findNode(root, updatedRoom.getRoomId());
        if (node == null) return false;
        node.room = updatedRoom;
        return true;
    }

    private Node findNode(Node node, String roomId) {
        if (node == null) return null;
        int cmp = roomId.compareTo(node.room.getRoomId());
        if (cmp == 0) return node;
        if (cmp < 0)  return findNode(node.left,  roomId);
        return             findNode(node.right, roomId);
    }

    // DELETE — remove a room from the tree (3 cases)
    public boolean delete(String roomId) {
        if (search(roomId) == null) return false;
        root = deleteRec(root, roomId);
        return true;
    }

    private Node deleteRec(Node node, String roomId) {
        if (node == null) return null;

        int cmp = roomId.compareTo(node.room.getRoomId());

        if (cmp < 0) {
            node.left = deleteRec(node.left, roomId);
        } else if (cmp > 0) {
            node.right = deleteRec(node.right, roomId);
        } else {
            // Found it — 3 cases:
            if (node.left == null && node.right == null) return null;      // Case 1: leaf
            if (node.left  == null) return node.right;                     // Case 2a: only right child
            if (node.right == null) return node.left;                      // Case 2b: only left child

            // Case 3: two children — find in-order successor
            Node successor = findMin(node.right);
            node.room  = successor.room;
            node.right = deleteRec(node.right, successor.room.getRoomId());
        }
        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    public int     size()    { return sizeRec(root); }
    private int    sizeRec(Node n) { return n == null ? 0 : 1 + sizeRec(n.left) + sizeRec(n.right); }
    public boolean isEmpty() { return root == null; }
}