package dhbw.it15002.rmi;
import java.rmi.*;

public class RMIClient {
	public static void main(String args[]){
		Hello sampleHello = null;
		try{
			sampleHello = (Hello) Naming.lookup("//localhost/dhbw.it15002.rmi.HelloServant");
			
			sampleHello.printHello();
		} catch(RemoteException e) {
			System.out.println(e.getMessage());
		} catch(Exception e) {
			System.out.println("Client:" + e.getMessage());
		}
	}
}
