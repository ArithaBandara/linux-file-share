package github.com.arithabandar.waiter.application.fileHandlers;

import github.com.arithabandar.waiter.application.whoHost.File.Logs;
import javafx.concurrent.Task;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Get {
    int PORT;
    String IP;


    public Get(String IP, int PORT) {
        this.IP = IP;
        this.PORT = PORT;
    }

    public void read() {
        Task<Void> readTask = new Task<>() {
            @Override
            protected Void call() {
                ServerSocket serverSocket;
                Socket socket = new Socket();
                try {
                    serverSocket = new ServerSocket(PORT, 5, InetAddress.getByName(IP));
                    socket = serverSocket.accept();
                    System.out.println(socket.getInetAddress().getHostAddress());
                    if (!IP.equals(socket.getInetAddress().getHostAddress())) {
                        System.out.println("worng ip");
                    }
                } catch (IOException e) {
                    new Logs(Get.class, e.getMessage());
                }

                try (
                        InputStream is = socket.getInputStream();
                        DataInputStream dis = new DataInputStream(is)) {

                    String fileName = dis.readUTF();
                    FileOutputStream fos = new FileOutputStream(fileName);

                    byte[] buffer = new byte[2048];
                    int bytesRead;

                    while ((bytesRead = dis.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                    fos.flush();
                    fos.close();

                } catch (IOException e) {
                    new Logs(Get.class, e.getMessage());
                }

                return null;
            }
        };
        Thread socketThread = new Thread(readTask);
        socketThread.setDaemon(true);
        socketThread.start();

    }


}
