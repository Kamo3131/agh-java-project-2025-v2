package server;

import server.db_objects.SavedFile;
import server.db_objects.User;
import common.PermissionsEnum;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.ArrayList;

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
            return new User(rs.getString("id"), rs.getString("username"), rs.getString("password"));
        }
        else {
            return null;
        }
    }

    public void insertUser(User user) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO USERS (id, username, password) VALUES (?, ?, ?)");

        stmt.setString(1, user.id());
        stmt.setString(2, user.username());
        stmt.setString(3, user.password());

        stmt.executeUpdate();
    }

    public SavedFile getSavedFile(String filename) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM FILES WHERE filename = ?");
        stmt.setString(1, filename);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new SavedFile(rs.getString("uploader_id"), rs.getString("uploader_name"), rs.getString("filename"), rs.getString("content_type"), PermissionsEnum.valueOf(rs.getString("permission_type")), rs.getDouble("size"), rs.getString("path"), rs.getLong("date"));
        }
        else {
            return null;
        }
    }

    public void insertSavedFile(SavedFile savedFile) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO FILES (uploader_id, uploader_name, filename, content_type, permission_type, size, path, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

        stmt.setString(1, savedFile.userID());
        stmt.setString(2, savedFile.username());
        stmt.setString(3, savedFile.filename());
        stmt.setString(4, savedFile.contentType());
        stmt.setString(5, savedFile.permission().name());
        stmt.setDouble(6, savedFile.size());
        stmt.setString(7, savedFile.path());
        stmt.setLong(8, savedFile.date());

        stmt.executeUpdate();
    }

    public List<SavedFile> getTopFiles(String userID, int n_page) throws SQLException {
        ArrayList<SavedFile> list = new ArrayList<>();

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM FILES WHERE PERMISSION_TYPE = 'PUBLIC' OR PERMISSION_TYPE = 'PROTECTED' OR (PERMISSION_TYPE = 'PRIVATE' AND UPLOADER_ID = ?) LIMIT ?, 15");

        stmt.setString(1, userID);
        stmt.setInt(2, 15 * n_page);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            list.add(new SavedFile(rs.getString("uploader_id"), rs.getString("uploader_name"), rs.getString("filename"), rs.getString("content_type"), PermissionsEnum.valueOf(rs.getString("permission_type")), rs.getDouble("size"), rs.getString("path"), rs.getLong("date")));
        }

        return list;
    }

    public List<SavedFile> getUserFiles(String userID, int n_page) throws SQLException {
        ArrayList<SavedFile> list = new ArrayList<>();

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM FILES WHERE UPLOADER_ID = ? LIMIT ?, 15");

        stmt.setString(1, userID);
        stmt.setInt(2, 15 * n_page);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            list.add(new SavedFile(rs.getString("uploader_id"), rs.getString("uploader_name"), rs.getString("filename"), rs.getString("content_type"), PermissionsEnum.valueOf(rs.getString("permission_type")), rs.getDouble("size"), rs.getString("path"), rs.getLong("date")));
        }

        return list;
    }

    public List<SavedFile> getAllSavedFiles(String userID) throws SQLException {
        ArrayList<SavedFile> list = new ArrayList<>();

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM FILES WHERE PERMISSION_TYPE = 'PUBLIC' OR PERMISSION_TYPE = 'PROTECTED' OR (PERMISSION_TYPE = 'PRIVATE' AND UPLOADER_ID = ?)");

        stmt.setString(1, userID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            list.add(new SavedFile(rs.getString("uploader_id"), rs.getString("uploader_name"), rs.getString("filename"), rs.getString("content_type"), PermissionsEnum.valueOf(rs.getString("permission_type")), rs.getDouble("size"), rs.getString("path"), rs.getLong("date")));
        }

        return list;
    }

    public void updateSavedFile(String userID, String filename, long date) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("UPDATE FILES SET DATE=? WHERE FILENAME=? AND UPLOADER_ID=?");
        System.out.println("Halo");
        stmt.setLong(1, date);
        stmt.setString(2, filename);
        stmt.setString(3, userID);

        stmt.executeUpdate();
    }
}

