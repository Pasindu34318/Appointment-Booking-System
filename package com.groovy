package com.appointments;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class AppointmentServlet extends HttpServlet {

    // Handle appointment form submission
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String date = request.getParameter("date");
        String time = request.getParameter("time");

        // Insert the appointment into the database
        try (Connection conn = DBHelper.getConnection()) {
            String sql = "INSERT INTO appointments (name, email, date, time) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, date);
                stmt.setString(4, time);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Redirect to appointments page
        response.sendRedirect("appointments");
    }

    // Display all appointments
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection conn = DBHelper.getConnection()) {
            String sql = "SELECT * FROM appointments ORDER BY date, time";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                request.setAttribute("appointments", rs);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/appointments.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
