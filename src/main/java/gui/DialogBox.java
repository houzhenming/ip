package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class DialogBox extends HBox {

    private final Label bubble;
    private final ImageView avatar;

    private static final double AVATAR_SIZE = 36;
    private static final double MAX_BUBBLE_WIDTH = 420;

    private DialogBox(String text, ImageView imageView) {
        initRoot();
        this.avatar = prepareAvatar(imageView);
        this.bubble = buildBubble(text);
        this.getChildren().addAll(bubble, avatar);
    }

    /* ---------- helpers ---------- */

    private void initRoot() {
        this.setAlignment(Pos.TOP_RIGHT);
        this.setSpacing(10);
        this.setPadding(new Insets(6, 12, 6, 12));
    }

    private ImageView prepareAvatar(ImageView img) {
        img.setFitWidth(AVATAR_SIZE);
        img.setFitHeight(AVATAR_SIZE);
        img.setClip(new Circle(AVATAR_SIZE / 2.0, AVATAR_SIZE / 2.0, AVATAR_SIZE / 2.0));
        return img;
    }

    private Label buildBubble(String text) {
        Label lbl = new Label(text);
        lbl.setWrapText(true);
        lbl.setMaxWidth(MAX_BUBBLE_WIDTH);
        lbl.setPadding(new Insets(10, 14, 10, 14));
        lbl.setStyle(
                "-fx-background-color: #d2e3ff;" +
                        "-fx-text-fill: -fx-text-base-color;" +
                        "-fx-background-radius: 16 16 4 16;" +
                        "-fx-font-size: 13px;" +
                        "-fx-line-spacing: 2px;"
        );
        lbl.setEffect(defaultShadow());
        return lbl;
    }

    private DropShadow defaultShadow() {
        DropShadow ds = new DropShadow();
        ds.setRadius(4);
        ds.setOffsetY(1.5);
        ds.setColor(Color.rgb(0, 0, 0, 0.12));
        return ds;
    }

    /** Flip alignment + order for "received" messages (Snich). */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(tmp);
        this.getChildren().setAll(tmp);
    }

    /** Tweak bubble style for sender (user). */
    private void styleAsUser() {
        // Blue-ish bubble, tail on bottom-right
        this.bubble.setStyle(
                "-fx-background-color: #d2e3ff;" +
                        "-fx-text-fill: -fx-text-base-color;" +
                        "-fx-background-radius: 16 16 4 16;" +
                        "-fx-font-size: 13px;" +
                        "-fx-line-spacing: 2px;"
        );
    }

    /** Tweak bubble style for receiver (snich/bot). */
    private void styleAsSnich() {
        // Neutral grey bubble, tail on bottom-left
        this.bubble.setStyle(
                "-fx-background-color: #f1f3f4;" +
                        "-fx-text-fill: -fx-text-base-color;" +
                        "-fx-background-radius: 16 4 16 16;" +
                        "-fx-font-size: 13px;" +
                        "-fx-line-spacing: 2px;"
        );
    }

    public static DialogBox getUserDialog(String text, ImageView img) {
        var db = new DialogBox(text, img);
        db.styleAsUser();
        // Right-aligned order: [bubble][avatar]
        return db;
    }

    public static DialogBox getSnichDialog(String text, ImageView img) {
        var db = new DialogBox(text, img);
        db.styleAsSnich();
        db.flip(); // Left-aligned order: [avatar][bubble]
        return db;
    }
}