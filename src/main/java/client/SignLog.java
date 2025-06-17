package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;

public class SignLog extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SignLog.class.getResource("SignLog.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("SignWindow");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            SignLogController controller =  (SignLogController) fxmlLoader.getController();
//            try {
//                controller.closeConnection();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        });
    }
}
