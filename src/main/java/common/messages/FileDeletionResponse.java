package common.messages;

import java.io.Serializable;

public record FileDeletionResponse(boolean deleted) implements Serializable {}
