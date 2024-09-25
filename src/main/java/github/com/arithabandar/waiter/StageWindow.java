package github.com.arithabandar.waiter;

import github.com.arithabandar.waiter.application.whoHost.File.Logs;
import github.com.arithabandar.waiter.application.whoHost.Server;
import github.com.arithabandar.waiter.control.StageViewControl;
import github.com.arithabandar.waiter.window.StageDesign;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import static github.com.arithabandar.waiter.control.table.ViewTable.scheduler;


public class StageWindow extends Application {

    @Override
    public void start(Stage stage){
        FXMLLoader fxmlLoader = new FXMLLoader(StageWindow.class.getResource("stageView.fxml"));
        AnchorPane anchorPane = null;

        try {
            anchorPane= fxmlLoader.load();
        } catch (IOException e) {
            new Logs(StageWindow.class,e.getMessage());
        }

        StageDesign stageDesign=new StageDesign();
        double[] view=stageDesign.setStageCenter();

        StageViewControl controller = fxmlLoader.getController();

        double x=(((view[3]/2)/2)/2)+view[3]/2;
        controller.getTable(x, view[3], stage);
        controller.hBoxPlace(view[3]-view[2]/2);

        stage.getIcons().add(stageDesign.getIcon());
        Scene scene = new Scene(anchorPane,view[3],view[3]-(view[2]/2));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(_ -> {
            try {
                Server.serverSocket.close();
                scheduler.close();
                if (Server.clientSocket!=null){
                    Server.clientSocket.close();
                }
            } catch (IOException e) {
                new Logs(StageWindow.class,e.getMessage());
            }
        });

    }
}
