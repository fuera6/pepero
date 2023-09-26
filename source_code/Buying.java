package peperoPlatform;

import java.awt.*;
import java.awt.event.ActionEvent;
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

public class Buying extends Frame {
	Button btn_buy;
	public Buying(String[] info) {
		super("buying 화면");
		setScreen(info);
		
		btn_buy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int my_money = Integer.parseInt(Main.MONEY);
				int price = Integer.parseInt(info[2]);
				
				if(my_money < price) {
					JOptionPane.showMessageDialog(null, "잔액이 부족합니다.");
				}
				else {
					Main.MONEY = String.valueOf(my_money - price);
					setMoney(Main.ID, Main.MONEY);
					
					String seller_ID = info[0];
					int seller_money = Integer.parseInt(getMoney(seller_ID));
					setMoney(seller_ID, String.valueOf(seller_money + price));
					
					removePepero(Integer.parseInt(info[4]));
					JOptionPane.showMessageDialog(null, "구매하였습니다.");
					setVisible(false);
					new Platform();
				}
			}
		});;
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				setVisible(false);
				new Platform();
			}
		});
	}
	
	public void setScreen(String[] info) {
		setLayout(new BorderLayout());

		this.setSize(300, 200);
		this.setTitle("구매하기");
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = screenSize.width / 2 - this.getSize().width / 2;
		int y = screenSize.height / 2 - this.getSize().height / 2;
		this.setLocation(x, y);
		
		this.setLayout(new GridLayout(5, 1));
		Label lMessage = new Label("         빼빼로 구매하기");
		lMessage.setFont(new Font("Serif", Font.BOLD, 15
	));
		this.add(lMessage);
		this.add(new Label("판매자: " + info[1] + "(" + info[0] + ")님"));
		this.add(new Label("가격: " + info[2] + "원"));
		this.add(new Label("메모 가능 여부: " + info[3]));
		btn_buy = new Button("구매하기");
		this.add(btn_buy);
		
		this.setVisible(true);
		this.setResizable(false);
	}
	
	public void setMoney(String ID, String newMoney) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/user_database";
			conn = DriverManager.getConnection(url, "manager", "1234");
			
			String sql = "UPDATE users SET money = ? WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, newMoney);
			pstmt.setString(2, ID);
			
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
	
	public String getMoney(String ID) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String money = "0";
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				String url = "jdbc:mysql://localhost/user_database";
				conn = DriverManager.getConnection(url, "manager", "1234");
				stmt = conn.createStatement();
				
				String sql = "SELECT * FROM users";
				rs = stmt.executeQuery(sql);
				
				while(rs.next()) {
					if(ID.equals(rs.getString(1))) {
						money = rs.getString(4);
						break;
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
			return money;
	}
	
	public void removePepero(int register_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/user_database";
			conn = DriverManager.getConnection(url, "manager", "1234");
			
			String sql = "DELETE FROM pepero WHERE register_id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, register_id);
			
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
