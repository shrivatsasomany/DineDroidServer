package com.main.dinedroid.servercom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.main.dinedroid.models.Order;
import com.main.dinedroid.models.Restore;
import com.main.dinedroid.models.Table;
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
		/**
		 * To open a table, given a specified table ID from the client
		 */
		if(commands[1].equals("Open_Table"))
		{
			int tableId = Integer.parseInt(commands[2]);
			Table t = main.tc.findTable(tableId);
			if(t!=null && !t.isOccupied())
			{
				if(commands.length == 3)
				{
					boolean result = main.tc.openTable(tableId);
					try {
						ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
						out.writeObject(new Restore(false, null, null));
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						/*
						 * If result cannot be delivered to client:
						 * ROLLBACK by closing opened table. 
						 */
						main.tc.closeTable(tableId);
					}
				}
				/**
				 * To open a table, given a specified table ID from the client
				 * and a customer name (for reservations under a person)
				 */
				else
				{
					String customerName = commands[3];
					boolean result = main.tc.openTable(tableId, customerName);
					try {
						ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
						out.writeObject(new Restore(false, null, null));
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						/*
						 * If result cannot be delivered to client:
						 * ROLLBACK by closing opened table. 
						 */
						main.tc.closeTable(tableId);
					}
				}
			}
			else if(t!=null)
			{
				try {
					ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
					out.writeObject(new Restore(true, t.getOrder(), t.getWaiter().getId()));
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					/*
					 * If result cannot be delivered to client:
					 * ROLLBACK by closing opened table. 
					 */
					main.tc.closeTable(tableId);
				}
			}
		}
		/**
		 * To dynamically create and open a temporary table
		 */
		if(commands[1].equals("Open_Temp_Table"))
		{
			int id = main.tc.createTempTable();
			try {
				ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
				out.writeInt(id);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				/*
				 * If result cannot be delivered to client:
				 * ROLLBACK by closing opened temp table. 
				 */
				main.tc.closeTable(id);
			}
		}
		/**
		 * To close a table and settle any unpaid orders. 
		 * This is done through a table ID
		 */
		else if(commands[1].equals("Close_Table"))
		{
			int tableId = Integer.parseInt(commands[2]);
			Table oldTable = main.tc.findTable(tableId);
			boolean result = main.tc.closeTable(tableId);
			try {
				ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
				out.writeBoolean(result);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				/*
				 * If result cannot be delivered to client:
				 * ROLLBACK by completely replacing the table in question with it's
				 * old values (that we pulled up in var oldTable before closing the table). 
				 */
				main.tc.insertTable(oldTable);
			}

		}
		/**
		 * To get the current order attached to a table, given the table id
		 */
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
		/**
		 * To add an order to a table. 
		 */
		else if(commands[1].equals("Set_Table_Order"))
		{
			int tableId = Integer.parseInt(commands[2]);
			Table t = main.tc.findTable(tableId);
			Order oldOrder = t.getOrder(); //Returns null if current order being set is the first one for the table. 

			try {
				Order myOrder = (Order)in.readObject();
				boolean result = main.tc.setTableOrder(tableId, myOrder);
				in.close();
				ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
				out.writeBoolean(result);
				out.close();

			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				/*
				 * There was a previous order, so restore old order
				 * If you set null, that means there was no previous order,
				 * this still remains valid.
				 */
				main.tc.setTableOrder(tableId, oldOrder);
			}
		}
		/**
		 * To remove an order from a table without billing it. 
		 * Given a table ID
		 */
		else if(commands[1].equals("Remove_Table_Order"))
		{
			int tableId = Integer.parseInt(commands[2]);
			Order oldOrder = main.tc.findTable(tableId).getOrder(); //Returns null if table has no order or wasn't even open.
			boolean result = main.tc.removeTableOrder(tableId);
			try {
				ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
				out.writeBoolean(result);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				/*
				 * If result cannot be delivered to client:
				 * ROLLBACK by restoring old order
				 */
				main.tc.setTableOrder(tableId, oldOrder);
			}
		}

		/**
		 * To set the status of an order
		 * Given a table ID and order status ID
		 */
		else if(commands[1].equals("Set_Order_Status"))
		{
			int tableId = Integer.parseInt(commands[2]);
			int oldStatus = main.tc.findTable(tableId).getOrderStatus();
			String oldNotes = main.tc.findTable(tableId).getOrder().getOrderNotes(); //Null if there were no notes for the order
			int status = Integer.parseInt(commands[3]);
			boolean result;
			if(commands.length == 3) //If there are no notes included in status
			{
				result = main.tc.setOrderStatus(tableId, status);

			}
			else //If there ARE notes attached to the order status
			{
				String notes = commands[4];
				result = main.tc.setOrderStatus(tableId, status, notes);
			}
			try {
				ObjectOutputStream out = new ObjectOutputStream(mySocket.getOutputStream());
				out.writeBoolean(result);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				/*
				 * If result cannot be delivered to client:
				 * ROLLBACK by restoring old order status and notes
				 */
				main.tc.setOrderStatus(tableId, oldStatus, oldNotes);
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
