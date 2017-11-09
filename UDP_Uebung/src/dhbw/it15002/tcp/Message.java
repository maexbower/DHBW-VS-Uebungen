package dhbw.it15002.tcp;

public class Message {
	public enum messageType {Controll, Message};
	private String nickName;
	private String message;
	private Connection.controllCommands command;


	public Message(messageType pType, String pNickName, String pMessage)
	{
		setType(pType);
		setNickName(pNickName);
		setMessage(pMessage);
	}
	public Message(messageType pType, String pNickName, Connection.controllCommands pCommand)
	{
		setType(pType);
		setNickName(pNickName);
		setCommand(pCommand);
	}
	public Message(messageType pType, String pNickName, Connection.controllCommands pCommand, String pMessage)
	{
		setType(pType);
		setNickName(pNickName);
		setCommand(pCommand);
		setMessage(pMessage);
	}
	public Message()
	{
		setType(null);
		setNickName(null);
		setMessage(null);
		//allows empty message
	}
	public messageType getType() {
		return type;
	}

	public void setType(messageType type) {
		this.type = type;
	}
	private messageType type;
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Connection.controllCommands getCommand() {
		return command;
	}

	public void setCommand(Connection.controllCommands command) {
		this.command = command;
	}
}
