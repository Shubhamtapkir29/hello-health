package com.hellohealth.dao;

import com.hellohealth.model.User;

import java.sql.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    // ✅ Authenticate login
    public User validateUser(String email, String password) {

        String sql = "SELECT user_id, name, email, password, role FROM users WHERE email=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String dbPassword = rs.getString("password");

                    // ⚠️ Simple match (for now)
                    if (password.equals(dbPassword)) {

                        return new User(
                                rs.getInt("user_id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                dbPassword,
                                rs.getString("role")
                        );
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Error validating user", e);
        }

        return null;
    }

    // ✅ Fetch all users
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, name, email, password, role FROM users";

        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }

        } catch (Exception e) {
            logger.error("Error fetching users", e);
        }

        return users;
    }

    // ✅ Insert new user
    public boolean addUser(User user) {

        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error("Error adding user", e);
        }

        return false;
    }
}
