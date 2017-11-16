package dhbw.it15002.tcp;
import java.net.*;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.io.*;
import dhbw.it15002.rmi.*;
public class TCPServer extends Observable{
	private int port;
	private ServerSocket listenSocket;
	private RMIServer rmis;
	public TCPServer(int pPort)
	{
		setPort(pPort);
		startRMIServer();
		startServer();
		stopServer();
	}

	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public void sendMessage(Message m)
	{
		if(countObservers()>0){
			setChanged();
			notifyObservers(m);
		}
	}
	public void addMessage(Message m)
	{
		//Test if controll message
		try {
			switch (m.getType()) {
				case Controll:
					switch (m.getCommand()) {
						case Shutdown:
							sendMessage(new Message(Message.messageType.Message, "Server", m.getNickName() + " is shutting down the server. Bye."));
							sendMessage(new Message(Message.messageType.Controll, "Server", Connection.controllCommands.Disconnect));
							stopServer();
							break;
						case Disconnect:
							sendMessage(new Message(Message.messageType.Message, "Server", m.getNickName() + "  has disconnected. Bye."));
							break;
						case Information:
							sendMessage(new Message(Message.messageType.Message, "Server", m.getNickName() + " is saying: " + m.getMessage()));
							break;
						case Connect:
							sendMessage(new Message(Message.messageType.Message, "Server", m.getNickName() + " has joined."));
							break;
						default:
							sendMessage(new Message(Message.messageType.Message, "Server", m.getNickName() + " has used a command that is not implemented yet."));
					}
					break;
				case Message:
					sendMessage(new Message(Message.messageType.Message, m.getNickName(), m.getMessage()));
					break;
			}
		}catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}
	public void startServer()
	{
		try {
			listenSocket = new ServerSocket(getPort());
			System.out.println("Server wartet auf Port: "+getPort());
			while(true)
			{
				if(listenSocket.isClosed())
				{
					System.out.println("Port ist geschlossen");
					break;
				}
				Socket clientSocket = null;
				try {
					clientSocket = listenSocket.accept();
					Connection c = new Connection(clientSocket, this);
					this.addObserver(c);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(!listenSocket.isClosed())
			{
				try {
					this.deleteObservers();
					listenSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void stopServer()
	{
		try {
			rmis.stopServer();
			this.deleteObservers();
			listenSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void startRMIServer()
	{
		System.out.println("try to start RMI Server");

		try {
			RMIInterfaceServant servant = new RMIInterfaceServant(this);
			rmis = new RMIServer(servant, RMIInterfaceServant.class.getCanonicalName());
			rmis.start();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int serverport = 5000;
		TCPServer server = new TCPServer(serverport);
	}
	
}
