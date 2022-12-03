package client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dao.UserDAO;
import model.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Label;
import java.awt.Font;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameText;
	private JTextField passwordText;
	
	public static ConnectServerScreen connectServerScreen;
	public static Main frame;
	public static MainScreen mainScreen;
	public static SocketController socketController;
	
	private UserDAO userDAO = new UserDAO();
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				} catch (Exception e) {
					e.printStackTrace();
				}
				frame = new Main();
			}
		});
	}

	public Main() {
		setTitle("Đăng nhập");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 365, 274);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(Color.white);
		
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		contentPane.setLayout(null);
		setVisible(true);
		
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		setIconImage(new ImageIcon(s+"\\resources\\meeting.png").getImage());
		
		Label label = new Label("ĐĂNG NHẬP");
		label.setFont(new Font("Arial", Font.BOLD, 15));
		label.setBounds(123, 10, 121, 27);
		contentPane.add(label);
		
		JLabel usernameLabel = new JLabel("Tên đăng nhập:");
		usernameLabel.setBounds(31, 63, 102, 14);
		contentPane.add(usernameLabel);
		
		JLabel passwordLabel = new JLabel("Mật khẩu:");
		passwordLabel.setBounds(60, 110, 70, 14);
		contentPane.add(passwordLabel);

		usernameText = new JTextField();
		usernameText.setBounds(124, 57, 172, 27);
		contentPane.add(usernameText);
		usernameText.setColumns(10);
		
		passwordText = new JTextField();
		passwordText.setBounds(124, 104, 172, 27);
		contentPane.add(passwordText);
		passwordText.setColumns(10);
		
		JButton loginButton = new JButton(" Đăng nhập", new ImageIcon(s+"\\resources\\login.png"));
		loginButton.setBounds(122, 154, 109, 33);
		contentPane.add(loginButton);
		
		JLabel registerLabel = new JLabel("<html>Chưa có tài khoản? <b>Đăng ký</b></html>");
		registerLabel.setFont(new Font("Tahoma", Font.ITALIC, 11));
		registerLabel.setBounds(105, 196, 163, 23);
		contentPane.add(registerLabel);
		
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (usernameText.getText().equals("") || passwordText.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Hãy điền đầy đủ thông tin");
				} else {
					User user = new User(usernameText.getText(), passwordText.getText());
					checkAccount(user);
				}
			}
		});
		
		InputMap input = passwordText.getInputMap();
		input.put(KeyStroke.getKeyStroke("ENTER"), "text-submit");
		passwordText.getActionMap().put("text-submit", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				loginButton.doClick();
			}
		});
		
		registerLabel.addMouseListener((MouseListener) new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Register();
            }

        });
		
	}
	
	public void login(User user) {
		connectServerScreen = new ConnectServerScreen(user);
		frame.dispose();
	}
	
	public void checkAccount(User user) {
		List<User> users = userDAO.selectAllUsers();
		for (User u : users) {
			if (u.getUser_name().equals(user.getUser_name()) && u.getPassword().equals(user.getPassword())) {
				login(u);
				return;
			}
		}
		JOptionPane.showMessageDialog(null, "Tên đăng nhập hoặc mật khẩu không đúng");
	}
	
}
