package dhbw.it15002.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoteHelloClient
{
    public RemoteHelloClient(int port){
        RemoteHello myHello = null;
        String host = null;
        try {
            System.out.println("Try to get registry");
            Registry registry = LocateRegistry.getRegistry();
            System.out.println("got registry");
            System.out.println(String.join(",",registry.list()));
            System.out.println("lookup stub");
            RemoteHello stub = (RemoteHello) Naming.lookup("Hello");

            String response = stub.printHello();
            System.out.println("response: " + response);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
