package common;

import java.time.LocalDateTime;

public class FileModel {
    private final String fileName;
    private final String fileAuthor;
    private final PermissionsEnum permissions;
    private final LocalDateTime lastModified;
    private final double FileSize;

    FileModel(String fileName, String fileAuthor, PermissionsEnum permissions, LocalDateTime lastModified, double size) {
        this.fileName = fileName;
        this.fileAuthor = fileAuthor;
        this.permissions = permissions;
        this.lastModified = lastModified;
        this.FileSize = size;
    }
    public String getFileName() {
        return fileName;
    }
    public String getFileAuthor() {
        return fileAuthor;
    }
    public PermissionsEnum getPermissions() {
        return permissions;
    }
    public LocalDateTime getLastModified() {
        return lastModified;
    }
    public double getFileSize() {
        return FileSize;
    }
}
