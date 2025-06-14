package client;

import common.FileModel;
import common.PermissionsEnum;
import common.TCPCommunicator;
import common.messages.FileListRequest;
import common.messages.FileListResponse;
import common.messages.FileUploadMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import server.db_objects.SavedFile;
import server.db_objects.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.UUID;

/**
 * Controller class for the File Browser view.
 * Handles file selection, compression, permission settings, and file display in a paginated table.
 */
public class FileBrowserController {


    private int port_number;
    private String username;
    private String userID;
    @FXML private Button AddFileButtonSend;
    @FXML private MenuButton PermissionsMenuSend;
    @FXML private Button ButtonSend;
    @FXML private ProgressBar SendingProgressBarSend;
    @FXML private VBox filePathLabels;
    @FXML private Label permissionsLabel;
    @FXML private Button LogOutButton;
    @FXML private TextField newFilename;
    @FXML private Label compressionLabel;
    @FXML private Label usernameLabel;
    @FXML private TableView<FileModel> table;
    @FXML private TableColumn<FileModel, String> tableFilename;
    @FXML private TableColumn<FileModel, String> tableAuthor;
    @FXML private TableColumn<FileModel, String> tableDate;
    @FXML private TableColumn<FileModel, PermissionsEnum> tablePermissions;
    @FXML private TableColumn<FileModel, Double> tableSize;
    @FXML private TableColumn<FileModel, Void> tableDownloadIcon;

    @FXML private Pagination TablePagesIndicator;

    private static final int MAX_ROWS_PER_PAGE = 15;

    private ObservableList<FileModel> files = FXCollections.observableArrayList();
    private List<File> filesToSend;
    private List<File> filesToSendTemp;
    private PermissionsEnum permissions;
    private final ZipCompress zipCompress = new ZipCompress();
    private final ZipDecompress zipDecompress = new ZipDecompress();
    private final String basicExportDirName = "ExportDir";
    private String basicImportDirName = "ImportDir";
    private TCPCommunicator communicator;


    /**
     * Sets the username of the logged-in user.
     * @param username the name of the user
     */
    public void setUser(String username, String userID) {
        this.username = username;
        usernameLabel.setText(username);
        this.userID = userID;
    }

    /**
     * Returns the current username.
     * @return username of the user
     */
    public String getUser(){
        return username;
    }

    /**
     * Returns ObservableList from tableview table.
     * @return ObservableList of tableview's items.
     */
    private ObservableList<FileModel> getFiles(){
        return table.getItems();
    }
    /**
     * Initializes the controller after its root element has been completely processed.
     */
    @FXML
    private void initialize() throws IOException {
        try {
            communicator = TCPCommunicator.startClient(8080);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }

        this.permissionsLabel.setVisible(false);
        compressionLabel.setVisible(false);
        setPermissions();
        setTable();
        updatePagination();
        //Line below checks if a basic directory exists, then exports all
        //files from this dir to List<File> and puts this List in the tableview
//        addFiles(getFilesFromDirectory(createBasicDirectory()));
        getFilesFromDB(userID);
        updatePagination();
    }

    private File createBasicDirectory(){
        File directory = new File(basicExportDirName);
        if(!directory.exists()){
            if(directory.mkdir()){
                System.out.println("Directory created");
            }
            else{
                System.err.println("Directory not created");
            }
        }
        else{
            System.out.println("Directory already exists");
        }
        return directory;
    }

    private List<File> getFilesFromDirectory(File directory){
        return new ArrayList<>(Arrays.asList(Objects.requireNonNull(directory.listFiles())));
    }
    /**
     * Updates pagination based on the number of files.
     */
    private void updatePagination(){
        int pageCount = (int) Math.ceil((double) files.size() / MAX_ROWS_PER_PAGE);
        if (pageCount == 0) pageCount = 1;
        TablePagesIndicator.setPageCount(pageCount);
        TablePagesIndicator.setCurrentPageIndex(0);
        TablePagesIndicator.setPageFactory(this::createPage);
    }
    /**
     * Creates a paginated table page based on index.
     * @param pageIndex index of the page
     * @return Node representing the page
     */
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

    /**
     * Adds multiple files to the observable list and updates the table.
     * @param file_list list of files to add
     */
    private void getFilesFromDB(String userID) {
        try {
            communicator.sendMessage(TCPCommunicator.MessageType.GET_FILE_LIST);

            FileListResponse response = (FileListResponse)communicator.sendAndReceiveMessage(new FileListRequest(userID, 0, FileListRequest.ListType.ALL));

            for (SavedFile file : response.files()) {
                files.add(file.toFileModel());
            }
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Adds a single file to the observable list and updates the table.
     * @param file file to add
     */
    private void addFile(File file) {
            files.add(new FileModel(file.getName(), username, permissions,
                    Instant.ofEpochMilli(file.lastModified())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime(), (double) file.length() /(1024*1024)));
        updatePagination();
        table.setItems(FXCollections.observableArrayList(
                files.subList(0, Math.min(MAX_ROWS_PER_PAGE, files.size()))
        ));
    }

    private void TCPupload(File file) throws IOException {
        communicator.sendMessage(TCPCommunicator.MessageType.FILE_UPLOAD);
        communicator.sendMessage(new FileUploadMessage(file.getName(), username, UUID.randomUUID().toString(), "Archive", permissions, file.length()/(1024*1024), file.lastModified()));
        communicator.sendFile(file);
    }

    /**
     * Sets up the table columns and formatting.
     */
    private void setTable(){
        tableFilename.setCellValueFactory(new PropertyValueFactory<>("Filename"));
        tablePermissions.setCellValueFactory(new PropertyValueFactory<>("Permissions"));
        tableAuthor.setCellValueFactory(new PropertyValueFactory<>("Author"));
        tableDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        tableSize.setCellValueFactory(new PropertyValueFactory<>("Size"));

        tableSize.setCellFactory(column -> new TableCell<FileModel, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f MB", item));
                }
            }
        });
        //When row is clicked two times, the file will be decompressed to the basic directory
        table.setRowFactory(tv -> {
            TableRow<FileModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                   handleRowSelection();
               }
            });
            return row;
        });
    }

    private void handleRowSelection(){
        FileModel file = table.getSelectionModel().getSelectedItem();
        System.out.println(file.getFilename());
        try {
            zipDecompress.decompress(basicExportDirName +"/"+ file.getFilename(), basicImportDirName);
        } catch (IOException e) {
            System.err.println("Error while decompressing file " + file.getFilename());
        }
    }
    /**
     * Initializes file permission menu items and their event handlers.
     */
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
    /**
     * Public method to trigger file compression and sending.
     */
    public void userSendingAFile(){handleSending();};
    /**
     * Sets the port number for network operations.
     * @param port_number port number to use
     */
    public void setPort_number(int port_number) {
        this.port_number = port_number;
    }

    /**
     * Adds a label to the file path VBox.
     * @param filePathLabel the path of the file to show
     */
    public void setFilePathLabel(String filePathLabel) {
        filePathLabels.getChildren().addFirst(new Label(filePathLabel));
    }

    /**
     * Public method to trigger the file adder dialog.
     */
    public void userFileAdding(){handleAddFileButtonSending();}

    /**
     * Opens file adder dialog and collects files to be compressed.
     */
    private void handleAddFileButtonSending(){
        Window window = AddFileButtonSend.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        filesToSend = fileChooser.showOpenMultipleDialog(window);
        filesToSendTemp = filesToSend;
        filesToSendTemp.forEach(System.out::println);
        for(File file : filesToSend){
            String path = file.getAbsolutePath();
            zipCompress.addSourceFiles(path);
            setFilePathLabel(path);
        }


    }
    /**
     * Public method to clear selected files and reset UI labels.
     */
    public void userRemoveFiles(){handleRemoveFiles(); resetLabels();}
    /**
     * Clears selected files from the compression queue and UI.
     */
    private void handleRemoveFiles(){
        zipCompress.clearSourceFiles();
        filePathLabels.getChildren().clear();
    }
    /**
     * Resets the visibility of status labels.
     */
    private void resetLabels(){
        permissionsLabel.setVisible(false);
        compressionLabel.setVisible(false);
    }

    private String fileNaming() {
        String newFilenameString = newFilename.getText();
        if (newFilenameString == null || newFilenameString.isEmpty()) {
            newFilenameString = "NewArchive";
        }
        Set<String> existingFilenames = new HashSet<>();
        for (FileModel file : files){
            existingFilenames.add(file.getBaseName());
        }
        int copyIndex = 0;
        String temp = newFilenameString;
        while(existingFilenames.contains(temp)) {
            copyIndex++;
            temp = newFilenameString + "(" + copyIndex + ")";
        }
        return temp;
    }


    /**
     * Handles file compression and updates the table upon success.
     */
    private void handleSending(){
        if(permissions==null){
            permissionsLabel.setText("Current permissions for this file: None.\n\tSet permissions for current file.");
            permissionsLabel.setVisible(true);
        } else if(zipCompress.sizeSourceFiles()==0) {
            compressionLabel.setText("There are no files to send.");
            compressionLabel.setVisible(true);
        }else {
            files = getFiles();
//            files.forEach(System.out::println);
            String finalNewFilename = fileNaming();
            Task<File> compressionTask = new Task<>() {
                @Override
                protected File call() throws Exception {
                    return zipCompress.compress(basicExportDirName+"/"+finalNewFilename + ".zip");
                }
            };

            compressionTask.setOnSucceeded(event -> {
                File compressedFile = compressionTask.getValue();
                try {
                    TCPupload(compressedFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                addFile(compressedFile);
                handleRemoveFiles();
            });

            compressionTask.setOnFailed(event -> {
                compressionLabel.setText("Compression failed. Trying again!");
                compressionLabel.setVisible(true);
            });

            new Thread(compressionTask).start();
        }
    }

    /**
     * Handles logout: returns to the SignLog view.
     * @throws IOException if the FXML resource cannot be loaded
     */
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
