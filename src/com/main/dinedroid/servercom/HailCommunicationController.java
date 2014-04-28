package com.main.dinedroid.servercom;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.main.dinedroid.models.Table;
import com.main.dinedroid.models.Waiter;
import com.main.dinedroid.server.main;

public class HailCommunicationController implements Runnable
{

	Socket mySocket;
	String [] commands;
	/**
	 * @param mySocket Socket from the listener
	 * @param commands String array of commands
	 */
	public HailCommunicationController(Socket mySocket, String[] commands)
	{
		this.mySocket = mySocket;
		this.commands = commands;
	}
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		/**
		 * If the command is Get_Hail, 
		 * Get the waiter ID, 
		 * query WaitersController to get hails using ID
		 * Write out the hails to the connected client
		 */
		if(commands[1].equals("Get_Hail"))
		{
			try
			{
				System.err.println("In [Hail]. Received waiterId: "+commands[2]);
				int waiterId = Integer.parseInt(commands[2]);
				ArrayList<Table> hails = main.wc.getQueue(waiterId);
				ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
				out.writeObject(hails);
				out.close();
				mySocket.close();
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		/**
		 * If the command is Set_Hail, 
		 * Get the table ID, 
		 * query WaitersController to add hail using ID
		 * Write out the result to the client
		 */
		else if(commands[1].equals("Set_Hail"))
		{
			int tableId = Integer.parseInt(commands[2]);
			Waiter w = main.tc.findTable(tableId).getWaiter();
			boolean result = main.wc.hailWaiter(tableId);
			System.out.println("HAIL RESULT: "+result);
			try {
				ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
				out.writeBoolean(result);
				out.close();
				mySocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				/*
				 * If result cannot be sent back to client,
				 * ROLLBACK by removing hail
				 */
				if(w!=null)
				{
					main.wc.removeHail(tableId, w.getId());
				}
			}
		}
		/**
		 * If the command is Remove_Hail, 
		 * Get the waiter ID, 
		 * Get the table ID
		 * query WaitersController to remove hails using IDs
		 * Write out the hails to the connected client
		 */
		else
		{
			int waiterId = Integer.parseInt(commands[2]);
			int tableId = Integer.parseInt(commands[3]);
			Boolean result = main.wc.removeHail(waiterId, tableId);
			try {
				ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
				out.flush();
				out.writeBoolean(result);
				out.flush();
				out.close();
				mySocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				/*
				 * If client is down, ROLLBACK by re-adding hail
				 */
				main.wc.hailWaiter(tableId);
			}
		}
	}
	

}
