package common.messages;

import common.PermissionsEnum;

import java.io.Serializable;

public record FileUploadMessage(String filename, String username, String userID, String contentType, PermissionsEnum permission,
                                long size, long date) implements Serializable {}
