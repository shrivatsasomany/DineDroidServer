package com.main.dinedroid.server;

import com.main.dinedroid.models.Table;
import com.main.dinedroid.models.Waiter;
import com.main.dinedroid.serverUI.MainServerGUI;
import com.main.dinedroid.servercom.ServerListener;

public class main {
	
	public static WaitersController wc = new WaitersController();
	public static TablesController tc = new TablesController();
	public static MenuController mc = new MenuController();
	/**
	 * Invoke the server
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		wc.run();
		tc.run();
		mc.run();
		Thread sl = new Thread(new ServerListener());
		sl.start();
		MainServerGUI gui = new MainServerGUI(tc, wc, mc);
		gui.setVisible(true);		
		
	}

}
