package client;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OverwriteAlertBox {
    private static AlertState overwrite;
    public static AlertState displayOverwriteAlert(String filename, Stage ownerStage){
        Stage stage = new Stage();
        stage.setTitle("Overwrite Alert");
        stage.setResizable(false);
        stage.setWidth(200);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(ownerStage);

        Label label = new Label("Do you want to overwrite file named: "+filename);
        Button yes = new Button("Yes");
        Button no = new Button("No");
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);

        yes.setOnAction(e -> {
            overwrite = AlertState.OVERWRITE;
            stage.close();
        });
        no.setOnAction(e -> {
            overwrite = AlertState.SEND_NEW;
            stage.close();
        });
        stage.setOnCloseRequest(e -> overwrite = AlertState.CLOSED);
        VBox vbox = new VBox(label, yes, no);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);


        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.showAndWait();
        return overwrite;
    }


}
