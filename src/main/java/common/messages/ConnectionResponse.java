package common.messages;

import java.io.Serializable;

public record ConnectionResponse(int port) implements Serializable {}
