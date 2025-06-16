package common.messages;

import java.io.Serializable;

public record FileUpdateMessage(String userID, String filename, String date) implements Serializable {}
