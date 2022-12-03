package client;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MainScreen extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	public static int chattingRoom = -1;
	public boolean shareScreen = true;
	public int isShareScreen = 0;
	
	JList<String> connectedServerInfoJList;

	JList<String> onlineUserJList;
	JList<String> groupJList;

	JTabbedPane roomTabbedPane;
	List<RoomMessagesPanel> roomMessagesPanels;
	JList<String> roomUsersJList;

	JPanel enterMessagePanel, showScreenPanel;
	JTextArea messageArea;

	private Socket soc;

	public MainScreen() {
		GBCBuilder gbc = new GBCBuilder(1, 1);
		JPanel mainContent = new JPanel(new GridBagLayout());

		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		
		connectedServerInfoJList = new JList<String>(
				new String[] { "Số thành viên: " + Main.socketController.connectedServer.connectAccountCount });

		connectedServerInfoJList.setBorder(BorderFactory
				.createTitledBorder(String.format("Phòng họp: %s", Main.connectServerScreen.connectedServer.name)));

		onlineUserJList = new JList<String>();
		onlineUserJList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					String clickedUser = onlineUserJList.getSelectedValue();
					Room foundRoom = Room.findPrivateRoom(Main.socketController.allRooms, clickedUser);
					if (foundRoom == null) {
						Main.socketController.createPrivateRoom(clickedUser);
					} else {
						int roomTabIndex = -1;
						for (int i = 0; i < roomTabbedPane.getTabCount(); i++) {
							JScrollPane currentScrollPane = (JScrollPane) roomTabbedPane.getComponentAt(i);
							RoomMessagesPanel currentRoomMessagePanel = (RoomMessagesPanel) currentScrollPane
									.getViewport().getView();
							if (currentRoomMessagePanel.room.id == foundRoom.id) {
								roomTabIndex = i;
								break;
							}
						}

						if (roomTabIndex == -1) { 	// room tồn tại nhưng tab bị chéo trước đó
							newRoomTab(foundRoom);
							roomTabbedPane.setSelectedIndex(roomTabbedPane.getTabCount() - 1);
						} else {
							roomTabbedPane.setSelectedIndex(roomTabIndex);
						}
					}
				}
			}
		});
		JScrollPane onlineUserScrollPane = new JScrollPane(onlineUserJList);
		onlineUserScrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách thành viên online"));

		
		groupJList = new JList<String>();
		groupJList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					String clickedGroup = groupJList.getSelectedValue();
					System.out.println("Double click " + clickedGroup);
					Room foundRoom = Room.findGroup(Main.socketController.allRooms, clickedGroup);

					int roomTabIndex = -1;
					for (int i = 0; i < roomTabbedPane.getTabCount(); i++) {
						JScrollPane currentScrollPane = (JScrollPane) roomTabbedPane.getComponentAt(i);
						RoomMessagesPanel currentRoomMessagePanel = (RoomMessagesPanel) currentScrollPane.getViewport()
								.getView();
						if (currentRoomMessagePanel.room.id == foundRoom.id) {
							roomTabIndex = i;
							break;
						}
					}

					if (roomTabIndex == -1) {
						newRoomTab(foundRoom);
						roomTabbedPane.setSelectedIndex(roomTabbedPane.getTabCount() - 1);
					} else {
						roomTabbedPane.setSelectedIndex(roomTabIndex);
					}
				}
			}
		});
		JScrollPane groupListScrollPane = new JScrollPane(groupJList);
		groupListScrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách nhóm"));

		JButton createGroupButton = new JButton("Tạo nhóm", new ImageIcon(s+"\\resources\\group.png"));
		createGroupButton.setActionCommand("group");
		createGroupButton.addActionListener(this);

		JPanel groupPanel = new JPanel(new GridBagLayout());
		groupPanel.add(groupListScrollPane, gbc.setGrid(1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		groupPanel.add(createGroupButton, gbc.setGrid(1, 2).setWeight(1, 0));

		JSplitPane chatSubjectSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, onlineUserScrollPane, groupPanel);
		chatSubjectSplitPane.setDividerLocation(230);

		JPanel leftPanel = new JPanel(new GridBagLayout());
		leftPanel.add(connectedServerInfoJList, gbc.setGrid(1, 1).setWeight(1, 0).setFill(GridBagConstraints.BOTH));
		leftPanel.add(chatSubjectSplitPane, gbc.setGrid(1, 2).setWeight(1, 1));

		JPanel chatPanel = new JPanel(new GridBagLayout());
		enterMessagePanel = new JPanel(new GridBagLayout());
		enterMessagePanel.setBackground(Color.white);

		JButton sendButton, fileButton, emojiButton, audioButton, shareScreenButton;
		
		
		sendButton = new JButton(new ImageIcon(s+"\\resources\\send.png", "send"));
		sendButton.setActionCommand("send");
		sendButton.addActionListener(this);

		emojiButton = new JButton(new ImageIcon(s+"\\resources\\emoji.png", "emoji"));
		emojiButton.setActionCommand("emoji");
		emojiButton.addActionListener(this);

		fileButton = new JButton(new ImageIcon(s+"\\resources\\fileIcon.png", "icon"));
		fileButton.setActionCommand("file");
		fileButton.addActionListener(this);

		audioButton = new AudioButton();

		shareScreenButton = new JButton(new ImageIcon(s+"\\resources\\present.png", "present"));
		shareScreenButton.setActionCommand("shareScreen");
		shareScreenButton.addActionListener(this);

		messageArea = new JTextArea();
		messageArea.setBorder(BorderFactory.createLineBorder(Color.gray, 1, true));
		messageArea.setMinimumSize(new Dimension(100, 20));
		InputMap input = messageArea.getInputMap();
		input.put(KeyStroke.getKeyStroke("shift ENTER"), "insert-break");
		input.put(KeyStroke.getKeyStroke("ENTER"), "text-submit");
		messageArea.getActionMap().put("text-submit", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				sendButton.doClick();
			}
		});

		
		enterMessagePanel.add(messageArea, gbc.setGrid(1, 1).setWeight(1, 1));
		enterMessagePanel.add(sendButton,
				gbc.setGrid(2, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.NORTH));
		enterMessagePanel.add(emojiButton, gbc.setGrid(3, 1));
		enterMessagePanel.add(fileButton, gbc.setGrid(4, 1));
		enterMessagePanel.add(audioButton, gbc.setGrid(5, 1));
		enterMessagePanel.add(shareScreenButton, gbc.setGrid(6, 1));

		roomTabbedPane = new JTabbedPane();
		roomTabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JScrollPane selectedTab = (JScrollPane) roomTabbedPane.getSelectedComponent();
				if (selectedTab != null) {
					RoomMessagesPanel selectedMessagePanel = (RoomMessagesPanel) selectedTab.getViewport().getView();
					chattingRoom = selectedMessagePanel.room.id;
					updateRoomUsersJList();
				}
			}
		});
		roomMessagesPanels = new ArrayList<RoomMessagesPanel>();
		roomUsersJList = new JList<String>();
		roomUsersJList.setBorder(BorderFactory.createTitledBorder("Thành viên"));

//		chatPanel.setBackground(Color.white);
		chatPanel.add(roomTabbedPane, gbc.setGrid(1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		chatPanel.add(enterMessagePanel, gbc.setGrid(1, 2).setWeight(1, 0));

		JSplitPane shareScreenSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mainContent.add(shareScreenSplitPane, gbc.setGrid(1, 2).setWeight(1, 1));    
        
		showScreenPanel = new JPanel();
//		showScreenPanel.setBackground(Color.white);
		showScreenPanel.setForeground(Color.RED);
		showScreenPanel.setLayout(null);
		
		shareScreenSplitPane.setDividerLocation(1000);
		shareScreenSplitPane.setLeftComponent(showScreenPanel);
		shareScreenSplitPane.setRightComponent(roomUsersJList);
		
		JSplitPane roomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, leftPanel, chatPanel);
		JSplitPane mainSplitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, shareScreenSplitPane, roomSplitPane);

		mainSplitpane.setResizeWeight(1.0);
		shareScreenSplitPane.setResizeWeight(0.8);
		
		mainContent.add(mainSplitpane, gbc.setGrid(1, 1).setWeight(1, 1));

		shareScreenSplitPane.getActionMap().put("text-submit", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				shareScreenButton.doClick();
			}
		});
		
		this.setTitle("UB Meeting - " + Main.socketController.userName);
		this.setContentPane(mainContent);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setIconImage(new ImageIcon(s+"\\resources\\meeting.png").getImage());
		this.pack();
		this.setLocationRelativeTo(null);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
	}
	
	public void updateServerData() {
		Main.socketController.connectedServer.connectAccountCount = Main.socketController.onlineUsers.size();

		connectedServerInfoJList.setListData(
				new String[] { "Số thành viên: " + Main.socketController.connectedServer.connectAccountCount });
	}

	public void newRoomTab(Room room) {
		RoomMessagesPanel roomMessagesPanel = new RoomMessagesPanel(room);
		roomMessagesPanels.add(roomMessagesPanel);

		for (MessageData messageData : room.messages)
			addNewMessageGUI(room.id, messageData);

		JScrollPane messagesScrollPane = new JScrollPane(roomMessagesPanel);
		messagesScrollPane.setMinimumSize(new Dimension(100, 100));
		messagesScrollPane.getViewport().setBackground(Color.white);

		roomTabbedPane.addTab(room.name, messagesScrollPane);
		roomTabbedPane.setTabComponentAt(roomTabbedPane.getTabCount() - 1,
				new TabComponent(room.name, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						roomMessagesPanels.remove(roomMessagesPanel);
						roomTabbedPane.remove(messagesScrollPane);
					}
				}));
	}

	public void updateOnlineUserJList() {
		onlineUserJList.setListData(Main.socketController.onlineUsers.toArray(new String[0]));
	}

	public void updateRoomUsersJList() {
		System.out.println("updateRoomUsersJList");
		Room theChattingRoom = Room.findRoom(Main.socketController.allRooms, chattingRoom);
		if (theChattingRoom != null)
			roomUsersJList.setListData(theChattingRoom.users.toArray(new String[0]));
	}

	public void updateGroupJList() {
		List<String> groupList = new ArrayList<String>();
		for (Room room : Main.socketController.allRooms) {
			if (room.type.equals("group"))
				groupList.add(room.name);
		}
		groupJList.setListData(groupList.toArray(new String[0]));
	}

	public void addNewMessage(int roomID, String type, String whoSend, String content) {
		MessageData messageData = new MessageData(whoSend, type, content);
		Room receiveMessageRoom = Room.findRoom(Main.socketController.allRooms, roomID);
		receiveMessageRoom.messages.add(messageData);

		addNewMessageGUI(roomID, messageData);
	}

	private void addNewMessageGUI(int roomID, MessageData messageData) {

		MessagePanel newMessagePanel = new MessagePanel(messageData);
		newMessagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		RoomMessagesPanel receiveMessageRoomMessagesPanel = RoomMessagesPanel.findRoomMessagesPanel(roomMessagesPanels,
				roomID);
		receiveMessageRoomMessagesPanel.add(Box.createHorizontalGlue());
		receiveMessageRoomMessagesPanel.add(newMessagePanel);
		receiveMessageRoomMessagesPanel.validate();
		receiveMessageRoomMessagesPanel.repaint();
		roomTabbedPane.validate();
		roomTabbedPane.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "group": {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setBounds(100, 100, 450, 300);
			frame.setTitle("Tạo nhóm mới");
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			
			JPanel chooseUserContent = new JPanel();
			
			JList<String> onlineUserJList = new JList<String>(Main.socketController.onlineUsers.toArray(new String[0]));
			JScrollPane onlineUserScrollPanel = new JScrollPane(onlineUserJList);
			onlineUserScrollPanel.setBounds(10, 11, 414, 171);
			onlineUserScrollPanel.setBorder(BorderFactory.createTitledBorder("Chọn thành viên để thêm vào nhóm"));
			onlineUserScrollPanel.setBackground(Color.white);

			JLabel groupNameLabel = new JLabel(" Tên nhóm: ");
			groupNameLabel.setBounds(10, 193, 67, 14);
			JTextField groupNameField = new JTextField();
			groupNameField.setBounds(77, 189, 345, 26);
			groupNameField.setBorder(new LineBorder(new Color(215, 215, 215)));
			JButton createButton = new JButton("Tạo nhóm", new ImageIcon(Paths.get("").toAbsolutePath().toString()+"\\resources\\group.png"));
			createButton.setBounds(10, 222, 414, 31);

			createButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String groupName = groupNameField.getText();
					if (groupName.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Tên nhóm không được trống", "Lỗi tạo nhóm",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					List<String> chosenUsers = onlineUserJList.getSelectedValuesList();
					if (chosenUsers.size() < 2) {
						JOptionPane.showMessageDialog(null, "Nhóm phải có từ 3 người trở lên",
								"Lỗi tạo group", JOptionPane.WARNING_MESSAGE);
						return;
					}
					Main.socketController.createGroup(groupName, chosenUsers);
					setVisible(false);
					dispose();
				}
			});
			chooseUserContent.setLayout(null);

			chooseUserContent.add(onlineUserScrollPanel);
			chooseUserContent.add(groupNameLabel);
			chooseUserContent.add(groupNameField);
			chooseUserContent.add(createButton);
			chooseUserContent.setBackground(Color.white);

			frame.setContentPane(chooseUserContent);
			frame.setIconImage(new ImageIcon(Paths.get("").toAbsolutePath().toString()+"\\resources\\meeting.png").getImage());
			break;
		}

		case "send": {
			String content = messageArea.getText();
			if (content.isEmpty())
				break;
			if (chattingRoom != -1)
				Main.socketController.sendTextToRoom(chattingRoom, content);
			messageArea.setText("");
			break;
		}

		case "emoji": {
			JDialog emojiDialog = new JDialog();
			Object[][] emojiMatrix = new Object[6][6];
			int emojiCode = 0x1F601;
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 6; j++)
					emojiMatrix[i][j] = new String(Character.toChars(emojiCode++));
			}

			JTable emojiTable = new JTable();
			emojiTable.setModel(new DefaultTableModel(emojiMatrix, new String[] { "", "", "", "", "", "" }) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			});
			emojiTable.setFont(new Font("Dialog", Font.PLAIN, 20));
			emojiTable.setShowGrid(false);
			emojiTable.setIntercellSpacing(new Dimension(0, 0));
			emojiTable.setRowHeight(30);
			emojiTable.getTableHeader().setVisible(false);

			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			for (int i = 0; i < emojiTable.getColumnCount(); i++) {
				emojiTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
				emojiTable.getColumnModel().getColumn(i).setMaxWidth(30);
			}
			emojiTable.setCellSelectionEnabled(true);
			emojiTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			emojiTable.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					messageArea.setText(messageArea.getText() + emojiTable
							.getValueAt(emojiTable.rowAtPoint(e.getPoint()), emojiTable.columnAtPoint(e.getPoint())));
				}
			});

			emojiDialog.setContentPane(emojiTable);
			emojiDialog.setTitle("Chọn emoji");
			emojiDialog.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
			emojiDialog.pack();
			emojiDialog.setLocationRelativeTo(this);
			emojiDialog.setVisible(true);
			break;
		}

		case "file": {
			if (chattingRoom == -1)
				break;
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Chọn file để gửi");
			int result = jfc.showDialog(null, "Chọn file");
			jfc.setVisible(true);

			if (result == JFileChooser.APPROVE_OPTION) {
				String fileName = jfc.getSelectedFile().getName();
				String filePath = jfc.getSelectedFile().getAbsolutePath();

				Main.socketController.sendFileToRoom(chattingRoom, fileName, filePath);
			}
		}

		case "shareScreen": {
			if (isShareScreen == 0) {
				shareScreen = true;
				shareScreen();
				isShareScreen = 1;
			} else {
				shareScreen = false;
				isShareScreen = 0;
				showScreenPanel.revalidate();
				showScreenPanel.repaint();
			}
			
		}

		}

	}

	public void shareScreen() {
		EventQueue.invokeLater(() -> {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Robot rob = new Robot();
						Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

						while (shareScreen) {
							try {

								ServerSocket soc = new ServerSocket(888);
								Socket so = soc.accept();
								BufferedImage img = rob.createScreenCapture(
										new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));

								ByteArrayOutputStream ous = new ByteArrayOutputStream();
								ImageIO.write(img, "png", ous);
								so.getOutputStream().write(ous.toByteArray());
								soc.close();
							} catch (Exception e) {

							}
							try {
								Thread.sleep(1);
							} catch (Exception e) {
							}
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, e);
					}
				}
			}).start();

			InetAddress inetAddress;

			try {
				inetAddress = InetAddress.getLocalHost();
				String ipAddress = inetAddress.getHostAddress();
				System.out.println(ipAddress);
			} catch (Exception ex) {

			}

		});
		EventQueue.invokeLater(() -> {
			showScreen();
		});

	}

	public void showScreen() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (shareScreen) {
						try {
							InetAddress inetAddress = InetAddress.getLocalHost();
							String ipAddress = inetAddress.getHostAddress();
							soc = new Socket(ipAddress, 888);
							BufferedImage img = ImageIO.read(soc.getInputStream());
							showScreenPanel.getGraphics().drawImage(img, 0, 0, showScreenPanel.getWidth(), showScreenPanel.getHeight(), null);

							soc.close();
						} catch (Exception e) {
//							System.out.println(e.getMessage());
						}

						try {
							Thread.sleep(1);
						} catch (Exception e) {
//							System.out.println(e.getMessage());
						}
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);
				}

			}
		}).start();
	}

	public static class AudioButton extends JButton implements ActionListener {
		private static final long serialVersionUID = 1L;

		public boolean isRecording;
		ImageIcon microphoneImage;
		ImageIcon stopImage;

		public AudioButton() {
			Path currentRelativePath = Paths.get("");
			String s = currentRelativePath.toAbsolutePath().toString();
			
			microphoneImage = new ImageIcon(s+"\\resources\\microphone.png", "microphone");
			stopImage = new ImageIcon(s+"\\resources\\stop.png", "stop");

			this.setIcon(microphoneImage);
			this.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (chattingRoom == -1)
				return;

			isRecording = !isRecording;
			if (isRecording) {
				this.setIcon(stopImage);
				AudioController.startRecord();

			} else {
				this.setIcon(microphoneImage);
				byte[] audioBytes = AudioController.stopRecord();

				String[] options = { "Gửi", "Huỷ" };
				int choice = JOptionPane.showOptionDialog(Main.mainScreen, "Bạn muốn gửi đoạn âm thanh vừa ghi không?",
						"Câu hỏi", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (choice == 0) {
					Main.socketController.sendAudioToRoom(chattingRoom, audioBytes);
				}
			}
		}
	}

	public static class RoomMessagesPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		public Room room;

		public RoomMessagesPanel(Room room) {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.setBackground(Color.white);
			this.room = room;
		}

		public static RoomMessagesPanel findRoomMessagesPanel(List<RoomMessagesPanel> roomMessagesPanelList, int id) {
			for (RoomMessagesPanel roomMessagesPanel : roomMessagesPanelList) {
				if (roomMessagesPanel.room.id == id)
					return roomMessagesPanel;
			}
			return null;
		}
	}

	public static class TabComponent extends JPanel {

		private static final long serialVersionUID = 1L;
	
		public TabComponent(String tabTitle, ActionListener closeButtonListener) {
			JLabel titleLabel = new JLabel(tabTitle);
			JButton closeButton = new JButton(new ImageIcon(Paths.get("").toAbsolutePath().toString()+"\\resources\\cancel.png"));
			closeButton.addActionListener(closeButtonListener);
			closeButton.setPreferredSize(new Dimension(16, 16));
			closeButton.setBorderPainted(false);
			closeButton.setBorder(null);
			closeButton.setMargin(new Insets(0, 0, 0, 0));
			closeButton.setContentAreaFilled(false);
			
			this.setLayout(new FlowLayout());
			this.add(titleLabel);
			this.add(closeButton);
			this.setOpaque(false);
		}

	}

}
