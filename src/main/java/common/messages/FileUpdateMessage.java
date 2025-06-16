package common.messages;

import java.io.Serializable;

public record FileUpdateMessage(String userID, String filename, long date) implements Serializable {}
