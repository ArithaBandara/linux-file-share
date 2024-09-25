package github.com.arithabandar.waiter.application.whoHost.datagramSocket;

import github.com.arithabandar.waiter.application.whoHost.File.Logs;
import github.com.arithabandar.waiter.application.whoHost.File.MeWrite;
import github.com.arithabandar.waiter.application.whoHost.Server;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Datagram {
    private static final int START_IP = 1;
    private static final int END_IP = 21;
    private static final String BASE_IP = "192.168.1.";
    private static int i;

    private final int numberOfThreads = Runtime.getRuntime().availableProcessors() - 1;
    private final ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

    public Task<Void> discoverDevices() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                List<Future<Void>> futures = new ArrayList<>();

                for (i = START_IP; i <= END_IP; i++) {
                    final int currentIpIndex = i;
                    futures.add(executorService.submit(() -> {
                        String ip = BASE_IP + currentIpIndex;
                        if (ip.equals(Server.inetAddress)) {
                            return null; // Skip the server's own IP address
                        }
                        try (Socket socket = new Socket(InetAddress.getByName(ip), 6061)) {
                            if (socket.isConnected()) {
                                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                                    String val = in.readLine();

                                    String[] parts = val.split(";");

                                    String ipAddress = parts[0];
                                    String extractedName = parts[1];

                                    new MeWrite().writeFile(ipAddress, extractedName);

                                } catch (IOException e) {
                                    new Logs(Datagram.class, e.getMessage());
                                }
                            }
                        } catch (Exception e) {
                            new Logs(Datagram.class, e.getMessage());
                        }
                         updateProgress(i, END_IP);
                        System.out.println(ip);
                        return null;
                    }));
                }

                for (Future<Void> future : futures) {
                    try {
                        future.get();
                    } catch (Exception e) {
                        new Logs(Datagram.class, e.getMessage());
                    }
                }

                executorService.shutdown();
                return null;
            }
        };

        Thread taskThread = new Thread(task);
        taskThread.setDaemon(true);
        taskThread.start();
        return task;
    }


}