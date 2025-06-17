package server.db_objects;

import common.FileModel;
import common.PermissionsEnum;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;

public record SavedFile(String userID, String username, String filename, String contentType, PermissionsEnum permission, double size, String path, long date) implements Serializable {
    public FileModel toFileModelXT() {
        long temp = date;
        if(date < Math.pow(10, 11)){
            temp *= 1000;
        }

        return new FileModel(filename, username, permission, Instant.ofEpochMilli(temp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime(), size);
    }
}
