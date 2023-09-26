package peperoPlatform;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.*;

public class Platform extends JFrame {
	ScrollPane scrollPane;
	Button[] btns;
	ArrayList<String[]> btns_list;
	
	public Platform() {
		super("platform 화면");
		setScreen();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}

	public void setScreen() {
		setLayout(new BorderLayout());

		this.setSize(350, 700);
		this.setTitle("공유플랫폼의 혁신: 상부상조");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = screenSize.width / 2 - this.getSize().width / 2;
		int y = screenSize.height / 2 - this.getSize().height / 2;
		this.setLocation(x, y);

		JMenuBar mb = new JMenuBar();
		JMenu m = new JMenu("설정");

		JMenuItem profile = new JMenuItem("프로필");
		JMenuItem recharge = new JMenuItem("충전하기");
		JMenuItem pepero = new JMenuItem("빼빼로 등록하기");
		JMenuItem logout = new JMenuItem("로그아웃");

		m.add(profile);
		m.add(recharge);
		m.add(pepero);
		m.add(logout);
		
		mb.add(m);
		this.setJMenuBar(mb);

		profile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Profile();
			}
		});

		recharge.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Recharge();
			}
		});
		
		pepero.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new Pepero();
			}
		});
		
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.ID = null;
				Main.PW = null;
				Main.NAME = null;
				Main.MONEY = "0";
				new Login();
				setVisible(false);
			}
		});
		
		setPeperoList();
		setButtonsListener();

		this.setVisible(true);
		this.setResizable(false);
	}
	
	public void setPeperoList() {
		Panel panel = new Panel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		this.add(panel);
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		btns_list = new ArrayList<>();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/user_database";
			conn = DriverManager.getConnection(url, "manager", "1234");
			stmt = conn.createStatement();
			
			String sql = "SELECT * FROM pepero";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				btns_list.add(new String[] {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), String.valueOf(rs.getInt(5))});
			}
			
		}
		catch(ClassNotFoundException e) { System.out.println("드라이버 로딩 실패"); }
		catch(SQLException e) { System.out.println("에러 " + e); }
		finally {
			try{
                if( conn != null && !conn.isClosed()){ conn.close(); }
                if( stmt != null && !stmt.isClosed()){ stmt.close(); }
                if( rs != null && !rs.isClosed()){ rs.close(); }
            } catch(SQLException e){ e.printStackTrace(); }
		}
		
		
		btns = new Button[btns_list.size()];
		for (int i=0; i<btns_list.size(); i++) {
			btns[i] = new Button(btns_list.get(i)[1] + "(" + btns_list.get(i)[0] + ")님의 빼빼로   /   " + btns_list.get(i)[2] + "원");
			btns[i].setSize(300, 100);
			panel.add(BorderLayout.CENTER, btns[i]);
		}
	}
	
	public void setButtonsListener() {
		for(int i=0; i<btns.length; i++) {
			int index = i;
			btns[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					setVisible(false);
					new Buying(btns_list.get(index));
				}
			});
		}
	}

}
