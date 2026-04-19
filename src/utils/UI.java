package utils;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.*;

public class UI {
    public static final Color DARK  = new Color(8,25,65);
    public static final Color BLUE  = new Color(0,100,220);
    public static final Color GREEN = new Color(20,160,80);
    public static final Color RED   = new Color(200,60,60);
    public static final Color ORG   = new Color(200,100,0);
    public static final Color PURP  = new Color(120,40,180);
    public static final Color GRAY  = new Color(100,110,130);
    public static final Color BG    = new Color(240,244,252);

    public static JPanel card(){
        JPanel c=new JPanel(){@Override protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(Color.WHITE);g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),16,16));}};
        c.setBorder(BorderFactory.createEmptyBorder(18,22,18,22));return c;
    }
    public static JButton btn(String text, Color color){
        JButton b=new JButton(text){@Override protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(getModel().isRollover()?color.darker():color);g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),8,8));g2.setColor(Color.WHITE);g2.setFont(new Font("Segoe UI",Font.BOLD,12));FontMetrics fm=g2.getFontMetrics();g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,(getHeight()+fm.getAscent()-fm.getDescent())/2);}};
        b.setPreferredSize(new Dimension(120,36));b.setContentAreaFilled(false);b.setBorderPainted(false);b.setFocusPainted(false);b.setCursor(new Cursor(Cursor.HAND_CURSOR));return b;
    }
    public static JButton wideBtn(String text, Color color, int w){JButton b=btn(text,color);b.setPreferredSize(new Dimension(w,42));return b;}
    public static JTextField field(){
        JTextField f=new JTextField();f.setFont(new Font("Segoe UI",Font.PLAIN,13));f.setPreferredSize(new Dimension(210,36));
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200,210,230)),BorderFactory.createEmptyBorder(4,10,4,10)));return f;
    }
    public static JComboBox<String> combo(){JComboBox<String> c=new JComboBox<>();c.setFont(new Font("Segoe UI",Font.PLAIN,13));c.setPreferredSize(new Dimension(210,36));return c;}
    public static JLabel label(String t){JLabel l=new JLabel(t);l.setFont(new Font("Segoe UI",Font.BOLD,12));l.setForeground(new Color(40,60,100));return l;}
    public static void styleTable(JTable table){
        table.setFont(new Font("Segoe UI",Font.PLAIN,13));table.setRowHeight(34);table.setShowVerticalLines(false);
        table.setGridColor(new Color(230,235,245));table.setSelectionBackground(new Color(210,228,255));
        table.setIntercellSpacing(new Dimension(0,1));table.setBackground(Color.WHITE);
        JTableHeader h=table.getTableHeader();
        h.setDefaultRenderer(new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){
                JLabel l=new JLabel(v!=null?v.toString():"");l.setFont(new Font("Segoe UI",Font.BOLD,13));l.setForeground(Color.WHITE);l.setBackground(DARK);l.setOpaque(true);
                l.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,0,1,new Color(255,255,255,30)),BorderFactory.createEmptyBorder(6,10,6,10)));return l;}
        });
        h.setPreferredSize(new Dimension(0,38));h.setBackground(DARK);h.setOpaque(true);h.setReorderingAllowed(false);
        table.setDefaultRenderer(Object.class,new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){
                JLabel l=(JLabel)super.getTableCellRendererComponent(t,v,s,f,r,c);l.setFont(new Font("Segoe UI",Font.PLAIN,13));l.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
                if(s){l.setBackground(new Color(210,228,255));l.setForeground(DARK);}else{l.setBackground(r%2==0?Color.WHITE:new Color(247,250,255));l.setForeground(new Color(30,50,80));}return l;}
        });
    }
    public static JPanel statCard(String title,String value,Color color){
        JPanel c=new JPanel(){@Override protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(Color.WHITE);g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),14,14));g2.setColor(color);g2.fill(new RoundRectangle2D.Double(0,0,5,getHeight(),3,3));}};
        c.setLayout(new BorderLayout());c.setBorder(BorderFactory.createEmptyBorder(14,18,14,18));
        JLabel t=new JLabel(title);t.setFont(new Font("Segoe UI",Font.PLAIN,12));t.setForeground(new Color(120,140,180));
        JLabel v=new JLabel(value);v.setFont(new Font("Segoe UI",Font.BOLD,26));v.setForeground(color);
        c.add(t,BorderLayout.NORTH);c.add(v,BorderLayout.CENTER);return c;
    }
}
