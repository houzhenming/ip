package gui;

import Snich.Snich;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SnichGUI extends Application {

    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;
    private final Image snichImage = new Image(this.getClass().getResourceAsStream("/tars.png"));
    private final Image userImage = new Image(this.getClass().getResourceAsStream("/Josephcooper1.jpeg"));
    private final Snich snich = new Snich();

    public SnichGUI() throws IOException { }

    @Override
    public void start(Stage stage) {
        initContainers();
        AnchorPane mainLayout = buildLayout();
        scene = new Scene(mainLayout);
        configureStage(stage, scene);
        configureScrollPane();
        configureSizing(mainLayout);
        layoutAnchors();
        wireEvents();
        enableAutoScroll();
        showWelcome();
    }

    /* ---------- helpers ---------- */

    private void initContainers() {
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        sendButton = new Button("Send");
    }

    private AnchorPane buildLayout() {
        AnchorPane root = new AnchorPane();
        root.getChildren().addAll(scrollPane, userInput, sendButton);
        return root;
    }

    private void configureStage(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.setTitle("Snich");
        stage.setResizable(false);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);
        stage.show();
    }

    private void configureScrollPane() {
        scrollPane.setPrefSize(385, 535);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);
        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
    }

    private void configureSizing(AnchorPane root) {
        root.setPrefSize(400.0, 600.0);
        userInput.setPrefWidth(325.0);
        sendButton.setPrefWidth(55.0);
    }

    private void layoutAnchors() {
        AnchorPane.setTopAnchor(scrollPane, 1.0);

        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);

        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);
    }

    private void wireEvents() {
        sendButton.setOnMouseClicked(e -> safeHandleUserInput());
        userInput.setOnAction(e -> safeHandleUserInput());
    }

    private void enableAutoScroll() {
        dialogContainer.heightProperty().addListener(o -> scrollPane.setVvalue(1.0));
    }

    private void safeHandleUserInput() {
        try {
            handleUserInput();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private ImageView createAvatar(Image img) {
        ImageView v = new ImageView(img);
        v.setFitWidth(100);
        v.setFitHeight(100);
        v.setPreserveRatio(true);
        v.setSmooth(true);
        v.setCache(true);
        return v;
    }

    /**
     * Creates a dialog box containing user input, and appends it to
     * the dialog container. Clears the user input after processing.
     */
    private void handleUserInput() throws IOException {
        ImageView snichView = new ImageView(snichImage);
        ImageView userView = createAvatar(userImage);

        String userText = userInput.getText();
        String snichText = snich.getResponse(userText);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, userView),
                DialogBox.getSnichDialog(snichText, snichView)
        );
        userInput.clear();
    }

    private void showWelcome() {
        ImageView snichView = new ImageView(snichImage);
        String snichText = snich.showWelcome();
        dialogContainer.getChildren().addAll(
                DialogBox.getSnichDialog(snichText, snichView)
        );
        userInput.clear();
    }
}