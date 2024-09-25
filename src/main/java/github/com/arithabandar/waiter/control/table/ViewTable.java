package github.com.arithabandar.waiter.control.table;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import github.com.arithabandar.waiter.application.whoHost.File.Logs;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ViewTable {
    private static TableView<ListTable> table;
    private static final String FILE_NAME = "i_Know_u.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private final double v;
    public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public ViewTable(TableView<ListTable> table, double v) {
        ViewTable.table = table;
        this.v = v;
        startFileAvailabilityListener();
    }

    @SafeVarargs
    private void initializeColumns(TableColumn<ListTable, ?>... columns) {
        table.getColumns().addAll(columns);
    }

    public void getTableView() {
        TableColumn<ListTable, String> ip = new TableColumn<>("IP");
        ip.setMinWidth(v);
        ip.setCellValueFactory(new PropertyValueFactory<>("Ip_address"));

        TableColumn<ListTable, String> name = new TableColumn<>("Name");
        name.setMinWidth(v);
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<ListTable, Boolean> preConnection = new TableColumn<>("Connection");
        preConnection.setMinWidth(v);
        preConnection.setCellValueFactory(new PropertyValueFactory<>("PreConnected"));

        TableColumn<ListTable, Boolean> alive = new TableColumn<>("IsAlive");
        alive.setMinWidth(v);
        alive.setCellValueFactory(new PropertyValueFactory<>("alive"));

        table.setItems(getList());
        initializeColumns(ip, name, preConnection, alive);
    }

    private static ObservableList<ListTable> getList() {
        ObservableList<ListTable> list = FXCollections.observableArrayList();
        ArrayNode arrayNode = readJsonFile();
        if (arrayNode != null) {
            for (JsonNode node : arrayNode) {
                list.add(new ListTable(
                        node.get("Device_IP").toString().replace("\"", ""),
                        node.get("Device_Name").toString().replace("\"", ""),
                        true,
                        true)
                );
            }
        }
        return list;
    }

    private static ArrayNode readJsonFile() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try {
                return (ArrayNode) mapper.readTree(file);
            } catch (IOException e) {
                new Logs(ViewTable.class, e.getMessage());
            }
        }
        return mapper.createArrayNode();
    }

    private void startFileAvailabilityListener() {
        scheduler.scheduleAtFixedRate(() -> {
            File file = new File(FILE_NAME);
            if (file.exists()) {
                Platform.runLater(this::updateTable);
            }else {
                Platform.runLater(this::updateTable);
            }
        }, 0, 10, TimeUnit.SECONDS); // Check every 10 seconds
    }

    public void updateTable() {
        table.setItems(getList());
    }
}