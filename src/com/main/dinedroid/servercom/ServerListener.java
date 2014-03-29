package com.main.dinedroid.servercom;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

public class ServerListener extends Thread 
{
        public void run() {
            try {
                ServerSocket listen = new ServerSocket(4322);

                while (true) {
                    Socket socket = listen.accept();
                    String clientInetAddr = socket.getInetAddress().toString();
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    System.out.println("Connected to: " + clientInetAddr);
                    try
                    {
                    	String getCommand = (String) in.readObject();
                    	String[] commands = getCommand.split("\\|\\|");
                    	String getCase = commands[0];
                    	System.out.println(getCase);
                        System.out.println("Command <<"+commands[0]+">>");

                    	
                    	if(getCase.equals("Menu"))
                    	{
                    		Thread t = new Thread(new MenuCommunicationController(socket, commands));
                    		t.run();
                    	}
                    	
                    	if(getCase.equals("Hail"))
                    	{
                    		Thread t = new Thread(new HailCommunicationController(socket, commands));
                    		t.run();
                    	}
	
                    	if(getCase.equals("Waiter"))
                    	{
                    		Thread t = new Thread(new WaiterCommunicationController(socket, commands));
                    		t.run();
                    	}
                    	
                    	if(getCase.equals("Table"))
                    	{
                    		Thread t = new Thread(new TableCommunicationsController(socket, commands, in));
                    		t.run();
                    	}
                    	
                    }
                    
                    catch(IOException e)
                    {
                    	System.err.println(e.getMessage());
                    }

                    in.close();
                    //socket.close();
                }
            }
            catch (Exception e) {
                System.err.println("Error in run()");
                e.printStackTrace();
            }
            
        }
    }
   
