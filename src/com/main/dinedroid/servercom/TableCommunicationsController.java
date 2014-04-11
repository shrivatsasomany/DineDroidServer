package com.main.dinedroid.servercom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.main.dinedroid.models.Order;
import com.main.dinedroid.server.main;

public class TableCommunicationsController implements Runnable {

	Socket mySocket;
	String [] commands;
	ObjectInputStream in;
	public TableCommunicationsController(Socket mySocket, String[] commands, ObjectInputStream in)
	{
		this.mySocket = mySocket;
		this.commands = commands;
		this.in = in;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(commands[1].equals("Open_Table"))
		{
			if(commands.length == 3)
			{
				int tableId = Integer.parseInt(commands[2]);
				main.tc.openTable(tableId);
			}
			else
			{
				int tableId = Integer.parseInt(commands[2]);
				String customerName = commands[3];
				main.tc.openTable(tableId, customerName);
			}
		}
		if(commands[1].equals("Open_Temp_Table"))
		{
			main.tc.createTempTable(); /* Also opens*/
		}
		else if(commands[1].equals("Close_Table"))
		{
			int tableId = Integer.parseInt(commands[2]);
			main.tc.closeTable(tableId);
			
		}
		else if(commands[1].equals("Get_Table_Order"))
		{
			int tableId = Integer.parseInt(commands[2]);
			Order temp = main.tc.getTableOrder(tableId);
			try {
				ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
				out.writeObject(temp);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(commands[1].equals("Set_Table_Order"))
		{
			int tableId = Integer.parseInt(commands[2]);
			try {
				Order myOrder = (Order)in.readObject();
				main.tc.setTableOrder(tableId, myOrder);
				in.close();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(commands[1].equals("Remove_Table_Order"))
		{
			int tableId = Integer.parseInt(commands[2]);
			main.tc.removeTableOrder(tableId);
		}
		else if(commands[1].equals("Set_Order_Status"))
		{
			if(commands.length == 3)
			{
				int tableId = Integer.parseInt(commands[2]);
				int status = Integer.parseInt(commands[3]);
				main.tc.setOrderStatus(tableId, status);
			}
			else
			{
				int tableId = Integer.parseInt(commands[2]);
				int status = Integer.parseInt(commands[3]);
				String notes = commands[4];
				main.tc.setOrderStatus(tableId, status, notes);
			}
		}
		else
		{
			//Default not used for easier understandability of code above. 
		}
		
		try {				
			mySocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
