package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import dao.UserDAO;
import model.User;

public class Register extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nameText;
	private JPasswordField passwordText;
	private JButton registerButton;
	
	private UserDAO userDAO = new UserDAO();
	private JTextField usernameText;

	public Register() {
		setTitle("Đăng ký");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 387, 324);
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
		
		Label label = new Label("ĐĂNG KÝ");
		label.setFont(new Font("Arial", Font.BOLD, 15));
		label.setBounds(140, 10, 121, 27);
		contentPane.add(label);
		
		JLabel nameLabel = new JLabel("Họ và tên:");
		nameLabel.setBounds(73, 63, 70, 14);
		contentPane.add(nameLabel);
		
		JLabel passwordLabel = new JLabel("Mật khẩu:");
		passwordLabel.setBounds(73, 160, 70, 14);
		contentPane.add(passwordLabel);

		nameText = new JTextField();
		nameText.setBounds(140, 57, 163, 27);
		contentPane.add(nameText);
		nameText.setColumns(10);
		
		passwordText = new JPasswordField();
		passwordText.setBounds(138, 154, 163, 27);
		contentPane.add(passwordText);
		passwordText.setColumns(10);
		
		registerButton = new JButton(" Đăng ký", new ImageIcon(s+"\\resources\\register.png"));
		registerButton.setBounds(137, 203, 102, 33);
		contentPane.add(registerButton);
		
		JLabel usernameLabel = new JLabel("Tên đăng nhập:");
		usernameLabel.setBounds(47, 112, 102, 14);
		contentPane.add(usernameLabel);
		
		usernameText = new JTextField();
		usernameText.setColumns(10);
		usernameText.setBounds(140, 106, 163, 27);
		contentPane.add(usernameText);
		
		JLabel registerLabel = new JLabel("<html>Đã có tài khoản? <b>Đăng nhập</b></html>");
		registerLabel.setFont(new Font("Tahoma", Font.ITALIC, 11));
		registerLabel.setBounds(116, 246, 163, 23);
		contentPane.add(registerLabel);
		
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (nameText.getText().equals("") || usernameText.getText().equals("") || passwordText.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Hãy điền đầy đủ thông tin");
				} else {
					User user = new User(nameText.getText(), usernameText.getText(), passwordText.getText());
					if (checkAccount(user)) {
						register(user);
					} else {
						JOptionPane.showMessageDialog(null, "Tên đăng nhập này đã tồn tại");
					}
				}
			}
		});
		
		registerLabel.addMouseListener((MouseListener) new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }

        });
		
		InputMap input = passwordText.getInputMap();
		input.put(KeyStroke.getKeyStroke("ENTER"), "text-submit");
		passwordText.getActionMap().put("text-submit", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				registerButton.doClick();
			}
		});
		
	}
	
	public void register(User user) {
		try {
			userDAO.insertUser(user);
			JOptionPane.showMessageDialog(null, "Đăng ký thành công");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean checkAccount(User user) {
		List<User> users = userDAO.selectAllUsers();
		for (User u : users) {
			if (u.getUser_name().equals(user.getUser_name())) {
				return false;
			}
		}
		return true;
	}

}
