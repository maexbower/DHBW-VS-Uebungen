package dhbw.it15002.rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends Remote {
	void printHello() throws RemoteException;
}