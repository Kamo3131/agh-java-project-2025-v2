package common.messages;

import java.io.Serializable;

public record LoginValidationMessage(boolean valid, String message, String id) implements Serializable {}

