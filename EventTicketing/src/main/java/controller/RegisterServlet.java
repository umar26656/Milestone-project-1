package controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

import util.DBConnection;

public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO users(name,email,password) VALUES(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);

            int i = ps.executeUpdate();
            if(i > 0) {
                res.sendRedirect("login.html");
            } else {
                res.getWriter().println("Registration failed!");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}