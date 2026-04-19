package views;
import controllers.UserController;
import models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class LoginForm extends JFrame {
    private JTextField txtUser; private JPasswordField txtPass; private JLabel lblErr;
    private UserController uc;

    public LoginForm(){uc=new UserController();init();}

    private void init(){
        setTitle("Khairpur Multi-Transport System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000,600);setLocationRelativeTo(null);setResizable(false);

        JPanel main=new JPanel(new GridBagLayout()){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp=new GradientPaint(0,0,new Color(6,18,50),getWidth(),getHeight(),new Color(0,82,155));g2.setPaint(gp);g2.fillRect(0,0,getWidth(),getHeight());
                // decorative circles
                g2.setColor(new Color(255,255,255,12));g2.fillOval(-60,getHeight()-250,280,280);g2.fillOval(getWidth()-150,-80,220,220);
            }
        };
        main.setLayout(new GridBagLayout());

        // LEFT panel
        JPanel left=new JPanel(){@Override protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(new Color(255,255,255,15));g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),20,20));}};
        left.setOpaque(false);left.setPreferredSize(new Dimension(380,480));left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS));left.setBorder(BorderFactory.createEmptyBorder(40,35,40,35));
        JLabel ico=new JLabel("🚌🏍️🚗✈️");ico.setFont(new Font("Segoe UI Emoji",Font.PLAIN,32));ico.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel title=new JLabel("Khairpur Transport");title.setFont(new Font("Segoe UI",Font.BOLD,22));title.setForeground(Color.WHITE);title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel sub=new JLabel("Multi-Transport System");sub.setFont(new Font("Segoe UI",Font.PLAIN,15));sub.setForeground(new Color(180,210,255));sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel desc=new JLabel("<html><center>Bus • Car • Bike • Airline<br><br>Book any transport from<br>Khairpur to anywhere!</center></html>");desc.setFont(new Font("Segoe UI",Font.PLAIN,13));desc.setForeground(new Color(160,200,255));desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel dev=new JLabel("Developer: Abdul Rauf");dev.setFont(new Font("Segoe UI",Font.ITALIC,11));dev.setForeground(new Color(130,170,255));dev.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(Box.createVerticalGlue());left.add(ico);left.add(Box.createVerticalStrut(12));left.add(title);left.add(Box.createVerticalStrut(5));left.add(sub);left.add(Box.createVerticalStrut(20));left.add(desc);left.add(Box.createVerticalStrut(25));left.add(dev);left.add(Box.createVerticalGlue());

        // RIGHT panel
        JPanel right=new JPanel(){@Override protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(Color.WHITE);g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),22,22));}};
        right.setOpaque(false);right.setPreferredSize(new Dimension(360,480));right.setLayout(null);

        JLabel lw=new JLabel("Welcome Back!");lw.setFont(new Font("Segoe UI",Font.BOLD,26));lw.setForeground(new Color(8,25,65));lw.setBounds(45,42,280,35);
        JLabel ls=new JLabel("Sign in to continue");ls.setFont(new Font("Segoe UI",Font.PLAIN,13));ls.setForeground(Color.GRAY);ls.setBounds(45,80,280,20);
        JLabel lu=new JLabel("Username");lu.setFont(new Font("Segoe UI",Font.BOLD,13));lu.setForeground(new Color(40,60,100));lu.setBounds(45,128,260,20);
        txtUser=new JTextField();txtUser.setFont(new Font("Segoe UI",Font.PLAIN,14));txtUser.setBounds(45,150,270,40);txtUser.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200,210,230),2),BorderFactory.createEmptyBorder(5,12,5,12)));
        JLabel lp=new JLabel("Password");lp.setFont(new Font("Segoe UI",Font.BOLD,13));lp.setForeground(new Color(40,60,100));lp.setBounds(45,205,260,20);
        txtPass=new JPasswordField();txtPass.setFont(new Font("Segoe UI",Font.PLAIN,14));txtPass.setBounds(45,228,270,40);txtPass.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200,210,230),2),BorderFactory.createEmptyBorder(5,12,5,12)));
        lblErr=new JLabel("");lblErr.setFont(new Font("Segoe UI",Font.PLAIN,12));lblErr.setForeground(new Color(200,50,50));lblErr.setBounds(45,276,270,20);lblErr.setHorizontalAlignment(SwingConstants.CENTER);
        JButton btn=new JButton("LOGIN"){
            @Override protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(getModel().isRollover()?new Color(0,60,130):new Color(8,25,65));g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),10,10));g2.setColor(Color.WHITE);g2.setFont(new Font("Segoe UI",Font.BOLD,15));FontMetrics fm=g2.getFontMetrics();g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,(getHeight()+fm.getAscent()-fm.getDescent())/2);}
        };
        btn.setBounds(45,305,270,45);btn.setContentAreaFilled(false);btn.setBorderPainted(false);btn.setFocusPainted(false);btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JLabel hint=new JLabel("admin/admin123  |  user1/user123");hint.setFont(new Font("Segoe UI",Font.PLAIN,10));hint.setForeground(Color.LIGHT_GRAY);hint.setBounds(35,380,300,18);hint.setHorizontalAlignment(SwingConstants.CENTER);
        right.add(lw);right.add(ls);right.add(lu);right.add(txtUser);right.add(lp);right.add(txtPass);right.add(lblErr);right.add(btn);right.add(hint);

        GridBagConstraints gbc=new GridBagConstraints();gbc.insets=new Insets(0,18,0,18);
        gbc.gridx=0;main.add(left,gbc);gbc.gridx=1;main.add(right,gbc);
        setContentPane(main);

        btn.addActionListener(e->login());
        txtPass.addKeyListener(new KeyAdapter(){@Override public void keyPressed(KeyEvent e){if(e.getKeyCode()==KeyEvent.VK_ENTER) login();}});
        txtUser.addKeyListener(new KeyAdapter(){@Override public void keyPressed(KeyEvent e){if(e.getKeyCode()==KeyEvent.VK_ENTER) txtPass.requestFocus();}});
    }

    private void login(){
        String u=txtUser.getText().trim(),p=new String(txtPass.getPassword()).trim();
        if(u.isEmpty()||p.isEmpty()){lblErr.setText("⚠ Enter username and password!");return;}
        User user=uc.login(u,p);
        if(user!=null){
            lblErr.setForeground(new Color(20,160,80));lblErr.setText("✓ Login successful!");
            Timer t=new Timer(400,e->{dispose();if(user.isAdmin()) new AdminDashboard(user).setVisible(true);else new UserDashboard(user).setVisible(true);});
            t.setRepeats(false);t.start();
        }else{lblErr.setForeground(new Color(200,50,50));lblErr.setText("✗ Invalid username or password!");txtPass.setText("");}
    }
}
