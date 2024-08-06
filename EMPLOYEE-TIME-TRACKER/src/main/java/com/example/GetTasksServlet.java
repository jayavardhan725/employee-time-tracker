package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/getTasks")
public class GetTasksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer employeeId = (Integer) request.getSession().getAttribute("employeeId");

        if (employeeId == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb2", "root", "Jaya@123");

            String sql = "SELECT * FROM Task WHERE employee_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();

            ArrayList<Task> tasks = new ArrayList<>();
            while (rs.next()) {
                Task task = new Task(
                    rs.getString("project"),
                    rs.getDate("date"),
                    rs.getTime("start_time"),
                    rs.getTime("end_time"),
                    rs.getString("task_category"),
                    rs.getString("description")
                );
                tasks.add(task);
            }

            request.setAttribute("tasks", tasks);
            request.getRequestDispatcher("employeeDashboard.jsp").forward(request, response);
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static class Task {
        private String project;
        private java.sql.Date date;
        private java.sql.Time startTime;
        private java.sql.Time endTime;
        private String taskCategory;
        private String description;

        public Task(String project, java.sql.Date date, java.sql.Time startTime, java.sql.Time endTime, String taskCategory, String description) {
            this.project = project;
            this.date = date;
            this.startTime = startTime;
            this.endTime = endTime;
            this.taskCategory = taskCategory;
            this.description = description;
        }

        public String getProject() {
            return project;
        }

        public java.sql.Date getDate() {
            return date;
        }

        public java.sql.Time getStartTime() {
            return startTime;
        }

        public java.sql.Time getEndTime() {
            return endTime;
        }

        public String getTaskCategory() {
            return taskCategory;
        }

        public String getDescription() {
            return description;
        }
    }
}