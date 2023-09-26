package peperoPlatform;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Register extends Frame {
	Label lMessage;
	TextField name, id, pw;
	Button btn_register, btn_cancel;
	
	public Register() {
		super("register ȭ��");
		setScreen();
		
		btn_register.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String ID = id.getText();
				String PW = pw.getText();
				String NAME = name.getText();
				
				if(ID.equals("") || PW.equals("") || NAME.equals("")) {
					JOptionPane.showMessageDialog(null, "������ ��� ä���ּ���.");
					return;
				}
				
				boolean exists = isIDExists(ID);
				if(exists) {
					JOptionPane.showMessageDialog(null, "������ ���̵� �����մϴ�.");
				}
				else {
					addUser(ID, PW, NAME);
					JOptionPane.showMessageDialog(null, "��ϵǾ����ϴ�.");
					new Login();
					setVisible(false);
				}
			}
		});
		
		btn_cancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Login();
				setVisible(false);
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
		
		lMessage = new Label("                                   ȸ������");
		lMessage.setFont(new Font("Serif", Font.BOLD, 15));
		this.add(BorderLayout.NORTH, lMessage);
		
		Panel panel = new Panel();
		panel.setLayout(new BorderLayout());
		this.add(panel);
		
		Panel panel_1 = new Panel();
		panel_1.setLayout(new GridLayout(3, 1));
		panel.add(BorderLayout.WEST, panel_1);
		
		panel_1.add(new Label("�̸�"));
		panel_1.add(new Label("���̵�"));
		panel_1.add(new Label("��й�ȣ"));
		
		Panel panel_2 = new Panel();
		panel_2.setLayout(new GridLayout(3, 1));
		panel.add(BorderLayout.CENTER, panel_2);
		
		name = new TextField(20);
		id = new TextField(20);
		pw = new TextField(20);
		
		panel_2.add(name);
		panel_2.add(id);
		panel_2.add(pw);
		
		Panel panel_3 = new Panel();
		btn_register = new Button("ȸ������");
		btn_cancel = new Button("���");
		panel_3.add(btn_register);
		panel_3.add(btn_cancel);
		panel.add(BorderLayout.SOUTH, panel_3);
		
		this.setSize(350, 170);
		this.setTitle("ȸ������");
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = screenSize.width/2 - this.getSize().width/2 ;
		int y = screenSize.height/2 - this.getSize().height/2 ;
		this.setLocation(x,y);
		
		this.setVisible(true);
		this.setResizable(false);
		
	}
	
	public boolean isIDExists(String ID) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<String> IDs = new ArrayList<>();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/user_database";
			conn = DriverManager.getConnection(url, "manager", "1234");
			stmt = conn.createStatement();
			
			String sql = "SELECT * FROM users";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) { IDs.add(rs.getString(1)); }
			
		}
		catch(ClassNotFoundException e) { System.out.println("����̹� �ε� ����"); }
		catch(SQLException e) { System.out.println("���� " + e); }
		finally {
			try{
                if( conn != null && !conn.isClosed()){ conn.close(); }
                if( stmt != null && !stmt.isClosed()){ stmt.close(); }
                if( rs != null && !rs.isClosed()){ rs.close(); }
            } catch(SQLException e){ e.printStackTrace(); }
		}
		
		for (int i=0; i<IDs.size(); i++) {
			if(IDs.get(i).equals(ID)) {
				return true;
			}
		}
		return false;
	}
	
	public void addUser(String ID, String PW, String NAME) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/user_database";
			conn = DriverManager.getConnection(url, "manager", "1234");
			
			String sql = "INSERT INTO users VALUES(?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, ID);
			pstmt.setString(2, PW);
			pstmt.setString(3, NAME);
			pstmt.setString(4, "0");
			
			int count = pstmt.executeUpdate();
            if( count == 0 ){ System.out.println(ID + " ������ �Է� ����"); }
            else{ System.out.println(ID + " ������ �Է� ����"); }
        }
        catch( ClassNotFoundException e){ System.out.println("����̹� �ε� ����"); }
        catch( SQLException e){ System.out.println("���� " + e); }
        finally{
            try{
                if( conn != null && !conn.isClosed()){ conn.close(); }
                if( pstmt != null && !pstmt.isClosed()){ pstmt.close(); }
            }
            catch( SQLException e){ e.printStackTrace(); }
        }
	}
	
}
