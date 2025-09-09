package gui;

import Snich.Snich;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;


import java.awt.*;
import java.io.IOException;


public class SnichGUI extends Application {

    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/tars.png"));
    private Image userImage = new Image(this.getClass().getResourceAsStream("/Josephcooper1.jpeg"));
    private Snich snich = new Snich();

    public SnichGUI() throws IOException {
    }

    @Override
    public void start(Stage stage) {
        //Setting up required components

        ImageView userView = new ImageView(userImage);
        userView.setFitWidth(100);     // target width (px)
        userView.setFitHeight(100);    // target height (px)
        userView.setPreserveRatio(true);
        userView.setSmooth(true);     // nicer downscaling
        userView.setCache(true);

        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        sendButton = new Button("Send");

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        scene = new Scene(mainLayout);

        stage.setScene(scene);
        stage.show();

        stage.setTitle("Duke");
        stage.setResizable(false);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);

        mainLayout.setPrefSize(400.0, 600.0);

        scrollPane.setPrefSize(385, 535);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);

        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);

        userInput.setPrefWidth(325.0);

        sendButton.setPrefWidth(55.0);

        AnchorPane.setTopAnchor(scrollPane, 1.0);

        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);

        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);

        sendButton.setOnMouseClicked((event) -> {
            try {
                handleUserInput();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        userInput.setOnAction((event) -> {
            try {
                handleUserInput();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));
        //More code to be added here later
    }

    /**
     * Creates a dialog box containing user input, and appends it to
     * the dialog container. Clears the user input after processing.
     */
    private void handleUserInput() throws IOException {
        ImageView snichView = new ImageView(dukeImage);

        ImageView userView = new ImageView(userImage);
        userView.setFitWidth(100);     // target width (px)
        userView.setFitHeight(100);    // target height (px)
        userView.setPreserveRatio(true);
        userView.setSmooth(true);     // nicer downscaling
        userView.setCache(true);

        String userText = userInput.getText();
        String dukeText = snich.getResponse(userInput.getText());
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, userView),
                DialogBox.getDukeDialog(dukeText, snichView)
        );
        userInput.clear();
    }

}

