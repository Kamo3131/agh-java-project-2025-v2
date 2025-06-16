package common.messages;

import java.io.Serializable;

public record FileDeletionRequest(String userID, String filename) implements Serializable {}
