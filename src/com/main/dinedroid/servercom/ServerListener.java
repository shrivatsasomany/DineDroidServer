package com.main.dinedroid.servercom;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

public class ServerListener extends Thread 
{
	/**
	 * ALL commands are of the form:<br>
	 * <b>Controller</b>||<b>Command</b>||<b>ID</b>||<b>ID</b><br>
	 * 1:Asks the server to invoke the appropriate controller<br>
	 * 2:Asks the controller to invoke the appropriate command<br>
	 * 3:Any IDs<br>
	 * 4:Any IDs<br>
	 * All this gets put into the commands array in the same order, after being split.
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			ServerSocket listen = new ServerSocket(4322);

			/**
			 * Start listening on port 4322
			 */
			while (true) {
				Socket socket = listen.accept(); //Accept a connection
				String clientInetAddr = socket.getInetAddress().toString();
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); //Get the input stream
				System.out.println("Connected to: " + clientInetAddr);
				try
				{

					String getCommand = (String) in.readObject(); //Read in the string command
					String[] commands = getCommand.split("\\|\\|"); //Split the command to get the distinct entities
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

