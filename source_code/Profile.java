package peperoPlatform;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Profile extends Frame {
	Button btn_close;
	
	public Profile() {
		super("profile 화면");
		setScreen();
		
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
		 
		 this.setSize(200,300);
		 this.setTitle("프로필");

		 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		 int x = screenSize.width/2 - this.getSize().width/2 ;
		 int y = screenSize.height/2 - this.getSize().height/2 ;
		 this.setLocation(x,y);
		 
		 Panel panel = new Panel();
		 panel.setLayout(new BorderLayout());
		 this.add(panel);
		 
		 Panel panel_1 = new Panel();
		 panel_1.setLayout(new GridLayout(4, 1));
		 panel.add(BorderLayout.WEST, panel_1);
		 
		 panel_1.add(new Label("이름"));
		 panel_1.add(new Label("아이디"));
		 panel_1.add(new Label("비밀번호"));
		 panel_1.add(new Label("잔액"));
		 
		 Panel panel_2 = new Panel();
		 panel_2.setLayout(new GridLayout(4, 1));
		 panel.add(BorderLayout.CENTER, panel_2);
		 
		 Label tfn = new Label(Main.NAME);
		 tfn.setFont(new Font("Serif", Font.BOLD, 12));
		 panel_2.add(tfn);
		 Label tfi = new Label(Main.ID);
		 tfi.setFont(new Font("Serif", Font.BOLD, 12));
		 panel_2.add(tfi);
		 Label tfp = new Label(Main.PW);
		 tfp.setFont(new Font("Serif", Font.BOLD, 12));
		 panel_2.add(tfp);
		 Label tfm = new Label(Main.MONEY + "원");
		 tfm.setFont(new Font("Serif", Font.BOLD, 12));
		 panel_2.add(tfm);		 
		 
		 Panel panel_3 = new Panel();
		 panel.add(BorderLayout.SOUTH, panel_3);
		 btn_close = new Button("닫기");
		 panel_3.add(btn_close);
		 
		 this.setVisible(true);
		 this.setResizable(false);
	 }
	
}
