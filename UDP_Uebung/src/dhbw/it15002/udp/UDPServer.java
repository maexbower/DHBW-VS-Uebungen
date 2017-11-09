package dhbw.it15002.udp;
import java.net.*;
import java.io.*;

public class UDPServer extends Thread{
	private int port;
	private int buffersize;
	private DatagramSocket udpSock = null;
	public UDPServer(int pPort, int pBuffersize)
	{
		setPort(pPort);
		setBuffersize(pBuffersize);
		this.start();
	}
	public void run(){
		this.startServer();
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
	public void startServer()
	{
		try {
			udpSock = new DatagramSocket(getPort());
			printStartInfo();
			while(true)
			{
				byte buffer[] = new byte[buffersize];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				udpSock.receive(request);
				printPacketInfo(request);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			stopServer();
		}
	}
	public void stopServer()
	{
		if(udpSock != null)
		{
			udpSock.close();
		}
	}
	public void printStartInfo()
	{
		System.out.println("UDP Server gestartet mit auf Port: "+getPort());
	}
	public void printPacketInfo(DatagramPacket pkg)
	{
		System.out.println("UDP Packet Received from: "+pkg.getAddress()+" with content: "+new String(pkg.getData()));
	}
}
