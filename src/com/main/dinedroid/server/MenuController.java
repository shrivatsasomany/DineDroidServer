package com.main.dinedroid.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.main.dinedroid.menu.FoodItem;
import com.main.dinedroid.menu.Menu;
import com.main.dinedroid.models.ExtraGroup;
import com.main.dinedroid.serverlistener.MenuChangeListener;

public class MenuController implements Runnable {

	/**
	 * Instance of a Menu
	 */
	private Menu menu = new Menu();
	/**
	 * Map to flatten menu structure for easy item retrieval 
	 */
	private HashMap<Object, FoodItem> foodMap = new HashMap<Object, FoodItem>();
	/**
	 * This stores the latest ID
	 */
	private Integer latestId = 0;
	/**
	 * Instance of AllExtras (holds ExtraGroups)
	 */
	private AllExtras everyExtra = new AllExtras();
	private String kitchenIP = "localhost";

	@Override
	public void run() {
		loadMenu();
		loadKitchenIP();
		menu.populateMap(foodMap);
		menu.setMap(foodMap);
		menu.setParent();
		try {
			loadIdCounter();
		} catch (Exception e) {
			/*
			 * If there is problem loading the ID counter,
			 * find and retrieve it from the map. 
			 */
			Entry<Object, FoodItem> maxEntry = null;
			for(Entry<Object, FoodItem> entry : foodMap.entrySet())
			{
				if(maxEntry == null || (Integer)entry.getKey() > (Integer)maxEntry.getKey())
				{
					maxEntry = entry;
					latestId = (Integer) maxEntry.getKey();
				}
			}
			++latestId;
			saveIdCounter();
		}
	}
	
	/**
	 * Used to set the address of the kitchen server
	 * 
	 * @param ip A String IP address or hostname
	 */
	public void setKitchenIP(String ip){
		kitchenIP = ip;
	}
	/**
	 * Get the address of the kitchen
	 * @return the address of the kitchen
	 */
	public String getKitchenIP() {
		return kitchenIP;
	}

	/**
	 * @param e A FoodItem
	 * @return true or false, depending on successful addition to the list
	 */
	public boolean addTopCategory(FoodItem e) {
		boolean result = menu.addItem(e);
		foodMap.put(e.getID(), e);
		return result;
	}

	/**
	 * @param e A FoodItem
	 * @return true or false, depending on successful deletion from the list
	 */
	public boolean removeTopCategory(FoodItem e) {
		boolean result = menu.removeItem(e);
		foodMap.remove(e.getID());
		return result;
	}

	/**
	 * @return The entire Menu
	 */
	public Menu getMenu()
	{
		return menu;
	}
	
	/**
	 * Set the availability of an Item in the menu
	 * @param itemId The ID of the item
	 * @param availability What you want the availability to be
	 * @return true or false, depending on if the item was found and flipped or not.
	 */
	public synchronized boolean setAvailability(int itemId, boolean availability)
	{
		/*
		 * Find the item
		 */
		FoodItem item = foodMap.get(itemId);
		if(item == null)
		{
			return false;
		}
		else
		{
			/*
			 * Set the availability
			 */
			item.setAvailable(availability);
			foodMap.get(itemId).setAvailable(availability);
			return true;
		}
		
	}

	/**
	 * Add a FoodItem to another FoodItem
	 * Then push the updated menu to the kitchen
	 * @param parent The item to add to
	 * @param child The item to be added
	 */
	public void processCategory(FoodItem parent, FoodItem child)
	{
		parent.addItem(child);
		foodMap.put(child.getID(), child);
		child.setParent(parent);
		callChangedListeners("Category");
		pushMenuToKitchen();
	}
	
	/**
	 * Pushes the menu to the kitchen
	 */
	public void pushMenuToKitchen()
	{
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Socket s;
				try {
					s = new Socket(main.tc.getKitchenIP(), 4355);
					ObjectOutputStream kitchenOut = new ObjectOutputStream(s.getOutputStream());
					kitchenOut.flush();
					kitchenOut.writeObject("Menu||Set_Menu");
					kitchenOut.flush();
					kitchenOut.writeObject(menu);
					kitchenOut.flush();
					kitchenOut.close();
					s.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		});
		t.run();
	}
	
	
	/**
	 * Get a FoodItem from the MAP
	 * @param id the ID of the item
	 * @return FoodItem (or null)
	 */
	public FoodItem getItem(int id)
	{
		return foodMap.get(id);
	}

	/**
	 * Get the latest ID to be assigned to the foodItem
	 * This is persisted in a file
	 * @return
	 */
	public int getLatestId()
	{
		++latestId;
		saveIdCounter();
		return latestId;
	}

	/**
	 * Get the list of prefedined extras
	 * @return List of ExtraGroups
	 */
	public ArrayList<ExtraGroup> getPredefinedExtras()
	{
		return everyExtra.getExtras();
	}

	/**
	 * Add a predefined ExtraGroup to the list of extras
	 * @param addThis an ExtraGroup
	 */
	public void addPredefinedExtra(ExtraGroup addThis)
	{
		everyExtra.addExtra(addThis);
		//saveExtras();
		callChangedListeners("Extra");
	}

	/**
	 * Save the ID counter
	 * @return true or false, depending on successful saving of the file
	 */
	public boolean saveIdCounter() {
		try {
			ObjectOutputStream os = new ObjectOutputStream(
					new FileOutputStream("counter.dat"));
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
	 * Load the ID counter
	 * @return true if it loads successfully, false otherwise
	 * @throws Exception IOException or ClassNotFoundException
	 */
	public boolean loadIdCounter() throws Exception {
		try {
			ObjectInputStream is = new ObjectInputStream(
					new FileInputStream("counter.dat"));
			latestId = (Integer)is.readObject();
			is.close();
			return true;

		} catch (IOException | ClassNotFoundException e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Presists menu to file
	 * @return true if saved successfully, false if not
	 */
	public boolean saveMenu() {
		try {
			ObjectOutputStream os = new ObjectOutputStream(
					new FileOutputStream("menu.dat"));
			os.writeObject(menu);
			os.close();
			return true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Loads menu from file
	 * @return true if set successfully, false if not.
	 */
	public boolean loadMenu() {
		try {
			ObjectInputStream is = new ObjectInputStream(
					new FileInputStream("menu.dat"));
			menu = (Menu)is.readObject();
			menu.setParent();
			is.close();
			return true;

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Loads the kitchen address
	 */
	public void loadKitchenIP()
	{
		try {
			ObjectInputStream is = new ObjectInputStream(
					new FileInputStream("serverIP.dat"));
			kitchenIP = (String)is.readObject();
			is.close();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	


	/**
	 * MVC Change listener used to collect different listeners
	 */
	private ArrayList<MenuChangeListener> changedListeners = new ArrayList <MenuChangeListener>();
	/**
	 * Add listener to the list
	 * @param l
	 */
	public void addChangedListener(MenuChangeListener l)
	{
		changedListeners.add(l);
	}

	/**
	 * Interface to call the appropriate listener
	 * This is ONLY used to automatically tell the GUI to refresh the Menu List
	 * @param changeType
	 */
	private void callChangedListeners(String changeType)
	{
		for (int i = 0;i < changedListeners.size();++i)
		{
			changedListeners.get(i).DoSomething(changeType);
		}
	}

}
