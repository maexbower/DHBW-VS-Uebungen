package dhbw.it15002.rmi;
import dhbw.it15002.tcp.Message;

import java.io.Serializable;
import java.rmi.*;
import java.util.Observable;
import java.util.Observer;

public class RMIClient implements Observer, Remote, Serializable {
	private String nickName = "unknown";
	private RMIInterface serverInterface = null;
	private Terminal term;
	public RMIClient(Terminal pTerm) throws RemoteException {
		try{
			term = pTerm;
			serverInterface = (RMIInterface) Naming.lookup("//localhost/dhbw.it15002.rmi.RMIInterfaceServant");

		} catch(RemoteException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Client:" + e.getMessage());
		}
	}
	public void registerClient(RMIClient obj)
	{
		try {
			serverInterface.registerObserver(this);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	public void unregisterClient(RMIClient obj)
	{
		try {
			serverInterface.removeObserver(this);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public RMIInterface getServerInterface() {
		return serverInterface;
	}

	public void setServerInterface(RMIInterface serverInterface) {
		this.serverInterface = serverInterface;
	}
	@Override
	public void update(Observable observable, Object o) {
		Message m = (Message)o;
		term.printMessage("RMIClient: Got message from Server:"+m.getMessage());
	}
}
