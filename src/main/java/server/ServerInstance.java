package server;

import common.HashPassword;
import common.messages.*;
import server.db_objects.SavedFile;
import server.db_objects.User;
import common.PermissionsEnum;
import common.TCPCommunicator;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ServerInstance {
    private final TCPCommunicator communicator;
    private final DatabaseORM db;

    ServerInstance(TCPCommunicator communicator) throws SQLException {
        this.communicator = communicator;
        this.db = new DatabaseORM();
    }

    public void run() throws IOException, SQLException, ClassNotFoundException {
        while (true) {
            TCPCommunicator.MessageType type = (TCPCommunicator.MessageType)this.communicator.receiveMessage();

            switch (type) {
                case LOGIN:
                    UserLoginMessage user_login = (UserLoginMessage)this.communicator.receiveMessage();
                    this.handleUserLogin(user_login);

                    break;
                case REGISTER:
                    UserLoginMessage user_register = (UserLoginMessage)this.communicator.receiveMessage();
                    this.handleUserRegistration(user_register);

                    break;
                case FILE_UPLOAD:
                    FileUploadMessage file_upload = (FileUploadMessage)this.communicator.receiveMessage();
                    this.handleFileUpload(file_upload);

                    break;
                case FILE_DOWNLOAD:
                    FileDownloadMessage file_download = (FileDownloadMessage)this.communicator.receiveMessage();
                    this.handleFileDownload(file_download);

                    break;
                case GET_FILE_LIST:
                    FileListRequest  file_list_request = (FileListRequest)this.communicator.receiveMessage();
                    this.handleFileListRequest(file_list_request);

                    break;
                case FILE_UPDATE:
                    FileUpdateMessage file_update = (FileUpdateMessage)this.communicator.receiveMessage();
                    this.handleFileUpdateMessage(file_update);

                    break;
            }
        }
    }

    private void handleUserLogin(UserLoginMessage user_login) throws IOException, SQLException {
        User u = this.db.getUser(user_login.username());

        LoginValidationMessage validation;

        if (u == null) {
            validation = new LoginValidationMessage(false, LoginValidationMessage.LoginError.USER_NOT_FOUND, "");
        }
        else {
            if (HashPassword.verify(user_login.password(), u.password())) {
                validation = new LoginValidationMessage(true, LoginValidationMessage.LoginError.PASSED, u.id());
            }
            else {
                validation = new LoginValidationMessage(false, LoginValidationMessage.LoginError.WRONG_PASSWORD, "");
            }
        }

        this.communicator.sendMessage(validation);
    }

    private void handleUserRegistration(UserLoginMessage user_register) throws IOException, SQLException {
        User u = this.db.getUser(user_register.username());

        if (u == null) {
            String uuid = UUID.randomUUID().toString();

            User new_user = new User(uuid, user_register.username(), (new HashPassword(user_register.password(), 32, 65000, 512)).hash());
            this.db.insertUser(new_user);

            LoginValidationMessage registration = new LoginValidationMessage(true, LoginValidationMessage.LoginError.PASSED, uuid);
            this.communicator.sendMessage(registration);
        }
        else {
            LoginValidationMessage registration = new LoginValidationMessage(false, LoginValidationMessage.LoginError.ALREADY_EXISTS, "");
            this.communicator.sendMessage(registration);
        }
    }

    private void handleFileUpload(FileUploadMessage file_upload) throws IOException, SQLException {
        this.communicator.receiveAndSaveFile("saved_files/" + file_upload.filename());

        SavedFile saved_file = new SavedFile(file_upload.userID(), file_upload.username(), file_upload.filename(), file_upload.contentType(), file_upload.permission(), file_upload.size(), "saved_files/" + file_upload.filename(), file_upload.date());
        this.db.insertSavedFile(saved_file);
    }

    private void handleFileDownload(FileDownloadMessage file_download) throws IOException, SQLException {
        SavedFile saved_file = this.db.getSavedFile(file_download.filename());

        String file_path = saved_file.path();
        File file;

        switch (saved_file.permission()) {
            case PermissionsEnum.PUBLIC:
                ;
            case PermissionsEnum.PROTECTED:
                file = new File(file_path);
                this.communicator.sendFile(file);
                break;
            case PermissionsEnum.PRIVATE:
                if (saved_file.userID().equals(file_download.userID())) {
                    file = new File(file_path);
                    this.communicator.sendFile(file);
                }
                break;
        }
    }

    private void handleFileListRequest(FileListRequest file_list_request) throws IOException, SQLException {
        List<SavedFile> files = switch (file_list_request.type()) {
            case USER_ONLY -> this.db.getUserFiles(file_list_request.userID(), file_list_request.page_num());
            case TOP_15 -> this.db.getTopFiles(file_list_request.userID(), file_list_request.page_num());
            case ALL -> this.db.getAllSavedFiles(file_list_request.userID());
        };

        this.communicator.sendMessage(new FileListResponse(files));
    }

    private void handleFileUpdateMessage(FileUpdateMessage file_update) throws IOException, SQLException {
        File file = new File("saved_files/" + file_update.filename());

        if (file.delete()) {
            this.communicator.receiveAndSaveFile("saved_files/" + file_update.filename());
        }

        this.db.updateSavedFile(file_update.userID(), file_update.filename(), file_update.date());
    }
}
