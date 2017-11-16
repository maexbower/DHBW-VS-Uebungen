package dhbw.it15002.rmi;

import dhbw.it15002.tcp.Message;
import dhbw.it15002.tcp.TCPServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observer;

public class RMIInterfaceServant extends UnicastRemoteObject implements RMIInterface {
    private TCPServer parentServer;
    public RMIInterfaceServant(TCPServer server) throws RemoteException {
        super();
        parentServer = server;
    }
    public void addMessage(Message m) throws RemoteException {
        System.out.println("Passing Message: "+m.getMessage());
        parentServer.addMessage(m);

    }
    public void registerObserver(Observer obj) throws RemoteException
    {
        parentServer.addObserver(obj);
    }
    public void removeObserver(Observer obj)  throws RemoteException
    {
        parentServer.deleteObserver(obj);
    }

}
