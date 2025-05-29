package server;

import server.db_objects.SavedFile;
import server.db_objects.User;
import common.PermissionsEnum;

import java.sql.*;

public class DatabaseORM {
    private static Connection connection;

    public DatabaseORM() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            System.out.println("Opened database successfully");
        }
    }

    public User getUser(String username) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM USERS WHERE username = ?");
        stmt.setString(1, username);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new User(rs.getString("id"), rs.getString("username"), rs.getString("password"), rs.getString("salt"));
        }
        else {
            return null;
        }
    }

    public void insertUser(User user) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO USERS (id, username, password, salt) VALUES (?, ?, ?, ?)");

        stmt.setString(1, user.id());
        stmt.setString(2, user.username());
        stmt.setString(3, user.password());
        stmt.setString(4, user.salt());

        stmt.executeUpdate();
    }

    public SavedFile getSavedFile(String filename) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM FILES WHERE filename = ?");
        stmt.setString(1, filename);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new SavedFile(rs.getString("uploader_id"), rs.getString("filename"), rs.getString("content_type"), PermissionsEnum.valueOf(rs.getString("permission_type")), rs.getLong("size"), rs.getString("path"));
        }
        else {
            return null;
        }
    }

    public void insertSavedFile(SavedFile savedFile) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO FILES (uploader_id, filename, content_type, permission_type, size, path) VALUES (?, ?, ?, ?, ?, ?)");

        stmt.setString(1, savedFile.userID());
        stmt.setString(2, savedFile.filename());
        stmt.setString(3, savedFile.contentType());
        stmt.setString(4, savedFile.permission().name());
        stmt.setLong(5, savedFile.size());
        stmt.setString(6, savedFile.path());

        stmt.executeUpdate();
    }
}

