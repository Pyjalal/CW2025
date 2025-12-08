package com.comp2042;

import com.comp2042.tetris.ui.GuiController;
import com.comp2042.tetris.controllers.GameController;
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
            new GameController(c);
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
