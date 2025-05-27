package client;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;

public class SignLogController {

    // Logowanie
    @FXML
    private TextField LoginLogin;

    @FXML
    private PasswordField LoginPassword;

    @FXML
    private Button LogInButton;

    @FXML
    private Label LogInLabel;

    // Rejestracja
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

    @FXML
    private void initialize() {
        HollowLoginLabel.setVisible(false);
        HollowSignLabel.setVisible(false);
        HollowSignSuccesLabel.setVisible(false);
        HollowLogSuccesLabel.setVisible(false);
        // Inicjalizacja przycisków i innych komponentów
    }

    public void userLogin(){
        handleLogin();
    }
    public void userSignUp(){
        handleSignUp();
    }
    private boolean isLogInCorrect(){
        // This class will be provided with id's from SQLite and until then won't be finished
        return (LoginLogin.getText().equals("Marek Lis") || LoginLogin.getText().equals("mareklis123@gmail.com"))
                && LoginPassword.getText().equals("12345678");
    }
    private void handleLogin() {
        if(isLogInCorrect()){
            System.out.println("Login OK");
            HollowLoginLabel.setVisible(false);
            HollowLogSuccesLabel.setText("Log in has been successful!");
            HollowLogSuccesLabel.setVisible(true);
        }
        else{
            System.out.println("Login Error");
            HollowLoginLabel.setText("Wrong login or password");
            HollowLoginLabel.setVisible(true);
            HollowLogSuccesLabel.setVisible(false);
        }

        // Logika logowania
    }

    private void handleSignUp() {
        if(!SignLogin.getText().equals("Marek Lis") && SignPassword.getText().length()>=8 &&
                SignPasswordRepeat.getText().equals(SignPassword.getText())){
            System.out.println("SignUp OK");
            HollowSignLabel.setVisible(false);
            HollowSignSuccesLabel.setText("Sign up has been successful!");
            HollowSignSuccesLabel.setVisible(true);
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
}
