package peperoPlatform;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Login extends Frame {
	Label lMessage;
	TextField tfid,tfpw;
	Checkbox cbidsave;
	Label lSave;
	Button btn_register, btn_login;
	
	public static final int NO_MEMBER_EXISTS = 0;
	public static final int PW_WRONG = 1;
	public static final int PW_RIGHT = 2;
	 
	 public Login() {
		 super("login 화면");
		 setScreen();
		 
		 btn_register.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Register();
				setVisible(false);
			 }
		 });
		 
		 btn_login.addMouseListener(new MouseAdapter() {
		  @Override
		  public void mouseClicked(MouseEvent e) {
			 String ID = tfid.getText();
			 String PW = tfpw.getText();
			 switch(check(ID, PW)) {
			 case NO_MEMBER_EXISTS:
				 JOptionPane.showMessageDialog(null, "정보가 존재하지 않습니다.");
				 break;
			 case PW_WRONG:
				 JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
				 break;
			 case PW_RIGHT:
				 String[] identity = getIdentity(ID);
				 Main.ID = identity[0];
				 Main.PW = identity[1];
				 Main.NAME = identity[2];
				 Main.MONEY = identity[3];
				 
				 new Platform();
				 setVisible(false);
				 break;
			 }
		  	}
		 });
		  
		 
		 addWindowListener(new WindowAdapter() {
			 @Override
			 public void windowClosing(WindowEvent we) {
				 System.exit(0);
				 }
			 });
	 }
	 
	 public void setScreen() {
		 setLayout(new BorderLayout());
		 lMessage = new Label("           빼빼로공유플랫폼 상부상조에 로그인합니다.");
		 lMessage.setFont(new Font("Serif", Font.BOLD, 12));
		 this.add(BorderLayout.NORTH, lMessage);
		  
		 Panel panel = new Panel();
		 panel.setLayout(new BorderLayout());
		 this.add(panel);
		 
		 Panel panel_1 = new Panel();
		 panel_1.setLayout(new GridLayout(2,1));
		 panel.add(BorderLayout.WEST,panel_1);
		 
		 Panel panel_1up = new Panel();
		 tfid = new TextField("아이디",20);
		 panel_1up.add(tfid);
		 panel_1.add(panel_1up);
		 
		 Panel panel_1down = new Panel();
		 tfpw = new TextField("비밀번호",20);
		 panel_1down.add(tfpw);
		 panel_1.add(panel_1down);
		 
		 Panel panel_2 = new Panel();
		 panel_2.setLayout(new GridLayout(2,1));
		 panel.add(BorderLayout.CENTER, panel_2);
		 
		 Panel panel_2up = new Panel();
		 btn_register = new Button("회원가입");
		 panel_2up.add(btn_register);
		 panel_2.add(panel_2up);
		 
		 Panel panel_2down = new Panel();
		 btn_login = new Button("로그인");
		 panel_2down.add(btn_login);
		 panel_2.add(panel_2down);
		 
		 this.setSize(350,150);
		 this.setTitle("공유플랫폼의 혁신: 상부상조");

		 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		 int x = screenSize.width/2 - this.getSize().width/2 ;
		 int y = screenSize.height/2 - this.getSize().height/2 ;
		 this.setLocation(x,y);
		 
		 this.setVisible(true);
		 this.setResizable(false);
	 }
	 
	 public int check(String ID, String PW) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String selected_ID = null;
		String selected_PW = null;
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				String url = "jdbc:mysql://localhost/user_database";
				conn = DriverManager.getConnection(url, "manager", "1234");
				stmt = conn.createStatement();
				
				String sql = "SELECT * FROM users";
				rs = stmt.executeQuery(sql);
				
				while(rs.next()) {
					if(ID.equals(rs.getString(1))) {
						selected_ID = rs.getString(1);
						selected_PW = rs.getString(2);
					}
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
			
			if(selected_ID != null) {
				if(ID.equals(selected_ID) && PW.equals(selected_PW))
					return PW_RIGHT;
				else
					return PW_WRONG;
			}
			else {
				return NO_MEMBER_EXISTS;
			}
	 }
	 
	 public String[] getIdentity(String ID) {
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String[] identity = new String[4];
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				String url = "jdbc:mysql://localhost/user_database";
				conn = DriverManager.getConnection(url, "manager", "1234");
				stmt = conn.createStatement();
				
				String sql = "SELECT * FROM users";
				rs = stmt.executeQuery(sql);
				
				while(rs.next()) {
					if(ID.equals(rs.getString(1))) {
						identity = new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)};
					}
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
			
			return identity;
		}

}
