package github.com.arithabandar.waiter.application.whoHost.datagramSocket;

import github.com.arithabandar.waiter.application.whoHost.File.Logs;

import java.net.*;
import java.util.*;

public class NetworkManager {
    private HashMap<NetworkInterface, InetAddress> list = new HashMap<>();

    /*
     * @ This class for the Network related data
     * */

    public HashMap<NetworkInterface, InetAddress> lookForNetwork() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                    // Get the IP addresses associated with this interface
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

                    while (addresses.hasMoreElements()) {
                        InetAddress adder = addresses.nextElement();
                        // Check if it is an IPv4 address
                        if (adder instanceof Inet4Address) {
                            String interfaceDisplayName = networkInterface.getDisplayName();
                            networkInterface = NetworkInterface.getByName(interfaceDisplayName);

                            if (System.getProperty("os.name").toLowerCase().equals("linux")){
                                switch (interfaceDisplayName.toLowerCase().substring(0, 2)) {
                                    case "en":
                                        // System.out.println(STR."Ethernet interfaces :\{interfaceDisplayName}");
                                        break;
                                    case "wl":
                                        // System.out.println(STR."WLAN interfaces :\{interfaceDisplayName}");
                                        break;
                                    default:
                                        // System.out.println(STR."Unknown Adapter :\{interfaceDisplayName}");
                                        break;
                                }
                                list.put(networkInterface, adder);

                                if (interfaceDisplayName.toLowerCase().charAt(0) == 's') {
                                    list.put(networkInterface,adder);
                                }

                            } else if (System.getProperty("os.name").toLowerCase().contains("win")) {

                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            new Logs(NetworkManager.class, e.getMessage());
        }
        return list;
    }
}
