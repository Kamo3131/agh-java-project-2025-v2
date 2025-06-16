package server.db_objects;

import common.FileModel;
import common.PermissionsEnum;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public record SavedFile(String userID, String username, String filename, String contentType, PermissionsEnum permission, double size, String path, long date) implements Serializable {
    public FileModel toFileModel() {
        return new FileModel(filename, username, permission, Instant.ofEpochMilli(1000 * date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime(), size);
    }
}
