package common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileModel {
    private final String fileName;
    private final String fileAuthor;
    private final PermissionsEnum permissions;
    private final String lastModified;
    private final double FileSize;

    public FileModel(String fileName, String fileAuthor, PermissionsEnum permissions, LocalDateTime lastModified, double size) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm:ss");
        this.fileName = fileName;
        this.fileAuthor = fileAuthor;
        this.permissions = permissions;
        this.lastModified = lastModified.format(formatter);
        this.FileSize = size;
    }
    public String getFilename() {
        return fileName;
    }
    public String getAuthor() {
        return fileAuthor;
    }
    public PermissionsEnum getPermissions() {
        return permissions;
    }
    public String getDate() {
        return lastModified;
    }
    public double getSize() {
        return FileSize;
    }
    public String toString() {
        return "Filename: "+fileName+", Author: "+fileAuthor+", Permissions: "+permissions+", Date: "+lastModified;
    }
}
