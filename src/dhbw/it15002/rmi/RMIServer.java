package dhbw.it15002.rmi;
import java.net.MalformedURLException;
import java.rmi.*;
import java.util.Observable;
import java.util.Observer;


public class RMIServer extends Thread implements Observer{
	 private Remote obj;
	 private String canName;
	 public static void main(String args[]) {
		 try {
			 //DEMO
			 Hello sampleHello = new HelloServant();
			 Naming.rebind("//localhost/dhbw.it15002.rmi.HelloPrinter", sampleHello);
			 
			 System.out.println("Hello Server gestartet.");

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	 }
	 public RMIServer(Remote pObj, String str)
	 {
		 obj = pObj;
		 canName = str;
	 }
	 public void run()
	 {
		 try {
			 System.out.println("Try to Bind "+canName+"");
			 Naming.rebind("//localhost/"+canName, obj);
			 System.out.println("RMI Server mit Namen "+canName+" gestartet");
		 }catch(MalformedURLException me)
		 {
			 me.printStackTrace();
		 }catch(RemoteException re)
		 {
			 re.printStackTrace();
		 }

	 }
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
