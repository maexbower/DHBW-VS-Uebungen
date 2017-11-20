package dhbw.it15002.rmi;

import dhbw.it15002.tcp.Connection;
import dhbw.it15002.tcp.Message;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Terminal extends Thread implements Serializable {
    private RMIClient client;
    public Terminal()
        {
            super();
        }
    public void run()
    {
        printMessage(Connection.WELCOME_STRING);
        OUT:
        while (true) {
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
                            break OUT;
                        case "information":
                            client.getServerInterface().addMessage(new Message(Message.messageType.Controll, client.getNickName(), Connection.controllCommands.Information, m_split[1]));
                            break;
                        case "shutdown":
                            client.getServerInterface().addMessage(new Message(Message.messageType.Controll, client.getNickName(), Connection.controllCommands.Shutdown, m_split[1]));
                            break;
                        default:
                            client.getServerInterface().addMessage(new Message(Message.messageType.Controll, client.getNickName(), Connection.controllCommands.Information, "Unknown Command " + m_split[0] + " " + m_split[1]));
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            try {
                //Check if we need to get the Nick Name:
                if (client.getNickName().equals("unknown")) {
                    client.setNickName(message);
                    client.getServerInterface().addMessage(new Message(Message.messageType.Controll, client.getNickName(), Connection.controllCommands.Connect));
                } else {
                    client.getServerInterface().addMessage(new Message(Message.messageType.Message, client.getNickName(), message));
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        client.unregisterClient(client);
    }
    public void printMessage(String text)
    {
        System.out.println(text);
    }
    public static void main(String[] args) {
        Terminal term = new Terminal();
        try {
            RMIClient client = new RMIClient(term);
            term.setClient(client);
            term.start();
            client.registerClient(client);
            Message m = new Message(Message.messageType.Message, "Server", "This is a RMI Test Message");
            client.getServerInterface().addMessage(m);
            //client.unregisterClient(client);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
    public RMIClient getClient() {
        return client;
    }

    public void setClient(RMIClient client) {
        this.client = client;
    }
}
