package views;
import models.User;
import controllers.*;
import utils.UI;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class AdminDashboard extends JFrame {
    private User user; private JPanel content; private JLabel pgTitle;
    private JButton btnDash,btnBus,btnCar,btnBike,btnAir,btnVehicles,btnDrivers,btnRoutes,btnUsers,btnReports,btnLogout,active;
    private BookingController bc; private RouteController rc; private VehicleController vc; private DriverController dc;

    public AdminDashboard(User user){this.user=user;bc=new BookingController();rc=new RouteController();vc=new VehicleController();dc=new DriverController();init();}

    private void init(){
        setTitle("Khairpur Multi-Transport - Admin");setDefaultCloseOperation(EXIT_ON_CLOSE);setExtendedState(MAXIMIZED_BOTH);setMinimumSize(new Dimension(1100,650));setLayout(new BorderLayout());
        add(sidebar(),BorderLayout.WEST);
        JPanel main=new JPanel(new BorderLayout());main.setBackground(UI.BG);main.add(topbar(),BorderLayout.NORTH);
        content=new JPanel(new BorderLayout());content.setBackground(UI.BG);content.setBorder(BorderFactory.createEmptyBorder(22,28,22,28));
        main.add(content,BorderLayout.CENTER);add(main,BorderLayout.CENTER);showDash();
    }

    private JPanel sidebar(){
        JPanel s=new JPanel(){@Override protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);GradientPaint gp=new GradientPaint(0,0,new Color(6,18,50),0,getHeight(),new Color(12,40,100));g2.setPaint(gp);g2.fillRect(0,0,getWidth(),getHeight());g2.setColor(new Color(255,255,255,8));g2.fillOval(-40,getHeight()-220,200,200);g2.fillOval(90,-70,150,150);}};
        s.setPreferredSize(new Dimension(240,0));s.setLayout(new BorderLayout());
        JPanel logo=new JPanel();logo.setOpaque(false);logo.setLayout(new BoxLayout(logo,BoxLayout.Y_AXIS));logo.setBorder(BorderFactory.createEmptyBorder(25,22,20,22));
        addL(logo,new JLabel("🚌🏍️🚗✈️"){{setFont(new Font("Segoe UI Emoji",Font.PLAIN,28));setAlignmentX(LEFT_ALIGNMENT);}});
        addL(logo,new JLabel("KT System"){{setFont(new Font("Segoe UI",Font.BOLD,19));setForeground(Color.WHITE);setAlignmentX(LEFT_ALIGNMENT);}});
        addL(logo,new JLabel("  Admin Panel"){{setFont(new Font("Segoe UI",Font.PLAIN,11));setForeground(new Color(100,160,255));setAlignmentX(LEFT_ALIGNMENT);}});
        JPanel nav=new JPanel();nav.setOpaque(false);nav.setLayout(new BoxLayout(nav,BoxLayout.Y_AXIS));nav.setBorder(BorderFactory.createEmptyBorder(8,10,8,10));
        JLabel sec1=sec("BOOKINGS");
        btnDash   =sb("🏠","Dashboard","dash");
        btnBus    =sb("🚌","Bus Bookings","bus");
        btnCar    =sb("🚗","Car Bookings","car");
        btnBike   =sb("🏍️","Bike Bookings","bike");
        btnAir    =sb("✈️","Airline Bookings","air");
        JLabel sec2=sec("MANAGEMENT");
        btnVehicles=sb("🔧","Vehicles","vehicles");
        btnDrivers =sb("👤","Drivers","drivers");
        btnRoutes  =sb("🛣️","Routes","routes");
        btnUsers   =sb("👥","Users","users");
        JLabel sec3=sec("OTHER");
        btnReports=sb("📊","Reports","reports");
        btnLogout =sb("🚪","Logout","logout");
        nav.add(sec1);
        for(JButton b:new JButton[]{btnDash,btnBus,btnCar,btnBike,btnAir}){nav.add(b);nav.add(Box.createVerticalStrut(2));}
        nav.add(Box.createVerticalStrut(8));nav.add(sec2);
        for(JButton b:new JButton[]{btnVehicles,btnDrivers,btnRoutes,btnUsers}){nav.add(b);nav.add(Box.createVerticalStrut(2));}
        nav.add(Box.createVerticalStrut(8));nav.add(sec3);nav.add(btnReports);nav.add(Box.createVerticalStrut(2));
        JPanel bot=new JPanel();bot.setOpaque(false);bot.setLayout(new BoxLayout(bot,BoxLayout.Y_AXIS));bot.setBorder(BorderFactory.createEmptyBorder(0,10,15,10));
        JPanel uc=userCard();bot.add(uc);bot.add(Box.createVerticalStrut(8));bot.add(btnLogout);
        s.add(logo,BorderLayout.NORTH);s.add(nav,BorderLayout.CENTER);s.add(bot,BorderLayout.SOUTH);
        setAct(btnDash);return s;
    }

    private JLabel sec(String t){JLabel l=new JLabel(t);l.setFont(new Font("Segoe UI",Font.BOLD,10));l.setForeground(new Color(80,120,190));l.setBorder(BorderFactory.createEmptyBorder(4,13,6,0));l.setAlignmentX(Component.LEFT_ALIGNMENT);return l;}
    private void addL(JPanel p,JLabel l){p.add(l);p.add(Box.createVerticalStrut(4));}

    private JPanel userCard(){
        JPanel c=new JPanel(){@Override protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(new Color(255,255,255,18));g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),10,10));}};
        c.setOpaque(false);c.setLayout(new BorderLayout(10,0));c.setBorder(BorderFactory.createEmptyBorder(10,12,10,12));c.setMaximumSize(new Dimension(220,55));
        JLabel av=new JLabel("👑");av.setFont(new Font("Segoe UI Emoji",Font.PLAIN,22));
        JPanel np=new JPanel(new GridLayout(2,1));np.setOpaque(false);
        JLabel nm=new JLabel(user.getFullName());nm.setFont(new Font("Segoe UI",Font.BOLD,12));nm.setForeground(Color.WHITE);
        JLabel rl=new JLabel("Administrator");rl.setFont(new Font("Segoe UI",Font.PLAIN,11));rl.setForeground(new Color(120,170,255));
        np.add(nm);np.add(rl);c.add(av,BorderLayout.WEST);c.add(np,BorderLayout.CENTER);return c;
    }

    private JButton sb(String ico,String lbl,String action){
        JButton b=new JButton(){@Override protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);if(this==active){g2.setColor(new Color(0,100,220));g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),10,10));g2.setColor(new Color(255,255,255,50));g2.fill(new RoundRectangle2D.Double(0,0,4,getHeight(),2,2));}else if(getModel().isRollover()){g2.setColor(new Color(255,255,255,15));g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),10,10));}g2.setFont(new Font("Segoe UI Emoji",Font.PLAIN,13));g2.setColor(this==active?Color.WHITE:new Color(160,200,255));g2.drawString(ico,14,getHeight()/2+6);g2.setFont(new Font("Segoe UI",Font.BOLD,12));g2.setColor(this==active?Color.WHITE:new Color(180,210,255));g2.drawString(lbl,40,getHeight()/2+5);}};
        b.setPreferredSize(new Dimension(220,40));b.setMaximumSize(new Dimension(220,40));b.setContentAreaFilled(false);b.setBorderPainted(false);b.setFocusPainted(false);b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addActionListener(e->{setAct(b);route(action);});return b;
    }
    private void setAct(JButton b){active=b;for(JButton x:new JButton[]{btnDash,btnBus,btnCar,btnBike,btnAir,btnVehicles,btnDrivers,btnRoutes,btnUsers,btnReports,btnLogout})if(x!=null)x.repaint();}

    private JPanel topbar(){
        JPanel bar=new JPanel(new BorderLayout());bar.setBackground(Color.WHITE);bar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(220,228,240)),BorderFactory.createEmptyBorder(13,26,13,26)));
        pgTitle=new JLabel("Dashboard");pgTitle.setFont(new Font("Segoe UI",Font.BOLD,22));pgTitle.setForeground(UI.DARK);
        JLabel wl=new JLabel("👑  "+user.getFullName());wl.setFont(new Font("Segoe UI",Font.PLAIN,13));wl.setForeground(new Color(100,120,160));
        bar.add(pgTitle,BorderLayout.WEST);bar.add(wl,BorderLayout.EAST);return bar;
    }

    private void route(String a){
        switch(a){
            case "dash":showDash();break;
            case "bus":show("Bus Bookings",new BookingListPanel(user,true,"Bus"));break;
            case "car":show("Car Bookings",new BookingListPanel(user,true,"Car"));break;
            case "bike":show("Bike Bookings",new BookingListPanel(user,true,"Bike"));break;
            case "air":show("Airline Bookings",new BookingListPanel(user,true,"Airline"));break;
            case "vehicles":show("Vehicles",new VehiclePanel());break;
            case "drivers":show("Drivers",new DriverPanel());break;
            case "routes":show("Routes",new RoutePanel());break;
            case "users":show("Users",new UserPanel());break;
            case "reports":show("Reports",new ReportsPanel());break;
            case "logout":if(JOptionPane.showConfirmDialog(this,"Logout?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){dispose();new LoginForm().setVisible(true);}break;
        }
    }
    private void show(String title,JPanel panel){pgTitle.setText(title);content.removeAll();content.add(panel);content.revalidate();content.repaint();}

    private void showDash(){
        pgTitle.setText("Dashboard");content.removeAll();
        JPanel page=new JPanel(new BorderLayout(0,20));page.setOpaque(false);
        int buses=bc.getByType("Bus").size(),cars=bc.getByType("Car").size(),bikes=bc.getByType("Bike").size(),airlines=bc.getByType("Airline").size();
        double income=bc.getTotalIncome();int routes=rc.getAll().size();
        JPanel cards=new JPanel(new GridLayout(1,5,14,0));cards.setOpaque(false);
        cards.add(UI.statCard("Bus Bookings",String.valueOf(buses),UI.BLUE));
        cards.add(UI.statCard("Car Bookings",String.valueOf(cars),UI.GREEN));
        cards.add(UI.statCard("Bike Bookings",String.valueOf(bikes),UI.ORG));
        cards.add(UI.statCard("✈️ Airline",String.valueOf(airlines),UI.PURP));
        cards.add(UI.statCard("💰 Income","Rs."+String.format("%.0f",income),new Color(0,130,120)));
        JLabel ql=new JLabel("Quick Actions");ql.setFont(new Font("Segoe UI",Font.BOLD,16));ql.setForeground(UI.DARK);
        JPanel qr=new JPanel(new FlowLayout(FlowLayout.LEFT,12,0));qr.setOpaque(false);
        String[][] qa={{"🚌 Book Bus",""+"bus"},{"🚗 Book Car","car"},{"🏍️ Book Bike","bike"},{"✈️ Book Airline","air"}};
        Color[] qc={UI.BLUE,UI.GREEN,UI.ORG,UI.PURP};
        JButton[] navBtns={btnBus,btnCar,btnBike,btnAir};
        for(int i=0;i<4;i++){final int fi=i;JButton b=UI.wideBtn(qa[fi][0],qc[fi],160);b.addActionListener(e->{setAct(navBtns[fi]);route(qa[fi][1]);});qr.add(b);}
        JPanel qa2=new JPanel(new BorderLayout(0,10));qa2.setOpaque(false);qa2.add(ql,BorderLayout.NORTH);qa2.add(qr,BorderLayout.CENTER);
        JPanel top=new JPanel(new BorderLayout(0,18));top.setOpaque(false);top.add(cards,BorderLayout.NORTH);top.add(qa2,BorderLayout.SOUTH);
        // Recent bookings table
        JLabel rl=new JLabel("Recent Bookings");rl.setFont(new Font("Segoe UI",Font.BOLD,16));rl.setForeground(UI.DARK);
        String[] cols={"#","Type","Passenger","Route","Vehicle","Date","Fare","Status"};
        java.util.List<models.Booking> recent=bc.getAll();
        Object[][] data=new Object[Math.min(6,recent.size())][8];
        for(int i=0;i<data.length;i++){models.Booking b=recent.get(i);data[i]=new Object[]{i+1,b.getTransportType(),b.getPassengerName(),b.getRouteName(),b.getVehicleNumber(),b.getTravelDate(),"Rs."+String.format("%.0f",b.getFare()),b.getStatus()};}
        JTable tbl=new JTable(data,cols){public boolean isCellEditable(int r,int c){return false;}};
        UI.styleTable(tbl);tbl.getColumnModel().getColumn(0).setMaxWidth(40);
        JScrollPane sc=new JScrollPane(tbl);sc.setBorder(BorderFactory.createLineBorder(new Color(215,225,242)));sc.getViewport().setBackground(Color.WHITE);
        JPanel tc=UI.card();tc.setLayout(new BorderLayout(0,12));tc.add(rl,BorderLayout.NORTH);tc.add(sc,BorderLayout.CENTER);
        page.add(top,BorderLayout.NORTH);page.add(tc,BorderLayout.CENTER);
        content.add(page);content.revalidate();content.repaint();
    }
}
