<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.temporal.WeekFields" %>
<%
    Integer employeeId = (Integer) session.getAttribute("employeeId");
    String username = (String) session.getAttribute("username");

    if (employeeId == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    Map<String, Integer> timeSpentMap = new HashMap<>();
    Map<String, Integer> monthlyTimeMap = new HashMap<>();
    Map<String, Integer> weeklyTimeMap = new HashMap<>();

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb2", "root", "Wj28@krhps");

        String sql = "SELECT task_category, start_time, end_time, TIMESTAMPDIFF(MINUTE, start_time, end_time) AS duration FROM Task WHERE employee_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, employeeId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String taskCategory = rs.getString("task_category");
            int duration = rs.getInt("duration");
            LocalDate startDate = rs.getTimestamp("start_time").toLocalDateTime().toLocalDate();
            String monthKey = startDate.getYear() + "-" + startDate.getMonthValue();
            String weekKey = startDate.getYear() + "-" + startDate.get(WeekFields.ISO.weekOfYear());

            timeSpentMap.put(taskCategory, timeSpentMap.getOrDefault(taskCategory, 0) + duration);
            monthlyTimeMap.put(monthKey, monthlyTimeMap.getOrDefault(monthKey, 0) + duration);
            weeklyTimeMap.put(weekKey, weeklyTimeMap.getOrDefault(weekKey, 0) + duration);
        }

        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

    request.setAttribute("timeSpentMap", timeSpentMap);
    request.setAttribute("monthlyTimeMap", monthlyTimeMap);
    request.setAttribute("weeklyTimeMap", weeklyTimeMap);
%>
<!DOCTYPE html>
<html>
<head>
    <title>Employee Working Hours</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <h1>Welcome, <%= username %></h1>
    <h2>Employee Working Hours</h2>
    <canvas id="timeSpentChart"></canvas>
    <canvas id="monthlyTimeChart"></canvas>
    <canvas id="weeklyTimeChart"></canvas>
    <script>
        const pieLabels = [];
        const pieData = [];
        <%
            for (Map.Entry<String, Integer> entry : timeSpentMap.entrySet()) {
                String task = entry.getKey();
                Integer hours = entry.getValue();
        %>
                pieLabels.push("<%= task %>");
                pieData.push(<%= hours %>);
        <%
            }
        %>

        const monthlyLabels = [];
        const monthlyData = [];
        <%
            for (Map.Entry<String, Integer> entry : monthlyTimeMap.entrySet()) {
                String month = entry.getKey();
                Integer hours = entry.getValue();
        %>
                monthlyLabels.push("<%= month %>");
                monthlyData.push(<%= hours %>);
        <%
            }
        %>

        const weeklyLabels = [];
        const weeklyData = [];
        <%
            for (Map.Entry<String, Integer> entry : weeklyTimeMap.entrySet()) {
                String week = entry.getKey();
                Integer hours = entry.getValue();
        %>
                weeklyLabels.push("<%= week %>");
                weeklyData.push(<%= hours %>);
        <%
            }
        %>

        const ctxPie = document.getElementById('timeSpentChart').getContext('2d');
        new Chart(ctxPie, {
            type: 'pie',
            data: {
                labels: pieLabels,
                datasets: [{
                    data: pieData,
                    backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0']
                }]
            }
        });

        const ctxMonthly = document.getElementById('monthlyTimeChart').getContext('2d');
        new Chart(ctxMonthly, {
            type: 'bar',
            data: {
                labels: monthlyLabels,
                datasets: [{
                    label: 'Monthly Hours',
                    data: monthlyData,
                    backgroundColor: '#36A2EB'
                }]
            }
        });

        const ctxWeekly = document.getElementById('weeklyTimeChart').getContext('2d');
        new Chart(ctxWeekly, {
            type: 'bar',
            data: {
                labels: weeklyLabels,
                datasets: [{
                    label: 'Weekly Hours',
                    data: weeklyData,
                    backgroundColor: '#FFCE56'
                }]
            }
        });
    </script>
</body>
</html>
