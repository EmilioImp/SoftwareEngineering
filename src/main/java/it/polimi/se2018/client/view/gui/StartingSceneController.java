package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.GUIClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartingSceneController implements SceneController {
    private GUIClient guiClient;
    private Stage stage;
    @FXML
    private Button socketButton;
    @FXML
    private Button rmiButton;

    public void socketButtonClicked(){
        guiClient.createSocketConnection();
        changeScene(getScene());
    }

    public void rmiButtonClicked(){
        guiClient.createRMIConnection();
        changeScene(getScene());
    }

    public void setGuiClient(GUIClient guiClient){
        this.guiClient = guiClient;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Scene getScene(){
        return socketButton.getScene();
    }

    @Override
    public void changeScene(Scene scene){
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/playerNameScene.fxml")));
        try{
            Parent root = loader.load();
            PlayerNameSceneController playerNameSceneController = loader.getController();
            playerNameSceneController.setGuiClient(guiClient);
            playerNameSceneController.setStage(stage);
            scene.setRoot(root);
        }catch(IOException e){
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }
}
