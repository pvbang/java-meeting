package client;

import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;

import dao.ServerDAO;
import dao.UserRoomDAO;
import model.Server;
import model.User;
import model.UserRoom;

public class ConnectServerScreen extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private User user;
	public ServerData connectedServer;
	JTable serverTable;
	List<ServerData> serverList;
	private ServerDAO serverDAO = new ServerDAO();
	private UserRoomDAO userRoomDAO = new UserRoomDAO();
	String rand = getRandomString(3) + "-" + getRandomString(3) + "-" + getRandomString(3);

	public static JPopupMenu popupMenu;

	public ConnectServerScreen(User user) {
		GBCBuilder gbc = new GBCBuilder(1, 1);
		JPanel connectServerContent = new JPanel(new GridBagLayout());
		connectServerContent.setBackground(new Color(255, 255, 255));

		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		
		JButton refreshButton = new JButton(" Làm mới danh sách", new ImageIcon(s+"\\resources\\refresh.png", "refresh"));
		refreshButton.setActionCommand("refresh");
		refreshButton.addActionListener(this);
		this.user = user;
		
		serverTable = new JTable();
		serverTable.setForeground(new Color(0, 0, 0));
		serverTable.setRowHeight(25);
		serverTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (column == 6) {
					c.setForeground(value.toString().equals("Hoạt động") ? Color.green : Color.red);
					c.setFont(new Font("Dialog", Font.BOLD, 12));
				} else
					c.setForeground(Color.black);

				return c;
			}
		});

		updateServerTable();

		JScrollPane serverScrollPane = new JScrollPane(serverTable);
		serverScrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách các phòng họp"));
		serverScrollPane.setBackground(new Color(255, 255, 255));
		
		JPopupMenu menuLogout = new JPopupMenu();
		JMenuItem logoutItem = new JMenuItem("Đăng xuất");
		logoutItem.setIcon(new ImageIcon(s+"\\resources\\exit.png", "exit"));
		menuLogout.add(logoutItem);
		
        JLabel nameUserLabel = new JLabel(user.getName(), new ImageIcon(s+"\\resources\\user.png", "user"), JLabel.CENTER);
		nameUserLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		nameUserLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1 || e.getButton() == 3) {
					menuLogout.show(e.getComponent(), e.getX(), e.getY());

					logoutItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent ae) {
							dispose();
							new Main();
						}
					});
				}
			}
		});
		
		popupMenu = new JPopupMenu();
		JMenuItem roomItem = new JMenuItem("Quản lý phòng");
		roomItem.setIcon(new ImageIcon(s+"\\resources\\management.png", "management"));
		JMenuItem copyItem = new JMenuItem("Copy mã phòng");
		copyItem.setIcon(new ImageIcon(s+"\\resources\\copy.png", "copy"));
		popupMenu.add(roomItem);
		popupMenu.add(copyItem);
		
		JButton joinButton = new JButton(" Tham gia cuộc họp", new ImageIcon(s+"\\resources\\meeting.png"));
		joinButton.addActionListener(this);
		joinButton.setActionCommand("join");
		serverTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					joinButton.doClick();
				}
				if (e.getButton() == 3) {
					popupMenu.show(e.getComponent(), e.getX(), e.getY());

					roomItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent ae) {
							quanLyRoom();
						}
					});
					
					copyItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent ae) {
							copyCodeRoom();
						}
					});
				}
			}
		});
		serverTable.setSelectionBackground(Color.green);

		JButton addButton = new JButton(" Tạo phòng họp", new ImageIcon(s+"\\resources\\add.png"));
		addButton.addActionListener(this);
		addButton.setActionCommand("add");

		JButton editButton = new JButton(" Sửa phòng họp", new ImageIcon(s+"\\resources\\change.png"));
		editButton.addActionListener(this);
		editButton.setActionCommand("edit");

		JButton deleteButton = new JButton(" Xoá phòng họp", new ImageIcon(s+"\\resources\\remove.png"));
		deleteButton.addActionListener(this);
		deleteButton.setActionCommand("delete");

		connectServerContent.add(joinButton,
				gbc.setGrid(4, 1).setWeight(1, 0.015).setSpan(1, 1).setFill(GridBagConstraints.BOTH));
		connectServerContent.add(nameUserLabel,
				gbc.setGrid(3, 1).setWeight(1, 0.015).setSpan(1, 1).setFill(GridBagConstraints.BOTH));
		
		connectServerContent.add(serverScrollPane,
				gbc.setGrid(1, 2).setWeight(1, 1).setSpan(4, 1).setFill(GridBagConstraints.BOTH).setInsets(5));

		connectServerContent.add(addButton,
				gbc.setGrid(1, 3).setWeight(1, 0.015).setSpan(1, 1).setFill(GridBagConstraints.BOTH));
		connectServerContent.add(editButton, gbc.setGrid(2, 3).setWeight(1, 0).setSpan(1, 1));
		connectServerContent.add(deleteButton, gbc.setGrid(3, 3).setWeight(1, 0).setSpan(1, 1));
		connectServerContent.add(refreshButton,
				gbc.setGrid(4, 3).setWeight(1, 0).setSpan(1, 1).setFill(GridBagConstraints.BOTH));

		connectServerContent.setPreferredSize(new Dimension(1000, 500));

		this.setTitle("Danh sách phòng họp - UB Meeting");
		this.setContentPane(connectServerContent);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setIconImage(new ImageIcon(s+"\\resources\\meeting.png").getImage());
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	JTextField nameText;

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "join": {
			if (serverTable.getSelectedRow() == -1) {
				
				JDialog addServerDialog = new JDialog();

				JLabel idRoomLabel = new JLabel("Mã Phòng: ");
				idRoomLabel.setHorizontalAlignment(SwingConstants.RIGHT);

				JTextField idRoomText = new JTextField();
				idRoomText.setPreferredSize(new Dimension(180, 23));

				JButton addServerButton = new JButton("Tham gia phòng họp");
				addServerButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						rand = getRandomString(3) + "-" + getRandomString(3) + "-" + getRandomString(3);

						Server server = serverDAO.selectServerCode(idRoomText.getText());
						
						UserRoom userRoom = new UserRoom();
						userRoom.setId_room(server.getId());
						userRoom.setId_user(user.getId());
								
						UserRoomDAO userRoomDAO = new UserRoomDAO();
						try {
							userRoomDAO.insert(userRoom);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

						updateServerTable();

						addServerDialog.setVisible(false);
						addServerDialog.dispose();
					}
				});
				addServerButton.setPreferredSize(new Dimension(180, 30));

				GBCBuilder gbc = new GBCBuilder(1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 0).setInsets(5);

				JPanel addServerContent = new JPanel(new GridBagLayout());
				addServerContent.add(idRoomLabel, gbc);
				addServerContent.add(idRoomText, gbc.setGrid(2, 1));
				addServerContent.add(addServerButton, gbc.setGrid(1, 2).setSpan(2, 1));
				addServerContent.setBackground(Color.white);
				
				addServerDialog.setContentPane(addServerContent);
				addServerDialog.setTitle("Tham gia phòng họp");
				addServerDialog.getRootPane().setDefaultButton(addServerButton);
				addServerDialog.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
				addServerDialog.pack();
				addServerDialog.setLocationRelativeTo(null);
				addServerDialog.setVisible(true);

				break;
			}
			
			String serverState = serverTable.getValueAt(serverTable.getSelectedRow(), 6).toString();
			if (serverState.equals("Không hoạt động")) {
				JOptionPane.showMessageDialog(this, "Server không hoạt động", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
				break;
			}

			String codeRoom = serverTable.getValueAt(serverTable.getSelectedRow(), 2).toString();
			ServerData selectedServer = serverList.stream().filter(x -> x.code_room.equals(codeRoom))
					.findAny().orElse(null);

			Main.socketController = new SocketController(user.getName(), selectedServer);
			Main.socketController.Login();
			
			break;
		}
		case "add": {
			new server.Main();
			server.Main.main(null, user, null, null);

			break;
		}
		case "delete": {
			if (serverTable.getSelectedRow() == -1)
				break;

			int idRoom = Integer.parseInt(serverTable.getValueAt(serverTable.getSelectedRow(), 0).toString());
			String nameUser = serverTable.getValueAt(serverTable.getSelectedRow(), 3).toString();
			
			if (nameUser.equals(user.getName())) {
				int deleteOrNot = JOptionPane.showConfirmDialog(null, "Bạn chắc chắn muốn xóa phòng họp này?", "Thông báo", 0);

				if (deleteOrNot == 0) {
					try {
						serverDAO.deleteServer(idRoom);
						userRoomDAO.delete(idRoom);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					updateServerTable();
					break;
				}
			} else {
				JOptionPane.showMessageDialog(null, "Bạn không phải chủ phòng họp");
			}
			
			break;
		}
		case "edit": {
			if (serverTable.getSelectedRow() == -1)
				break;

			int idRoom = Integer.parseInt(serverTable.getValueAt(serverTable.getSelectedRow(), 0).toString());
			String nameRoom = serverTable.getValueAt(serverTable.getSelectedRow(), 1).toString();
			String nameUser = serverTable.getValueAt(serverTable.getSelectedRow(), 3).toString();
			
			if (nameUser.equals(user.getName())) {
				JDialog editDialog = new JDialog();

				JLabel serverNameLabel = new JLabel("Tên phòng họp: ");
				serverNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				JTextField nickNameText = new JTextField(nameRoom);
				nickNameText.setPreferredSize(new Dimension(180, 23));
				
				Path currentRelativePath = Paths.get("");
				String s = currentRelativePath.toAbsolutePath().toString();
				
				JButton editButton = new JButton("Sửa", new ImageIcon(s+"\\resources\\change.png"));
				editButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							String newServerName = nickNameText.getText();

							serverDAO.updateNameServer(idRoom, newServerName);
							updateServerTable();

							editDialog.setVisible(false);
							editDialog.dispose();

						} catch (NumberFormatException ex) {
							JOptionPane.showMessageDialog(editDialog, "Port phải là 1 số nguyên dương", "Thông báo",
									JOptionPane.INFORMATION_MESSAGE);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				});
				editButton.setPreferredSize(new Dimension(180, 30));

				JPanel editContent = new JPanel(new GridBagLayout());
				GBCBuilder gbc = new GBCBuilder(1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 0).setInsets(5);
				editContent.add(serverNameLabel, gbc);
				editContent.add(nickNameText, gbc.setGrid(2, 1));
				editContent.add(editButton, gbc.setGrid(1, 2).setSpan(2, 1));
				editContent.setBackground(Color.white);
				
				editDialog.setTitle("Chỉnh sửa thông tin phòng họp");
				editDialog.setContentPane(editContent);
				editDialog.getRootPane().setDefaultButton(editButton);
				editDialog.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
				editDialog.setIconImage(new ImageIcon(s+"\\resources\\meeting.png").getImage());
				editDialog.pack();
				editDialog.setLocationRelativeTo(null);
				editDialog.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(null, "Bạn không phải chủ phòng họp");
			}
			
			break;
		}

		case "refresh": {
			updateServerTable();
			break;
		}
		}
	}

	public void loginResultAction(String result) {
		if (result.equals("success")) {
			String selectedIP = serverTable.getValueAt(serverTable.getSelectedRow(), 4).toString();
			String selectedPort = serverTable.getValueAt(serverTable.getSelectedRow(), 5).toString();
			connectedServer = serverList.stream().filter(x -> x.ip.equals(selectedIP) && x.port == selectedPort)
					.findAny().orElse(null);

//			this.setVisible(false);
//			this.dispose();
			Main.mainScreen = new MainScreen();

		} else if (result.equals("existed"))
			JOptionPane.showMessageDialog(this, "Tên người dùng đã tồn tại", "Thông báo",
					JOptionPane.INFORMATION_MESSAGE);
		else if (result.equals("closed"))
			JOptionPane.showMessageDialog(this, "Server đã đóng", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

	}

	public void updateServerTable() {
		serverList = FileManager.getServerList(user);
		if (serverList == null)
			return;
		for (ServerData serverData : serverList) {
			serverData.isOpen = SocketController.serverOnline(serverData.ip, Integer.parseInt(serverData.port));
			if (serverData.isOpen) {
				serverData.name = SocketController.serverName(serverData.ip, Integer.parseInt(serverData.port));
				serverData.connectAccountCount = SocketController.serverConnectedAccountCount(serverData.ip, Integer.parseInt(serverData.port));
			}
		}

		serverTable.setModel(
				new DefaultTableModel(FileManager.getServerObjectMatrix(serverList), new String[] { "ID phòng họp", "Tên phòng họp",
						"Mã phòng", "Chủ phòng", "IP", "Port", "Trạng thái", "Số người trong phòng" }) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isCellEditable(int arg0, int arg1) {
						return false;
					}

					@Override
					public Class<?> getColumnClass(int columnIndex) {
						return String.class;
					}

				});
	}

	public void copyCodeRoom() {
		if (serverTable.getSelectedRow() != -1) {
			String codeRoom = serverTable.getValueAt(serverTable.getSelectedRow(), 2).toString();
			StringSelection stringSelection = new StringSelection(codeRoom);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		}
	}
	
	public void quanLyRoom() {
		if (serverTable.getSelectedRow() != -1) {
			String nameRoom = serverTable.getValueAt(serverTable.getSelectedRow(), 1).toString();
			String portRoom = serverTable.getValueAt(serverTable.getSelectedRow(), 5).toString();
			
			new server.Main();
			server.Main.main(null, user, nameRoom, portRoom);
		}
	}

	public static String getRandomString(int n) {
		String AlphaNumericString = "abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}

		return sb.toString();
	}
}
