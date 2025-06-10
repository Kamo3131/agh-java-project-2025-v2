package client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
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


    /**
     * Initializes GUI components by setting status labels to invisible.
     */
    @FXML
    private void initialize() {
        HollowLoginLabel.setVisible(false);
        HollowSignLabel.setVisible(false);
        HollowSignSuccesLabel.setVisible(false);
        HollowLogSuccesLabel.setVisible(false);
        // Inicjalizacja przycisków i innych komponentów
    }

    /**
     * Calls the login handler method. Activated by LogInButton.
     */
    public void userLogin(){
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
        if(!LoginLogin.getText().isEmpty()) {
            handleSwitchToFileBrowser(LoginLogin.getText());
        } else if (!SignLogin.getText().isEmpty()) {
            handleSwitchToFileBrowser(SignLogin.getText());
        } else {
            handleSwitchToFileBrowser("Marek Lis");
        }
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
        if(isLogInCorrect()){
            System.out.println("Login OK");
            HollowLoginLabel.setVisible(false);
            HollowLogSuccesLabel.setText("Log in has been successful!");
            HollowLogSuccesLabel.setVisible(true);
            try {
                handleSwitchToFileBrowser(LoginLogin.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            System.out.println("Login Error");
            HollowLoginLabel.setText("Wrong login or password");
            HollowLoginLabel.setVisible(true);
            HollowLogSuccesLabel.setVisible(false);
        }

        // Logika logowania
    }
    /**
     * Handles the user sign-up process: checks password validity and uniqueness,
     * updates status labels, and switches scene on success.
     */
    private void handleSignUp() {
        if(!SignLogin.getText().equals("Marek Lis") && SignPassword.getText().length()>=8 &&
                SignPasswordRepeat.getText().equals(SignPassword.getText())){
            System.out.println("SignUp OK");
            HollowSignLabel.setVisible(false);
            HollowSignSuccesLabel.setText("Sign up has been successful!");
            HollowSignSuccesLabel.setVisible(true);
            try {
                handleSwitchToFileBrowser(SignLogin.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (SignPassword.getText().length()<8) {
            System.out.println("SignUp Error");
            HollowSignLabel.setText("Wrong password");
            HollowSignLabel.setVisible(true);
            HollowSignSuccesLabel.setVisible(false);
        } else if (!SignPasswordRepeat.getText().equals(SignPassword.getText())) {
            System.out.println("SignUp Error");
            HollowSignLabel.setText("Repeated password does not match");
            HollowSignLabel.setVisible(true);
            HollowSignSuccesLabel.setVisible(false);
        } else{
            System.out.println("SignUp Error");
            HollowSignLabel.setText("This login already exists");
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
    private void handleSwitchToFileBrowser(String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("FileBrowser.fxml")));
        Parent root = loader.load();


        FileBrowserController fileBrowserController = loader.getController();
        fileBrowserController.setUser(username);

        // Show the new scene
        Stage stage = (Stage) LogInButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("FileBrowser");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
