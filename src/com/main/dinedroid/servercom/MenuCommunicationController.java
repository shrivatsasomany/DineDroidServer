package com.main.dinedroid.servercom;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.main.dinedroid.menu.Menu;
import com.main.dinedroid.server.main;

public class MenuCommunicationController implements Runnable {
	Socket mySocket;
	String [] commands;
	public MenuCommunicationController(Socket mySocket, String[] commands)
	{
		this.mySocket = mySocket;
		this.commands = commands;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(commands[1].equals("Set_Item_Status"))
		{
			int itemId = Integer.parseInt(commands[2]);
			String status = commands[3];
			boolean availability = Boolean.parseBoolean(status);
			System.out.println(main.mc.setAvailability(itemId, availability));
			try {
				mySocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(commands[1].equals("Get_Menu"))
		{
			Menu temp = main.mc.getMenu();
			try {
				ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
				out.writeObject(temp);
				out.close();
				mySocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else
		{
			
		}
	}

}
