package com.main.dinedroid.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.main.dinedroid.menu.FoodItem;
import com.main.dinedroid.models.Order;
import com.main.dinedroid.models.Table;
import com.main.dinedroid.models.Waiter;
import com.main.dinedroid.serverlistener.TableChangeListener;

public class TablesController implements Runnable {

	private ArrayList<Table> tables;
	private int tempTableId = 1000;
	private int ORDER_OK = 1;
	private String kitchenIP = "localhost";

	@Override
	public void run() {
		// TODO Auto-generated method stub
		tables = new ArrayList<Table>();
		loadTables();
		fixReferences();
		loadKitchenIP();
		System.err.println("Loaded " + tables.size() + " tables");
	}
	
	public void fixReferences()
	{
		for(Table e:tables)
		{
			if(e.getWaiter()!=null)
			{
				Waiter w = main.wc.findWaiter(e.getWaiter().getId());
				e.setWaiter(w);
			}
		}
	}

	/**
	 * Used to set the address of the kitchen server
	 * 
	 * @param ip
	 *            A String IP address or hostname
	 */
	public void setKitchenIP(String ip) {
		kitchenIP = ip;
	}

	/**
	 * Get the address of the kitchen
	 * 
	 * @return the address of the kitchen
	 */
	public String getKitchenIP() {
		return kitchenIP;
	}

	/**
	 * Used only to roll back an unrecoverable table
	 * 
	 * @param t
	 *            A Table
	 * @return Setting a table
	 */
	public boolean insertTable(Table t) {
		for (int i = 0; i < tables.size(); ++i) {
			if (tables.get(i).equals(t)) {
				tables.set(i, t);
				return true;
			}
		}
		return false;
	}

	/**
	 * Add a table to the list
	 * 
	 * @param tableId
	 * @return true or false (if successfully added, or not)
	 */
	public boolean createTable(int tableId) /* nothing */
	{
		if (findTable(tableId) == null) {
			Table e = new Table(tableId);
			callChangedListeners("Table");
			boolean result = tables.add(e);
			saveTables();
			return result;
		}
		return false;
	}

	/**
	 * Create a temp table Temp tables have an ID of >= 1000
	 * 
	 * @return the ID of the created table
	 */
	public synchronized int createTempTable() {
		/* TEMP tables have id > 1000 */
		Table e = new Table(tempTableId);
		boolean result = tables.add(e);
		openTable(e.getId());
		callChangedListeners("Table");
		++tempTableId;
		return e.getId();
	}

	/**
	 * Remove a table from the list based on ID This also unassigns the waiter,
	 * to maintain consistency
	 * 
	 * @param tableId
	 *            the ID of the table
	 * @return true if it was removed, false otherwise
	 */
	public boolean removeTable(int tableId) {
		// remove table from DB if NOT temporary
		Table e = findTable(tableId);
		boolean result = tables.remove(e);
		if (e.getWaiter() != null) {
			main.wc.unassignWaiter(e.getWaiter().getId(), tableId);
		}
		saveTables();
		callChangedListeners("Table");
		return result;
	}

	/**
	 * Find a table given an ID
	 * 
	 * @param tableId
	 *            The Table ID
	 * @return the Table, if found. Null if not.
	 */
	public Table findTable(int tableId) {
		for (int i = 0; i < tables.size(); ++i) {
			if (tables.get(i).getId() == tableId) {
				return tables.get(i);
			}
		}

		return null;
	}

	/**
	 * Opens a table by table ID This means: Setting its occupied status to true
	 * 
	 * @param tableId
	 *            An ID
	 * @return true if the table was found and changed, false otherwise
	 */
	public boolean openTable(int tableId) {
		Table t = findTable(tableId);
		if (t != null) {
			t.setOccupied(true);
			callChangedListeners("Table");
			return true;
		}
		return false;
	}

	/**
	 * Opens a table by ID and adds a reservation name to it as well
	 * 
	 * @param tableId
	 *            An ID
	 * @param customerName
	 *            Name of the customer
	 * @return true if the table was found and changed, false otherwise
	 */
	public boolean openTable(int tableId, String customerName) {
		Table t = findTable(tableId);
		if (t != null) {
			t.setOccupied(true);
			t.setCustomerName(customerName);
			return true;
		}
		return false;
	}

	/**
	 * Close a table by ID This sets the table occupied status to false Closes a
	 * pending order, if it exists
	 * 
	 * @param tableId
	 *            An ID
	 * @return true if the table was found and changed, false otherwise
	 */
	public boolean closeTable(int tableId) {
		Table t = findTable(tableId);
		if (t != null) {
			if (t.getOrder() != null) {
				closeTableOrder(t.getId());
			}
			t.setOccupied(false);
			if(t.getWaiter()!=null)
			{
				main.wc.unassignWaiter(t.getWaiter().getId(), tableId);
				t.setWaiter(null);
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns the current order attached to the table Finds the table by ID,
	 * and gets the order
	 * 
	 * @param tableId
	 *            An ID
	 * @return Order, if it table is found. Null if not (Or if table has no
	 *         order)
	 */
	public Order getTableOrder(int tableId) {
		Table t = findTable(tableId);
		if (t != null) {
			return t.getOrder();
		}
		return null;
	}

	/**
	 * Sets an order to a table Finds a table by ID, sets the order
	 * 
	 * @param tableId
	 *            An ID
	 * @param order
	 *            An Order
	 * @return true if the table was found and changed, false otherwise
	 */
	public synchronized boolean setTableOrder(int tableId, Order order) {
		Table t = findTable(tableId);
		if (t != null) {
			t.setOrder(order);
			t.setOrderStatus(ORDER_OK);
			callChangedListeners("Order");
			return true;
		}
		return false;
	}

	/**
	 * Checks if the items in an order are available by querying the map from
	 * the MenuController
	 * 
	 * @param order
	 *            An Order
	 * @return the list of unavailable items. It is empty if there aren't any
	 */
	public ArrayList<FoodItem> verifyOrder(Order order) {
		ArrayList<FoodItem> unavailableItems = new ArrayList<FoodItem>();
		for (FoodItem e : order.getOrder()) {
			FoodItem f = main.mc.getItem(e.getID());
			if (!f.isAvailable()) {
				unavailableItems.add(f);
			}
		}
		return unavailableItems;
	}

	/**
	 * Close an order of a table Find the table by ID, get the order, calculate
	 * the price (display for now). Remove the order from the table
	 * 
	 * @param tableId
	 *            An ID
	 * @return true if the order was found and closed, false otherwise
	 */
	public boolean closeTableOrder(int tableId) {
		Table t = findTable(tableId);
		if (t != null) {
			Order order = t.getOrder();
			double price = getTotal(order);
			System.err.println("Price of order: " + price);
			removeTableOrder(tableId);
			callChangedListeners("Order");
			return true;
		}
		return false;
	}

	/**
	 * Remove order from a table <b>without</b> closing and processing it.
	 * 
	 * @param tableId
	 *            An ID
	 * @return true if the table was found and order was removed, false
	 *         otherwise
	 */
	public synchronized boolean removeTableOrder(int tableId) {
		Table t = findTable(tableId);
		if (t != null) {
			t.setOrder(null);
			t.setOrderStatus(null);
			return true;
		}
		return false;
	}

	/**
	 * Set the status of the order
	 * 
	 * @param tableId
	 *            An ID
	 * @param status
	 *            (Integer)<br>
	 *            1 for <b>OK</b><br>
	 *            2 for <b>Delayed</b><br>
	 *            3 for <b>Problem</b>
	 * @return true if the order was found and changed, false otherwise
	 */
	public boolean setOrderStatus(int tableId, int status) {
		Table t = findTable(tableId);
		if (t != null) {
			t.setOrderStatus(status);
			return true;
		}
		return false;
	}

	/**
	 * Set the order status with notes
	 * 
	 * @param tableId
	 *            An ID
	 * @param status
	 *            (Integer)<br>
	 *            1 for <b>OK</b><br>
	 *            2 for <b>Delayed</b><br>
	 *            3 for <b>Problem</b>
	 * @param orderNotes
	 *            Notes for an order
	 * @return
	 */
	public boolean setOrderStatus(int tableId, int status, String orderNotes) {
		Table t = findTable(tableId);
		if (t != null) {
			t.setOrderStatus(status);
			t.getOrder().setOrderNotes(orderNotes);
			return true;
		}
		return false;
	}

	/**
	 * Get the list of all tables
	 * 
	 * @return list of tables
	 */
	public ArrayList<Table> getAllTables() {
		return tables;
	}

	/**
	 * Calculate the total of an order
	 * 
	 * @param order
	 *            (An Order)
	 * @return Order price
	 */
	public double getTotal(Order order) {
		ArrayList<FoodItem> items;
		items = order.getOrder();

		double total = 0;

		for (FoodItem f : items) {
			total += f.getPrice();
			if (f.getExtras().size() > 0) {
				for (FoodItem e : f.getExtras()) {
					total += e.getPrice();
				}
			}

		}
		return total;
	}

	/**
	 * Load the tables from a file
	 * 
	 * @return true if file load was successful, false otherwise
	 */
	public boolean loadTables() {
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(
					"tables.dat"));
			tables = (ArrayList<Table>) is.readObject();
			is.close();
			return true;

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Save tables to a file
	 * 
	 * @return true if file save was successful, false otherwise
	 */
	public boolean saveTables() {
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

	/**
	 * Saves the kitchen address
	 */
	public void saveKitchenIP() {
		try {
			ObjectOutputStream os = new ObjectOutputStream(
					new FileOutputStream("serverIP.dat"));
			os.writeObject(kitchenIP);
			os.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Loads the kitchen address
	 */
	public void loadKitchenIP() {
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(
					"serverIP.dat"));
			kitchenIP = (String) is.readObject();
			is.close();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cleans up the class before quit: Removes any waiters attached to the
	 * tables If there are any pending orders, throws an exception!
	 * 
	 * @throws Exception
	 *             If there is an open order for a table
	 */
	public void cleanUp() throws Exception {
		for (Table t : tables) {
			if (t.getOrder() != null) {
				throw new Exception(
						"One or more tables have an open order.\nPlease close them before quitting");
			}
			closeTable(t.getId());
		}
		saveTables();
	}

	/**
	 * List to hold change listeners for tables
	 */
	private ArrayList<TableChangeListener> changedListeners = new ArrayList<TableChangeListener>();

	/**
	 * Add a listener to the table of listeners
	 * 
	 * @param l
	 *            A listener
	 */
	public void addChangedListener(TableChangeListener l) {
		changedListeners.add(l);
	}

	/**
	 * Call the doSomething for each listener in the table
	 * 
	 * @param changeType
	 */
	private void callChangedListeners(String changeType) {
		for (int i = 0; i < changedListeners.size(); ++i) {
			changedListeners.get(i).DoSomething(changeType);
		}
	}

}