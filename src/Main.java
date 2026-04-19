import views.LoginForm;
import database.DBConnection;
import javax.swing.*;
import java.awt.*;

/**
 * Khairpur Multi-Transport Management System
 * Developer: Abdul Rauf
 * Course: SW121 - Object Oriented Programming
 * Mehran University of Engineering and Technology
 */
public class Main {
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        // Splash
        JWindow splash = new JWindow();
        splash.setSize(400,200);
        splash.setLocationRelativeTo(null);
        JPanel sp = new JPanel(new BorderLayout(0,10)){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(8,25,65));g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
            }
        };
        sp.setBorder(BorderFactory.createEmptyBorder(28,28,28,28));
        JLabel ic=new JLabel("🚌🏍️🚗✈️",SwingConstants.CENTER);ic.setFont(new Font("Segoe UI Emoji",Font.PLAIN,34));
        JLabel nm=new JLabel("Khairpur Multi-Transport System",SwingConstants.CENTER);nm.setFont(new Font("Segoe UI",Font.BOLD,15));nm.setForeground(Color.WHITE);
        JLabel sub=new JLabel("Connecting to database...",SwingConstants.CENTER);sub.setFont(new Font("Segoe UI",Font.PLAIN,12));sub.setForeground(new Color(140,180,255));
        sp.add(ic,BorderLayout.NORTH);sp.add(nm,BorderLayout.CENTER);sp.add(sub,BorderLayout.SOUTH);
        splash.setContentPane(sp);splash.setVisible(true);

        new Thread(()->{
            boolean ok=DBConnection.testConnection();
            SwingUtilities.invokeLater(()->{
                splash.dispose();
                if(!ok){
                    JTextArea msg=new JTextArea("Cannot connect to database!\n\nPlease:\n1. Start MySQL Server\n2. Run transport.sql in MySQL Workbench\n3. Restart application\n\n"+DBConnection.getLastError());
                    msg.setEditable(false);msg.setFont(new Font("Segoe UI",Font.PLAIN,13));msg.setBackground(UIManager.getColor("OptionPane.background"));
                    JOptionPane.showMessageDialog(null,msg,"Database Error",JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }else{
                    new LoginForm().setVisible(true);
                }
            });
        }).start();
    }
}
