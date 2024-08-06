package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/assignTask")
public class AssignTaskServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String employeeUsername = request.getParameter("employeeUsername");
        String project = request.getParameter("project");
        String date = request.getParameter("date");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String taskCategory = request.getParameter("taskCategory");
        String description = request.getParameter("description");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb2", "root", "Jaya@123");

            // Find the employee ID based on the username
            String findEmployeeIdSql = "SELECT id FROM Employee WHERE username=?";
            PreparedStatement findEmployeeIdStmt = conn.prepareStatement(findEmployeeIdSql);
            findEmployeeIdStmt.setString(1, employeeUsername);
            ResultSet rs = findEmployeeIdStmt.executeQuery();

            if (rs.next()) {
                int employeeId = rs.getInt("id");

                // Insert the task into the Task table
                String insertTaskSql = "INSERT INTO Task (employee_id, project, date, start_time, end_time, task_category, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertTaskStmt = conn.prepareStatement(insertTaskSql);
                insertTaskStmt.setInt(1, employeeId);
                insertTaskStmt.setString(2, project);
                insertTaskStmt.setString(3, date);
                insertTaskStmt.setString(4, startTime);
                insertTaskStmt.setString(5, endTime);
                insertTaskStmt.setString(6, taskCategory);
                insertTaskStmt.setString(7, description);

                insertTaskStmt.executeUpdate();
                response.getWriter().println("Task Assigned Successfully");
            } else {
                response.getWriter().println("Employee not found");
            }

            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}