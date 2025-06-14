package common.messages;

import java.io.Serializable;

public record FileListRequest(String userID, int page_num, ListType type) implements Serializable {
    public enum ListType {
        USER_ONLY,
        TOP_15,
        ALL
    }
}
