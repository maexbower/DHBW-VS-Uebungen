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
	public void addMessage(String nickname, String message)
	{
		switch(message){
		case "/shutdown":
			addMessage("Server", nickname+" is shutting down the server. Bye.");
			stopServer();
			break;	
		}
		Message m = new Message();
		m.setMessage(message);
		m.setNickName(nickname);
		if(countObservers()>0){
            setChanged();
            notifyObservers(m);
        } 
	}
	public void startServer()
	{
		try {
			listenSocket = new ServerSocket(getPort());
			System.out.println("Server wartet auf Port: "+getPort());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		while(true)
		{
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
	}
	public void stopServer()
	{
		try {
			listenSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
