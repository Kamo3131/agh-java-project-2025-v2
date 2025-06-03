package client;

import common.FileModel;
import common.PermissionsEnum;
import common.TCPCommunicator;
import common.messages.FileUploadMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import server.db_objects.User;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FileBrowserController {

    private int port_number;
    private User user;
    @FXML
    private Button ChooseFileButtonSend;

    @FXML
    private MenuButton PermissionsMenuSend;
    @FXML
    private Button ButtonSend;

    @FXML
    private ProgressBar SendingProgressBarSend;

    @FXML
    private VBox filePathLabels;
    @FXML
    private Label permissionsLabel;
    @FXML
    private Button LogOutButton;

    @FXML
    private TableView<FileModel> table;
    @FXML
    private TableColumn<FileModel, String> tableFilename;
    @FXML
    private TableColumn<FileModel, String> tableAuthor;
    @FXML
    private TableColumn<FileModel, String> tableDate;
    @FXML
    private TableColumn<FileModel, PermissionsEnum> tablePermissions;
    @FXML
    private TableColumn<FileModel, Double> tableSize;
    private static final int MAX_ROWS_PER_PAGE = 15;

    @FXML
    private Pagination TablePagesIndicator;
//    private LinkedList<FileModel> files = new LinkedList<>();
    private ObservableList<FileModel> files = FXCollections.observableArrayList();

    private List<File> filesToSend;
    private PermissionsEnum permissions;
    private final ZipCompress zipCompress = new ZipCompress();

    @FXML
    private void initialize() {
        this.permissionsLabel.setVisible(false);
        setPermissions();
        setTable();
        addFiles();
//        files.forEach(System.out::println);
        updatePagination();
    }

    private void updatePagination(){
        int pageCount = (int) Math.ceil((double) files.size() / MAX_ROWS_PER_PAGE);
        if (pageCount == 0) pageCount = 1;
        TablePagesIndicator.setPageCount(pageCount);
        TablePagesIndicator.setCurrentPageIndex(0);
        TablePagesIndicator.setPageFactory(this::createPage);
    }
    private Node createPage(int pageIndex){
        int fromIndex = pageIndex * MAX_ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + MAX_ROWS_PER_PAGE, files.size());
        ObservableList<FileModel> pageItems = FXCollections.observableArrayList(
                files.subList(fromIndex, toIndex)
        );
//        pageItems.forEach(System.out::println);
        table.setItems(pageItems);
        return new VBox();
    }

    private void addFiles() {
        for(int i=0; i<20; i++) {
            files.add(new FileModel("Albert.txt", "Kacper", PermissionsEnum.PUBLIC, LocalDateTime.now(), 2.6));
        }
        updatePagination();
    }
    private void setTable(){
        tableFilename.setCellValueFactory(new PropertyValueFactory<>("Filename"));
        tablePermissions.setCellValueFactory(new PropertyValueFactory<>("Permissions"));
        tableAuthor.setCellValueFactory(new PropertyValueFactory<>("Author"));
        tableDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        tableSize.setCellValueFactory(new PropertyValueFactory<>("Size"));
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
    public void sendingAFile(){handleSending();};

    public void setPort_number(int port_number) {
        this.port_number = port_number;
    }

    public void setFilePathLabel(String filePathLabel) {
        filePathLabels.getChildren().addFirst(new Label(filePathLabel));
    }

    public void userFileChoosing(){handleChooseFileButtonSending();}
    private void handleChooseFileButtonSending(){
        Window window = ChooseFileButtonSend.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        filesToSend = fileChooser.showOpenMultipleDialog(window);
        for(File file : filesToSend){
            String path = file.getAbsolutePath();
            zipCompress.addSourceFiles(path);
            setFilePathLabel(path);
        }


    }

    public void userRemoveFiles(){handleRemoveFiles();}
    private void handleRemoveFiles(){
        zipCompress.clearSourceFiles();
        filePathLabels.getChildren().clear();
    }
    private void handleSending(){


//        try {
//            long fileSize = zipCompress.compress(fileToSend.getName()+".zip");
//            FileUploadMessage fileUploadMessage =
//                    new FileUploadMessage(fileToSend.getName(), user.id(), "File/s",
//                            permissions, fileSize);
//        } catch(IOException ex){
//            System.out.println(ex.getMessage());
//            System.exit(1);
//        }
//
//        try{
//            TCPCommunicator.startClient(port_number);
//        } catch(java.io.IOException ex){
//            System.err.println(ex.getMessage());
//        }
    }

    public void handleLogOut() throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SignLog.fxml")));
        Stage stage = (Stage) LogOutButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("SignLog");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
