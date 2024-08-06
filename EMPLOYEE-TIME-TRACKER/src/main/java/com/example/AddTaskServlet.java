package com.example;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/addTask")
public class AddTaskServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String project = request.getParameter("project");
        String date = request.getParameter("date");
        String timeDuration = request.getParameter("timeDuration");
        String taskCategory = request.getParameter("taskCategory");
        String description = request.getParameter("description");
        String username = (String) request.getSession().getAttribute("username");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb2", "root", "Jaya@123");

            String sql = "INSERT INTO Task (user_id, project, date, time_duration, task_category, description) VALUES ((SELECT id FROM Employee WHERE username=?), ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, project);
            ps.setString(3, date);
            ps.setString(4, timeDuration);
            ps.setString(5, taskCategory);
            ps.setString(6, description);

            ps.executeUpdate();
            response.sendRedirect("associateDashboard.jsp");
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}