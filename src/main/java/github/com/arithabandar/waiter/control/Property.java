package github.com.arithabandar.waiter.control;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public interface Property {
    String fileName=".dcn.txt";

    default void getTable(double H,double W, Stage primaryStage){}
    default void hBoxPlace(double v){}
    default void loadingBar(Task<Void> task){}
    default void setName(Stage stage){}
    default void fileWrite(ActionEvent actionEvent){}
}
