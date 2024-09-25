package github.com.arithabandar.waiter;

import github.com.arithabandar.waiter.application.whoHost.File.Logs;
import github.com.arithabandar.waiter.application.whoHost.Server;
import github.com.arithabandar.waiter.application.whoHost.datagramSocket.NetworkManager;
import github.com.arithabandar.waiter.control.DCVController;
import github.com.arithabandar.waiter.control.Property;
import github.com.arithabandar.waiter.window.StageDesign;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.util.Collection;

public class NTWMain extends Application {

    @Override
    public void start(Stage stage) {
        StageDesign stageDesign = new StageDesign();
        double[] view = stageDesign.setStageCenter();

        File file = new File(Property.fileName);
        if (!file.exists()) {
            FXMLLoader fxmlLoader = new FXMLLoader(NTWMain.class.getResource("DVCName.fxml"));
            AnchorPane anchorPane = null;

            try {
                anchorPane = fxmlLoader.load();
            } catch (IOException e) {
                new Logs(StageWindow.class, e.getMessage());
            }
            DCVController controller = fxmlLoader.getController();
            controller.setName(stage);
            Scene scene = new Scene(anchorPane, view[3] / 2, view[3] / 2);
            stage.setTitle("Create Account");
            stage.getIcons().add(stageDesign.getIcon());
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();
        } else {
            Collection<InetAddress> inetAddress;
            if ((inetAddress = new NetworkManager().lookForNetwork().values()).size() <= 1) {
                // If there's only one or no network interface, directly launch StageWindow
                new StageWindow().start(stage);
                new Server(inetAddress.iterator().next()).run();
            } else {
                // If there are multiple network interfaces, load NTW-fxml.fxml with NTWController
                FXMLLoader fxmlLoader = new FXMLLoader(NTWMain.class.getResource("NTW-fxml.fxml"));
                try {
                    Scene scene = new Scene(fxmlLoader.load(), view[3] , view[3] / 2);
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
    }

    public static void main(String[] args) {
        launch(args);
    }
}
