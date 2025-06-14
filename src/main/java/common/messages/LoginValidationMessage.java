package common.messages;

import java.io.Serializable;

public record LoginValidationMessage(boolean valid, LoginError message, String id) implements Serializable {
    public enum LoginError {
        USER_NOT_FOUND,
        WRONG_PASSWORD,
        PASSED,
        ALREADY_EXISTS
    }
}

