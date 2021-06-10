package emulator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class Controller {
    private Emulator emulator;

    @FXML
    private Pane gamePane;

    @FXML
    public void initialize() throws Exception {
        if(gamePane != null){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz grę");
            File file = fileChooser.showOpenDialog(new Stage());
            this.emulator = new Emulator(file, gamePane);
            startEmulatorTimer();
        }
    }

    @FXML
    private void onButtonPressed(ActionEvent event) throws IOException {
        event.consume();

        switchToGameScene(event);
    }

    private void switchToGameScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("emulator.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    private void startEmulatorTimer(){
        Timeline emulatorStepTimer = new Timeline(
                new KeyFrame(Duration.millis(1),
                        event -> emulator.step())
        );
        emulatorStepTimer.setCycleCount(Timeline.INDEFINITE);
        emulatorStepTimer.play();
    }
}
