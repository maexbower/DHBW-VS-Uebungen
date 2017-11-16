package dhbw.it15002.rmi;
import dhbw.it15002.tcp.Connection;
import dhbw.it15002.tcp.Message;

import java.rmi.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class RMIClient implements Observer {
	private String nickName = "unknown";
	private RMIInterface serverInterface = null;
	public static void main(String args[]){
		new RMIClient();
	}
	public RMIClient()
	{

		try{
			serverInterface = (RMIInterface) Naming.lookup("//localhost/dhbw.it15002.rmi.RMIInterfaceServant");
			Message m = new Message(Message.messageType.Message, "Server", "This is a RMI Test Message");
			serverInterface.addMessage(m);
			serverInterface.registerObserver(this);
			startTerminal();

		} catch(RemoteException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Client:" + e.getMessage());
		}finally {
			try {
				serverInterface.removeObserver(this);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	private void startTerminal() {
		while(true){
			Scanner scanner = new Scanner(System.in);
			System.out.print("> ");
			String message = scanner.next();
			if (message.length() > 1 && message.startsWith("/")) {
				String[] m_split = message.split(" ", 2);
				//workauround if only the command is passed
				if (m_split.length < 2) {
					String[] new_m_split = {String.copyValueOf(m_split[0].toCharArray()), ""};
					m_split = new_m_split;
				}
				//cut leading slash
				m_split[0] = m_split[0].substring(1).toLowerCase();
				try {
					switch (m_split[0]) {
						case "disconnect":
							break;
						case "information":
							serverInterface.addMessage(new Message(Message.messageType.Controll, getNickName(), Connection.controllCommands.Information, m_split[1]));
							break;
						case "shutdown":
							serverInterface.addMessage(new Message(Message.messageType.Controll, getNickName(), Connection.controllCommands.Shutdown, m_split[1]));
							break;
						default:
							serverInterface.addMessage(new Message(Message.messageType.Controll, getNickName(), Connection.controllCommands.Information, "Unknown Command " + m_split[0] + " " + m_split[1]));
					}
				}catch (RemoteException e)
				{
					e.printStackTrace();
				}

			}

			try{
				//Check if we need to get the Nick Name:
				if (nickName.equals("unknown")) {
					setNickName(message);
					serverInterface.addMessage(new Message(Message.messageType.Controll, getNickName(), Connection.controllCommands.Connect));
				} else {
					serverInterface.addMessage(new Message(Message.messageType.Message, getNickName(), message));
				}
			}catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}



	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	@Override
	public void update(Observable observable, Object o) {
		Message m = (Message)o;
		System.out.println("RMIClient: Got message from Server:"+m.getMessage());
	}
}
