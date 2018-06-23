package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.GUIClient;
import it.polimi.se2018.client.view.gui.button.*;
import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.PassMessage;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.ChangeActionException;
import it.polimi.se2018.utils.exceptions.HaltException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * (by Emilio)
 * This class is the controller of GameScene.fxml. Edoardo Note that every public method used by GUIController needs
 * the Platform.runLater call, otherwise you will get an exception due to some thread problems. see method
 * refreshRoundTracker() for an example.
 * Furthermore, please note that i didn't write a method that refreshes a specific window, it's a mess. For now you can
 * use refreshAll() if a window needs to be refreshed. If possible i will upgrade this some day.
 * Finally, please note that i couldn't test a lot of staff that i wrote because i needed the logic of what happens if
 * you click a button to be implemented. For example, i don't know if the drafted die is shown correctly, because by now
 * i can't draft a die. Same for the dice on roundTracker.
 */
public class GameSceneController implements SceneController, Initializable{
    private final GUIView guiView;
    private final GUIModel guiModel;
    private GUIClient guiClient;
    private final int playerID;
    private Die dieInHand;
    private ImageView dieInHandImageVIew;
    private Pane draftPoolPane;
    private Pane roundTrackerPane;
    private ToolCardMessage toolCardMessage;
    private final ToolCardGUI toolCardGUI;
    private State currentState;
    private Stage stage;
    private List<String> sortedPlayersNames;
    private List<Integer> sortedPlayersScores;
    private List<ButtonSquare> windowPlayerButtons;
    private List<ButtonDraftPool> draftPoolButtons;
    private List<List<MenuItemRoundTracker>> roundTrackerMenuItems;
    private List<ButtonToolCard> toolCardButtons;

    @FXML
    private BorderPane borderPane;

    @FXML
    private GridPane botGridPane;

    @FXML
    private GridPane rightGridPane;

    @FXML
    private GridPane leftGridPane;

    @FXML
    private Label serviceLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label favorPointsLabel;

    @FXML
    private Button passButton;

    public GameSceneController(GUIController guiController) {
        this.guiView = guiController.getGuiView();
        this.guiModel = guiController.getGuiModel();
        this.playerID = guiController.getPlayerID();
        toolCardGUI = new ToolCardGUI(this);
        currentState = new StateTurn(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        passButton.setOnAction(e -> passTurnButtonClicked());
        borderPane.setPadding(new Insets(120,20,110,20));
        windowPlayerButtons = new ArrayList<>();
        for(Square[] row : guiModel.getBoard().getPlayerWindows().get(guiModel.getBoard().getPlayerID().indexOf(playerID))){
            for(Square square : row){
                windowPlayerButtons.add(new ButtonSquare(new Coordinate(square.getRow(), square.getCol()),square));
            }
        }
        toolCardButtons = new ArrayList<>();
        for(int i=0; i < guiModel.getToolCards().size(); i++){
            toolCardButtons.add(new ButtonToolCard(i, guiModel.getToolCards().get(i)));
            //here you define what happens if you click on a toolcard
            toolCardButtons.get(i).setOnAction(e -> buttonToolCardClicked(((ButtonToolCard)e.getSource()).getToolCardNumber()));
        }
        draftPoolButtons = new ArrayList<>();
        roundTrackerMenuItems = new ArrayList<>();
        setRightGridpane();
        setBotGridPane();
        setLeftGridpane();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public GUIModel getGuiModel() {
        return guiModel;
    }

    public ToolCardMessage getToolCardMessage() {
        return toolCardMessage;
    }

    public GUIView getGuiView() {
        return guiView;
    }

    public int getPlayerID() {
        return playerID;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void disableAllButton(){
        for(ButtonSquare buttonSquare : windowPlayerButtons){
            buttonSquare.disarm();
        }
        for(ButtonDraftPool buttonDraftPool : draftPoolButtons){
            buttonDraftPool.disarm();
        }
        for(ButtonToolCard buttonToolCard : toolCardButtons){
            buttonToolCard.disarm();
        }
        for(List<MenuItemRoundTracker> round : roundTrackerMenuItems){
            for(MenuItemRoundTracker menuItemRoundTracker : round){
                menuItemRoundTracker.setDisable(true);
            }
        }
        //(comment by emilio, old) I don't know why you call refreshAll(). It creates new staff with buttons that are not disabled
        //(comment by emilio 22/06/2018) actually i think it's right. you pass the disabled buttons to the sub-fxmls
        refreshAll();
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public void setToolCardMessage(ToolCardMessage toolCardMessage) {
        this.toolCardMessage = toolCardMessage;
    }

    public void setAllButton(){
        for(ButtonSquare buttonSquare : windowPlayerButtons){
            buttonSquare.checkCondition(currentState.getButtonCheckUsabilityHandler());
        }
        for(ButtonDraftPool buttonDraftPool : draftPoolButtons){
            buttonDraftPool.checkCondition(currentState.getButtonCheckUsabilityHandler());
        }
        for(ButtonToolCard buttonToolCard : toolCardButtons){
            buttonToolCard.checkCondition(currentState.getButtonCheckUsabilityHandler());
        }
        for(List<MenuItemRoundTracker> round : roundTrackerMenuItems){
            for(MenuItemRoundTracker menuItemRoundTracker : round){
                menuItemRoundTracker.checkCondition(currentState.getButtonCheckUsabilityHandler());
            }
        }
    }

    public void sendToolCardMessage() {
        guiView.handleNetworkOutput(toolCardMessage);
        toolCardMessage = null;
    }

    //togliere le eccezioni e capire se servono o come gestirle
    //This method is called by network input when you receive an ack that allow you to use the toolcard
    public void useToolCard(int toolCardIndex) {
        ToolCard toolCard = guiModel.getToolCards().get(toolCardIndex);
        toolCard.handleGUI(toolCardGUI, toolCardIndex);
    }

    //This method is called by the controller of the button pass turn
    public void passTurnButtonClicked() {
        guiView.handleNetworkOutput(new PassMessage(playerID, guiModel.getBoard().getStateID(), false));
        disableAllButton();
    }


    //This method is called by controller of square button and round tracker button
    //Current state know how handle input
    public void buttonCoordinateClicked(Coordinate coordinate) {
        currentState.doActionWindow(coordinate);
    }

    //This method is called by controller of draft pool button
    public void buttonDraftPoolClicked(int drafPoolPosition){
        currentState.doActionDraftPool(drafPoolPosition);
    }

    //This method is called by controller of tool cards
    public void buttonToolCardClicked(int toolCardIndex){
        currentState.doActionToolCard(toolCardIndex);
    }

    public void setClientGUI(GUIClient guiClient) {
        this.guiClient = guiClient;
    }

    /**
     * This method is called (togheter with setSortedPlayersScores) when ScoreBoardResponse is received
     * @param sortedPlayersNames it's the list of players in order
     */
    public void setSortedPlayersNames(List<String> sortedPlayersNames) {
        this.sortedPlayersNames = sortedPlayersNames;
    }

    /**
     * This method is called (togheter with setSortedPlayersNames) when ScoreBoardResponse is received
     * @param sortedPlayersScores it's the list of scores in order
     */
    public void setSortedPlayersScores(List<Integer> sortedPlayersScores) {
        this.sortedPlayersScores = sortedPlayersScores;
    }

    @Override
    public void changeScene(Scene scene) {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/ScoreBoardScene.fxml")));
        try {
            ScoreBoardSceneController scoreBoardSceneController = new ScoreBoardSceneController(sortedPlayersNames, sortedPlayersScores);
            loader.setController(scoreBoardSceneController);
            Parent root = loader.load();
            ((GUIView) guiClient.getGUIView()).getGuiController().setSceneController(scoreBoardSceneController);
            stage.setWidth(600);
            stage.setHeight(623);
            scoreBoardSceneController.setStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    @Override
    public Scene getScene() {
        return borderPane.getScene();
    }

    private void setRightGridpane(){
        List<Square[][]> enemyWindows = new ArrayList<>(guiModel.getBoard().getPlayerWindows());
        enemyWindows.remove(guiModel.getBoard().getPlayerID().indexOf(playerID));
        for(int i=0; i < enemyWindows.size(); i++){
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/windowEnemyScene.fxml")));
            loader.setController(new WindowEnemySceneController(enemyWindows.get(i)));
            try {
                Node node = loader.load();
                Pane pane = new Pane();
                pane.getChildren().add(node);
                pane.setMaxWidth(206);
                pane.setMaxHeight(182);
                rightGridPane.add(pane,0,i);
            } catch (IOException e) {
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.ALL,e.getMessage());
            }
            rightGridPane.setVgap(200);
        }
    }

    private void setBotGridPane(){
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/windowScene.fxml")));
        loader.setController((new WindowSceneController(windowPlayerButtons,this)));
        try{
            Node node = loader.load();
            Pane pane = new Pane();
            pane.getChildren().add(node);
            pane.setMaxWidth(206);
            pane.setMaxHeight(182);
            botGridPane.add(pane,1,0);
        }catch(IOException e){
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
        int myIndex = guiModel.getBoard().getPlayerID().indexOf(playerID);
        nameLabel.setText(guiModel.getBoard().getPlayerNames().get(myIndex));
        favorPointsLabel.setText("You have " + guiModel.getBoard().getPlayerFavorPoint().get(myIndex) + " Favor Points");
        if(guiModel.getBoard().hasDieInHand()){
            dieInHand = guiModel.getBoard().getDieInHand();
            dieInHandImageVIew = new ImageView(new Image("./dice/"+ dieInHand.getColor().getAbbreviation()+ dieInHand.getValue()+ ".png"));
            dieInHandImageVIew.setFitWidth(30);
            dieInHandImageVIew.setFitHeight(30);
            botGridPane.add(dieInHandImageVIew,2,0);
        }

        ImageView imageView = new ImageView(new Image(guiModel.getPrivateObjective().getImagePath()));
        imageView.setFitWidth(181);
        imageView.setFitHeight(253);
        botGridPane.add(imageView,3,2);
        botGridPane.setHgap(80);
    }

    private void setLeftGridpane(){
        setDraftPoolButtons();
        setRoundTrackerMenuItems();
        for(int i=0; i < toolCardButtons.size(); i++){
            leftGridPane.add(toolCardButtons.get(i),i,0);
        }
        for(int i=0; i < guiModel.getPublicObjectives().size(); i++){
            ImageView imageView = new ImageView(new Image(guiModel.getPublicObjectives().get(i).getImagePath()));
            imageView.setFitWidth(181);
            imageView.setFitHeight(253);
            leftGridPane.add(imageView,i,1);
            draftPoolPane = paneDraftPool();
            leftGridPane.add(draftPoolPane,3,0);
            roundTrackerPane = paneRoundTracker();
            leftGridPane.add(roundTrackerPane,3,1);
        }

        leftGridPane.setVgap(220);
        leftGridPane.setHgap(100);
    }

    /**
     * This method loads the draftPool
     * @return a pane containing the draftPool
     */
    private Pane paneDraftPool(){
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/draftPoolScene.fxml")));
        loader.setController(new DraftPoolSceneController(draftPoolButtons,this));
        Pane pane = new Pane();
        try{
            Node node = loader.load();
            pane.getChildren().add(node);
            pane.setMaxWidth(500);
            pane.setMaxHeight(25);
        }catch(IOException e){
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
        return pane;
    }

    /**
     * This method loads the roundTracker
     * @return a pane containing the roundTracker
     */
    private Pane paneRoundTracker(){
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/RoundTrackerScene.fxml")));
        loader.setController(new RoundTrackerSceneController(roundTrackerMenuItems, this));
        Pane pane = new Pane();
        try{
            Node node = loader.load();
            pane.getChildren().add(node);
            pane.setMaxWidth(473);
            pane.setMaxHeight(99);
        }catch(IOException e){
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
        return pane;
    }

    private void setDraftPoolButtons(){
        draftPoolButtons.clear();
        for(Die die : guiModel.getBoard().getDraftPool()){
            draftPoolButtons.add(new ButtonDraftPool(die));
        }
    }

    private void setRoundTrackerMenuItems(){
        roundTrackerMenuItems.clear();
        List<List<Die>> roundTracker = new ArrayList<>(guiModel.getBoard().getRoundTracker());
        for(int i=0; i < roundTracker.size(); i++){
            List<MenuItemRoundTracker> singleRound = new ArrayList<>();
            for(int j=0; j < roundTracker.get(i).size(); j++){
                singleRound.add(new MenuItemRoundTracker(new Coordinate(i,j),roundTracker.get(i).get(j)));
                roundTrackerMenuItems.add(singleRound);
            }
        }
    }

    /**
     * This method is called when a ModelViewResponse is received. It refreshes the whole GameScene
     */
    public void refreshAll(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                leftGridPane.getChildren().clear();
                botGridPane.getChildren().remove(dieInHand);
                botGridPane.getChildren().remove(dieInHandImageVIew);
                rightGridPane.getChildren().clear();
                setLeftGridpane();
                setRightGridpane();
                setBotGridPane();
            }
        });
    }

    /**
     * This method is called when something happened and you need to comunicate it to the player
     * @param text it's the text you want to show
     */
    void setText(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                serviceLabel.setText(text);
            }
        });
    }

    /**
     * This method is called when it's needed to refresh the die in hand of the player and the number of his favor points
     */
    public void refreshFavorPointsAndDieInHand(){
        Platform.runLater((new Runnable() {
            @Override
            public void run() {
                if(guiModel.getBoard().getCurrentPlayerID()==playerID) {
                    favorPointsLabel.setText(String.valueOf(guiModel.getBoard().getPlayerFavorPoint()));
                    botGridPane.getChildren().remove(dieInHandImageVIew);
                    if (guiModel.getBoard().hasDieInHand()) {
                        dieInHand = guiModel.getBoard().getDieInHand();
                        dieInHandImageVIew = new ImageView(new Image("./dice/" + dieInHand.getColor().getAbbreviation() + dieInHand.getValue() + ".png"));
                        dieInHandImageVIew.setFitWidth(30);
                        dieInHandImageVIew.setFitHeight(30);
                        botGridPane.add(dieInHandImageVIew, 2, 0);
                    } else dieInHand = null;
                }
            }
        }));
    }

    /**
     * This method is called when draftPool needs to be refreshed
     */
    public void refreshDraftPool(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                leftGridPane.getChildren().remove(draftPoolPane);
                setDraftPoolButtons();
                draftPoolPane = paneDraftPool();
                leftGridPane.add(draftPoolPane,3,0);
            }
        });
    }

    /**
     * This method is called when roundTracker needs to be refreshed
     */
    public void refreshRoundTracker(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                leftGridPane.getChildren().remove(roundTrackerPane);
                setRoundTrackerMenuItems();
                roundTrackerPane = paneRoundTracker();
                leftGridPane.add(roundTrackerPane,3,1);
            }
        });
    }
}
