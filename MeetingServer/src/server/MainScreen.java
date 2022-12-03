package server;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.ServerDAO;
import dao.UserRoomDAO;
import model.Server;
import model.User;
import model.UserRoom;

public class MainScreen extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	JLabel portLabel;
	JTextField portText;
	JLabel serverNameLabel;
	JTextField serverNameText;
	JTextField idLabel;

	static JTable showClientTable;
	JButton openCloseButton;
	boolean isSocketOpened = false;

	private ServerDAO serverDAO;
	private UserRoomDAO userRoomDAO;
	User user;

	ImageIcon icon = new ImageIcon(Paths.get("").toAbsolutePath().toString()+"\\resources\\open.png");
	
	String rand = getRandomString(3)+ "-" +getRandomString(3)+ "-" +getRandomString(3);
	int randomPort = getRandomNum();
	Boolean haveServer = false;
	
	public MainScreen(User user, String nameRoom, String portRoom) {
		this.user = user;
		JPanel mainContent = new JPanel(new GridBagLayout());
		mainContent.setBackground(new Color(255, 255, 255));
		GBCBuilder gbc = new GBCBuilder(1, 1).setInsets(5);

		
		JLabel ipLabel = new JLabel("IP: " + SocketController.getThisIP());
		Border border1 = ipLabel.getBorder();
		Border margin1 = new EmptyBorder(1,9,1,1);
		ipLabel.setBorder(new CompoundBorder(border1, margin1));
		
		portLabel = new JLabel("Port:");
		Border border2 = portLabel.getBorder();
		Border margin2 = new EmptyBorder(1,15,1,1);
		portLabel.setBorder(new CompoundBorder(border2, margin2));
		
		idLabel = new JTextField ("Mã Phòng");
		Border border3 = portLabel.getBorder();
		Border margin3 = new EmptyBorder(1,21,1,1);
		idLabel.setBorder(new CompoundBorder(border3, margin3));
		idLabel.setBackground(Color.white);
		idLabel.setEditable(false);
		idLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					copyCodeRoom();
				}
			}
		});
		
		portText = new JTextField();
		serverNameLabel = new JLabel("Tên phòng họp:");
		serverNameText = new JTextField();
		
		if (nameRoom != null && portRoom != null) {
			serverNameText.setText(nameRoom);
			portText.setText(portRoom);
		}
		
		openCloseButton = new JButton(" Mở phòng họp", icon);
		openCloseButton.setBackground(UIManager.getColor("Button.background"));
		openCloseButton.addActionListener(this);

		showClientTable = new JTable(new Object[][] {}, new String[] { "Tên thành viên", "Port" });
		showClientTable.setRowHeight(25);
		showClientTable.setBackground(Color.white);
		
		JScrollPane showClientScrollPane = new JScrollPane(showClientTable);
		showClientScrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách các thành viên trong phòng"));
		showClientScrollPane.setBackground(Color.white);
		
		mainContent.add(ipLabel, gbc.setFill(GridBagConstraints.BOTH).setWeight(0.1, 0).setSpan(1, 1));
		
		mainContent.add(serverNameLabel, gbc.setGrid(2, 1).setWeight(0, 0).setSpan(1, 1));
		mainContent.add(serverNameText, gbc.setGrid(3, 1).setWeight(1, 0.015).setSpan(1, 1));
		
		mainContent.add(portLabel, gbc.setGrid(4, 1).setWeight(0, 0).setSpan(1, 1));
		mainContent.add(portText, gbc.setGrid(5, 1).setWeight(1, 0).setSpan(1, 1));
		
		mainContent.add(idLabel, gbc.setGrid(6, 1).setWeight(0.4, 0).setSpan(1, 1));
		
		mainContent.add(showClientScrollPane, gbc.setGrid(1, 3).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setSpan(6, 1));
		mainContent.add(openCloseButton, gbc.setGrid(1, 4).setWeight(1, 0.03).setSpan(6, 1));
		mainContent.setPreferredSize(new Dimension(1000, 500));

		this.setTitle("Quản lý phòng họp - UB Meeting");
		this.setContentPane(mainContent);
		this.getRootPane().setDefaultButton(openCloseButton);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setIconImage(new ImageIcon(Paths.get("").toAbsolutePath().toString()+"\\resources\\meeting.png").getImage());
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		Main.socketController = new SocketController();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!isSocketOpened) {
			try {
				if (serverNameText.getText().isEmpty())
					JOptionPane.showMessageDialog(this, "Tên phòng họp không được trống", "Lỗi", JOptionPane.WARNING_MESSAGE);
				else {
					if (portText.getText().isEmpty()) {
						portText.setText(""+randomPort);
					}
						
					Main.socketController.serverName = serverNameText.getText();
					Main.socketController.serverPort = Integer.parseInt(portText.getText());

					Main.socketController.OpenSocket(Main.socketController.serverPort);
					isSocketOpened = true;
					icon = new ImageIcon(Paths.get("").toAbsolutePath().toString()+"\\resources\\close.png");
					openCloseButton.setText(" Đang mở phòng họp (Nhấn để đóng)");
					openCloseButton.setIcon(icon);
					
					Server insertServer = new Server();
					insertServer.setName(serverNameText.getText());
					insertServer.setIp(SocketController.getThisIP());
					insertServer.setPort(portText.getText());
					insertServer.setCode_server(rand);
					insertServer.setId_user(user.getId());
					
					serverDAO = new ServerDAO();
					userRoomDAO = new UserRoomDAO();
					
					try {
						List<Server> servers = serverDAO.selectAllServers();
						for (Server server : servers) {
							if(server.getName().equals(serverNameText.getText()) && server.getPort().equals(portText.getText())) {
								haveServer = true;
								rand = server.getCode_server();
								
								if(!server.getIp().equals(SocketController.getThisIP())) {
									serverDAO.updateServer(server);
								}
							}
						}
						idLabel.setText(rand);
						idLabel.setForeground(Color.blue);
						idLabel.setFont(new Font("Arial", Font.BOLD, 13));
					
						if (!haveServer) {
							serverDAO.insertServer(insertServer);
							
							servers = serverDAO.selectAllServers();
							Collections.reverse(servers);
							for (Server server : servers) {
								if(server.getName().equals(serverNameText.getText()) && server.getPort().equals(portText.getText()) && server.getIp().equals(SocketController.getThisIP())) {
									userRoomDAO.insert(new UserRoom(user.getId(), server.getId()));
								}
							}							
							
							
						}
						
						haveServer = false;
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
				}

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Port phải là một số từ (0 -> 65536)", "Lỗi",
						JOptionPane.WARNING_MESSAGE);
			}
		} else {
			Main.socketController.CloseSocket();
			isSocketOpened = false;
			icon = new ImageIcon(Paths.get("").toAbsolutePath().toString()+"\\resources\\open.png");
			openCloseButton.setText(" Mở phòng họp");
			openCloseButton.setIcon(icon);
			
			idLabel.setText("Mã Phòng");
			idLabel.setForeground(Color.black);
			idLabel.setFont(new Font("", Font.BOLD, 12));
			
			rand = getRandomString(3)+ "-" +getRandomString(3)+ "-" +getRandomString(3);
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
	
	public static int getRandomNum() {
		int min = 0;
	    int max = 65536;
	        
	    int random_int = (int) Math.floor(Math.random()*(max-min+1)+min);
	   
	    return random_int;
	}
	
	public void copyCodeRoom() {
		StringSelection stringSelection = new StringSelection(idLabel.getText());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}
	
	public void updateClientTable() {

		Object[][] tableContent = new Object[Main.socketController.connectedClient.size()][2];
		for (int i = 0; i < Main.socketController.connectedClient.size(); i++) {
			tableContent[i][0] = Main.socketController.connectedClient.get(i).userName;
			tableContent[i][1] = Main.socketController.connectedClient.get(i).port;
		}

		showClientTable.setModel(new DefaultTableModel(tableContent, new String[] { "Tên thành viên", "Port" }));
	}
}