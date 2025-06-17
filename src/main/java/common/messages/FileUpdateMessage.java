package common.messages;

import java.io.Serializable;

public record FileUpdateMessage(String userID, String username, String filename, long date, long size) implements Serializable {}
