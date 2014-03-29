package com.main.dinedroid.server;

import java.util.ArrayList;

import com.main.dinedroid.models.Table;
import com.main.dinedroid.models.Waiter;
import com.main.dinedroid.serverlistener.TableChangeListener;
import com.main.dinedroid.serverlistener.WaiterChangeListener;

public class WaitersController implements Runnable 
{

	private ArrayList<Waiter> waiters;
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub	
		waiters = new ArrayList<Waiter>();
	}
	
	public boolean createWaiter(Waiter e) /* String name */
	{
		return waiters.add(e);
	}
	
	public boolean removeWater(Waiter e) /* int WaiterId */
	{
		return waiters.remove(e);
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
	
	public boolean hailWaiter(int tableId)
	{
		Table t = main.tc.findTable(tableId);
		Waiter w = t.getWaiter();
		return w.addHail(t);
	}
	
	public boolean removeQueueTable(int tableId, int waiterId)
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
	
	public boolean assignWaiter(int tableId, int waiterId)
	{
		Table t = main.tc.findTable(tableId);
		Waiter w = findWaiter(waiterId);
		
		if(w != null)
		{
			t.setWaiter(w);
			w.addTable(t);
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
	
	public void loadWaiters()
	{
		/* load from DB if temp file does not exist */
	}
	
	public void liveBackup()
	{
		/* save to temp file */
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
