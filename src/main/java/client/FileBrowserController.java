package client;

import common.FileModel;
import common.PermissionsEnum;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;
import java.time.LocalDateTime;

public class FileBrowserController {

    @FXML
    private Button ChooseFileButtonSend;

    @FXML
    private MenuButton PermissionsMenuSend;

    @FXML
    private Button ButtonSend;

    @FXML
    private ProgressBar SendingProgressBarSend;

    @FXML
    private TableView<FileModel> Table;
    @FXML
    private TableColumn<FileModel, String> TableFilename;
    @FXML
    private TableColumn<FileModel, String> TableAuthor;
    @FXML
    private TableColumn<FileModel, LocalDateTime> TableDate;
    @FXML
    private TableColumn<FileModel, PermissionsEnum> TablePermissions;
    @FXML
    private TableColumn<FileModel, Double> TableSize;

    @FXML
    private Pagination TablePagesIndicator;



}
