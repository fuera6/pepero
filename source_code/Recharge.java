package peperoPlatform;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Recharge extends Frame {
	Label lMoney;
	Button btn_recharge, btn_close;
	TextField tfr;
	
	public Recharge() {
		super("recharge 화면");
		setScreen();
		
		btn_recharge.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Main.MONEY = String.valueOf(Integer.parseInt(Main.MONEY) + Integer.parseInt(tfr.getText()));
					lMoney.setText(Main.MONEY + "원");
					setMoney(Main.MONEY);
				} catch(NumberFormatException ne) {
					JOptionPane.showMessageDialog(null, "자연수를 입력해주세요.");
					return;
				}
			}
		});
		
		btn_close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setVisible(false);
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				setVisible(false);
			}
		});
	}

	public void setScreen() {
		setLayout(new BorderLayout());

		this.setSize(300, 170);
		this.setTitle("충전하기");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = screenSize.width / 2 - this.getSize().width / 2;
		int y = screenSize.height / 2 - this.getSize().height / 2;
		this.setLocation(x, y);

		Panel panel = new Panel();
		panel.setLayout(new BorderLayout());
		this.add(panel);
		
		Panel panel_1 = new Panel();
		panel_1.add(new Label("현재 잔액은"));
		lMoney = new Label(Main.MONEY + "원");
		lMoney.setFont(new Font("Serif", Font.BOLD, 20));
		panel_1.add(lMoney);
		panel.add(BorderLayout.NORTH, panel_1);
		
		Panel panel_2 = new Panel();
		tfr = new TextField(20);
		panel_2.add(tfr);
		panel.add(BorderLayout.CENTER, panel_2);
		
		Panel panel_3 = new Panel();
		btn_recharge = new Button("충전하기");
		panel_3.add(btn_recharge);
		panel.add(BorderLayout.EAST, panel_3);
		
		Panel panel_4 = new Panel();
		btn_close = new Button("닫기");
		panel_4.add(btn_close);
		panel.add(BorderLayout.SOUTH, panel_4);
		
		this.setVisible(true);
		this.setResizable(false);
	}
	
	public void setMoney(String money) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/user_database";
			conn = DriverManager.getConnection(url, "manager", "1234");
			
			String sql = "UPDATE users SET money = ? WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, money);
			pstmt.setString(2, Main.ID);
			
			int count = pstmt.executeUpdate();
            if( count == 0 ){ System.out.println("데이터 입력 실패"); }
            else{ System.out.println("데이터 입력 성공"); }
        }
        catch( ClassNotFoundException e){ System.out.println("드라이버 로딩 실패"); }
        catch( SQLException e){ System.out.println("에러 " + e); }
        finally{
            try{
                if( conn != null && !conn.isClosed()){ conn.close(); }
                if( pstmt != null && !pstmt.isClosed()){ pstmt.close(); }
            }
            catch( SQLException e){ e.printStackTrace(); }
        }
	}
	
}
