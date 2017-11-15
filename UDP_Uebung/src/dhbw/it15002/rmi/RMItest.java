package dhbw.it15002.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMItest {
    public static void main(String[] Args)
    {
        try {
            Registry registry = LocateRegistry.createRegistry(5001);
            RemoteHelloServer server = new RemoteHelloServer(registry);
            server.startServer();
            System.out.println(String.join(",",registry.list()));
            RemoteHelloClient client = new RemoteHelloClient(5001);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
