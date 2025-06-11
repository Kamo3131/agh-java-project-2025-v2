package common.messages;

import java.io.Serializable;

public record FileListRequest(String userID, int page_num, boolean user_only) implements Serializable {}
