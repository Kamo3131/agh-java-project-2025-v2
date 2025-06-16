package common.messages;

import java.io.Serializable;

public record FileUpdateMessage(String userID, String username, String filename, long date, double size) implements Serializable {}
