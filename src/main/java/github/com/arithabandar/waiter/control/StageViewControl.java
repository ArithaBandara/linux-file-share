package github.com.arithabandar.waiter.control;

import github.com.arithabandar.waiter.application.whoHost.File.Logs;
import github.com.arithabandar.waiter.application.whoHost.datagramSocket.Datagram;
import github.com.arithabandar.waiter.control.table.ListTable;
import github.com.arithabandar.waiter.control.table.ViewTable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;

public class StageViewControl implements Property {

    @FXML
    private TableView<ListTable> table;
    @FXML
    private BorderPane pane;
    @FXML
    private AnchorPane anchorPane;

    private double height;
    private double width;
    private double v;

    public StageViewControl() {
        /*
         * FXMLLoader requires a default constructor to instantiate the controller class
         * */
    }

    @Override
    public void getTable(double H, double W, Stage primaryStage) {
        Property.super.getTable(H, W, primaryStage);
        new ViewTable(table, 0).getTableView();
        this.height = H;
        this.width = W;
        table.setRowFactory(_ -> {
            TableRow<ListTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.SECONDARY) {
                    ListTable clickedRow = row.getItem();
                    MouseClicked.showChoiceBox(primaryStage, event.getScreenX(), event.getScreenY(), clickedRow);
                } else if (row.isEmpty() && event.getButton() == MouseButton.SECONDARY) {
                    row.setItem(new ListTable("ip", "name", true, true));
                }
            });


            return row;
        });

        pane.setMaxWidth(W);
        pane.setMinWidth(W);
        pane.setPrefWidth(W);

        pane.setMaxHeight(H);
        pane.setMinHeight(H);
        pane.setPrefHeight(H);


        anchorPane.heightProperty().addListener((_, _, newVal) -> {
            AnchorPane.setTopAnchor(pane, (newVal.doubleValue() - pane.getPrefHeight()));
        });

        anchorPane.widthProperty().addListener((_, _, newVal) -> {
            AnchorPane.setLeftAnchor(pane, (newVal.doubleValue() - pane.getPrefWidth()) / 2);
            AnchorPane.setRightAnchor(pane, (newVal.doubleValue() - pane.getPrefWidth()) / 2);
        });
    }

    private Button search;
    private Button reset;

    @Override
    public void hBoxPlace(double v) {
        Property.super.hBoxPlace(v);
        this.v = v;
        //PFP things
        String url = RIM();
        Image PFP = new Image(Objects.requireNonNull(getClass().getResourceAsStream(STR."/img/PFP/\{url}")));
        ImageView PFP_imageView = new ImageView(PFP);
        PFP_imageView.setPreserveRatio(true);
        PFP_imageView.setSmooth(true);

        Circle PFP_icon = getCircle(url);

        Label PFP_label = new Label();
        try {
            PFP_label = new Label(Files.readString(Paths.get(Property.fileName)));
        } catch (IOException e) {
            new Logs(StageViewControl.class, e.getMessage());
        }
        PFP_label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-underline: true; -fx-font-family: sans-serif;");

        StackPane root = new StackPane();
        root.getChildren().clear();
        root.getChildren().addAll(PFP_icon, PFP_imageView);
        StackPane.setAlignment(PFP_imageView, Pos.CENTER);

        search = new Button("Search");
        search.setStyle("-fx-font-size: 13px; -fx-background-color: #ad73c1;");

        reset = new Button("Reset");
        reset.setStyle("-fx-font-size: 13px; -fx-background-color: #ad73c1;");

        EventHandler<ActionEvent> eventHandler = _ -> {
            Task<Void> task = new Datagram().discoverDevices();
            task.setOnRunning(_ -> {
                search.setDisable(true);
                loadingBar(task);
                reset.setDisable(true);
            });
        };
        search.setOnAction(eventHandler);

        EventHandler<ActionEvent> eventHandlerReset = _ -> {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    new File("i_Know_u.json").delete();

                    final int max = 100;
                    for (int i = 1; i <= max; i++) {
                        if (isCancelled()) {
                            break;
                        }
                        updateProgress(i, max);
                        try {
                            Thread.sleep(60); // Simulate work by sleeping for 50 milliseconds
                        } catch (InterruptedException e) {
                            new Logs(StageViewControl.class, e.getMessage());
                        }
                    }
                    return null;
                }
            };
            Thread taskThread = new Thread(task);
            taskThread.setDaemon(true);
            taskThread.start();

            task.setOnRunning(_ -> {
                search.setDisable(true);
                loadingBar(task);
                reset.setDisable(true);
            });

        };
        reset.setOnAction(eventHandlerReset);


        reset.prefWidthProperty().bind(Bindings.max(search.widthProperty(), reset.widthProperty()));

        HBox BUTTON_BOX = new HBox(10, search, reset);
        HBox.setHgrow(BUTTON_BOX.getChildren().getFirst(), Priority.ALWAYS);

        anchorPane.getChildren().addAll(PFP_label, root, BUTTON_BOX);

        AnchorPane.setLeftAnchor(PFP_label, 10.0);
        AnchorPane.setTopAnchor(PFP_label, ((((v - height) / 2) / 2) / 2) + ((v - height) / 2) / 2);

        PFP_label.widthProperty().addListener((_, _, newVal) -> {
            AnchorPane.setLeftAnchor(root, 20.0 + newVal.doubleValue()); // 10 is the spacing
        });

        AnchorPane.setTopAnchor(root, ((((v - height) / 2) / 2) / 2) - 5);

        double x1 = (width - 10) / 2;
        double xx = ((x1 / 2) / 2);
        double x2 = (width - ((x1 / 2) / 2)) - xx / 2;

        AnchorPane.setLeftAnchor(BUTTON_BOX, x2);
        AnchorPane.setTopAnchor(BUTTON_BOX, ((v - height) / 2) + (((v - height) / 2) / 2));
        DateTime();
    }

    private static Circle getCircle(String url) {
        Color white = Color.WHITE;

        Circle PFP_icon = new Circle();
        PFP_icon.setRadius(36);
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
    public void loadingBar(Task<Void> task) {
        Property.super.loadingBar(task);
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);
        Label loading = new Label("Loading");
        loading.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        progressBar.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(_ -> {
            search.setDisable(false);
            reset.setDisable(false);
            HBox parent = (HBox) progressBar.getParent();
            parent.getChildren().removeAll(progressBar, loading);
        });

        HBox hBox = new HBox(15, loading, progressBar);
        HBox.setHgrow(hBox.getChildren().getFirst(), Priority.ALWAYS);

        anchorPane.getChildren().add(hBox);
        AnchorPane.setLeftAnchor(hBox, 220.0);
        AnchorPane.setTopAnchor(hBox, ((v - height) / 2) + (((v - height) / 2) / 2));
    }

    private void DateTime() {
        Label dateTimeLabel = new Label();
        dateTimeLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        dateTimeLabel.setTextFill(Color.WHITE);

        Label today = new Label("ToDay:");
        today.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        today.setTextFill(Color.WHITE);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), _ -> {
            LocalDateTime now = LocalDateTime.now();
            dateTimeLabel.setText(now.format(dateTimeFormatter));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        HBox hbox = new HBox(10, today, dateTimeLabel);

        anchorPane.getChildren().add(hbox);

        double y = ((width / 2) - ((width / 2) / 2) / 2) - ((width / 2) / 2) / 2;

        AnchorPane.setTopAnchor(hbox, 0.0);
        AnchorPane.setLeftAnchor(hbox, y);
    }

    private String RIM() {
        Random random = new Random();
        int min = 1;
        int max = 28;
        int randomNumber = random.nextInt((max - min) + 1) + min;
        return STR."\{randomNumber}.png";
    }
}
