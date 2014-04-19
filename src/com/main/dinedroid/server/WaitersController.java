package com.main.dinedroid.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.main.dinedroid.models.Table;
import com.main.dinedroid.models.Waiter;
import com.main.dinedroid.serverlistener.TableChangeListener;
import com.main.dinedroid.serverlistener.WaiterChangeListener;

public class WaitersController implements Runnable 
{

	private ArrayList<Waiter> waiters;
	private Integer latestId = 0;

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub	
		waiters = new ArrayList<Waiter>();
		loadWaiters();
		try {
			loadIdCounter();
		} catch (Exception e) {
			/*
			 * Recover latestId if id counter file is missing/corrupt
			 */
			for(Waiter w : waiters)
			{
				if(w.getId() > latestId)
				{
					latestId = w.getId();
				}
			}
			++latestId;
			saveIdCounter();
		}
	}

	/**
	 * Add a new waiter to the list
	 * The ID is assigned automatically
	 * @param fname First Name
	 * @param lname Last Name
	 * @return
	 */
	public boolean createWaiter(String fname, String lname) /* String name */
	{
		boolean result = waiters.add(new Waiter(latestId, fname, lname));
		++latestId;
		saveIdCounter();
		callChangedListeners("Waiter");
		saveWaiters();
		return result;
	}

	/**
	 * Remove waiter based on ID
	 * @param id An ID
	 * @return true if waiter was found and deleted, false otherwise
	 */
	public boolean removeWater(int id) /* int WaiterId */
	{
		Waiter e = findWaiter(id);
		if(e!=null)
		{
			boolean result = waiters.remove(e);
			callChangedListeners("Waiter");
			saveWaiters();
			return result;
		}
		return false;
	}

	/**
	 * Find and return a waiter
	 * @param waiterId An ID
	 * @return A waiter object if found, null otherwise
	 */
	public Waiter findWaiter(int waiterId)
	{
		for(Waiter w:waiters)
		{
			if(w.getId() == waiterId)
			{
				return w;
			}
		}
		return null;
	}

	/**
	 * Hail a waiter
	 * Find the table through the table ID, hail the waiter attached to the table
	 * @param tableId An ID
	 * @return true if table and waiter were found and hail was added, false otherwise
	 */
	public synchronized boolean hailWaiter(int tableId)
	{
		Table t = main.tc.findTable(tableId);
		if(t!=null)
		{
			Waiter w = t.getWaiter();
			boolean result = w.addHail(t);
			return result;
		}
		return false;
	}

	/**
	 * Remove a hail from a waiter's hail queue
	 * @param tableId A table ID to remove
	 * @param waiterId A waiter ID to remove from
	 * @return true if waiter found and hail removed, false otherwise
	 */
	public synchronized boolean removeHail(int tableId, int waiterId)
	{
		Waiter w = findWaiter(waiterId);
		if(w != null)
		{
			return w.removeHail(tableId);
		}
		return false;
	}

	/**
	 * Get the hail queue of a waiter by ID
	 * @param waiterId A waiter ID
	 * @return list of hails if waiter is found, null otherwise
	 */
	public ArrayList<Table> getQueue(int waiterId)
	{
		Waiter w = findWaiter(waiterId);
		if(w != null)
		{
			return w.getHailQueue();
		}
		return null;
	}

	/**
	 * Detach a waiter from a table
	 * Find waiter with ID, remove table and hail with the table ID
	 * @param waiterId A waiter ID to detach
	 * @param tableId A table ID to detach from
	 * @return
	 */
	public boolean unassignWaiter(int waiterId, int tableId)
	{
		Waiter w = findWaiter(waiterId);
		if(w != null)
		{
			callChangedListeners("Waiter");
			w.removeHail(tableId);
			return (w.removeTable(tableId));
		}
		else
		{
			return false;
		}

	}

	/**
	 * Assign a waiter to a table
	 * Find a table using the ID, find a waiter using the ID, assign waiter to table
	 * @param tableId A table ID to attach to
	 * @param waiterId A waiter ID to attach to
	 * @return true if table and waiter are found and assigned, false otherwise
	 */
	public boolean assignWaiter(int tableId, int waiterId)
	{
		Table t = main.tc.findTable(tableId);
		Waiter w = findWaiter(waiterId);

		if(t!=null && w != null)
		{
			t.setWaiter(w);
			w.addTable(t);
			callChangedListeners("Waiter");
			return true;
		}
		return false;
	}

	/**
	 * Get all the tables a waiter is assigned to
	 * @param waiterId An ID
	 * @return list of tables if waiter is found, null otherwise
	 */
	public ArrayList<Table> getAssignedTables(int waiterId)
	{
		Waiter w = findWaiter(waiterId);
		if(w != null)
		{
			return w.getTables();
		}

		return null;
	}

	/**
	 * Get the list of waiters
	 * @return the list of waiters
	 */
	public ArrayList<Waiter> getAllWaiters()
	{
		return waiters;
	}

	/**
	 * Load the waiters from file
	 * @return true if loaded successfully, false otherwise
	 */
	public boolean loadWaiters()
	{
		/* load from DB if temp file does not exist */
		try {
			ObjectInputStream is = new ObjectInputStream(
					new FileInputStream("waiters.dat"));
			waiters = (ArrayList<Waiter>)is.readObject();
			is.close();
			return true;

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Save the waiters to a file
	 * @return true if saved successfully, false otherwise
	 */
	public boolean saveWaiters()
	{
		try {
			ObjectOutputStream os = new ObjectOutputStream(
					new FileOutputStream("waiters.dat"));
			os.writeObject(waiters);
			os.close();
			return true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Unimplemented
	 */
	public void liveBackup()
	{
		/* save to temp file */
	}

	/**
	 * Save the ID counter to a file
	 * @return true if saved successfully, false otherwise
	 */
	public boolean saveIdCounter() {
		try {
			ObjectOutputStream os = new ObjectOutputStream(
					new FileOutputStream("waiterId.dat"));
			os.writeObject(latestId);
			os.close();
			return true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Load the ID counter from a file
	 * @return true if loaded successfully, false otherwise
	 * @throws Exception IOException or ClassNotFoundException
	 */
	public boolean loadIdCounter() throws Exception {
		try {
			ObjectInputStream is = new ObjectInputStream(
					new FileInputStream("waiterId.dat"));
			latestId = (Integer)is.readObject();
			is.close();
			return true;

		} catch (IOException | ClassNotFoundException e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * List of changed listeners for waiters
	 */
	private ArrayList<WaiterChangeListener> changedListeners = new ArrayList <WaiterChangeListener>();
	/**
	 * Add a change listener to the list of listeners
	 * @param l A change listener
	 */
	public void addChangedListener(WaiterChangeListener l)
	{
		changedListeners.add(l);
	}

	/**
	 * Call the changed listeners in the list and invoke DoSomething for each
	 * @param changeType A string
	 */
	private void callChangedListeners(String changeType)
	{
		for (int i = 0;i < changedListeners.size();++i)
		{
			changedListeners.get(i).DoSomething(changeType);
		}
	}

}
