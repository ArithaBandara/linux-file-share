package github.com.arithabandar.waiter.application.fileHandlers;

import github.com.arithabandar.waiter.application.whoHost.File.Logs;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class Send {

    int PORT;
    String IP;

    public Send(String IP, int PORT) {
        this.IP = IP;
        this.PORT = PORT;
    }

    public void fileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select one File to Send");
        File theChosenOne = fileChooser.showOpenDialog(stage);
        if (theChosenOne != null) {
            write(theChosenOne);
        }
    }

    private void write(File file) {
        Task<Void> writeTask = new Task<>() {
            @Override
            protected Void call() {
                boolean connected = false;
                int retryCount = 0;
                final int maxRetries = 10;
                Socket socket = new Socket();

                while (!connected && retryCount < maxRetries) {
                    try {
                        socket = new Socket(InetAddress.getByName(IP), PORT);
                        System.out.println(socket.getInetAddress().getHostAddress());
                        connected = true;
                    } catch (ConnectException e) {
                        System.err.println(STR."Connection failed, retrying... (\{retryCount + 1}/\{maxRetries})");
                        retryCount++;
                        try {
                            Thread.sleep(10000); // Wait 10 second before quiting
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    } catch (NullPointerException | IOException nullPointerException) {
                        new Logs(Get.class, nullPointerException.getMessage());
                    }
                }

                if (!connected) {
                    System.err.println(STR."Could not connect after \{maxRetries} attempts. Exiting.");// TODO: 19/7/24 warn user
                }

                try (
                        FileInputStream fis = new FileInputStream(file);
                        OutputStream os = socket.getOutputStream();
                        DataOutputStream dos = new DataOutputStream(os)) {

                    dos.writeUTF(file.getName());
                    dos.flush();

                    byte[] buffer = new byte[2048];
                    int bytesRead;

                    while ((bytesRead = fis.read(buffer)) != -1) {
                        dos.write(buffer, 0, bytesRead);
                    }
                    dos.flush();

                } catch (IOException e) {
                    new Logs(Send.class, e.getMessage());

                }
                return null;
            }
        };

        Thread serverThread = new Thread(writeTask);
        serverThread.setDaemon(true);
        serverThread.start();

    }


}
