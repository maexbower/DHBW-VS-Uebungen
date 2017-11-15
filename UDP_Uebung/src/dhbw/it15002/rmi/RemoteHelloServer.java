package dhbw.it15002.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoteHelloServer {
    private Registry reg;
    public RemoteHelloServer(Registry pReg) throws RemoteException
    {
        reg = pReg;
    }
    public void startServer() throws RemoteException
    {
        //System.err.println("try to get Registry");

        System.out.println("try to get Servant");
        RemoteHelloServant obj = new RemoteHelloServant();
        System.out.println("Got Servant");
        try {
            //RemoteHello stub = (RemoteHello) UnicastRemoteObject.exportObject(obj, 0);
            // Bind the remote object's stub in the registry
            System.out.println("Try to bind Servant");
            reg.rebind("Hello", obj);
            System.out.println("Server ready");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
