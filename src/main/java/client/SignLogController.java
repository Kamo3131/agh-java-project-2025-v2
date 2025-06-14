package client;

import common.TCPCommunicator;
import common.messages.LoginValidationMessage;
import common.messages.UserLoginMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import server.db_objects.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class SignLogController {
    // Login
    @FXML
    private TextField LoginLogin;

    @FXML
    private PasswordField LoginPassword;

    @FXML
    private Button LogInButton;

    @FXML
    private Label LogInLabel;

    // Sign up
    @FXML
    private TextField SignLogin;

    @FXML
    private PasswordField SignPassword;

    @FXML
    private PasswordField SignPasswordRepeat;

    @FXML
    private Button SignUpButton;

    @FXML
    private Label SignUpLabel;

    @FXML
    private Label HollowLoginLabel;

    @FXML
    private Label HollowSignLabel;

    @FXML
    private Label HollowLogSuccesLabel;

    @FXML
    private Label HollowSignSuccesLabel;

    private TCPCommunicator communicator;


    /**
     * Initializes GUI components by setting status labels to invisible.
     */
    @FXML
    private void initialize() throws IOException {
        HollowLoginLabel.setVisible(false);
        HollowSignLabel.setVisible(false);
        HollowSignSuccesLabel.setVisible(false);
        HollowLogSuccesLabel.setVisible(false);
        // Inicjalizacja przycisków i innych komponentów

        this.communicator = TCPCommunicator.startClient(8080);
    }

    /**
     * Calls the login handler method. Activated by LogInButton.
     */
    public void userLogin() {
        handleLogin();
    }
    /**
     * Calls the sign-up handler method. Activated by SignUpButton.
     */
    public void userSignUp(){
        handleSignUp();
    }
    /**
     * Switches to the FileBrowser view and passes the username.
     * @throws IOException if the FXML file cannot be loaded
     */
    public void userSwitchToFileBrowser() throws IOException {
//        if(!LoginLogin.getText().isEmpty()) {
//            handleSwitchToFileBrowser(LoginLogin.getText());
//        } else if (!SignLogin.getText().isEmpty()) {
//            handleSwitchToFileBrowser(SignLogin.getText());
//        } else {
//            handleSwitchToFileBrowser("Marek Lis");
//        }
    }
    /**
     * Verifies if the provided login credentials are correct.
     * @return true if login and password are correct, false otherwise
     */
    private boolean isLogInCorrect(){
        // This class will be provided with id's from SQLite and until then won't be finished
        return (LoginLogin.getText().equals("Marek Lis") || LoginLogin.getText().equals("mareklis123@gmail.com"))
                && LoginPassword.getText().equals("12345678");
    }
    /**
     * Handles the user login process: validates input, sets labels, and switches scene on success.
     */
    private void handleLogin() {
        try {
            UserLoginMessage user_login = new UserLoginMessage(LoginLogin.getText(), LoginPassword.getText());

            communicator.sendMessage(TCPCommunicator.MessageType.LOGIN);
            LoginValidationMessage valid = (LoginValidationMessage)communicator.sendAndReceiveMessage(user_login);

            if (valid.valid()) {
                System.out.println("Login OK");
                HollowLoginLabel.setVisible(false);
                HollowLogSuccesLabel.setText("Log in has been successful!");
                HollowLogSuccesLabel.setVisible(true);
                try {
                    handleSwitchToFileBrowser(LoginLogin.getText(), valid.id());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                if (valid.message() == LoginValidationMessage.LoginError.USER_NOT_FOUND) {
                    System.out.println("Login Error");
                    HollowLoginLabel.setText("User was not found!");
                    HollowLoginLabel.setVisible(true);
                    HollowLogSuccesLabel.setVisible(false);
                }
                else if (valid.message() == LoginValidationMessage.LoginError.WRONG_PASSWORD) {
                    System.out.println("Login Error");
                    HollowLoginLabel.setText("Wrong password!");
                    HollowLoginLabel.setVisible(true);
                    HollowLogSuccesLabel.setVisible(false);
                }
            }
        }
        catch (ClassNotFoundException | IOException e) {
            System.out.println(e.getMessage());
        }

        // Logika logowania
    }
    /**
     * Handles the user sign-up process: checks password validity and uniqueness,
     * updates status labels, and switches scene on success.
     */
    private void handleSignUp() {
        if (SignPassword.getText().length() >= 8 && SignPasswordRepeat.getText().equals(SignPassword.getText())) {
            try {
                UserLoginMessage user_register = new UserLoginMessage(SignLogin.getText(), SignPassword.getText());

                communicator.sendMessage(TCPCommunicator.MessageType.REGISTER);
                LoginValidationMessage valid = (LoginValidationMessage)communicator.sendAndReceiveMessage(user_register);

                if (valid.valid()) {
                    System.out.println("SignUp OK");
                    HollowSignLabel.setVisible(false);
                    HollowSignSuccesLabel.setText("Sign up has been successful!");
                    HollowSignSuccesLabel.setVisible(true);
                    try {
                        handleSwitchToFileBrowser(SignLogin.getText(), valid.id());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    System.out.println("SignUp Error");
                    HollowSignLabel.setText("This login already exists");
                    HollowSignLabel.setVisible(true);
                    HollowSignSuccesLabel.setVisible(false);
                }

            }
            catch (ClassNotFoundException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
        else if (SignPassword.getText().length() < 8) {
            System.out.println("SignUp Error");
            HollowSignLabel.setText("Wrong password");
            HollowSignLabel.setVisible(true);
            HollowSignSuccesLabel.setVisible(false);
        } else {
            System.out.println("SignUp Error");
            HollowSignLabel.setText("Repeated password does not match");
            HollowSignLabel.setVisible(true);
            HollowSignSuccesLabel.setVisible(false);
        }
        // Logika rejestracji
    }
    /**
     * Switches the scene to FileBrowser and passes the given username to the controller.
     * @param username the name of the logged-in or newly registered user
     * @throws IOException if the FXML file cannot be loaded
     */
    private void handleSwitchToFileBrowser(String username, String userID) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("FileBrowser.fxml")));
        Parent root = loader.load();


        FileBrowserController fileBrowserController = loader.getController();
        fileBrowserController.setUser(username, userID);

        // Show the new scene
        Stage stage = (Stage) LogInButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("FileBrowser");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
