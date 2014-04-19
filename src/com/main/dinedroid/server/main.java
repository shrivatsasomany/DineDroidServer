package com.main.dinedroid.server;

import com.main.dinedroid.models.Table;
import com.main.dinedroid.models.Waiter;
import com.main.dinedroid.serverUI.MainServerGUI;
import com.main.dinedroid.servercom.ServerListener;

public class main {
	
	public static WaitersController wc = new WaitersController();
	public static TablesController tc = new TablesController();
	public static MenuController mc = new MenuController();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		wc.run();
		tc.run();
		mc.run();
		Thread sl = new Thread(new ServerListener());
		sl.start();
		//Waiter w = new Waiter(1, "Shrivatsa");
		//Waiter w2 = new Waiter(2, "Harshi");
//		Table t = new Table(1);
//		Table t2 = new Table(2);
//		tc.createTable(t);
//		tc.createTable(t2);
		//System.out.println(wc.createWaiter(w));
		//System.out.println(wc.createWaiter(w2));
		//System.out.println(wc.removeWater(w));
		//wc.assignWaiter(2, 2);
		//wc.assignWaiter(1, 1);

		MainServerGUI gui = new MainServerGUI(tc, wc, mc);
		gui.setVisible(true);		
		
	}

}
