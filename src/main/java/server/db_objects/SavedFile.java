package server.db_objects;

import common.PermissionsEnum;

public record SavedFile(String userID, String filename, String contentType, PermissionsEnum permission, long size,
                        String path) {}
