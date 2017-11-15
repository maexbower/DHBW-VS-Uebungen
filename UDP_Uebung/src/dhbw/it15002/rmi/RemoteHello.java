package dhbw.it15002.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteHello extends Remote {
    String printHello() throws RemoteException;
}
