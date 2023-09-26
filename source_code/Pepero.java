package peperoPlatform;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Pepero extends Frame {
	Choice c;
	CheckboxGroup group;
	Checkbox yes, no;
	TextField add_price;
	Label price;
	Button btn_estimate, btn_register;
	int pepero_price = 0;
	boolean isMemoPossible = false;

	public Pepero() {
		super("pepero ȭ��");
		setScreen();

		c.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				switch (c.getSelectedItem()) {
				case "��ü����":
					pepero_price = 0;
					break;
				case "�Ե� �������� ������ 46g (+1000��)":
					pepero_price = 1000;
					break;
				case "�Ե� �Ƹ�� ������ 37g (+1200��)":
					pepero_price = 1200;
					break;
				}
			}
		});
		
		yes.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(yes.getState())
					isMemoPossible = true;
				else
					isMemoPossible = false;
			}
		});
		
		no.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(no.getState())
					isMemoPossible = false;
				else
					isMemoPossible = true;
			}
		});
		
		btn_estimate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					price.setText("���󰡰�: " + (pepero_price + Integer.parseInt(add_price.getText()) + "��"));
			} catch(NumberFormatException ne) {
				JOptionPane.showMessageDialog(null, "�߰����ݿ� 0 �Ǵ� �ڿ����� �Է����ּ���.");
				return;
				}
			}
		});
		
		btn_register.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addNewPepero(pepero_price + Integer.parseInt(add_price.getText()), isMemoPossible);
				setVisible(false);
				new Platform();
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				setVisible(false);
				new Platform();
			}
		});
	}

	public void setScreen() {
		setLayout(new BorderLayout());

		this.setSize(320, 200);
		this.setTitle("������ ����ϱ�");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = screenSize.width / 2 - this.getSize().width / 2;
		int y = screenSize.height / 2 - this.getSize().height / 2;
		this.setLocation(x, y);

		Label lMessage = new Label("                        ������ ����ϱ�");
		lMessage.setFont(new Font("Serif", Font.BOLD, 14));
		this.add(BorderLayout.NORTH, lMessage);

		Panel panel = new Panel();
		panel.setLayout(new BorderLayout());
		this.add(panel);

		Panel panel_1 = new Panel();
		panel_1.setLayout(new GridLayout(3, 1));
		panel.add(BorderLayout.WEST, panel_1);

		panel_1.add(new Label("���� ����"));
		panel_1.add(new Label("�޸� ����"));
		panel_1.add(new Label("�߰� ����"));

		Panel panel_2 = new Panel();
		panel_2.setLayout(new GridLayout(3, 1));
		panel.add(BorderLayout.CENTER, panel_2);

		c = new Choice();
		c.add("��ü����");
		c.add("�Ե� �������� ������ 46g (+1000��)");
		c.add("�Ե� �Ƹ�� ������ 37g (+1200��)");
		panel_2.add(c);
		group = new CheckboxGroup();
		yes = new Checkbox("�޸𰡴�", false, group);
		no = new Checkbox("�޸����", true, group);
		Panel panel_21 = new Panel();
		panel_21.setLayout(new GridLayout(1, 2));
		panel_21.add(yes);
		panel_21.add(no);
		panel_2.add(panel_21);
		add_price = new TextField(20);
		panel_2.add(add_price);

		Panel panel_3 = new Panel();
		panel.add(BorderLayout.SOUTH, panel_3);

		price = new Label("���󰡰�: 0��");
		price.setFont(new Font("Serif", Font.BOLD, 10));
		panel_3.add(price);

		btn_estimate = new Button("���󰡰� ����");
		panel_3.add(btn_estimate);

		btn_register = new Button("����ϱ�");
		panel_3.add(btn_register);

		this.setVisible(true);
		this.setResizable(false);
	}
	
	public void addNewPepero(int price, boolean isMemoPossible) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int register_id = 0;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/user_database";
			conn = DriverManager.getConnection(url, "manager", "1234");
			
			String sql = "INSERT INTO pepero VALUES(?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, Main.ID);
			pstmt.setString(2, Main.NAME);
			pstmt.setString(3, String.valueOf(price));
			pstmt.setString(4, isMemoPossible ? "yes" : "no");
			while(true) {
				register_id = (int) (Math.random()*10000);
				if(!isRegisterIdExists(register_id)) {
					break;
				}
				System.out.println(register_id);
			}
			pstmt.setInt(5, register_id);
			
			int count = pstmt.executeUpdate();
            if( count == 0 ){ System.out.println("������ �Է� ����"); }
            else{ System.out.println("������ �Է� ����"); }
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
	
	public boolean isRegisterIdExists(int register_id) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				String url = "jdbc:mysql://localhost/user_database";
				conn = DriverManager.getConnection(url, "manager", "1234");
				stmt = conn.createStatement();
				
				String sql = "SELECT * FROM pepero";
				rs = stmt.executeQuery(sql);
				
				while(rs.next()) {
					if(register_id == rs.getInt(5)) {
						return true;
					}
				}				
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
			return false;
	}
	
}
