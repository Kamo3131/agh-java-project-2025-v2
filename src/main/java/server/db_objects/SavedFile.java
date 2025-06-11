package server.db_objects;

import common.PermissionsEnum;

import java.io.Serializable;

public record SavedFile(String userID, String filename, String contentType, PermissionsEnum permission, long size,
                        String path, long date) implements Serializable {}
