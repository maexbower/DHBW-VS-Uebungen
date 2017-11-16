package dhbw.it15002.udp;
import java.net.*;
import java.io.*;
public class UDPClient {
	private int port;
	private int buffersize;
	private DatagramSocket udpSock = null;
	public UDPClient(int pBuffersize, int pPort)
	{
		setBuffersize(pBuffersize);
		setPort(pPort);
		try {
			udpSock = new DatagramSocket(getPort());
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
	public UDPClient(int pBuffersize)
	{
		setBuffersize(pBuffersize);
		setPort(0);
		try {
			udpSock = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getBuffersize() {
		return buffersize;
	}
	public void setBuffersize(int buffersize) {
		this.buffersize = buffersize;
	}
	public void sendText(String host, int hostport, String message)
	{
		byte bytes[] =  message.getBytes();
		if( bytes.length > getBuffersize())
		{
			System.err.println("Message passt nicht in Puffer!");
			return;
		}
		try {
			InetAddress aHost = InetAddress.getByName(host);
			DatagramPacket request = new DatagramPacket(bytes, bytes.length, aHost, hostport);
			udpSock.send(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		
	}
	
}
