package dhbw.it15002.udp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UDPempf {
	public static final String EXIT_COMMAND = "exit";	
	public static void main(String[] args) {
		int serverport = 5000;
		int buffersize = 255;
		UDPServer udp1 = new UDPServer(serverport, buffersize);
		UDPClient udpc1 = new UDPClient(buffersize);
		udpc1.sendText("localhost", serverport, "Connection is now open.");
		chatconsole(udpc1, "localhost", serverport);
	}
	public static void chatconsole(UDPClient socket, String host, int hostport)
	{
	      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	      System.out.println("Enter some text, or '" + EXIT_COMMAND + "' to quit");
	      while (true) {
	         System.out.print("> ");
	         String input;
			try {
				input = br.readLine();
		         System.out.println(input);
		         socket.sendText(host, hostport, input);
		         if (input.length() == EXIT_COMMAND.length() && input.toLowerCase().equals(EXIT_COMMAND)) {
		            System.out.println("Exiting.");
		            return;
		         }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	      }
	}
}
