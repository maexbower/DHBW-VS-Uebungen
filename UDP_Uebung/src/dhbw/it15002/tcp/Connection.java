package dhbw.it15002.tcp;
import java.net.*;
import java.util.Observable;
import java.util.Observer;
import java.io.*;
public class Connection extends Thread implements Observer{
	public static final String EXIT_COMMAND = "stop";	
	public static final String WELCOME_STRING = "Hello Please send me your Nickname: ";	
	public static final String EXIT_STRING = "Bye. Your connection is closed.\r\n";
	public static enum controllCommands {Disconnect, Information, Shutdown, Connect};
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
					if(getClientSocket().isClosed())
					{
						break;
					}

					//read message
					String message = receiveData(getIn());

					//Check incomming Message if its the escape message
					if (message.length() == EXIT_COMMAND.length() && message.toLowerCase().equals(EXIT_COMMAND)) {
			            disconnect();
			         }
			         //controll commands start with an /
					if(message.length()>1 && message.startsWith("/"))
					{
						String[] m_split = message.split(" ",2);
						//workauround if only the command is passed
						if(m_split.length<2)
						{
							String[] new_m_split = {String.copyValueOf(m_split[0].toCharArray()), ""};
							m_split = new_m_split;
						}
						//cut leading slash
						m_split[0] = m_split[0].substring(1).toLowerCase();
						switch(m_split[0])
						{
							case "disconnect":
								disconnect(m_split[1]);
								break;
							case "information":
								parent.addMessage(new Message(Message.messageType.Controll, getNickName(), controllCommands.Information, m_split[1]));
								break;
							case "shutdown":
								parent.addMessage(new Message(Message.messageType.Controll, getNickName(), controllCommands.Shutdown, m_split[1]));
								break;
							default:
								parent.addMessage(new Message(Message.messageType.Controll, getNickName(), controllCommands.Information, "Unknown Command "+m_split[0]+" "+m_split[1]));
						}

					}


					//Check if we need to get the Nick Name:
					if(nickName.equals("unknown"))
					{
						setNickName(message);
						parent.addMessage(new Message(Message.messageType.Controll, getNickName(), controllCommands.Connect));
					}else{
						parent.addMessage(new Message(Message.messageType.Message,getNickName(), message));
					}
					
				}
			}finally{
				try {
					if(!clientSocket.isClosed())
						clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

	}
	public void disconnect()
	{
		disconnect("");
	}
	public void disconnect(String message)
	{
		sendData(EXIT_STRING);
		parent.addMessage(new Message(Message.messageType.Controll, getNickName(), controllCommands.Disconnect, message));
		parent.deleteObserver(this);
		if(clientSocket != null)
		{
			try {
				if(!clientSocket.isClosed())
					clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return;
	}
	public String receiveData(DataInputStream data)
	{
		BufferedReader br = null;
		String message = "";
		try {
			br = new BufferedReader(new InputStreamReader(getIn()));
			message = br.readLine();
			//message = data.readLine();
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
			getOut().writeBytes(data);
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
		switch(m.getType())
		{
			case Message:
				addMessage(m.getNickName(), m.getMessage());
				break;
			case Controll:
				switch(m.getCommand()){
					case Information:
						addMessage(m.getNickName(), m.getMessage());
						break;
					case Disconnect:
						disconnect();
						break;
					default:
						addMessage(m.getNickName(), "Server did send an unknown command");
				}

		}


	}

	
}
