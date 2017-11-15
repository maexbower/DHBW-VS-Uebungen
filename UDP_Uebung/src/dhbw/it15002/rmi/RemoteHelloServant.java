package dhbw.it15002.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RemoteHelloServant implements RemoteHello
{
    public RemoteHelloServant() {}
    public String printHello()
    {
            return("Hallo verteilte Welt.");
    }

}
