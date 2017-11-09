package dhbw.it15002.tcp;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.io.*;
public class TCPServer extends Observable{
	private int port;
	private ServerSocket listenSocket;
	public TCPServer(int pPort)
	{
		setPort(pPort);
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
		switch(m.getType()){
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
						sendMessage(new Message(Message.messageType.Message, "Server", m.getNickName() + " is saying: "+m.getMessage()));
						break;
					case Connect:
						sendMessage(new Message(Message.messageType.Message, "Server", m.getNickName() + " has joined."));
						break;
					default:
						sendMessage(new Message(Message.messageType.Message, "Server", m.getNickName() + " has used a command that is not implemented yet."));
				}
				break;
			case Message:
					sendMessage(new Message(Message.messageType.Message,m.getNickName(), m.getMessage()));
				break;
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
			this.deleteObservers();
			listenSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
