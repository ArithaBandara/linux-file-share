package github.com.arithabandar.waiter.control;

import github.com.arithabandar.waiter.NTWMain;
import github.com.arithabandar.waiter.application.fileHandlers.Get;
import github.com.arithabandar.waiter.application.fileHandlers.Send;
import github.com.arithabandar.waiter.control.table.ListTable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.Objects;

public class MouseClicked {

    static Popup popup;
    private static Stage owner;

    public static String showChoiceBox(Stage owner, double x, double y, ListTable listTable) {
        MouseClicked.owner=owner;
        MenuButton menuButton = new MenuButton("I want to:");
        ImageView sendIMG = new ImageView(new Image(Objects.requireNonNull(NTWMain.class.getResourceAsStream("/img/send.png"))));
        ImageView getIMG = new ImageView(new Image(Objects.requireNonNull(NTWMain.class.getResourceAsStream("/img/get.png"))));

        MenuItem send_file = new MenuItem("Send File", sendIMG);
        MenuItem get_file = new MenuItem("Get File", getIMG);
        menuButton.getItems().addAll(send_file, get_file);

        popup = new Popup();
        popup.getContent().add(menuButton);
        popup.show(owner, x, y);
        popup.setAutoHide(true);

        send_file.setOnAction(_ -> send(listTable));
        get_file.setOnAction(_ -> get(listTable));

        return listTable.getIp_address();
    }

    private static void send(ListTable listTable) {
        new Send(listTable.getIp_address(),6060).fileChooser(owner);
        popup.hide();
    }

    private static void get(ListTable listTable) {
            new Get(listTable.getIp_address(),6060).read();
            popup.hide();
    }
}
