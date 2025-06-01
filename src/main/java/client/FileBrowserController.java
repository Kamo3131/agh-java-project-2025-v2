package client;

import common.FileModel;
import common.PermissionsEnum;
import common.TCPCommunicator;
import common.messages.FileUploadMessage;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import server.db_objects.User;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class FileBrowserController {

    private int port_number;
    @FXML
    private Button ChooseFileButtonSend;

    @FXML
    private MenuButton PermissionsMenuSend;
    @FXML
    private Button ButtonSend;

    @FXML
    private ProgressBar SendingProgressBarSend;

    @FXML
    private Label filePathLabel;
    @FXML
    private Label permissionsLabel;


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

    private File fileToSend;
    private PermissionsEnum permissions;
    @FXML
    private void initialize() {
        this.filePathLabel.setVisible(false);
        this.permissionsLabel.setVisible(false);
        setPermissions();
    }
    @FXML
    private void setPermissions(){
        final MenuItem publicPermission = new MenuItem("PUBLIC");
        final MenuItem privatePermission = new MenuItem("PRIVATE");
        final MenuItem protectedPermission = new MenuItem("PROTECTED");
        publicPermission.setOnAction(actionEvent -> {
            permissions = PermissionsEnum.PUBLIC;
            PermissionsMenuSend.setText("PUBLIC");
            permissionsLabel.setText("Current permissions for this file: PUBLIC");
            permissionsLabel.setVisible(true);
        });

        privatePermission.setOnAction(actionEvent -> {
            permissions = PermissionsEnum.PRIVATE;
            PermissionsMenuSend.setText("PRIVATE");
            permissionsLabel.setText("Current permissions for this file: PRIVATE");
            permissionsLabel.setVisible(true);
        });
        protectedPermission.setOnAction(actionEvent -> {
            permissions = PermissionsEnum.PROTECTED;
            PermissionsMenuSend.setText("PROTECTED");
            permissionsLabel.setText("Current permissions for this file: PROTECTED");
            permissionsLabel.setVisible(true);
        });
        PermissionsMenuSend.getItems().addAll(publicPermission, privatePermission, protectedPermission);
    }
    public void sendingAFile(User user){handleSending(user);};

    public void setPort_number(int port_number) {
        this.port_number = port_number;
    }

    public void setFilePathLabel(String filePathLabel, boolean set_visible) {
        this.filePathLabel.setText(filePathLabel);
        if(set_visible) {
            this.filePathLabel.setVisible(true);
        }
    }

    public void userFileChoosing(){handleChooseFileButtonSending();}
    private void handleChooseFileButtonSending(){
        Window window = ChooseFileButtonSend.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileToSend = fileChooser.showOpenDialog(window);
        String path = fileToSend.getAbsolutePath();
        setFilePathLabel(fileToSend.getAbsolutePath(), true);
    }
    public void userPermissionsChoosing(){handleChoosingPermissions(); permissionsLabel.setVisible(true);}
    private void handleChoosingPermissions() {
        permissions = switch (PermissionsMenuSend.getText()){
            case "PUBLIC" -> {
                PermissionsMenuSend.setText("PUBLIC");
                yield PermissionsEnum.PUBLIC;
            }
            case "PRIVATE" -> {
                PermissionsMenuSend.setText("PRIVATE");
                yield PermissionsEnum.PRIVATE;
            }
            case "PROTECTED" -> {
                PermissionsMenuSend.setText("PROTECTED");
                yield PermissionsEnum.PROTECTED;
            }
            default -> {
                throw new IllegalArgumentException("Invalid permissions");
            }
        };

    }
    private void handleSending(User user){
        ZipCompress zipCompress = new ZipCompress();
        zipCompress.addSourceFiles(fileToSend.getAbsolutePath());
        try {
            long fileSize = zipCompress.compress(fileToSend.getName()+".zip");
            FileUploadMessage fileUploadMessage =
                    new FileUploadMessage(fileToSend.getName(), user.id(), "File/s",
                            permissions, fileSize);
        } catch(IOException ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }

        try{
            TCPCommunicator.startClient(port_number);
        } catch(java.io.IOException ex){
            System.err.println(ex.getMessage());
        }
    }


}
