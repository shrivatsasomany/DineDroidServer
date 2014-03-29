package com.main.dinedroid.servercom;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.main.dinedroid.models.Table;
import com.main.dinedroid.server.main;

public class WaiterCommunicationController implements Runnable {

	Socket mySocket;
	String [] commands;
	public WaiterCommunicationController(Socket mySocket, String[] commands)
	{
		this.mySocket = mySocket;
		this.commands = commands;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(commands[1].equals("Assign_Waiter"))
		{
			int tableId = Integer.parseInt(commands[2]);
			int waiterId = Integer.parseInt(commands[3]);
			main.wc.assignWaiter(tableId, waiterId);
			try {
				mySocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(commands[1].equals("Get_Assigned_Tables"))
		{
			int waiterId = Integer.parseInt(commands[2]);
			ArrayList<Table> temp = main.wc.getAssignedTables(waiterId);
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
