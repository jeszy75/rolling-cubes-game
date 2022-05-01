package rollingcubes.javafx.controller;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.inject.Inject;

import org.tinylog.Logger;

import rollingcubes.results.GameResult;
import rollingcubes.results.GameResultDao;
import rollingcubes.state.RollingCubesState;
import util.javafx.ControllerHelper;
import util.javafx.Stopwatch;

public class GameController {

    @FXML
    private Label messageLabel;

    @FXML
    private GridPane gameBoard;

    @FXML
    private Label stepsLabel;

    @FXML
    private Label stopwatchLabel;

    @FXML
    private Button resetButton;

    @FXML
    private Button giveUpFinishButton;

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameResultDao gameResultDao;

    private RollingCubesState gameState;

    private Stopwatch stopwatch = new Stopwatch();

    private String playerName;

    private IntegerProperty steps = new SimpleIntegerProperty();

    private Instant startTime;

    private List<Image> cubeImages;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @FXML
    private void initialize() {
        cubeImages = List.of(
                new Image("/images/cube0.png"),
                new Image("/images/cube1.png"),
                new Image("/images/cube2.png"),
                new Image("/images/cube3.png"),
                new Image("/images/cube4.png"),
                new Image("/images/cube5.png"),
                new Image("/images/cube6.png")
        );
        Logger.info(cubeImages);
        stepsLabel.textProperty().bind(steps.asString());
        stopwatchLabel.textProperty().bind(stopwatch.hhmmssProperty());
        Platform.runLater(() -> messageLabel.setText(String.format("Good luck, %s!", playerName)));
        resetGame();
    }

    private void resetGame() {
        gameState = new RollingCubesState();
        bindGameStateToUI();
        steps.set(0);
        startTime = Instant.now();
        if (stopwatch.getStatus() == Animation.Status.PAUSED) {
            stopwatch.reset();
        }
        stopwatch.start();
    }

    private void bindGameStateToUI() {
        for (var i = 0; i < 3; i++) {
            for (var j = 0; j < 3; j++) {
                var imageView = (ImageView) gameBoard.getChildren().get(i * 3 + j);
                var p = gameState.cubeProperty(i, j);
                imageView.imageProperty().bind(
                        new ObjectBinding<Image>() {
                            {
                                super.bind(p);
                            }
                            @Override
                            protected Image computeValue() {
                                return cubeImages.get(p.get().getValue());
                            }
                        }
                );
            }
        }
        gameState.solvedProperty().addListener(this::handleSolved);
    }

    @FXML
    private void handleClickOnCube(MouseEvent mouseEvent) {
        var row = GridPane.getRowIndex((Node) mouseEvent.getSource());
        var col = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        Logger.debug("Cube ({}, {}) is clicked", row, col);
        if (gameState.canRollToEmptySpace(row, col)) {
            Logger.debug("Rolling the cube");
            steps.set(steps.get() + 1);
            gameState.rollToEmptySpace(row, col);
            Logger.debug("New state:\n{}", gameState);
        } else {
            Logger.debug("Invalid move");
        }
    }

    private void handleSolved(ObservableValue<? extends Boolean> observableValue, boolean oldValue, boolean newValue) {
        if (newValue) {
            Logger.info("Player {} has solved the game in {} steps", playerName, steps.get());
            stopwatch.stop();
            messageLabel.setText(String.format("Congratulations, %s!", playerName));
            resetButton.setDisable(true);
            giveUpFinishButton.setText("Finish");
        }
    }

    @FXML
    private void handleResetButton(ActionEvent actionEvent)  {
        Logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        Logger.info("Resetting game");
        stopwatch.stop();
        resetGame();
    }

    @FXML
    private void handleGiveUpFinishButton(ActionEvent actionEvent) throws IOException {
        var buttonText = ((Button) actionEvent.getSource()).getText();
        Logger.debug("{} is pressed", buttonText);
        if (buttonText.equals("Give Up")) {
            stopwatch.stop();
            Logger.info("The game has been given up");
        }
        Logger.debug("Saving result");
        gameResultDao.persist(createGameResult());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        ControllerHelper.loadAndShowFXML(fxmlLoader, "/fxml/highscores.fxml", stage);
    }

    private GameResult createGameResult() {
        return GameResult.builder()
                .player(playerName)
                .solved(gameState.isSolved())
                .duration(Duration.between(startTime, Instant.now()))
                .steps(steps.get())
                .build();
    }

}
