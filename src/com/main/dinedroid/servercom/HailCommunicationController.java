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
	public HailCommunicationController(Socket mySocket, String[] commands)
	{
		this.mySocket = mySocket;
		this.commands = commands;
	}
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
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
					main.wc.removeQueueTable(tableId, w.getId());
				}
			}
		}
		else
		{
			main.wc.removeQueueTable(Integer.parseInt(commands[2]), Integer.parseInt(commands[3]));
			try {
				mySocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
