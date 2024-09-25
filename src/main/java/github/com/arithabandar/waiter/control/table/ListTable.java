package github.com.arithabandar.waiter.control.table;

public class ListTable {
    public String getIp_address() {return Ip_address;}

    public void setIp_address() {setIp_address(null);}

    public void setIp_address(String ip_address) {
        Ip_address = ip_address;
    }

    public String getName() {
        return name;
    }

    public void setName() {
        setName(null);
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPreConnected() {
        return preConnected;
    }

    public void setPreConnected() {
        setPreConnected(false);
    }

    public void setPreConnected(boolean preConnected) {
        this.preConnected = preConnected;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive() {
        setAlive(false);
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    private String Ip_address;
    private String name;
    private boolean preConnected;
    private boolean alive;

    public ListTable(String ipAddress, String name, boolean preConnected, boolean alive) {
        this.Ip_address = ipAddress;
        this.name = name;
        this.preConnected = preConnected;
        this.alive = alive;
    }
}
