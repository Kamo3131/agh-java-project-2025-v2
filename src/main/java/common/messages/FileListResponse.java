package common.messages;

import server.db_objects.SavedFile;

import java.io.Serializable;
import java.util.List;

public record FileListResponse(List<SavedFile> files) implements Serializable {}
