package common.messages;

import java.io.Serializable;

public record UserLoginMessage(String username, String password, String salt) implements Serializable {}

