package com.tranquility.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonHelper {

    public static void send(HttpServletResponse res, String json) throws IOException {

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type");

        PrintWriter out = res.getWriter();
        out.print(json);
        out.flush();
    }

    public static void ok(HttpServletResponse res, String message) throws IOException {
        send(res, "{\"success\":true," + "\"message\":\"" + esc(message) + "\"}");
    }

    public static void err(HttpServletResponse res, String message) throws IOException {
        send(res, "{\"success\":false," + "\"message\":\"" + esc(message) + "\"}");
    }

    public static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
