package github.com.arithabandar.waiter.application.whoHost;

import github.com.arithabandar.waiter.application.whoHost.File.Logs;
import github.com.arithabandar.waiter.application.whoHost.File.MeWrite;
import github.com.arithabandar.waiter.application.whoHost.datagramSocket.Datagram;
import github.com.arithabandar.waiter.control.table.ViewTable;
import javafx.concurrent.Task;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Server {
    private static final int PORT = 6061;
    private static final int BACKLOG = 3;
    public static String inetAddress;

    public static ServerSocket serverSocket;
    public static Socket clientSocket;

    public Server(InetAddress inetAddress) {
        Server.inetAddress = inetAddress.getHostName();
    }

    public Boolean run() {
        Task<Void> serverTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    serverSocket = new ServerSocket(PORT, BACKLOG, InetAddress.getByName(inetAddress));
                    System.out.println(STR."Server started on port \{inetAddress} : \{PORT}");// TODO: 24.png/4/24.png background work

                    while (true) {
                        try {
                            Socket clientSocket = serverSocket.accept();
                            if (clientSocket.isConnected()) {
                                System.out.println("New connection established: " + clientSocket.getInetAddress());
                                handleClient(clientSocket);
                            }
                        } catch (IOException e) {
                            new Logs(Server.class, e.getMessage());
                            break;
                        }
                    }

                } catch (IOException e) {
                    new Logs(Server.class, e.getMessage());
                }
                return null;
            }
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.setDaemon(true);
        serverThread.start();
        return true;
    }

    private void handleClient(Socket clientSocket) {
        if (clientSocket.isConnected()) {
            System.out.println(STR."New connection established: \{clientSocket.getInetAddress()}");// TODO: 24.png/4/24.png add to connected list
            try (PrintWriter in = new PrintWriter(clientSocket.getOutputStream())) {
                in.println(STR."\{Server.inetAddress};\{Files.readString(Path.of(".dcn.txt"))}");
                in.flush();
            } catch (IOException e) {
                new Logs(Server.class, e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Connection closed: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    new Logs(Server.class, e.getMessage());
                }
            }
        }
    }
}
