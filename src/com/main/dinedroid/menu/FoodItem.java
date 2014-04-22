package com.main.dinedroid.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Shrivatsa
 * 
 */

public class FoodItem implements Serializable {
	private int id;
	private String name;
	private Double price;
	private ArrayList<FoodItem> items;
	private ArrayList<FoodItem> extras;
	private String notes;
	private int quantity;
	private boolean isAvailable = true;
	private boolean isCategory;
	private FoodItem parent;

	/**
	 * @param id ID of the item
	 * @param name Name of the item
	 * @param price price (decimal)
	 * @param isCategory If this item is a category
	 */
	public FoodItem(int id, String name, double price, boolean isCategory) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.isCategory = isCategory;
		this.items = new ArrayList<FoodItem>();
		this.extras = new ArrayList<FoodItem>();
	}

	/**
	 * This constructor is used to reuse this class as a food item in an order
	 * @param id ID of the item
	 * @param name Name of the item
	 * @param price Price (decimal)
	 * @param dishes list of dishes
	 * @param extras list of extras
	 * @param notes notes about the item
	 * @param quantity quantity desired
	 */
	public FoodItem(int id, String name, Double price,
			ArrayList<FoodItem> dishes, ArrayList<FoodItem> extras,
			String notes, int quantity) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.items = dishes;
		this.extras = extras;
		this.notes = notes;
		this.quantity = quantity;
	}

	/**
	 * Remove the parent of the food item to flatten the structure
	 */
	public void nullifyParent() {
		this.parent = null;
		if (items != null) {
			for (int i = 0; i < items.size(); ++i) {
				items.get(i).nullifyParent();
			}
		}
	}

	/**
	 * Set the parent of this food item, and call this function on every food
	 * item in items
	 * 
	 * @param parent
	 */
	public void setParent(FoodItem parent) {
		this.parent = parent;
		if (items != null) {
			for (int i = 0; i < items.size(); ++i) {
				items.get(i).setParent(this);
			}
		}
	}

	/**
	 * Flatten the entire menu into a given map for fast retrieval
	 * Call this recursively on every item in the list of items.
	 * @param myMap
	 */
	public void populateMap(HashMap<Object, FoodItem> myMap) {
		if (items != null) {
			for (int i = 0; i < items.size(); ++i) {
				myMap.put(new Integer(items.get(i).getID()), items.get(i));
				items.get(i).populateMap(myMap);
			}
		}
	}

	/**
	 * @return
	 */
	public FoodItem getParent() {
		return parent;
	}

	/**
	 * @return
	 */
	public boolean isChecked() {
		return isAvailable;
	}

	/**
	 * change the availibility 
	 */
	public void flipChecked() {
		if (isAvailable == true) {
			isAvailable = false;
		} else {
			isAvailable = true;
		}
	}

	/**
	 * @param itemId ID of item
	 * @return a found food item
	 */
	public FoodItem findItem(int itemId) {
		FoodItem result = null;
		for (int i = 0; i < items.size(); ++i) {
			if (items.get(i).isCategory) {
				result = items.get(i).findItem(itemId);
			} else {
				if (items.get(i).id == itemId) {
					result = items.get(i);
				}
			}
		}
		return result;
	}

	/**
	 * @return quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param q quantity desired
	 */
	public void setQuantity(int q) {
		quantity = q;
	}

	/**
	 * @return ID
	 */
	public int getID() {
		return id;
	}

	/**
	 * @return Name of item
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return price of the item
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @return list of items
	 */
	public ArrayList<FoodItem> getItems() {
		return items;
	}

	/**
	 * @return list of extras
	 */
	public ArrayList<FoodItem> getExtras() {
		return extras;
	}

	/**
	 * @return notes of the item
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @return if the item is available
	 */
	public boolean isAvailable() {
		return isAvailable;
	}

	/**
	 * Add notes to this item
	 * @param note
	 */
	public void addNotes(String note) {
		notes = note;
	}

	/**
	 * @param item
	 * @return true or false, depending on successful addition to the list
	 */
	public boolean addItem(FoodItem item) {
		return items.add(item);
	}

	/**
	 * @param item
	 * @return true of false, depending on successful deletion from the list
	 */
	public boolean removeItem(FoodItem item) {
		return items.remove(item);
	}

	/**
	 * Set the list of items 
	 * @param items
	 */
	public void setItems(ArrayList<FoodItem> items) {
		this.items = items;
	}

	/**
	 * add an extra to this item
	 * @param extra
	 */
	public void addExtra(FoodItem extra) {
		extras.add(extra);
	}

	/**
	 * remove an extra from this item
	 * @param extra
	 */
	public void removeExtra(FoodItem extra) {
		extras.remove(extra);
	}

	/**
	 * set the list of extras directly
	 * @param extras
	 */
	public void setExtra(ArrayList<FoodItem> extras) {
		this.extras = extras;
	}

	/**
	 * Set if this is a category or not
	 * @param isCategory
	 */
	public void setCategory(boolean isCategory) {
		this.isCategory = isCategory;
	}

	/**
	 * If this item is available or not
	 * @param availability
	 */
	public void setAvailable(boolean availability) {
		isAvailable = availability;
	}

	/**
	 * Get if this is a category
	 * @return true or false
	 */
	public boolean isCategory() {
		return isCategory;
	}

	/**
	 * Compare this item with another item by ID
	 * @param compare
	 * @return true or false
	 */
	public boolean equals(FoodItem compare) {
		if (this.id == compare.id) {
			return true;
		}

		else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name + " - " + id;
	}

	/**
	 * returns a detailed string of the food item
	 * @return name + price
	 */
	public String toDetailedString() {
		return name + "\n" + price;
	}

	/**
	 * Display formatted detailed information
	 * @return Name, Quantity, and added extras
	 */
	public String displayString() {
		String temp;
		temp = "\t\tDish: " + name + "\n" + "\t\tQuantity: " + quantity + "\n";
		if (extras.isEmpty() == true) {
			temp = temp + "\t\tExtras: None\n";
			return temp;
		}

		else {
			String chosen_extras = "\t\tExtras:\n";
			for (int i = 0; i < extras.size(); ++i) {
				chosen_extras += "\t\t\t\t" + extras.get(i) + "\n";
			}

			return temp + chosen_extras;
		}
	}
}
