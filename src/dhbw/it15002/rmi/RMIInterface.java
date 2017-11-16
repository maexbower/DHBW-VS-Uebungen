package dhbw.it15002.rmi;

import dhbw.it15002.tcp.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Observer;

public interface RMIInterface extends Remote {
    public void addMessage(Message m) throws RemoteException;
    public void registerObserver(Observer obj) throws RemoteException;
    public void removeObserver(Observer obj)  throws RemoteException;
}
