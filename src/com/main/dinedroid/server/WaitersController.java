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
		loadIdCounter();
		loadWaiters();
	}

	public boolean createWaiter(String fname, String lname) /* String name */
	{
		boolean result = waiters.add(new Waiter(latestId, fname, lname));
		++latestId;
		saveIdCounter();
		callChangedListeners("Waiter");
		saveWaiters();
		return result;
	}

	public boolean removeWater(int id) /* int WaiterId */
	{
		Waiter e = findWaiter(id);
		boolean result = waiters.remove(e);
		callChangedListeners("Waiter");
		saveWaiters();
		return result;
		/*update db*/
	}

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

	public synchronized boolean hailWaiter(int tableId)
	{
		Table t = main.tc.findTable(tableId);
		if(t!=null)
		{
			Waiter w = t.getWaiter();
			JOptionPane.showMessageDialog(null, "Table "+tableId+" Hailed "+w.getFName());
			return w.addHail(t);
		}
		return false;
	}

	public synchronized boolean removeQueueTable(int tableId, int waiterId)
	{
		Waiter w = findWaiter(waiterId);
		if(w != null)
		{
			return w.removeHail(tableId);
		}
		return false;
	}

	public ArrayList<Table> getQueue(int waiterId)
	{
		Waiter w = findWaiter(waiterId);
		if(w != null)
		{
			return w.getHailQueue();
		}
		return null;
	}

	public boolean unassignWaiter(int waiterId, int tableId)
	{
		Waiter w = findWaiter(waiterId);
		if(w != null)
		{
			callChangedListeners("Waiter");
			return (w.removeTable(tableId));
		}
		else
		{
			return false;
		}

	}

	public boolean assignWaiter(int tableId, int waiterId)
	{
		Table t = main.tc.findTable(tableId);
		Waiter w = findWaiter(waiterId);

		if(w != null)
		{
			t.setWaiter(w);
			w.addTable(t);
			callChangedListeners("Waiter");
			return true;
		}
		return false;
	}

	public ArrayList<Table> getAssignedTables(int waiterId)
	{
		Waiter w = findWaiter(waiterId);
		if(w != null)
		{
			return w.getTables();
		}

		return null;
	}

	public ArrayList<Waiter> getAllWaiters()
	{
		return waiters;
	}

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

	public void liveBackup()
	{
		/* save to temp file */
	}

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

	public boolean loadIdCounter() {
		try {
			ObjectInputStream is = new ObjectInputStream(
					new FileInputStream("waiterId.dat"));
			latestId = (Integer)is.readObject();
			is.close();
			return true;

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	private ArrayList<WaiterChangeListener> changedListeners = new ArrayList <WaiterChangeListener>();
	public void addChangedListener(WaiterChangeListener l)
	{
		changedListeners.add(l);
	}

	private void callChangedListeners(String changeType)
	{
		for (int i = 0;i < changedListeners.size();++i)
		{
			changedListeners.get(i).DoSomething(changeType);
		}
	}

}
