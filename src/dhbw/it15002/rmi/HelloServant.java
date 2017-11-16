package dhbw.it15002.rmi;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class HelloServant extends UnicastRemoteObject implements Hello {

	public HelloServant() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	public void printHello() throws RemoteException {
		System.out.println("Hallo verteilte Welt");
		
	}

}
