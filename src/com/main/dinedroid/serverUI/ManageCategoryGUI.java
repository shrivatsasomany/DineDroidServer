/**
 * @author Shrivatsa
 * GUI for managing categories
 */
package com.main.dinedroid.serverUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Locale.Category;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.main.dinedroid.menu.FoodItem;
import com.main.dinedroid.server.MenuController;
import com.main.dinedroid.serverlistener.MenuChangeListener;
import com.main.dinedroid.swing.CascadingJFrame;

public class ManageCategoryGUI extends CascadingJFrame
{

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JScrollPane jScrollPane1 = null;
	private JList jList1 = null;
	private JLabel jLabel1 = null;
	private JTabbedPane jTabbedPane1 = null;
	private JPanel jPanel1 = null;
	private JPanel jPanel2 = null;
	private JLabel jLabel2 = null;
	private JTextField categoryName = null;
	private JButton addCategory = null;
	private JLabel jLabel3 = null;
	private JTextField itemName = null;
	private JButton addItem = null;
	private JLabel jLabel5 = null;
	private JLabel jLabel4 = null;
	private FoodItem givenCategory = null;
	private FoodItem selectedItem = null;
	private MenuController mc = null;
	private int refreshFlag = 1;
	private JButton jButton3 = null;
	private JLabel jLabel6 = null;
	private JTextField itemPrice = null;
	private JLabel jLabel7 = null;

	/**
	 * This is the default constructor
	 */
	public ManageCategoryGUI(FoodItem sent, MenuController mc)
	{
		super("Add To Category");
		givenCategory = sent;
		this.mc = mc;
		initialize();
		refreshList();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(432, 387);
		this.setContentPane(getJContentPane());
		this.setTitle("Add To Category");
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new Insets(5, 10, 76, 6);  // Generated
			gridBagConstraints3.gridy = 1;  // Generated
			gridBagConstraints3.ipadx = 25;  // Generated
			gridBagConstraints3.gridx = 1;  // Generated
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;  // Generated
			gridBagConstraints2.gridx = 1;  // Generated
			gridBagConstraints2.gridy = 0;  // Generated
			gridBagConstraints2.ipadx = 64;  // Generated
			gridBagConstraints2.ipady = 119;  // Generated
			gridBagConstraints2.weightx = 1.0;  // Generated
			gridBagConstraints2.weighty = 1.0;  // Generated
			gridBagConstraints2.insets = new Insets(64, 11, 4, 11);  // Generated
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(35, 20, 189, 19);  // Generated
			gridBagConstraints1.gridy = 0;  // Generated
			gridBagConstraints1.ipadx = 4;  // Generated
			gridBagConstraints1.ipady = 15;  // Generated
			gridBagConstraints1.gridx = 0;  // Generated
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;  // Generated
			gridBagConstraints.gridheight = 2;  // Generated
			gridBagConstraints.gridx = 0;  // Generated
			gridBagConstraints.gridy = 0;  // Generated
			gridBagConstraints.ipadx = -70;  // Generated
			gridBagConstraints.ipady = 155;  // Generated
			gridBagConstraints.weightx = 1.0;  // Generated
			gridBagConstraints.weighty = 1.0;  // Generated
			gridBagConstraints.insets = new Insets(64, 11, 14, 9);  // Generated
			jLabel1 = new JLabel();
			jLabel1.setText("Items in Selected Category");  // Generated
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJScrollPane1(), gridBagConstraints);  // Generated
			jContentPane.add(jLabel1, gridBagConstraints1);  // Generated
			jContentPane.add(getJTabbedPane1(), gridBagConstraints2);  // Generated
			jContentPane.add(getJButton3(), gridBagConstraints3);  // Generated
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
						FoodItem selectedObject = (FoodItem) temp.getSelectedValue();

						if(e.getClickCount() == 2)
						{
							if(selectedObject.isCategory())
							{
								ManageCategoryGUI n = new ManageCategoryGUI(selectedObject, mc);
								n.setVisible(true);
							}
							else
							{
								ManageItemGUI n = new ManageItemGUI(selectedObject, mc);
								n.setVisible(true);
							}
						}
					}
				});
				jScrollPane1 = new JScrollPane(jList1);
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jTabbedPane1	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane1()
	{
		if (jTabbedPane1 == null)
		{
			try
			{
				jTabbedPane1 = new JTabbedPane();
				jTabbedPane1.addTab("Add Category", null, getJPanel1(), null);  // Generated
				jTabbedPane1.addTab("Add Food Item", null, getJPanel2(), null);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jTabbedPane1;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1()
	{
		if (jPanel1 == null)
		{
			try
			{
				jLabel4 = new JLabel();
				jLabel4.setBounds(new Rectangle(55, 0, 63, 16));  // Generated
				jLabel4.setText("Category");  // Generated
				jLabel2 = new JLabel();
				jLabel2.setBounds(new Rectangle(5, 35, 41, 26));  // Generated
				jLabel2.setText("Name:");  // Generated
				jPanel1 = new JPanel();
				jPanel1.setLayout(null);  // Generated
				jPanel1.add(jLabel2, null);  // Generated
				jPanel1.add(getCategoryName(), null);  // Generated
				jPanel1.add(getAddCategory(), null);  // Generated
				jPanel1.add(jLabel4, null);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2()
	{
		if (jPanel2 == null)
		{
			try
			{
				jLabel7 = new JLabel();
				jLabel7.setBounds(new Rectangle(145, 65, 34, 26));  // Generated
				jLabel7.setText("$");  // Generated
				jLabel6 = new JLabel();
				jLabel6.setBounds(new Rectangle(5, 65, 41, 24));  // Generated
				jLabel6.setText("Price:");  // Generated
				jLabel5 = new JLabel();
				jLabel5.setBounds(new Rectangle(55, 0, 67, 16));  // Generated
				jLabel5.setText("Food Item");  // Generated
				jLabel3 = new JLabel();
				jLabel3.setBounds(new Rectangle(5, 35, 41, 26));  // Generated
				jLabel3.setText("Name:");  // Generated
				jPanel2 = new JPanel();
				jPanel2.setLayout(null);  // Generated
				jPanel2.add(jLabel3, null);  // Generated
				jPanel2.add(getItemName(), null);  // Generated
				jPanel2.add(getAddItem(), null);  // Generated
				jPanel2.add(jLabel5, null);  // Generated
				jPanel2.add(jLabel6, null);  // Generated
				jPanel2.add(getItemPrice(), null);  // Generated
				jPanel2.add(jLabel7, null);  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jPanel2;
	}

	/**
	 * This method initializes jTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getCategoryName()
	{
		if (categoryName == null)
		{
			try
			{
				categoryName = new JTextField();
				categoryName.setBounds(new Rectangle(45, 35, 126, 26));  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return categoryName;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddCategory()
	{
		if (addCategory == null)
		{
			try
			{
				addCategory = new JButton();
				addCategory.setBounds(new Rectangle(55, 60, 75, 29));  // Generated
				addCategory.setText("Add");  // Generated
				addCategory.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent e)
					{

						try
						{
							String name = categoryName.getText();
							if(name.equals("") == true)
							{
								JOptionPane.showMessageDialog(null, "Name cannot be blank");
							}
							else
							{
								int id = mc.getLatestId();

								FoodItem tempCategory = new FoodItem(id, name, 0, true);
								mc.processCategory(givenCategory, tempCategory);
								refreshList();
							}
						}
						catch (Exception e1)
						{
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
						categoryName.setText("");
						categoryName.requestFocus();
						refreshFlag = 0;
						refreshList();
					}
				});
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return addCategory;
	}

	/**
	 * This method initializes jTextField2	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getItemName()
	{
		if (itemName == null)
		{
			try
			{
				itemName = new JTextField();
				itemName.setBounds(new Rectangle(45, 35, 126, 26));  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return itemName;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddItem()
	{
		if (addItem == null)
		{
			try
			{
				addItem = new JButton();
				addItem.setBounds(new Rectangle(55, 110, 75, 29));  // Generated
				addItem.setText("Add");  // Generated
				addItem.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent e)
					{

						try
						{
							String name = itemName.getText();
							String priceString = itemPrice.getText();
							double price;
							try
							{
								price = Double.parseDouble(priceString);
								if(name.equals("") == true)
								{
									JOptionPane.showMessageDialog(null, "Name cannot be blank");
								}
								else
								{
									int id = mc.getLatestId();
									givenCategory.addItem(new FoodItem(id, name, price, false));
								}
								itemName.setText("");
								itemPrice.setText("");
							}
							catch (Exception e1)
							{
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Please check all fields");
							}
						}
						catch(NumberFormatException ex)
						{
							JOptionPane.showMessageDialog(null, "Please check the price you have entered, the field can only contain numbers in the form of 0 or 0.00");
						}
						refreshFlag = 1;
						refreshList();		

						itemName.requestFocus();
					}
				});

			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return addItem;
	}

	private void refreshList()
	{
		if(refreshFlag == -1)
		{
			Object[] temp1 = new Object[0];
			jList1.setListData(temp1);
		}

		else
		{
			ArrayList <FoodItem> jListMenuItems = givenCategory.getItems();

			int get = jListMenuItems.size();   	
			final FoodItem[] temp1 = new FoodItem[get];
			for(int i = 0; i < get; ++i)
			{
				temp1[i] = (FoodItem) jListMenuItems.get(i);
			}
			jList1.setListData(temp1);

		}
	}

	/**
	 * This method initializes jButton3	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton3()
	{
		if (jButton3 == null)
		{
			try
			{
				jButton3 = new JButton();
				jButton3.setText("Remove Selected Item");  // Generated
				jButton3.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent e)
					{
						Object selectedObject = jList1.getSelectedValue();
						FoodItem removeThis = (FoodItem) selectedObject;
						givenCategory.removeItem(removeThis);
						if(givenCategory.getItems().size() == 0)
						{
							refreshFlag = -1;
						}

						refreshList();
					}
				});
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return jButton3;
	}

	/**
	 * This method initializes jTextField3	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getItemPrice()
	{
		if (itemPrice == null)
		{
			try
			{
				itemPrice = new JTextField();
				itemPrice.setBounds(new Rectangle(45, 65, 101, 26));  // Generated
			}
			catch (java.lang.Throwable e)
			{
				// TODO: Something
			}
		}
		return itemPrice;
	}

}  //  @jve:decl-index=0:visual-constraint="160,20"
