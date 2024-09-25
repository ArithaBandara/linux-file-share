package github.com.arithabandar.waiter.control;

import github.com.arithabandar.waiter.NTWMain;
import github.com.arithabandar.waiter.StageWindow;
import github.com.arithabandar.waiter.application.whoHost.File.Logs;
import github.com.arithabandar.waiter.application.whoHost.Server;
import github.com.arithabandar.waiter.application.whoHost.datagramSocket.NetworkManager;
import github.com.arithabandar.waiter.window.StageDesign;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;

public class DCVController implements Property {
    @FXML
    ImageView imageView;
    @FXML
    TextField field;
    @FXML
    Button button;
    @FXML
    AnchorPane pane;
    @FXML
    Label label;

    @Override
    public void setName(Stage stage) {
        Property.super.setName(stage);
        String url = RIM();
        Image PFP = new Image(Objects.requireNonNull(getClass().getResourceAsStream(STR."/img/PFP/\{url}")));

        Circle PFP_icon = getCircle(url);

        imageView.setImage(PFP);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        StackPane root = new StackPane();
        root.getChildren().clear();
        root.getChildren().addAll(PFP_icon, imageView);
        StackPane.setAlignment(imageView, Pos.CENTER);

        String hint = "Enter Username";
        field.setMaxWidth(200);
        field.setMinWidth(200);
        field.setPrefWidth(200);
        field.setPromptText(hint);

        button.setMaxWidth(200);
        button.setMinWidth(200);
        button.setPrefWidth(200);

        button.setMinHeight(25);
        button.setMaxHeight(25);
        button.setPrefHeight(25);
        button.setText("Create");

        pane.widthProperty().addListener((_, _, newVal) -> {
            AnchorPane.setLeftAnchor(root, (newVal.doubleValue() - imageView.getFitWidth()) / 2);
            AnchorPane.setRightAnchor(root, (newVal.doubleValue() - imageView.getFitWidth()) / 2);

            AnchorPane.setLeftAnchor(field, (newVal.doubleValue() / 2) - 100);
            AnchorPane.setLeftAnchor(label, (newVal.doubleValue() / 2) - 100);

            AnchorPane.setLeftAnchor(button, (newVal.doubleValue() / 2) - 100);
        });

        pane.heightProperty().addListener((_, _, newVal) -> {
            AnchorPane.setTopAnchor(root, newVal.doubleValue() / 10);

            AnchorPane.setTopAnchor(field, newVal.doubleValue() / 2);
            AnchorPane.setTopAnchor(label, (newVal.doubleValue() / 2) + 25);

            AnchorPane.setTopAnchor(button, (newVal.doubleValue() / 2) + 50);
        });
        field.textProperty().addListener((_, old, val) -> {
            label.setText(STR."Need more, \{10 - val.length()} letters.");
            if (val.length() == 11) {
                field.setText(old);
            } else {
                if (!pane.getChildren().contains(label)) {
                    pane.getChildren().add(label);
                }
            }
            if (val.length() == 10) {
                pane.getChildren().remove(label);
            }
        });

        pane.getChildren().clear();
        pane.getChildren().addAll(root, field, button, label);
    }

    public void alertBox(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private static Circle getCircle(String url) {
        Color white = Color.WHITE;

        Circle PFP_icon = new Circle();
        PFP_icon.setRadius(60);
        PFP_icon.setStroke(Color.valueOf("#444342"));

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(20);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);

        switch (url) {
            case "17.png":
                PFP_icon.setFill(Color.valueOf("#D0BCBE"));
                dropShadow.setColor(white);
                break;
            case "25.png":
                PFP_icon.setFill(Color.BLACK);
                dropShadow.setColor(white);
                break;
            default:
                PFP_icon.setFill(white);
                dropShadow.setColor(white);
                break;
        }
        PFP_icon.setEffect(dropShadow);
        return PFP_icon;
    }

    @Override
    public void fileWrite(ActionEvent actionEvent) {
        Property.super.fileWrite(actionEvent);
        String value = field.getText();

        boolean textLength;
        textLength = value.length() == 10;

        if (value.trim().isEmpty()) {
            alertBox("The value cannot be empty and must contain at least 10 letters.");
        } else if (!textLength) {
            alertBox("Please enter a Name that contain 10 letters.");
        } else {
            File file = new File(Property.fileName);
            if (!file.exists()) {
                try {
                    PrintWriter printWrite = new PrintWriter(new FileWriter(file));
                    printWrite.println(value);
                    printWrite.flush();
                    printWrite.close();
                } catch (IOException e) {
                    new Logs(DCVController.class, e.getMessage());
                }
            }
        }
        field.clear();
        bigWindow();
    }

    private void bigWindow() {
        Collection<InetAddress> inetAddress;
        StageDesign stageDesign = new StageDesign();
        double[] view = stageDesign.setStageCenter();
        Stage stage = (Stage) pane.getScene().getWindow(); // Get the current stage

        if ((inetAddress = new NetworkManager().lookForNetwork().values()).size() <= 1) {
            // If there's only one or no network interface, directly launch StageWindow
            new StageWindow().start(stage);
            new Server(inetAddress.iterator().next()).run();
        } else {
            // If there are multiple network interfaces, load NTW-fxml.fxml with NTWController
            FXMLLoader fxmlLoader = new FXMLLoader(NTWMain.class.getResource("NTW-fxml.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load(), view[3], view[3] / 2);
                stage.setTitle("Choose Network Adapter");
                stage.getIcons().add(stageDesign.getIcon());
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                new Logs(NTWMain.class, e.getMessage());
            }

        }
    }

    private String RIM() {
        Random random = new Random();
        int min = 1;
        int max = 28;
        int randomNumber = random.nextInt((max - min) + 1) + min;
        return STR."\{randomNumber}.png";
    }

}
