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
        this.setAlignment(Pos.TOP_RIGHT);
        this.setSpacing(10);
        this.setPadding(new Insets(6, 12, 6, 12));

        // Avatar (circular clip like Telegram)
        this.avatar = imageView;
        this.avatar.setFitWidth(AVATAR_SIZE);
        this.avatar.setFitHeight(AVATAR_SIZE);
        Circle clip = new Circle(AVATAR_SIZE / 2.0, AVATAR_SIZE / 2.0, AVATAR_SIZE / 2.0);
        this.avatar.setClip(clip);

        // Bubble
        this.bubble = new Label(text);
        this.bubble.setWrapText(true);
        this.bubble.setMaxWidth(MAX_BUBBLE_WIDTH);
        this.bubble.setPadding(new Insets(10, 14, 10, 14));
        this.bubble.setStyle(
                "-fx-background-color: #d2e3ff;" +           // default user color
                        "-fx-text-fill: -fx-text-base-color;" +
                        "-fx-background-radius: 16 16 4 16;" +       // one corner sharper (tail side)
                        "-fx-font-size: 13px;" +
                        "-fx-line-spacing: 2px;"
        );

        DropShadow ds = new DropShadow();
        ds.setRadius(4);
        ds.setOffsetY(1.5);
        ds.setColor(Color.rgb(0, 0, 0, 0.12));
        this.bubble.setEffect(ds);

        this.getChildren().addAll(bubble, avatar);
    }

    /** Flip alignment + order for "received" messages (Duke). */
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

    /** Tweak bubble style for receiver (duke/bot). */
    private void styleAsDuke() {
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

    public static DialogBox getDukeDialog(String text, ImageView img) {
        var db = new DialogBox(text, img);
        db.styleAsDuke();
        db.flip(); // Left-aligned order: [avatar][bubble]
        return db;
    }
}