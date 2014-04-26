/**
 * @author Shrivatsa
 * GUI for Managing item extras
 */
package com.main.dinedroid.serverUI;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.main.dinedroid.menu.FoodItem;
import com.main.dinedroid.models.ExtraGroup;
import com.main.dinedroid.server.MenuController;
import com.main.dinedroid.swing.BlurJButton;
import com.main.dinedroid.swing.CascadingJFrame;
import com.main.dinedroid.swing.VectorButton;

public class ManageItemGUI extends CascadingJFrame
{

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel jLabel1 = null;
	private JScrollPane jScrollPane1 = null;
	private JList jList1 = null;
	private JList jList2 = null;
	private JScrollPane jScrollPane2 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JButton jButton1 = null;
	private JLabel jLabel4 = null;
	private JTextField jTextField1 = null;
	private JButton jButton2 = null;
	private MenuController mc = null;
	private FoodItem passedItem = null;
	private FoodItem selectedExtra = null;
	private ArrayList<ExtraGroup> predefinedExtras= null;
	private JLabel jLabel5 = null;
	private JTextField jTextField2 = null;
	private JLabel jLabel6 = null;
	/**
	 * This is the default constructor
	 */
	public ManageItemGUI(FoodItem passed, MenuController mc)
	{
		super("Manage Food Item");
		this.mc = mc;
		passedItem = passed;
		predefinedExtras = mc.getPredefinedExtras();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(400, 373);
		this.setContentPane(getJContentPane());
		this.setTitle("Manage Food Item");
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		if(passedItem.getExtras().size() != 0)
		{
			refreshList1();
		}
		if(predefinedExtras.size() != 0)
		{
			refreshList2();
		}
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			jLabel6 = new JLabel();
			jLabel6.setBounds(new Rectangle(175, 100, 13, 25));  // Generated
			jLabel6.setText("$");  // Generated
			jLabel5 = new JLabel();
			jLabel5.setBounds(new Rectangle(65, 100, 71, 26));  // Generated
			jLabel5.setText("Extra Price:");  // Generated
			jLabel4 = new JLabel();
			jLabel4.setBounds(new Rectangle(90, 65, 44, 26));  // Generated
			jLabel4.setText("Name:");  // Generated
			jLabel4.setEnabled(false);
			jLabel3 = new JLabel();
			jLabel3.setBounds(new Rectangle(251, 140, 111, 16));  // Generated
			jLabel3.setText("Predefined Extras");  // Generated
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(50, 140, 86, 16));  // Generated
			jLabel2.setText("Added Extras");  // Generated
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(0, 0, 401, 25));  // Generated
			jLabel1.setHorizontalAlignment(SwingConstants.CENTER);  // Generated
			jLabel1.setText(passedItem.getName());  // Generated
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(jLabel1, null);  // Generated
			jContentPane.add(getJScrollPane1(), null);  // Generated
			jContentPane.add(getJScrollPane2(), null);  // Generated
			jContentPane.add(jLabel2, null);  // Generated
			jContentPane.add(jLabel3, null);  // Generated
			jContentPane.add(getJButton1(), null);  // Generated
			jContentPane.add(jLabel4, null);  // Generated
			jContentPane.add(getJTextField1(), null);  // Generated
			jContentPane.add(getJButton2(), null);  // Generated
			jContentPane.add(jLabel5, null);  // Generated
			jContentPane.add(getJTextField2(), null);
			jContentPane.add(jLabel6, null);  // Generated
		}
		return jContentPane;
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane1()
	{
		if (jScrollPane1 == null)
		{
			try
			{
				jList1 = new JList();
				jList1.addMouseListener(new java.awt.event.MouseAdapter()
				{   
					public void mouseClicked(java.awt.event.MouseEvent e) 
					{    
						JList temp = (JList) e.getSource();
						Object selectedObject = temp.getSelectedValue();
						selectedExtra = (FoodItem) selectedObject;
						
						if(e.getClickCount() == 2)
						{
							passedItem.removeExtra(selectedExtra);
							refreshList1();
						}
					}
				});
				jScrollPane1 = new JScrollPane(jList1);
				jScrollPane1.setBounds(new Rectangle(10, 165, 174, 178));  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jScrollPane2	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane2()
	{
		if (jScrollPane2 == null)
		{
			try
			{
				jList2 = new JList();
				jList2.addMouseListener(new java.awt.event.MouseAdapter()
				{   
					public void mouseClicked(java.awt.event.MouseEvent e) 
					{    
						JList temp = (JList) e.getSource();
						if(e.getClickCount() == 2)
						{
							ExtraGroup selectedGroup = (ExtraGroup) jList2.getSelectedValue();
							ArrayList<FoodItem> addThese = selectedGroup.getExtras();
							passedItem.setExtra(addThese);
							refreshList1();
						}
					}
				});
				jScrollPane2 = new JScrollPane(jList2);
				jScrollPane2.setBounds(new Rectangle(215, 165, 174, 178));  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jScrollPane2;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1()
	{
		if (jButton1 == null)
		{
			try
			{
				jButton1 = new VectorButton();
				jButton1.setForeground(Color.white);
				jButton1.setBounds(new Rectangle(160, 25, 85, 29));  // Generated
				jButton1.setText("Add Extra");  // Generated
				jButton1.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent e)
					{
						jLabel4.setEnabled(true);
						jTextField1.setEnabled(true);
						jButton2.setEnabled(true);
						jTextField1.requestFocus();
					}
				});
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jButton1;
	}

	/**
	 * This method initializes jTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField1()
	{
		if (jTextField1 == null)
		{
			try
			{
				jTextField1 = new JTextField();
				jTextField1.setBounds(new Rectangle(140, 65, 121, 28));  // Generated
				jTextField1.setText("");  // Generated
				jTextField1.setEnabled(false);
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jTextField1;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton2()
	{
		if (jButton2 == null)
		{
			try
			{
				jButton2 = new JButton();
				jButton2.setBounds(new Rectangle(270, 65, 75, 29));  // Generated
				jButton2.setText("Done");  // Generated
				jButton2.setEnabled(false);
				jButton2.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent e)
					{
						if(jTextField1.getText().equals("") || jTextField2.getText().equals(""))
						{
							JOptionPane.showMessageDialog(null, "You cannot leave any field blank");
						}
						
						else
						{
							int id = mc.getLatestId();
							String name = jTextField1.getText();
							String tempPrice = jTextField2.getText();
							try
							{
								Double price = Double.parseDouble(tempPrice);
								FoodItem extra = new FoodItem(id, name, price, false);
								passedItem.addExtra(extra);
								refreshList1();
								jLabel4.setEnabled(false);
								jTextField1.setEnabled(false);
								jTextField1.setText("");
								jButton2.setEnabled(false);
								jButton1.requestFocus();
							}
							catch(NumberFormatException f)
							{
								f.printStackTrace();
								JOptionPane.showMessageDialog(null, "Please enter a valid price");
							}
							
						}
					}
				});
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jButton2;
	}
	
	/**
	 * Refresh the extras list of the item in question
	 */
	private void refreshList1()
	{
		ArrayList <FoodItem> jListCategories = passedItem.getExtras();
	  
		int get = jListCategories.size();   
	    FoodItem[] temp1 = new FoodItem[get];
	    for(int i = 0; i < get; ++i)
	    {
	    	temp1[i] = (FoodItem) jListCategories.get(i);
	    }
	    jList1.setListData(temp1);
	}
	
	/**
	 * Refresh the list of predefined extras
	 */
	private void refreshList2()
	{
	  
		int get = predefinedExtras.size();   
	    ExtraGroup[] temp1 = new ExtraGroup[get];
	    for(int i = 0; i < get; ++i)
	    {
	    	temp1[i] = (ExtraGroup) predefinedExtras.get(i);
	    }
	    jList2.setListData(temp1);
	}

	/**
	 * This method initializes jTextField2	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField2()
	{
		if (jTextField2 == null)
		{
			try
			{
				jTextField2 = new JTextField();
				jTextField2.setBounds(new Rectangle(140, 100, 36, 26));  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jTextField2;
	}

}  //  @jve:decl-index=0:visual-constraint="228,16"
