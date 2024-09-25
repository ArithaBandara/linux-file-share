package github.com.arithabandar.waiter.control.networkAdapter;

import github.com.arithabandar.waiter.StageWindow;
import github.com.arithabandar.waiter.application.whoHost.File.Logs;
import github.com.arithabandar.waiter.application.whoHost.Server;
import github.com.arithabandar.waiter.application.whoHost.datagramSocket.NetworkManager;
import github.com.arithabandar.waiter.control.Property;
import github.com.arithabandar.waiter.window.StageDesign;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class NTWController {

    @FXML
    private AnchorPane anchorPane;

    private final double lX;
    private final double lY;
    private final double realSceneInHalf;


    public NTWController() {
        double[] scene = new StageDesign().setStageCenter();
        realSceneInHalf = scene[3] / 2;
        lX = realSceneInHalf / 2;
        lY = realSceneInHalf / 2;
    }

    private Button WLAN;
    private Button LAN;
    private Button HOT_PLUG;

    public void initialize() {

        ArrayList<Button> buttonList = new ArrayList<>();

        double[] position = positionImage(realSceneInHalf * 2);

        WLAN = createImageButton("/img/wifi.jpg");
        LAN = createImageButton("/img/connection.jpg");
        HOT_PLUG = createImageButton("/img/wireless.jpg");

        HashMap<NetworkInterface, InetAddress> list = new NetworkManager().lookForNetwork();
        if (list.size() <= 1) {
            System.out.println(list.values());
        } else {
            list.forEach((networkInterface, inetAddress) -> {
                switch (networkInterface.getDisplayName().toLowerCase().substring(0, 2)) {
                    case "en":
                        // System.out.println(STR."Ethernet interfaces :\{interfaceDisplayName}");
                        //positionImageButton(imageButton2, realSceneInHalf - halfLX / 2 / 2, lY / 2);
                        positionImageButton(LAN, (position[0] * 2) - position[2], lX / 2);
                        LAN.setUserData(inetAddress);
                        buttonList.add(LAN);
                        break;
                    case "wl":
                        // System.out.println(STR."WLAN interfaces :\{interfaceDisplayName}");
                        //positionImageButton(imageButton1, halfLX / 2, lY / 2);
                        positionImageButton(WLAN, position[2] * 2, lY / 2);
                        WLAN.setUserData(inetAddress);
                        buttonList.add(WLAN);
                        break;
                    default:
                        // System.out.println(STR."Unknown Adapter :\{interfaceDisplayName}");
                        //positionImageButton(imageButton3, halfLX / 2, lY / 2);
                        positionImageButton(WLAN, position[0]-(position[1]+position[2]), lX / 2);
                        positionImageButton(LAN, (position[0] * 2) - (position[1]+position[2]), lX / 2);
                        positionImageButton(HOT_PLUG, (position[0] * 3)- (position[1]+position[2]), lY / 2);
                        HOT_PLUG.setUserData(inetAddress);
                        buttonList.add(HOT_PLUG);
                        break;
                }
            });
        }
        anchorPane.getChildren().addAll(buttonList);
    }

    private Button createImageButton(String imagePath) {
        Button button = new Button();
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(realSceneInHalf / 3);
        imageView.setFitHeight(realSceneInHalf / 3);
        button.setGraphic(imageView);
        button.setOnAction(_ -> handleClick(button));
        return button;
    }



    private void handleClick(Button button) {
         InetAddress inetAddress;
        if (button == WLAN) {
            inetAddress = (InetAddress) WLAN.getUserData();
        } else if (button == LAN) {
            inetAddress = (InetAddress) LAN.getUserData();
        } else {
            inetAddress = (InetAddress) HOT_PLUG.getUserData();
        }

        /* File file = new File(Property.temp);
            if (!file.exists()) {
                try {
                    PrintWriter printWrite = new PrintWriter(new FileWriter(file));
                    printWrite.println(inetAddress);
                    printWrite.flush();
                    printWrite.close();
                } catch (IOException e) {
                    new Logs(NTWController.class, e.getMessage());
                }
            }
        */
        new Server(inetAddress).run();

         Stage stage = (Stage) anchorPane.getScene().getWindow();
         new StageWindow().start(stage);

    }


    private void positionImageButton(Button button, double layoutX, double layoutY) {
        button.setLayoutX(layoutX);
        button.setLayoutY(layoutY);
    }

    private double[] positionImage(double number) {
        double[] quotientList = new double[3];
        double quotient = number / 3;
        double halfQuotient = quotient / 2;
        double quarter = halfQuotient / 2;

        quotientList[0] = quotient;
        quotientList[1] = halfQuotient;
        quotientList[2] = quarter;

        return quotientList;
    }


}
