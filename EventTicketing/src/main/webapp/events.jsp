<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.sql.Connection, java.sql.Statement, java.sql.ResultSet" %>
<%@ page import="util.DBConnection" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html>
<head>
    <title>Events</title>

    <style>
        body {
            font-family: 'Poppins', sans-serif;
            margin: 0;
            background: linear-gradient(135deg, #1e3c72, #2a5298);
            color: white;
        }

        .header {
            text-align: center;
            padding: 20px;
            font-size: 26px;
            font-weight: bold;
        }

        .container {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 25px;
            padding: 20px;
        }

        .card {
            background: white;
            color: #333;
            width: 280px;
            padding: 20px;
            border-radius: 15px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
            transition: 0.3s;
        }

        .card:hover {
            transform: translateY(-10px);
        }

        .event-name {
            font-size: 20px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .info {
            margin: 5px 0;
        }

        .price {
            font-size: 18px;
            color: #2a5298;
            font-weight: bold;
            margin-top: 10px;
        }

        input[type="number"] {
            width: 60px;
            padding: 5px;
            margin-top: 10px;
        }

        input[type="submit"] {
            display: block;
            width: 100%;
            margin-top: 10px;
            padding: 10px;
            border: none;
            border-radius: 8px;
            background: linear-gradient(135deg, #ff416c, #ff4b2b);
            color: white;
            font-weight: bold;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            opacity: 0.9;
        }
    </style>
</head>

<body>

<div class="header">
    🎟 Welcome, <%= session.getAttribute("user_name") %>
</div>

<div class="container">

<%
try {
    Connection conn = DBConnection.getConnection();
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM events");

    while(rs.next()) {
%>

    <div class="card">
        <div class="event-name"><%= rs.getString("name") %></div>

        <div class="info">📅 Date: <%= rs.getDate("date") %></div>
        <div class="info">📍 Venue: <%= rs.getString("venue") %></div>

        <div class="price">₹<%= rs.getDouble("price") %></div>

        <form action="TicketBookingServlet" method="post">
            <input type="hidden" name="user_id" value="<%= session.getAttribute("user_id") %>"/>
            <input type="hidden" name="event_id" value="<%= rs.getInt("id") %>"/>

            Tickets:
            <input type="number" name="quantity" value="1" min="1">

            <input type="submit" value="Book Now">
        </form>
    </div>

<%
    }

    conn.close();

} catch(Exception e) {
    out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
}
%>

</div>

</body>
</html>