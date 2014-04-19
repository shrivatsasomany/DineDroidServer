package com.main.dinedroid.server;

import java.io.Serializable;
import java.util.ArrayList;

import com.main.dinedroid.models.ExtraGroup;

/**
 * @author Shrivatsa
 * @author Harshitha
 *
 */
public class AllExtras implements Serializable
{

	private ArrayList<ExtraGroup> bunchaExtras;
	
	/**
	 * Constructor
	 */
	public AllExtras()
	{
		bunchaExtras = new ArrayList <ExtraGroup>();
	}
	
	/**
	 * Add an extra group
	 * @param addThis An ExtraGroup
	 */
	public void addExtra(ExtraGroup addThis)
	{
		boolean flag = false;
		for(int i = 0; i < bunchaExtras.size(); ++i)
		{
			if(bunchaExtras.get(i).equals(addThis))
			{
				System.out.println("Found duplicate");
				bunchaExtras.remove(i);
				bunchaExtras.add(i, addThis);
				flag = true;
				break;
			}
		}
		if(flag == false)
		{
			bunchaExtras.add(addThis);
		}
	}
	
	/**
	 * Return the list of ExtraGroups
	 * @return
	 */
	public ArrayList<ExtraGroup> getExtras()
	{
		return bunchaExtras;
	}
}
