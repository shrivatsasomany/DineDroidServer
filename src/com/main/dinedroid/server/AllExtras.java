package com.main.dinedroid.server;

import java.io.Serializable;
import java.util.ArrayList;

import com.main.dinedroid.models.ExtraGroup;

public class AllExtras implements Serializable
{
	private ArrayList<ExtraGroup> bunchaExtras;
	
	public AllExtras()
	{
		bunchaExtras = new ArrayList <ExtraGroup>();
	}
	
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
	
	public ArrayList<ExtraGroup> getExtras()
	{
		return bunchaExtras;
	}
}
