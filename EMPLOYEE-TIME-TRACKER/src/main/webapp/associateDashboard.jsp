<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Associate Dashboard</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <h1>Associate Dashboard</h1>
    <form action="addTask" method="post">
        <label>Project: </label><input type="text" name="project"><br>
        <label>Date: </label><input type="date" name="date"><br>
        <label>Time Duration: </label><input type="time" name="timeDuration"><br>
        <label>Task Category: </label><input type="text" name="taskCategory"><br>
        <label>Description: </label><textarea name="description"></textarea><br>
        <input type="submit" value="Add Task">
    </form>
    <h2>Your Tasks</h2>
    <ul>
        <%
            String username = (String) session.getAttribute("username");
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb2", "root", "Jaya@123");

                String sql = "SELECT * FROM Task WHERE user_id=(SELECT id FROM Employee WHERE username=?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                rs = ps.executeQuery();

                while (rs.next()) {
                    out.println("<li>");
                    out.println("Project: " + rs.getString("project") + "<br>");
                    out.println("Date: " + rs.getDate("date") + "<br>");
                    out.println("Time Duration: " + rs.getTime("time_duration") + "<br>");
                    out.println("Task Category: " + rs.getString("task_category") + "<br>");
                    out.println("Description: " + rs.getString("description"));
                    out.println("</li>");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
                if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
                if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        %>
    </ul>
</body>
</html>