package common.messages;

import java.io.Serializable;

public record FileDownloadMessage(String filename, String userID) implements Serializable {}
