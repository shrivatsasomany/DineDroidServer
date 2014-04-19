package com.main.dinedroid.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Menu implements Serializable {
	private ArrayList<FoodItem> items = new ArrayList<FoodItem>();

	/**
	 * Constructor 
	 */
	public Menu() {
	}

	/**
	 * Invoke the tree building of the menu structure
	 */
	public void setParent() {
		if (items != null || !items.isEmpty()) {
			for (int i = 0; i < items.size(); ++i) {
				items.get(i).setParent(null);
			}
		}
	}

	/**
	 * Invoke the map building of the menu structure
	 * @param myMap Map<Object(Integer), FoodItem>
	 */
	public void populateMap(HashMap<Object, FoodItem> myMap) {
		if (items != null || !items.isEmpty()) {
			for (int i = 0; i < items.size(); ++i) {
				myMap.put(new Integer(items.get(i).getID()), items.get(i));
				items.get(i).populateMap(myMap);
			}
		}
	}

	/**
	 * @return the list of items
	 */
	public ArrayList<FoodItem> getItems() {
		return items;
	}

	/**
	 * @param e A food item
	 * @return true or false, depending on successful addition of a food item
	 */
	public boolean addItem(FoodItem e) {
		boolean x = items.add(e);
		return x;
	}

	/**
	 * @param e A food item
	 * @return true or false, depending on successful deletion of an item
	 */
	public boolean removeItem(FoodItem e) {
		return items.remove(e);
	}
	
	/**
	 * Find a food item
	 * @param itemId
	 * @return found fooditem or null
	 */
	public FoodItem findItem(int itemId)
	{
		FoodItem result = null;
		for(int i = 0; i < items.size(); ++i)
		{
			result = items.get(i).findItem(itemId);
			if (result != null)
			{
				break;
			}
		}
		return result;
	}
}
