package com.main.dinedroid.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.main.dinedroid.menu.FoodItem;
import com.main.dinedroid.menu.Menu;
import com.main.dinedroid.models.ExtraGroup;
import com.main.dinedroid.serverlistener.MenuChangeListener;

public class MenuController implements Runnable {

	private Menu menu = new Menu();
	private Integer latestId = 0;
	private AllExtras everyExtra = new AllExtras();

	@Override
	public void run() {
		// TODO Auto-generated method stub
		loadIdCounter();
		loadMenu();
	}

	public boolean addTopCategory(FoodItem e) {
		return menu.addItem(e);
	}

	public boolean removeTopCategory(FoodItem e) {
		return menu.removeItem(e);
	}

	public Menu getMenu()
	{
		return menu;
	}
	
	public boolean setAvailability(int itemId, boolean availability)
	{
		FoodItem item = menu.findItem(itemId);
		if(item == null)
		{
			return false;
		}
		else
		{
			item.setAvailable(availability);
			return true;
		}
		
	}

	public void processCategory(FoodItem parent, FoodItem child)
	{
		parent.addItem(child);
		callChangedListeners("Category");
	}

	public int getLatestId()
	{
		++latestId;
		saveIdCounter();
		return latestId;
	}

	public ArrayList<ExtraGroup> getPredefinedExtras()
	{
		return everyExtra.getExtras();
	}

	public void addPredefinedExtra(ExtraGroup addThis)
	{
		everyExtra.addExtra(addThis);
		//saveExtras();
		callChangedListeners("Extra");
	}











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

	public boolean loadIdCounter() {
		try {
			ObjectInputStream is = new ObjectInputStream(
					new FileInputStream("counter.dat"));
			latestId = (Integer)is.readObject();
			is.close();
			return true;

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

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

	private ArrayList<MenuChangeListener> changedListeners = new ArrayList <MenuChangeListener>();
	public void addChangedListener(MenuChangeListener l)
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
