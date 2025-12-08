package com.comp2042;

import com.comp2042.tetris.ui.GuiController;
import com.comp2042.tetris.controllers.GameController;
import com.comp2042.tetris.patterns.DifficultyStrategy;
import com.comp2042.tetris.patterns.EasyDifficulty;
import com.comp2042.tetris.patterns.MediumDifficulty;
import com.comp2042.tetris.patterns.HardDifficulty;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("TetrisJFX - COMP2042 Coursework");
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(400);
        primaryStage.setResizable(true);

        showMainMenu();
    }

    private void showMainMenu() throws Exception {
        URL menuLocation = getClass().getClassLoader().getResource("mainMenu.fxml");
        FXMLLoader menuLoader = new FXMLLoader(menuLocation);
        Parent menuRoot = menuLoader.load();

        /* Get buttons and set actions */
        VBox menuVBox = (VBox) menuRoot;
        Button singlePlayerBtn = (Button) menuVBox.lookup("#singlePlayerBtn");
        Button multiplayerBtn = (Button) menuVBox.lookup("#multiplayerBtn");
        Button exitBtn = (Button) menuVBox.lookup("#exitBtn");

        singlePlayerBtn.setOnAction(e -> startSinglePlayer());
        multiplayerBtn.setOnAction(e -> startMultiplayer());
        exitBtn.setOnAction(e -> Platform.exit());

        Scene menuScene = new Scene(menuRoot, 500, 400);
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    private void startSinglePlayer() {
        showDifficultySelection();
    }

    private void showDifficultySelection() {
        try {
            URL location = getClass().getClassLoader().getResource("difficultySelect.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();

            /* Get buttons and set actions */
            Button easyBtn = (Button) root.lookup("#easyBtn");
            Button mediumBtn = (Button) root.lookup("#mediumBtn");
            Button hardBtn = (Button) root.lookup("#hardBtn");
            Button backBtn = (Button) root.lookup("#backBtn");

            easyBtn.setOnAction(e -> startGameWithDifficulty(new EasyDifficulty()));
            mediumBtn.setOnAction(e -> startGameWithDifficulty(new MediumDifficulty()));
            hardBtn.setOnAction(e -> startGameWithDifficulty(new HardDifficulty()));
            backBtn.setOnAction(e -> {
                try { showMainMenu(); } catch (Exception ex) { ex.printStackTrace(); }
            });

            Scene scene = new Scene(root, 500, 450);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startGameWithDifficulty(DifficultyStrategy difficulty) {
        try {
            URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            GuiController c = fxmlLoader.getController();

            Scene scene = new Scene(root, 700, 600);
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(500);

            root.requestFocus();
            new GameController(c, difficulty);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startMultiplayer() {
        try {
            /* Load multiplayer-specific layout with two boards */
            URL location = getClass().getClassLoader().getResource("multiplayerLayout.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 900, 720);
            primaryStage.setScene(scene);
            /* Prevent resizing smaller than content needs */
            primaryStage.setMinWidth(900);
            primaryStage.setMinHeight(720);

            root.requestFocus();
            System.out.println("Multiplayer mode started - HP-based battle system active!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
