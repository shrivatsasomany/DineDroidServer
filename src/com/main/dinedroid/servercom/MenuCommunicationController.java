package com.main.dinedroid.servercom;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.main.dinedroid.menu.FoodItem;
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
			FoodItem oldItem = main.mc.getItem(itemId);
			boolean oldStatus = oldItem.isAvailable();
			String status = commands[3];
			boolean availability = Boolean.parseBoolean(status);
			boolean result = main.mc.setAvailability(itemId, availability);
			try {
				ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
				out.writeBoolean(result);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				/*
				 * If result cannot be delivered to client:
				 * ROLLBACK by setting old availibility
				 */
				oldItem.setAvailable(oldStatus);
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
