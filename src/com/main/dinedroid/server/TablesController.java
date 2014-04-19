package com.main.dinedroid.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.main.dinedroid.menu.FoodItem;
import com.main.dinedroid.menu.Menu;
import com.main.dinedroid.models.Order;
import com.main.dinedroid.models.Table;
import com.main.dinedroid.models.Waiter;
import com.main.dinedroid.serverlistener.TableChangeListener;

public class TablesController implements Runnable {

	private ArrayList<Table> tables;
	private int tempTableId = 1000;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		tables = new ArrayList<Table>();
		loadTables();
		System.err.println("Loaded " + tables.size() + " tables");
	}

	/**
	 * Used only to roll back an unrecoverable table
	 * @param t
	 * @return
	 */
	public boolean insertTable(Table t)
	{
		for(int i = 0; i < tables.size(); ++i)
		{
			if(tables.get(i).equals(t))
			{
				tables.set(i, t);
				return true;
			}
		}
		return false;
	}

	public boolean createTable(int tableId) /* nothing */
	{
		if(findTable(tableId) == null)
		{
			Table e = new Table(tableId);
			callChangedListeners("Table");
			boolean result =  tables.add(e);
			saveTables();
			return result;
		}
		return false;
	}

	public synchronized int createTempTable()
	{
		/* TEMP tables have id > 1000 */
		Table e = new Table(tempTableId);
		boolean result =  tables.add(e);
		openTable(e.getId());
		callChangedListeners("Table");
		++tempTableId;
		return e.getId();
	}

	public boolean removeTable(int tableId) /* int tableId */
	{
		//remove table from DB if NOT temporary
		Table e = findTable(tableId);
		boolean result = tables.remove(e);
		if(e.getWaiter()!=null)
		{
			main.wc.unassignWaiter(e.getWaiter().getId(), tableId);
		}
		saveTables();
		callChangedListeners("Table");
		return result;
	}

	public Table findTable(int tableId)
	{
		for(int i = 0; i < tables.size(); ++i)
		{
			if(tables.get(i).getId() == tableId)
			{
				return tables.get(i);
			}
		}

		return null;
	}

	public boolean openTable(int tableId)
	{
		Table t = findTable(tableId);
		if(t != null)
		{
			t.setOccupied(true);
			callChangedListeners("Table");
			return true;
		}
		return false;
	}

	public boolean openTable(int tableId, String customerName)
	{
		Table t = findTable(tableId);
		if(t != null)
		{
			t.setOccupied(true);
			t.setCustomerName(customerName);
			return true;
		}
		return false;
	}

	public boolean closeTable(int tableId)
	{
		Table t = findTable(tableId);
		if(t != null)
		{
			if(t.getOrder() != null)
			{
				closeTableOrder(t.getId());
			}
			t.setOccupied(false);
			main.wc.unassignWaiter(t.getWaiter().getId(), tableId);
			t.setWaiter(null);
			return true;
		}
		return false;
	}

	public Order getTableOrder(int tableId)
	{
		Table t = findTable(tableId);
		if(t != null)
		{
			return t.getOrder();
		}
		return null;
	}

	public synchronized boolean setTableOrder(int tableId, Order order)
	{
		Table t = findTable(tableId);
		if(t != null)
		{
			t.setOrder(order);
			callChangedListeners("Order");
			return true;
		}
		return false;	
	}

	public boolean closeTableOrder(int tableId)
	{
		Table t = findTable(tableId);
		Order order = t.getOrder();
		double price = getTotal(order);
		System.err.println("Price of order: " + price);
		removeTableOrder(tableId);
		closeTable(tableId);
		callChangedListeners("Order");
		return true;
	}

	public synchronized boolean removeTableOrder(int tableId)
	{
		Table t = findTable(tableId);
		if(t!=null)
		{
			t.setOrder(null);
			t.setOrderStatus(null);
			return true;
		}
		return false;		
	}

	public boolean setOrderStatus(int tableId, int status)
	{
		Table t = findTable(tableId);
		if(t != null)
		{
			t.setOrderStatus(status);
			return true;
		}
		return false;
	}

	public boolean setOrderStatus(int tableId, int status, String orderNotes)
	{
		Table t = findTable(tableId);
		if(t != null)
		{
			t.setOrderStatus(status);
			t.getOrder().setOrderNotes(orderNotes);
			return true;
		}
		return false;
	}

	public ArrayList<Table> getAllTables()
	{
		return tables;
	}

	public double getTotal(Order order)
	{
		ArrayList<FoodItem> items;
		items = order.getOrder();

		double total = 0;

		for(FoodItem f:items)
		{
			total += f.getPrice();
			if(f.getExtras().size() > 0)
			{
				for(FoodItem e:f.getExtras())
				{
					total += e.getPrice();
				}
			}

		}
		return total;
	}

	public boolean loadTables()
	{
		try {
			ObjectInputStream is = new ObjectInputStream(
					new FileInputStream("tables.dat"));
			tables = (ArrayList<Table>)is.readObject();
			is.close();
			return true;

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean saveTables()
	{
		try {
			ObjectOutputStream os = new ObjectOutputStream(
					new FileOutputStream("tables.dat"));
			os.writeObject(tables);
			os.close();
			return true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private ArrayList<TableChangeListener> changedListeners = new ArrayList <TableChangeListener>();
	public void addChangedListener(TableChangeListener l)
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