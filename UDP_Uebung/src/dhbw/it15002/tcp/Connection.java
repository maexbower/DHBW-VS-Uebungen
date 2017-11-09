package dhbw.it15002.tcp;
import java.net.*;
import java.util.Observable;
import java.util.Observer;
import java.io.*;
public class Connection extends Thread implements Observer{
	public static final String EXIT_COMMAND = "stop";	
	public static final String WELCOME_STRING = "Hello Please send me your Nickname: ";	
	public static final String EXIT_STRING = "Bye. Your connection is closed.\r\n";	

	private DataInputStream in;
	private DataOutputStream out;
	private Socket clientSocket;
	private String nickName = "unknown";
	private TCPServer parent;
	public Connection(Socket pClientSocket, TCPServer pParent)
	{
		System.out.println("Neue Verbindung aufgebaut.");
		setClientSocket(pClientSocket);
		setParent(pParent);
		try {
			setIn(new DataInputStream(getClientSocket().getInputStream()));
			setOut(new DataOutputStream(getClientSocket().getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		this.start();
	}
	public void run()
	{
			try{
				//send welcome Message
				sendData(WELCOME_STRING);	
				while(true)
				{
					//read message
					String message = new String(receiveData(in));
					
					//Check incomming Message if its the escape message
					if (message.length() == EXIT_COMMAND.length() && message.toLowerCase().equals(EXIT_COMMAND)) {
			            System.out.println("Exiting.");
			            sendData(EXIT_STRING);
			            parent.addMessage("Info", getNickName()+" has left the chat.");
						if(clientSocket != null)
						{
							try {
								clientSocket.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
			            return;
			         }
					//Check if we need to get the Nick Name:
					if(nickName == "unknown")
					{
						setNickName(message);
						parent.addMessage("Info", getNickName()+" has joined the chat.");
					}else{
						parent.addMessage(getNickName(), message);
					}
					
				}
			}finally{
				try {
					clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

	}
	public String receiveData(DataInputStream data)
	{
		String message = "";
		try {
			message = data.readLine();
			System.out.println("Received from "+getNickName()+": "+message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
	public void sendData(String data)
	{
		try {
			out.writeChars(data);
			System.out.println("Send: "+data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void addMessage(String nickname, String message)
	{
		sendData(nickname+":"+message+"\r\n");
	}
	public DataInputStream getIn() {
		return in;
	}
	public void setIn(DataInputStream in) {
		this.in = in;
	}
	public DataOutputStream getOut() {
		return out;
	}
	public void setOut(DataOutputStream out) {
		this.out = out;
	}
	public Socket getClientSocket() {
		return clientSocket;
	}
	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public TCPServer getParent() {
		return parent;
	}
	public void setParent(TCPServer parent) {
		this.parent = parent;
	}
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		Message m = (Message)arg;
		addMessage(m.getNickName(), m.getMessage());
	}

	
}
