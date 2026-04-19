package views;
import models.User;
import controllers.BookingController;
import utils.UI;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class UserDashboard extends JFrame {
    private User user; private JPanel content; private JLabel pgTitle;
    private JButton btnHome,btnBus,btnCar,btnBike,btnAir,btnMyBus,btnMyCar,btnMyBike,btnMyAir,btnRoutes,btnLogout,active;
    private BookingController bc;

    public UserDashboard(User user){this.user=user;bc=new BookingController();init();}

    private void init(){
        setTitle("Khairpur Multi-Transport - Passenger Portal");setDefaultCloseOperation(EXIT_ON_CLOSE);setExtendedState(MAXIMIZED_BOTH);setMinimumSize(new Dimension(1000,650));setLayout(new BorderLayout());
        add(sidebar(),BorderLayout.WEST);
        JPanel main=new JPanel(new BorderLayout());main.setBackground(UI.BG);main.add(topbar(),BorderLayout.NORTH);
        content=new JPanel(new BorderLayout());content.setBackground(UI.BG);content.setBorder(BorderFactory.createEmptyBorder(22,28,22,28));
        main.add(content,BorderLayout.CENTER);add(main,BorderLayout.CENTER);showHome();
    }

    private JPanel sidebar(){
        JPanel s=new JPanel(){@Override protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);GradientPaint gp=new GradientPaint(0,0,new Color(6,18,50),0,getHeight(),new Color(12,40,100));g2.setPaint(gp);g2.fillRect(0,0,getWidth(),getHeight());g2.setColor(new Color(255,255,255,8));g2.fillOval(-40,getHeight()-220,200,200);g2.fillOval(90,-70,150,150);}};
        s.setPreferredSize(new Dimension(240,0));s.setLayout(new BorderLayout());
        JPanel logo=new JPanel();logo.setOpaque(false);logo.setLayout(new BoxLayout(logo,BoxLayout.Y_AXIS));logo.setBorder(BorderFactory.createEmptyBorder(25,22,20,22));
        JLabel ic=new JLabel("🚌🏍️🚗✈️");ic.setFont(new Font("Segoe UI Emoji",Font.PLAIN,28));ic.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel nm=new JLabel("KT System");nm.setFont(new Font("Segoe UI",Font.BOLD,19));nm.setForeground(Color.WHITE);nm.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel rl=new JLabel("  Passenger Portal");rl.setFont(new Font("Segoe UI",Font.PLAIN,11));rl.setForeground(new Color(100,160,255));rl.setAlignmentX(Component.LEFT_ALIGNMENT);
        logo.add(ic);logo.add(Box.createVerticalStrut(6));logo.add(nm);logo.add(rl);

        JPanel nav=new JPanel();nav.setOpaque(false);nav.setLayout(new BoxLayout(nav,BoxLayout.Y_AXIS));nav.setBorder(BorderFactory.createEmptyBorder(8,10,8,10));
        JLabel s1=sec("BOOK TRANSPORT");
        btnHome=sb("🏠","Home","home");
        btnBus =sb("🚌","Book Bus","bus");
        btnCar =sb("🚗","Book Car","car");
        btnBike=sb("🏍️","Book Bike","bike");
        btnAir =sb("✈️","Book Airline","air");
        JLabel s2=sec("MY BOOKINGS");
        btnMyBus =sb("📋","My Bus Bookings","mybus");
        btnMyCar =sb("📋","My Car Bookings","mycar");
        btnMyBike=sb("📋","My Bike Bookings","mybike");
        btnMyAir =sb("📋","My Airline","myair");
        JLabel s3=sec("INFO");
        btnRoutes=sb("🛣️","View Routes","routes");
        btnLogout=sb("🚪","Logout","logout");

        nav.add(s1);
        for(JButton b:new JButton[]{btnHome,btnBus,btnCar,btnBike,btnAir}){nav.add(b);nav.add(Box.createVerticalStrut(2));}
        nav.add(Box.createVerticalStrut(8));nav.add(s2);
        for(JButton b:new JButton[]{btnMyBus,btnMyCar,btnMyBike,btnMyAir}){nav.add(b);nav.add(Box.createVerticalStrut(2));}
        nav.add(Box.createVerticalStrut(8));nav.add(s3);nav.add(btnRoutes);nav.add(Box.createVerticalStrut(2));

        JPanel bot=new JPanel();bot.setOpaque(false);bot.setLayout(new BoxLayout(bot,BoxLayout.Y_AXIS));bot.setBorder(BorderFactory.createEmptyBorder(0,10,15,10));
        JPanel uc=userCard();bot.add(uc);bot.add(Box.createVerticalStrut(8));bot.add(btnLogout);
        s.add(logo,BorderLayout.NORTH);s.add(nav,BorderLayout.CENTER);s.add(bot,BorderLayout.SOUTH);
        setAct(btnHome);return s;
    }

    private JLabel sec(String t){JLabel l=new JLabel(t);l.setFont(new Font("Segoe UI",Font.BOLD,10));l.setForeground(new Color(80,120,190));l.setBorder(BorderFactory.createEmptyBorder(4,13,6,0));l.setAlignmentX(Component.LEFT_ALIGNMENT);return l;}

    private JPanel userCard(){
        JPanel c=new JPanel(){@Override protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(new Color(255,255,255,18));g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),10,10));}};
        c.setOpaque(false);c.setLayout(new BorderLayout(10,0));c.setBorder(BorderFactory.createEmptyBorder(10,12,10,12));c.setMaximumSize(new Dimension(220,55));
        JLabel av=new JLabel("👤");av.setFont(new Font("Segoe UI Emoji",Font.PLAIN,22));
        JPanel np=new JPanel(new GridLayout(2,1));np.setOpaque(false);
        JLabel nm=new JLabel(user.getFullName());nm.setFont(new Font("Segoe UI",Font.BOLD,12));nm.setForeground(Color.WHITE);
        JLabel rl=new JLabel("Passenger");rl.setFont(new Font("Segoe UI",Font.PLAIN,11));rl.setForeground(new Color(120,170,255));
        np.add(nm);np.add(rl);c.add(av,BorderLayout.WEST);c.add(np,BorderLayout.CENTER);return c;
    }

    private JButton sb(String ico,String lbl,String action){
        JButton b=new JButton(){@Override protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);if(this==active){g2.setColor(new Color(0,100,220));g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),10,10));g2.setColor(new Color(255,255,255,50));g2.fill(new RoundRectangle2D.Double(0,0,4,getHeight(),2,2));}else if(getModel().isRollover()){g2.setColor(new Color(255,255,255,15));g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),10,10));}g2.setFont(new Font("Segoe UI Emoji",Font.PLAIN,13));g2.setColor(this==active?Color.WHITE:new Color(160,200,255));g2.drawString(ico,14,getHeight()/2+6);g2.setFont(new Font("Segoe UI",Font.BOLD,12));g2.setColor(this==active?Color.WHITE:new Color(180,210,255));g2.drawString(lbl,40,getHeight()/2+5);}};
        b.setPreferredSize(new Dimension(220,38));b.setMaximumSize(new Dimension(220,38));b.setContentAreaFilled(false);b.setBorderPainted(false);b.setFocusPainted(false);b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addActionListener(e->{setAct(b);route(action);});return b;
    }
    private void setAct(JButton b){active=b;for(JButton x:new JButton[]{btnHome,btnBus,btnCar,btnBike,btnAir,btnMyBus,btnMyCar,btnMyBike,btnMyAir,btnRoutes,btnLogout})if(x!=null)x.repaint();}

    private JPanel topbar(){
        JPanel bar=new JPanel(new BorderLayout());bar.setBackground(Color.WHITE);bar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(220,228,240)),BorderFactory.createEmptyBorder(13,26,13,26)));
        pgTitle=new JLabel("Home");pgTitle.setFont(new Font("Segoe UI",Font.BOLD,22));pgTitle.setForeground(UI.DARK);
        JLabel wl=new JLabel("👋  "+user.getFullName());wl.setFont(new Font("Segoe UI",Font.PLAIN,13));wl.setForeground(new Color(100,120,160));
        bar.add(pgTitle,BorderLayout.WEST);bar.add(wl,BorderLayout.EAST);return bar;
    }

    private void route(String a){
        switch(a){
            case "home": showHome(); break;
            case "bus":  show("Book Bus",   new BookingListPanel(user,false,"Bus"));   break;
            case "car":  show("Book Car",   new BookingListPanel(user,false,"Car"));   break;
            case "bike": show("Book Bike",  new BookingListPanel(user,false,"Bike"));  break;
            case "air":  show("Book Airline",new BookingListPanel(user,false,"Airline"));break;
            case "mybus":  show("My Bus Bookings",   new MyBookingsPanel(user,"Bus"));   break;
            case "mycar":  show("My Car Bookings",   new MyBookingsPanel(user,"Car"));   break;
            case "mybike": show("My Bike Bookings",  new MyBookingsPanel(user,"Bike"));  break;
            case "myair":  show("My Airline Bookings",new MyBookingsPanel(user,"Airline"));break;
            case "routes": show("Available Routes",  new RouteViewPanel()); break;
            case "logout": if(JOptionPane.showConfirmDialog(this,"Logout?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){dispose();new LoginForm().setVisible(true);} break;
        }
    }
    private void show(String title,JPanel panel){pgTitle.setText(title);content.removeAll();content.add(panel);content.revalidate();content.repaint();}

    private void showHome(){
        pgTitle.setText("Welcome Back!");content.removeAll();
        JPanel page=new JPanel(new BorderLayout(0,20));page.setOpaque(false);
        long myBus=bc.getByUser(user.getId()).stream().filter(b->"Bus".equals(b.getTransportType())&&"Booked".equals(b.getStatus())).count();
        long myCar=bc.getByUser(user.getId()).stream().filter(b->"Car".equals(b.getTransportType())&&"Booked".equals(b.getStatus())).count();
        long myBike=bc.getByUser(user.getId()).stream().filter(b->"Bike".equals(b.getTransportType())&&"Booked".equals(b.getStatus())).count();
        long myAir=bc.getByUser(user.getId()).stream().filter(b->"Airline".equals(b.getTransportType())&&"Booked".equals(b.getStatus())).count();
        JPanel cards=new JPanel(new GridLayout(1,4,14,0));cards.setOpaque(false);
        cards.add(UI.statCard("🚌 Bus Bookings",String.valueOf(myBus),UI.BLUE));
        cards.add(UI.statCard("🚗 Car Bookings",String.valueOf(myCar),UI.GREEN));
        cards.add(UI.statCard("🏍️ Bike Bookings",String.valueOf(myBike),UI.ORG));
        cards.add(UI.statCard("✈️ Airline",String.valueOf(myAir),UI.PURP));
        JLabel ql=new JLabel("Book Transport");ql.setFont(new Font("Segoe UI",Font.BOLD,16));ql.setForeground(UI.DARK);
        JPanel qr=new JPanel(new FlowLayout(FlowLayout.LEFT,12,0));qr.setOpaque(false);
        Object[][] qa={{"Book Bus",UI.BLUE,btnBus,"bus"},{"Book Car",UI.GREEN,btnCar,"car"},{"Book Bike",UI.ORG,btnBike,"bike"},{"Book Airline",UI.PURP,btnAir,"air"}};
        for(Object[] q:qa){final JButton nav=(JButton)q[2];final String ac=(String)q[3];JButton b=UI.wideBtn((String)q[0],(Color)q[1],175);b.addActionListener(e->{setAct(nav);route(ac);});qr.add(b);}
        JPanel qa2=new JPanel(new BorderLayout(0,10));qa2.setOpaque(false);qa2.add(ql,BorderLayout.NORTH);qa2.add(qr,BorderLayout.CENTER);

        // Airport info card
        JPanel airCard=UI.card();airCard.setLayout(new BorderLayout(0,8));
        JLabel al=new JLabel("✈️  Sukkur Airport Booking Guide");al.setFont(new Font("Segoe UI",Font.BOLD,15));al.setForeground(UI.DARK);
        JLabel as=new JLabel("<html><body style='font-family:Segoe UI;font-size:12px;color:#445;'>" +
            "Khairpur se Sukkur Airport sirf <b>1 Hour</b> door hai!<br><br>" +
            "<b>Step 1:</b> Pehle <b>Bus/Car/Bike</b> book karo: Khairpur → Sukkur Airport<br>" +
            "<b>Step 2:</b> Phir <b>Airline</b> book karo: Sukkur Airport → Karachi/Lahore/Islamabad<br><br>" +
            "🚌 Bus fare: Rs.160 &nbsp;&nbsp; 🚗 Car fare: Rs.900 &nbsp;&nbsp; 🏍️ Bike fare: Rs.320" +
            "</body></html>");
        airCard.add(al,BorderLayout.NORTH);airCard.add(as,BorderLayout.CENTER);

        JPanel top=new JPanel(new BorderLayout(0,18));top.setOpaque(false);top.add(cards,BorderLayout.NORTH);top.add(qa2,BorderLayout.SOUTH);
        page.add(top,BorderLayout.NORTH);page.add(airCard,BorderLayout.CENTER);
        content.add(page);content.revalidate();content.repaint();
    }
}
