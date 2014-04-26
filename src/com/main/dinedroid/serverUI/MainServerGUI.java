package com.main.dinedroid.serverUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.main.dinedroid.menu.FoodItem;
import com.main.dinedroid.models.Order;
import com.main.dinedroid.models.Table;
import com.main.dinedroid.models.Waiter;
import com.main.dinedroid.server.MenuController;
import com.main.dinedroid.server.TablesController;
import com.main.dinedroid.server.WaitersController;
import com.main.dinedroid.server.main;
import com.main.dinedroid.serverlistener.PrintActionListener;
import com.main.dinedroid.serverlistener.TableChangeListener;
import com.main.dinedroid.serverlistener.WaiterChangeListener;
import com.main.dinedroid.swing.CascadingJFrame;
import com.main.dinedroid.swing.VectorButton;

public class MainServerGUI extends CascadingJFrame {

	private static final long serialVersionUID = 1L;
	private JButton addCategory = null;
	private JButton addCategoryEnable = null;
	private JButton addWaiter;
	private JButton jButton3 = null;
	private JButton jButton4 = null;
	private VectorButton addWaiterEnable;
	private JTextField categoryName = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private DefaultListModel jList1Model = null;
	private JMenuBar jJMenuBar = null;
	private JMenu jMenu1 = null;
	private JMenu jMenu2 = null;
	private JMenuItem jMenuItem1 = null;
	private JMenuItem jMenuItem2 = null;
	private JMenuItem jMenuItem3 = null;
	private JScrollPane jScrollPane1 = null;
	private JScrollPane jScrollPane2 = null;
	private JScrollPane jScrollPane3 = null;
	private JScrollPane jScrollPane4;
	private JTabbedPane jTabbedPane1 = null;
	private JTextField jTextField2 = null;
	public MenuController mc = null;
	private JList menuList;
	private JPanel menuPanel = null;
	private JMenu mnNewMenu;
	private JMenuItem mntmCreateTable;
	private JList orderList;
	private JPanel orderPanel = null;
	private FoodItem selectedCategory = null;
	private Order selectedOrder = null;
	private JList tablesList;
	private JPanel tablesPanel;
	public TablesController tc = null;
	private JTextField waiterFirstName;
	private JTextField waiterLastName;
	private JList waiterList;
	private JPanel waiterPanel;
	public WaitersController wc = null;

	/**
	 * This is the default constructor
	 */
	public MainServerGUI(TablesController tc, WaitersController wc,
			MenuController mc) {
		super("WaitDroid Server");
		jList1Model = new DefaultListModel();
		this.tc = tc;
		this.wc = wc;
		this.mc = mc;
		initialize();
		/**
		 * Add listeners
		 */
		tc.addChangedListener(new TableChangeListener() {
			@Override
			public void DoSomething(String changeType) {
				if (changeType.equals("Order")) {
					refreshOrders();
				}

				else {
					refreshTables();
				}
			}
		});
		wc.addChangedListener(new WaiterChangeListener() {
			@Override
			public void DoSomething(String changeType) {
				if (changeType.equals("Waiter")) {
					refreshWaiters();
				}
			}

		});

	}
	
	/**
	 * Generate the QR Code given the type and ID<br>
	 * This generates codes of the form:<br>
	 * <b>Type</b>||<b>ID</b><br>
	 * 1:Type is Waiter or Table<br>
	 * 2:ID to generate<br>
	 * This will generate and display a QR code in a JFrame
	 * @param type Waiter or Table
	 * @param id ID to generate
	 */
	public void generateQR(String type, int id) {
		String myCodeText = type+"||"+id;
		int size = 125;
		String fileType = "png";
		try {
			Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = qrCodeWriter.encode(myCodeText,
					BarcodeFormat.QR_CODE, size, size, hintMap);
			int CrunchifyWidth = byteMatrix.getWidth();
			final BufferedImage image = new BufferedImage(CrunchifyWidth,
					CrunchifyWidth, BufferedImage.TYPE_INT_RGB);
			image.createGraphics();

			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
			graphics.setColor(Color.BLACK);

			for (int i = 0; i < CrunchifyWidth; i++) {
				for (int j = 0; j < CrunchifyWidth; j++) {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}
			// ImageIO.write(image, fileType, myFile);
			CascadingJFrame frame = new CascadingJFrame("QR Code");
			frame.getContentPane().setLayout(new FlowLayout());
			frame.getContentPane().add(new JLabel(new ImageIcon(image)));
			VectorButton print = new VectorButton();
			print.setText("Print");
			print.setForeground(Color.WHITE);
			print.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
			print.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					new Thread(new PrintActionListener(image)).start();
				}
			});
			frame.getContentPane().add(print);
			frame.setResizable(false);
			frame.pack();
			frame.setVisible(true);
		} 
		catch (WriterException e) {
			e.printStackTrace();
		}
		System.out.println("\n\nYou have successfully created QR Code.");
	}

	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddCategory() {
		if (addCategory == null) {
			try {
				addCategory = new JButton();
				addCategory.setText("Add"); // Generated
				addCategory.setEnabled(false);
				addCategory
						.addActionListener(new java.awt.event.ActionListener() {
							@Override
							public void actionPerformed(
									java.awt.event.ActionEvent e) {
								String name = categoryName.getText();
								int id = mc.getLatestId();
								FoodItem newCategory = new FoodItem(id, name,
										0, true);
								jLabel1.setEnabled(false);
								categoryName.setEnabled(false);
								addCategory.setEnabled(false);
								mc.addTopCategory(newCategory);
								refreshCategories();
								addCategoryEnable.requestFocus();
							}
						});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return addCategory;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddCategoryEnable() {
		if (addCategoryEnable == null) {
			try {
				addCategoryEnable = new VectorButton();
				addCategoryEnable.setText("Add Category"); // Generated
				addCategoryEnable.setForeground(Color.white);
				addCategoryEnable.setFont(new Font("Helvetica Neue",
						Font.PLAIN, 13)); // Generated
				addCategoryEnable.setToolTipText("Add main category.");
				addCategoryEnable
						.addActionListener(new java.awt.event.ActionListener() {
							@Override
							public void actionPerformed(
									java.awt.event.ActionEvent e) {
								jLabel1.setEnabled(true);
								categoryName.setEnabled(true);
								addCategory.setEnabled(true);
								categoryName.requestFocus();
							}
						});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return addCategoryEnable;
	}

	private JButton getAddWaiterButton() {
		if (addWaiter == null) {
			addWaiter = new JButton();
			addWaiter.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String firstName = waiterFirstName.getText();
					String lastName = waiterLastName.getText();

					if (!firstName.equals("") && !lastName.equals(""))
						;
					{
						main.wc.createWaiter(firstName, lastName);
						waiterFirstName.setEnabled(false);
						waiterLastName.setEnabled(false);
						addWaiter.setEnabled(false);
					}
				}
			});
			addWaiter.setText("Add");
			addWaiter.setEnabled(false);
		}
		return addWaiter;
	}

	private VectorButton getAddWaiterEnable() {
		if (addWaiterEnable == null) {
			addWaiterEnable = new VectorButton();
			addWaiterEnable.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					waiterFirstName.setEnabled(true);
					waiterLastName.setEnabled(true);
					addWaiter.setEnabled(true);
					waiterFirstName.requestFocus();
				}
			});
			addWaiterEnable.setToolTipText("Add a Waiter");
			addWaiterEnable.setText("Add Waiter");
			addWaiterEnable.setForeground(Color.WHITE);
			addWaiterEnable.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		}
		return addWaiterEnable;
	}

	/**
	 * This method initializes jTextField1
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCategoryName() {
		if (categoryName == null) {
			try {
				categoryName = new JTextField();
				categoryName.setEnabled(false);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return categoryName;
	}

	private JButton getJButton4() {
		if (jButton4 == null) {
			try {
				jButton4 = new VectorButton();
				jButton4.setText("Add Category"); // Generated
				jButton4.setForeground(Color.white);
				jButton4.setFont(new Font("Helvetica Neue", Font.PLAIN, 13)); // Generated
				jButton4.setToolTipText("Add main category.");
				jButton4.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent e) {
						jTextField2.setEnabled(true);
						jButton3.setEnabled(true);
						jTextField2.requestFocus();
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jButton4;
	}

	/**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			try {
				jJMenuBar = new JMenuBar();
				jJMenuBar.setPreferredSize(new Dimension(0, 20)); // Generated
				jJMenuBar.add(getJMenu1()); // Generated
				jJMenuBar.add(getJMenu2()); // Generated
				jJMenuBar.add(getMnNewMenu());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jJMenuBar;
	}

	private JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new JLabel();
			jLabel2.setText("First Name");
			jLabel2.setEnabled(false);
		}
		return jLabel2;
	}

	private JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new JLabel();
			jLabel3.setText("Last Name");
			jLabel3.setEnabled(false);
		}
		return jLabel3;
	}

	/**
	 * This method initializes jMenu1
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenu1() {
		if (jMenu1 == null) {
			try {
				jMenu1 = new JMenu();
				jMenu1.setText("File"); // Generated
				jMenu1.add(getJMenuItem1()); // Generated
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jMenu1;
	}

	/**
	 * This method initializes jMenu2
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenu2() {
		if (jMenu2 == null) {
			try {
				jMenu2 = new JMenu();
				jMenu2.setText("Options"); // Generated
				jMenu2.add(getJMenuItem2()); // Generated
				jMenu2.add(getJMenuItem3());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jMenu2;
	}

	/**
	 * This method initializes jMenuItem1
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem1() {
		if (jMenuItem1 == null) {
			try {
				jMenuItem1 = new JMenuItem();
				jMenuItem1.setText("Save Changes"); // Generated
				jMenuItem1
						.addActionListener(new java.awt.event.ActionListener() {
							@Override
							public void actionPerformed(
									java.awt.event.ActionEvent e) {
								mc.saveMenu();
							}
						});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jMenuItem1;
	}

	/**
	 * This method initializes jMenuItem2
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem2() {
		if (jMenuItem2 == null) {
			try {
				jMenuItem2 = new JMenuItem();
				jMenuItem2.setText("Change Chef'sView IP");
				jMenuItem2
						.addActionListener(new java.awt.event.ActionListener() {
							@Override
							public void actionPerformed(
									java.awt.event.ActionEvent e) {

							}
						});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jMenuItem2;
	}

	private JMenuItem getJMenuItem3() {
		if (jMenuItem3 == null) {
			try {
				jMenuItem3 = new JMenuItem();
				jMenuItem3.setText("Manage Predefined Extras");
				jMenuItem3
						.addActionListener(new java.awt.event.ActionListener() {
							@Override
							public void actionPerformed(
									java.awt.event.ActionEvent e) {
								ManagePredefinedExtraGUI a = new ManagePredefinedExtraGUI(
										mc);
								a.setVisible(true);
							}

						});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jMenuItem3;
	}

	/**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			try {
				menuList = new JList();
				menuList.setFont(new Font("Helvetica Neue", Font.PLAIN, 12)); // Generated
				menuList.addMouseListener(new java.awt.event.MouseAdapter() {
					@Override
					public void mouseClicked(java.awt.event.MouseEvent e) {
						JList temp = (JList) e.getSource();
						selectedCategory = (FoodItem) temp.getSelectedValue();
						if (e.getClickCount() == 2) {
							ManageCategoryGUI catGUI = new ManageCategoryGUI(
									selectedCategory, mc);
							catGUI.setVisible(true);
						}
					}
				});
				menuList.addKeyListener(new java.awt.event.KeyAdapter() {
					@Override
					public void keyReleased(java.awt.event.KeyEvent e) {
						JList temp = (JList) e.getSource();
						selectedCategory = (FoodItem) temp.getSelectedValue();
						if (e.getKeyCode() == KeyEvent.VK_DELETE) {
							mc.removeTopCategory(selectedCategory);
							refreshCategories();
						}
					}
				});
				jScrollPane1 = new JScrollPane(menuList);
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
				orderList = new JList();
				orderList.addMouseListener(new java.awt.event.MouseAdapter() {
					@Override
					public void mouseClicked(java.awt.event.MouseEvent e) {
						JList temp = (JList) e.getSource();
						selectedOrder = (Order) temp.getSelectedValue();
						if (e.getClickCount() == 2) {
							JOptionPane.showMessageDialog(null, selectedOrder.detailedString());
						}
						if(e.isShiftDown())
						{
							main.tc.closeTableOrder(selectedOrder.getOrderTable());
						}
					}

				});
				orderList.addKeyListener(new java.awt.event.KeyAdapter() {
					@Override
					public void keyReleased(java.awt.event.KeyEvent e) {
						JList temp = (JList) e.getSource();
						selectedOrder = (Order) temp.getSelectedValue();
						if (e.getKeyCode() == KeyEvent.VK_DELETE) {
							tc.removeTableOrder(selectedOrder.getOrderTable());
							refreshOrders();
						}
					}
				});
				jScrollPane2 = new JScrollPane(orderList);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jScrollPane2;
	}

	private JScrollPane getJScrollPane4() {
		if (jScrollPane4 == null) {
			waiterList = new JList();
			waiterList.setToolTipText("Double-click to remove");
			waiterList.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {
					JList temp = (JList) e.getSource();
					Waiter selectedWaiter = (Waiter) temp.getSelectedValue();
					if (e.getClickCount() == 2) {
						generateQR("Waiter",selectedWaiter.getId());
					}
				}

			});
			waiterList.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyReleased(java.awt.event.KeyEvent e) {
					JList temp = (JList) e.getSource();
					Waiter selectedWaiter = (Waiter) temp.getSelectedValue();
					if (e.getKeyCode() == KeyEvent.VK_DELETE) {
						if (main.wc.removeWater(selectedWaiter.getId())) {
							JOptionPane.showMessageDialog(null,
									"Successfully removed waiter: "
											+ selectedWaiter);
							refreshWaiters();
						}
					}
				}
			});
			jScrollPane4 = new JScrollPane(waiterList);
		}
		return jScrollPane4;
	}

	/**
	 * This method initializes jTabbedPane1
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane1() {
		if (jTabbedPane1 == null) {
			try {
				jTabbedPane1 = new JTabbedPane();
				jTabbedPane1.setTabPlacement(SwingConstants.TOP); // Generated
				jTabbedPane1
						.setFont(new Font("Helvetica Neue", Font.PLAIN, 14)); // Generated
				jTabbedPane1.addTab("Orders", null, getOrderPanel(),
						"View Table Information"); // Generated
				jTabbedPane1.addTab("Menu", null, getMenuPanel(),
						"Edit Restaurant Menu");
				jTabbedPane1.addTab("Waiters", null, getWaiterPanel(), null);
				jTabbedPane1.addTab("Tables", null, getTablesPanel(), null);
				jTabbedPane1
						.addChangeListener(new javax.swing.event.ChangeListener() {
							@Override
							public void stateChanged(
									javax.swing.event.ChangeEvent e) {
								JTabbedPane temp = (JTabbedPane) e.getSource();
								if (temp.getTitleAt(1) == "Menu") {
									if (mc.getMenu().getItems().size() != 0) {
										refreshCategories();
										refreshOrders();
										refreshTables();
										refreshWaiters();
									}
								}
							}
						});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jTabbedPane1;
	}

	private JTextField getJTextField2() {
		if (jTextField2 == null) {
			try {
				jTextField2 = new JTextField();
				jTextField2.setEnabled(false);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jTextField2;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMenuPanel() {
		if (menuPanel == null) {
			try {
				GridBagConstraints gbc_addCategory = new GridBagConstraints();
				gbc_addCategory.insets = new Insets(7, 2, 3, 112); // Generated
				gbc_addCategory.gridy = 1; // Generated
				gbc_addCategory.gridx = 1; // Generated
				GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
				gridBagConstraints3.insets = new Insets(7, 100, 6, 157); // Generated
				gridBagConstraints3.gridy = 1; // Generated
				gridBagConstraints3.ipadx = 1; // Generated
				gridBagConstraints3.ipady = 10; // Generated
				gridBagConstraints3.gridx = 0; // Generated
				GridBagConstraints gbc_categoryName = new GridBagConstraints();
				gbc_categoryName.fill = GridBagConstraints.VERTICAL; // Generated
				gbc_categoryName.gridx = 0; // Generated
				gbc_categoryName.gridy = 1; // Generated
				gbc_categoryName.ipadx = 142; // Generated
				gbc_categoryName.ipady = -2; // Generated
				gbc_categoryName.weightx = 1.0; // Generated
				gbc_categoryName.insets = new Insets(7, 200, 6, 2); // Generated
				GridBagConstraints gbc_addCategoryEnable = new GridBagConstraints();
				gbc_addCategoryEnable.insets = new Insets(10, 195, 7, 186); // Generated
				gbc_addCategoryEnable.gridx = 0; // Generated
				gbc_addCategoryEnable.gridy = 0; // Generated
				gbc_addCategoryEnable.ipadx = 37; // Generated
				gbc_addCategoryEnable.ipady = -3; // Generated
				gbc_addCategoryEnable.gridwidth = 2; // Generated
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.fill = GridBagConstraints.BOTH; // Generated
				gridBagConstraints.gridwidth = 2; // Generated
				gridBagConstraints.gridx = 0; // Generated
				gridBagConstraints.gridy = 2; // Generated
				gridBagConstraints.ipadx = 276; // Generated
				gridBagConstraints.ipady = 139; // Generated
				gridBagConstraints.weightx = 1.0; // Generated
				gridBagConstraints.weighty = 1.0; // Generated
				gridBagConstraints.insets = new Insets(4, 5, 4, 6); // Generated
				jLabel1 = new JLabel();
				jLabel1.setText("Category Name:"); // Generated
				jLabel1.setEnabled(false);
				menuPanel = new JPanel();
				menuPanel.setLayout(new GridBagLayout()); // Generated
				menuPanel.add(getJScrollPane1(), gridBagConstraints); // Generated
				menuPanel.add(getAddCategoryEnable(), gbc_addCategoryEnable); // Generated
				menuPanel.add(getCategoryName(), gbc_categoryName); // Generated
				menuPanel.add(jLabel1, gridBagConstraints3); // Generated
				menuPanel.add(getAddCategory(), gbc_addCategory); // Generated
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return menuPanel;
	}

	private JMenu getMnNewMenu() {
		if (mnNewMenu == null) {
			mnNewMenu = new JMenu("Create");
			mnNewMenu.add(getMntmCreateTable());
		}
		return mnNewMenu;
	}

	private JMenuItem getMntmCreateTable() {
		if (mntmCreateTable == null) {
			mntmCreateTable = new JMenuItem("Create Table");
			mntmCreateTable.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String num = JOptionPane
							.showInputDialog("Please enter the table number");
					try {
						int tableNum = Integer.parseInt(num);
						if (main.tc.createTable(tableNum)) {
							JOptionPane.showMessageDialog(null,
									"Successfully added table!");
						} else {
							JOptionPane.showMessageDialog(null,
									"Error: Duplicate table number.");
						}
						refreshTables();
					} catch (Exception nf) {
						JOptionPane.showMessageDialog(null,
								"Invalid number, please try again.");
					}

				}
			});
		}
		return mntmCreateTable;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getOrderPanel() {
		if (orderPanel == null) {
			try {
				GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
				gridBagConstraints6.insets = new Insets(6, 15, 4, 448); // Generated
				gridBagConstraints6.gridy = 0; // Generated
				gridBagConstraints6.ipadx = -10; // Generated
				gridBagConstraints6.ipady = 11; // Generated
				gridBagConstraints6.gridx = 0; // Generated
				GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
				gridBagConstraints5.fill = GridBagConstraints.BOTH; // Generated
				gridBagConstraints5.gridx = 0; // Generated
				gridBagConstraints5.gridy = 1; // Generated
				gridBagConstraints5.ipadx = 276; // Generated
				gridBagConstraints5.ipady = 178; // Generated
				gridBagConstraints5.weightx = 1.0; // Generated
				gridBagConstraints5.weighty = 1.0; // Generated
				gridBagConstraints5.insets = new Insets(4, 5, 4, 6); // Generated
				orderPanel = new JPanel();
				orderPanel.setLayout(new GridBagLayout()); // Generated
				orderPanel.add(getJScrollPane2(), gridBagConstraints5); // Generated
				// jPanel1.setBackground(Color.gray); // Generated
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return orderPanel;
	}

	private JPanel getTablesPanel() {
		if (tablesPanel == null) {
			tablesPanel = new JPanel();
			tablesPanel.setLayout(null);

			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(6, 76, 535, 259);
			tablesPanel.add(scrollPane);

			tablesList = new JList();

			tablesList.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {
					JList temp = (JList) e.getSource();
					Table selectedTable = (Table) temp.getSelectedValue();
					if (e.getClickCount() == 2) {
						generateQR("Table", selectedTable.getId());
					}
				}

			});

			tablesList.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyReleased(java.awt.event.KeyEvent e) {
					JList temp = (JList) e.getSource();
					Table selectedTable = (Table) temp.getSelectedValue();
					if (e.getKeyCode() == KeyEvent.VK_DELETE) {
						if (main.tc.removeTable(selectedTable.getId())) {
							JOptionPane.showMessageDialog(null,
									"Successfully removed table: "
											+ selectedTable);
							refreshTables();
						}
					}
				}
			});
			scrollPane.setViewportView(tablesList);
		}
		return tablesPanel;
	}

	private JTextField getWaiterFirstName() {
		if (waiterFirstName == null) {
			waiterFirstName = new JTextField();
			waiterFirstName.setEnabled(false);
		}
		return waiterFirstName;
	}

	private JTextField getWaiterLastName() {
		if (waiterLastName == null) {
			waiterLastName = new JTextField();
			waiterLastName.setEnabled(false);
		}
		return waiterLastName;
	}

	private JPanel getWaiterPanel() {
		if (waiterPanel == null) {
			waiterPanel = new JPanel();
			GridBagLayout gbl_waiterPanel = new GridBagLayout();
			gbl_waiterPanel.columnWidths = new int[] { 0, 0, 0 };
			gbl_waiterPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
			gbl_waiterPanel.columnWeights = new double[] { 1.0, 0.0,
					Double.MIN_VALUE };
			gbl_waiterPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
					Double.MIN_VALUE };
			waiterPanel.setLayout(gbl_waiterPanel);
			GridBagConstraints gbc_addWaiterEnable = new GridBagConstraints();
			gbc_addWaiterEnable.gridwidth = 2;
			gbc_addWaiterEnable.ipady = -3;
			gbc_addWaiterEnable.ipadx = 37;
			gbc_addWaiterEnable.insets = new Insets(10, 195, 7, 186);
			gbc_addWaiterEnable.gridx = 0;
			gbc_addWaiterEnable.gridy = 0;
			waiterPanel.add(getAddWaiterEnable(), gbc_addWaiterEnable);
			GridBagConstraints gbc_jLabel2 = new GridBagConstraints();
			gbc_jLabel2.ipady = 10;
			gbc_jLabel2.ipadx = 1;
			gbc_jLabel2.insets = new Insets(7, 100, 6, 157);
			gbc_jLabel2.gridx = 0;
			gbc_jLabel2.gridy = 1;
			waiterPanel.add(getJLabel2(), gbc_jLabel2);
			GridBagConstraints gbc_waiterFirstName = new GridBagConstraints();
			gbc_waiterFirstName.weightx = 1.0;
			gbc_waiterFirstName.ipady = -2;
			gbc_waiterFirstName.ipadx = 142;
			gbc_waiterFirstName.insets = new Insets(7, 200, 6, 5);
			gbc_waiterFirstName.gridx = 0;
			gbc_waiterFirstName.gridy = 1;
			GridBagConstraints gbc_jLabel3 = new GridBagConstraints();
			gbc_jLabel3.ipady = 10;
			gbc_jLabel3.ipadx = 1;
			gbc_jLabel3.insets = new Insets(7, 100, 6, 157);
			gbc_jLabel3.gridx = 0;
			gbc_jLabel3.gridy = 2;
			waiterPanel.add(getJLabel3(), gbc_jLabel3);
			waiterPanel.add(getWaiterFirstName(), gbc_waiterFirstName);
			GridBagConstraints gbc_jTextField3 = new GridBagConstraints();
			gbc_jTextField3.anchor = GridBagConstraints.EAST;
			gbc_jTextField3.insets = new Insets(7, 2, 5, 112);
			gbc_jTextField3.gridx = 1;
			gbc_jTextField3.gridy = 2;
			waiterPanel.add(getAddWaiterButton(), gbc_jTextField3);
			GridBagConstraints gbc_waiterLastName = new GridBagConstraints();
			gbc_waiterLastName.fill = GridBagConstraints.BOTH;
			gbc_waiterLastName.weightx = 1.0;
			gbc_waiterLastName.insets = new Insets(7, 200, 6, 5);
			gbc_waiterLastName.gridx = 0;
			gbc_waiterLastName.gridy = 2;
			gbc_waiterLastName.ipady = 0;
			gbc_waiterLastName.ipadx = 142;
			waiterPanel.add(getWaiterLastName(), gbc_waiterLastName);
			GridBagConstraints gbc_jScrollPane4 = new GridBagConstraints();
			gbc_jScrollPane4.weighty = 1.0;
			gbc_jScrollPane4.weightx = 1.0;
			gbc_jScrollPane4.ipady = 139;
			gbc_jScrollPane4.ipadx = 276;
			gbc_jScrollPane4.insets = new Insets(4, 5, 4, 6);
			gbc_jScrollPane4.fill = GridBagConstraints.BOTH;
			gbc_jScrollPane4.gridwidth = 2;
			gbc_jScrollPane4.gridx = 0;
			gbc_jScrollPane4.gridy = 3;
			waiterPanel.add(getJScrollPane4(), gbc_jScrollPane4);
		}
		return waiterPanel;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(568, 429);
		this.setJMenuBar(getJJMenuBar()); // Generated
		this.setFont(new Font("Helvetica Neue", Font.PLAIN, 13)); // Generated
		// this.setBackground(Color.gray); // Generated
		this.setContentPane(getJTabbedPane1()); // Generated
		this.setTitle("DineDroid Server");
		refreshOrders();
		refreshTables();
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		WindowListener windowListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showConfirmDialog(null,
						"Would you like to quit?", "Quit?",
						JOptionPane.YES_NO_OPTION);
				int confirmSave = JOptionPane.showConfirmDialog(null,
						"Would you like to save?", "Save?",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION
						&& confirmSave == JOptionPane.YES_OPTION) {
					runSave();
					System.exit(0);
				}
				if (confirm == JOptionPane.YES_OPTION
						&& confirmSave == JOptionPane.NO_OPTION) {
					System.exit(0);
				} else {

				}
			}
		};
		this.addWindowListener(windowListener);
		// this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/**
	 * Refresh the Menu Categories list after querying the MenuController
	 */
	private void refreshCategories() {
		ArrayList<FoodItem> jListCategories = mc.getMenu().getItems();

		int get = jListCategories.size();
		System.out.println(get);
		final FoodItem[] temp1 = new FoodItem[get];
		for (int i = 0; i < get; ++i) {
			temp1[i] = jListCategories.get(i);
		}
		menuList.setListData(temp1);
	}

	/**
	 * Refresh the orders list after querying the TableController
	 */
	private void refreshOrders() {
		ArrayList<Order> jListCategories = new ArrayList<Order>();
		for (Table t : tc.getAllTables()) {
			jListCategories.add(t.getOrder());
		}

		int get = jListCategories.size();
		System.out.println(get);
		final Order[] temp1 = new Order[get];
		for (int i = 0; i < get; ++i) {
			temp1[i] = jListCategories.get(i);
		}
		orderList.setListData(temp1);
	}

	/**
	 * Refresh the tables list after querying the TableController
	 */
	private void refreshTables() {
		// TODO Auto-generated method stub
		ArrayList<Table> jListCategories = new ArrayList<Table>();
		for (Table t : tc.getAllTables()) {
			jListCategories.add(t);
		}
		int get = jListCategories.size();
		System.err.println("Number of tables: " + get);
		final Table[] temp1 = new Table[get];
		for (int i = 0; i < get; ++i) {
			temp1[i] = jListCategories.get(i);
		}
		tablesList.setListData(temp1);
	}

	/**
	 * Refresh the waiters list after querying the WaiterController
	 */
	private void refreshWaiters() {
		// TODO Auto-generated method stub
		ArrayList<Waiter> jListCategories = new ArrayList<Waiter>();
		for (Waiter w : wc.getAllWaiters()) {
			jListCategories.add(w);
		}
		int get = jListCategories.size();
		System.out.println(get);
		final Waiter[] temp1 = new Waiter[get];
		for (int i = 0; i < get; ++i) {
			temp1[i] = jListCategories.get(i);
		}
		waiterList.setListData(temp1);
	}

	/**
	 * Clean up and save the state
	 */
	public void runSave() {
		mc.saveMenu();
		tc.saveTables();
		wc.saveIdCounter();
	}
} // @jve:decl-index=0:visual-constraint="140,2"
