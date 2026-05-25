// Component 01 - Room Inventory Management - IT25102616
package com.tranquility.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonHelper {

    public static void send(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(json);
    }

    public static void ok(HttpServletResponse response, String message) throws IOException {
        send(response, "{\"success\":true,\"message\":\"" + escape(message) + "\"}");
    }

    public static void err(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("{\"success\":false,\"message\":\"" + escape(message) + "\"}");
    }

    public static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
