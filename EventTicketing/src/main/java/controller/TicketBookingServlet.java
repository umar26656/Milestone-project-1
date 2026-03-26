package controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

import util.DBConnection;

public class TicketBookingServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        int userId = Integer.parseInt(req.getParameter("user_id"));
        int eventId = Integer.parseInt(req.getParameter("event_id"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        try {
            Connection conn = DBConnection.getConnection();

            // 🔹 Get price
            String priceSql = "SELECT price FROM events WHERE id=?";
            PreparedStatement psPrice = conn.prepareStatement(priceSql);
            psPrice.setInt(1, eventId);
            ResultSet rs = psPrice.executeQuery();

            double totalPrice = 0;
            if(rs.next()) {
                totalPrice = rs.getDouble("price") * quantity;
            }

            // 🔹 Insert ticket
            String insertSql = "INSERT INTO tickets(user_id,event_id,quantity,total_price) VALUES(?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(insertSql);
            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            ps.setInt(3, quantity);
            ps.setDouble(4, totalPrice);

            int i = ps.executeUpdate();

            // 🔥 MODERN UI RESPONSE
            res.setContentType("text/html;charset=UTF-8");
            res.setCharacterEncoding("UTF-8");

            if(i > 0) {

                res.getWriter().println(
                    "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<title>Booking Success</title>" +

                    "<style>" +
                    "body {" +
                    " margin:0;" +
                    " font-family:Poppins, sans-serif;" +
                    " height:100vh;" +
                    " display:flex;" +
                    " justify-content:center;" +
                    " align-items:center;" +
                    " background: linear-gradient(135deg,#11998e,#38ef7d);" +
                    "}" +

                    ".card {" +
                    " background:white;" +
                    " padding:40px;" +
                    " border-radius:15px;" +
                    " text-align:center;" +
                    " box-shadow:0 10px 30px rgba(0,0,0,0.2);" +
                    "}" +

                    "h2 {" +
                    " color:#2ecc71;" +
                    " margin-bottom:10px;" +
                    "}" +

                    ".amount {" +
                    " font-size:20px;" +
                    " margin:15px 0;" +
                    " font-weight:bold;" +
                    "}" +

                    "a {" +
                    " display:inline-block;" +
                    " margin-top:20px;" +
                    " padding:10px 20px;" +
                    " background:#11998e;" +
                    " color:white;" +
                    " text-decoration:none;" +
                    " border-radius:8px;" +
                    "}" +

                    "a:hover {" +
                    " background:#0e8077;" +
                    "}" +

                    "</style>" +
                    "</head>" +

                    "<body>" +

                    "<div class='card'>" +
                    "<h2>🎉 Booking Successful!</h2>" +
                    "<div class='amount'>Total Paid: ₹" + totalPrice + "</div>" +
                    "<p>Your ticket has been confirmed.</p>" +
                    "<a href='events.jsp'>Back to Events</a>" +
                    "</div>" +

                    "</body>" +
                    "</html>"
                );

            } else {
                res.getWriter().println(
                    "<h2 style='color:red;text-align:center;'>Booking Failed!</h2>"
                );
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println(
                "<h2 style='color:red;text-align:center;'>Error: " + e.getMessage() + "</h2>"
            );
        }
    }
}