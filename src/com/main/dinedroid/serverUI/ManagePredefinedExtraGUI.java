package com.main.dinedroid.serverUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
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
import com.main.dinedroid.serverlistener.MenuChangeListener;

public class ManagePredefinedExtraGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JScrollPane jScrollPane1 = null;
	private JLabel jLabel1 = null;
	private JScrollPane jScrollPane2 = null;
	private JTextField extraGroupType = null;
	private JTextField extraName = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JList jList1 = null;
	private JList predefinedList;
	private JList jList2 = null;
	private JList extrasList;
	private JButton jButton2 = null;
	private MenuController mc = null;
	private ExtraGroup selectedExtraGroup = null;
	private JLabel lblNewLabel;
	private JTextField extraPrice;

	/**
	 * @param owner
	 */
	public ManagePredefinedExtraGUI(MenuController passed) {
		super("Manage Predefined Extras");
		mc = passed;
		initialize();
		mc.addChangedListener(new MenuChangeListener() {
			@Override
			public void DoSomething(String changeType) {
				if (changeType.equals("Extra")) {
					refreshExtras();
				}
			}
		});
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(416, 312);
		this.setTitle("Manage Predefined Extras"); // Generated
		this.setContentPane(getJContentPane());
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		refreshExtras();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.insets = new Insets(4, 18, 5, 96); // Generated
			gridBagConstraints7.gridy = 3; // Generated
			gridBagConstraints7.gridx = 2; // Generated
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.insets = new Insets(11, 25, 10, 6); // Generated
			gridBagConstraints6.gridy = 1; // Generated
			gridBagConstraints6.gridx = 1; // Generated
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.insets = new Insets(52, 25, 13, 5); // Generated
			gridBagConstraints5.gridy = 0; // Generated
			gridBagConstraints5.gridx = 1; // Generated
			GridBagConstraints gbc_extraName = new GridBagConstraints();
			gbc_extraName.fill = GridBagConstraints.VERTICAL; // Generated
			gbc_extraName.gridx = 2; // Generated
			gbc_extraName.gridy = 1; // Generated
			gbc_extraName.ipadx = 147; // Generated
			gbc_extraName.weightx = 1.0; // Generated
			gbc_extraName.insets = new Insets(6, 3, 5, 25); // Generated
			GridBagConstraints gbc_extraGroupType = new GridBagConstraints();
			gbc_extraGroupType.fill = GridBagConstraints.VERTICAL; // Generated
			gbc_extraGroupType.gridx = 2; // Generated
			gbc_extraGroupType.gridy = 0; // Generated
			gbc_extraGroupType.ipadx = 147; // Generated
			gbc_extraGroupType.weightx = 1.0; // Generated
			gbc_extraGroupType.insets = new Insets(47, 3, 6, 25); // Generated
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridheight = 2;
			gridBagConstraints2.fill = GridBagConstraints.BOTH; // Generated
			gridBagConstraints2.gridwidth = 2; // Generated
			gridBagConstraints2.gridx = 1; // Generated
			gridBagConstraints2.gridy = 4; // Generated
			gridBagConstraints2.ipadx = 232; // Generated
			gridBagConstraints2.ipady = 72; // Generated
			gridBagConstraints2.weightx = 1.0; // Generated
			gridBagConstraints2.weighty = 1.0; // Generated
			gridBagConstraints2.insets = new Insets(1, 5, 9, 15); // Generated
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(35, 30, 5, 21); // Generated
			gridBagConstraints1.gridy = 0; // Generated
			gridBagConstraints1.gridx = 0; // Generated
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH; // Generated
			gridBagConstraints.gridheight = 5; // Generated
			gridBagConstraints.gridx = 0; // Generated
			gridBagConstraints.gridy = 1; // Generated
			gridBagConstraints.ipadx = 137; // Generated
			gridBagConstraints.ipady = 222; // Generated
			gridBagConstraints.weightx = 1.0; // Generated
			gridBagConstraints.weighty = 1.0; // Generated
			gridBagConstraints.insets = new Insets(2, 15, 9, 5); // Generated
			jLabel3 = new JLabel();
			jLabel3.setText("Extra:"); // Generated
			jLabel2 = new JLabel();
			jLabel2.setText("Type:"); // Generated
			jLabel1 = new JLabel();
			jLabel1.setHorizontalAlignment(SwingConstants.CENTER); // Generated
			jLabel1.setText("Predefined Extras"); // Generated
			jContentPane = new JPanel();
			GridBagLayout gbl_jContentPane = new GridBagLayout();
			gbl_jContentPane.columnWeights = new double[] { 0.0, 0.0, 1.0 };
			jContentPane.setLayout(gbl_jContentPane); // Generated
			jContentPane.add(getJScrollPane1(), gridBagConstraints); // Generated
			jContentPane.add(jLabel1, gridBagConstraints1); // Generated
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel.insets = new Insets(11, 25, 10, 6);
			gbc_lblNewLabel.gridx = 1;
			gbc_lblNewLabel.gridy = 2;
			jContentPane.add(getLblNewLabel(), gbc_lblNewLabel);
			GridBagConstraints gbc_extraPrice = new GridBagConstraints();
			gbc_extraPrice.insets = new Insets(6, 3, 5, 25);
			gbc_extraPrice.fill = GridBagConstraints.HORIZONTAL;
			gbc_extraPrice.gridx = 2;
			gbc_extraPrice.gridy = 2;
			jContentPane.add(getExtraPrice(), gbc_extraPrice);
			jContentPane.add(getJScrollPane2(), gridBagConstraints2); // Generated
			jContentPane.add(getExtraGroupType(), gbc_extraGroupType); // Generated
			jContentPane.add(getExtraName(), gbc_extraName); // Generated
			jContentPane.add(jLabel2, gridBagConstraints5); // Generated
			jContentPane.add(jLabel3, gridBagConstraints6); // Generated
			jContentPane.add(getJButton2(), gridBagConstraints7); // Generated
			jContentPane.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					extraGroupType.setText("");
					extraName.setText("");
					selectedExtraGroup = null;
					jList1.setSelectedIndex(-1);
					jList2.setSelectedIndex(-1);
					clearList();
				}
			});
		}
		return jContentPane;
	}

	/**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			try {
				predefinedList = new JList();
				predefinedList
						.addMouseListener(new java.awt.event.MouseAdapter() {
							public void mouseClicked(java.awt.event.MouseEvent e) {
								JList temp = (JList) e.getSource();
								selectedExtraGroup = (ExtraGroup) temp
										.getSelectedValue();
								extraGroupType.setText(selectedExtraGroup
										.getExtraType());
								refreshSpecifics();
							}
						});
				jScrollPane1 = new JScrollPane(predefinedList);
			} catch (java.lang.Throwable e) {
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
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			try {
				extrasList = new JList();
				extrasList.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent e) {
						System.out.println("mouseClicked()"); // TODO
																// Auto-generated
																// Event stub
																// mouseClicked()
					}
				});
				jScrollPane2 = new JScrollPane(extrasList);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jScrollPane2;
	}

	/**
	 * This method initializes jTextField1
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getExtraGroupType() {
		if (extraGroupType == null) {
			try {
				extraGroupType = new JTextField();
				extraGroupType.setText(""); // Generated
				extraGroupType
						.addMouseListener(new java.awt.event.MouseAdapter() {
							public void mouseClicked(java.awt.event.MouseEvent e) {
								selectedExtraGroup = null;
								clearList();
							}
						});

			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return extraGroupType;
	}

	/**
	 * This method initializes jTextField2
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getExtraName() {
		if (extraName == null) {
			try {
				extraName = new JTextField();
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return extraName;
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
				jButton2.setText("Add");  // Generated
				jButton2.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent e)
					{
						
						if(extraGroupType.getText().equals("") && selectedExtraGroup == null)
						{
							JOptionPane.showMessageDialog(null, "Please check all fields");
						}
						if(extraGroupType.getText().equals("") != true && selectedExtraGroup != null)
						{
							if(extraName.getText().equals("") != true)
							{
								int id = mc.getLatestId();
								String name = extraName.getText();
								String tempPrice = extraPrice.getText();
								try
								{
									Double price = Double.parseDouble(tempPrice);
									FoodItem tempExtra = new FoodItem(id, name, price, false);
									selectedExtraGroup.addExtra(tempExtra);
									refreshSpecifics();
								}
								catch(NumberFormatException f)
								{
									JOptionPane.showMessageDialog(null, "Please enter a valid price");
								}
							
							}
							else
							{
								JOptionPane.showMessageDialog(null, "Please check all fields");
							}
							extraName.setText("");
							extraPrice.setText("");
						}
						
						if(extraGroupType.getText().equals("") != true && selectedExtraGroup == null)
						{
							String extraType = extraGroupType.getText();
							ExtraGroup addThis = new ExtraGroup(extraType);
							if(extraName.getText().equals("") != true)
							{
								int id = mc.getLatestId();
								String name = extraName.getText();
								String tempPrice = extraPrice.getText();
								try
								{
									Double price = Double.parseDouble(tempPrice);
									FoodItem tempExtra = new FoodItem(id, name, price, false);
									addThis.addExtra(tempExtra);
									selectedExtraGroup = addThis;
									refreshSpecifics();
								}
								catch(NumberFormatException f)
								{
									JOptionPane.showMessageDialog(null, "Please enter a valid price");
								}
							}
							mc.addPredefinedExtra(addThis);
							extraGroupType.setText("");
							extraName.setText("");
							extraPrice.setText("");
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

	@SuppressWarnings("unchecked")
	private void refreshExtras() {
		ArrayList<ExtraGroup> jListCategories = mc.getPredefinedExtras();
		if (jListCategories.size() > 0) {
			int get = jListCategories.size();
			System.out.println(get);
			final ExtraGroup[] temp1 = new ExtraGroup[get];
			for (int i = 0; i < get; ++i) {
				temp1[i] = jListCategories.get(i);
			}
			predefinedList.setListData(temp1);
		}

		else {

		}
	}

	private void refreshSpecifics() {
		ArrayList<FoodItem> jListCategories = selectedExtraGroup.getExtras();

		if (jListCategories.size() > 0) {
			int get = jListCategories.size();
			System.out.println(get);
			final FoodItem[] temp1 = new FoodItem[get];
			for (int i = 0; i < get; ++i) {
				temp1[i] = jListCategories.get(i);
			}
			extrasList.setListData(temp1);
		}

		else {

		}
	}

	private void clearList() {
		final String[] temp1 = new String[0];
		extrasList.setListData(temp1);
	}

	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Price:");
			lblNewLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		}
		return lblNewLabel;
	}

	private JTextField getExtraPrice() {
		if (extraPrice == null) {
			extraPrice = new JTextField();
			extraPrice.setColumns(10);
		}
		return extraPrice;
	}
} // @jve:decl-index=0:visual-constraint="198,20"
