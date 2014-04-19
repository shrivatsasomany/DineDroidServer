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
			boolean result = main.wc.assignWaiter(tableId, waiterId);
			
			try {
				ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
				out.writeBoolean(result);
				out.close();
				mySocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				/*
				 * If client times out or crashes, and server can't return the result:
				 * ROLLBACK by unassigning waiter. 
				 */
				main.wc.unassignWaiter(waiterId, tableId);
			}
		}
		/*
		 * Returns the tables assigned to a particular waiter
		 */
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
		/*
		 * Unassigns a waiter from a table
		 */
		else if(commands[1].equals("Unassign_Waiter"))
		{
			int tableId = Integer.parseInt(commands[2]);
			Table t = main.tc.findTable(tableId);
			int waiterId = t.getWaiter().getId();
			boolean result = main.wc.unassignWaiter(waiterId, tableId);
			try {
				ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
				out.writeBoolean(result);
				out.close();
				mySocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				/*
				 * If client times out or crashes, and server can't return the result:
				 * ROLLBACK by assigning old waiter again. 
				 */
				main.wc.assignWaiter(tableId, waiterId);
			}
		}
		else
		{
			
		}
	}

}
