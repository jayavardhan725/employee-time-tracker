<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.GetTasksServlet.Task" %>
<!DOCTYPE html>
<html>
<head>
    <title>Employee Dashboard</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <h1>Employee Dashboard</h1>
    <h2>Assigned Tasks</h2>
    <ul>
        <%
            ArrayList<Task> tasks = (ArrayList<Task>) request.getAttribute("tasks");
            if (tasks != null && !tasks.isEmpty()) {
                for (Task task : tasks) {
        %>
                    <li>
                        Project: <%= task.getProject() %> <br>
                        Date: <%= task.getDate() %> <br>
                        Start Time: <%= task.getStartTime() %> <br>
                        End Time: <%= task.getEndTime() %> <br>
                        Task Category: <%= task.getTaskCategory() %> <br>
                        Description: <%= task.getDescription() %>
                    </li>
        <%
                }
            } else {
        %>
                <li>No tasks assigned.</li>
        <%
            }
        %>
    </ul>
</body>
</html>