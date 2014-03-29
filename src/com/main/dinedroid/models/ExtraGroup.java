/**
 * @author Shrivatsa
 * Houses predefined extras
 */

package com.main.dinedroid.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.main.dinedroid.menu.FoodItem;

public class ExtraGroup implements Serializable
{
	private String itemToRepresent;
	private ArrayList<FoodItem> extras;
	
	/**
	 * 
	 * @param itemExtend
	 */
	public ExtraGroup(String itemExtend)
	{
		itemToRepresent = itemExtend;
		extras = new ArrayList<FoodItem>();
	}
	
	/**
	 * 
	 * @return Item to represent
	 */
	public String getExtraType()
	{
		return itemToRepresent;
	}
	
	/**
	 * 
	 * @param extra
	 */
	public void addExtra(FoodItem extra)
	{
		extras.add(extra);
	}
	
	/**
	 * 
	 * @param extra
	 * @return true or false
	 */
	public boolean removeExtra(String extra)
	{
		for(int i = 0; i < extras.size(); ++i)
		{
			if(extras.get(i).equals(extra) == true)
			{
				extras.remove(i);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return list of extras
	 */
	public ArrayList<FoodItem> getExtras()
	{
		// TODO Auto-generated method stub
		return extras;
	}
	
	/**
	 * toString function
	 */
	public String toString()
	{
		return itemToRepresent;
	}
}
