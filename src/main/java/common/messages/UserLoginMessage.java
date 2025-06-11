package common.messages;

import java.io.Serializable;

public record UserLoginMessage(String username, String password) implements Serializable {}

