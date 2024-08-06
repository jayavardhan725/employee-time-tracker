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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb2", "root", "Jaya@123");
            String sql = "";
            if ("admin".equals(role)) {
                sql = "SELECT * FROM Admin WHERE username=? AND password=?";
            } else if ("associate".equals(role)) {
                sql = "SELECT * FROM Employee WHERE username=? AND password=?";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                if ("admin".equals(role)) {
                    request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
                } else if ("associate".equals(role)) {
                    int employeeId = rs.getInt("id");
                    request.getSession().setAttribute("employeeId", employeeId);
                    request.getSession().setAttribute("username", username);
                    response.sendRedirect("showHours.jsp");
                }
            } else {
                response.getWriter().println("Invalid Username or Password");
            }
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}