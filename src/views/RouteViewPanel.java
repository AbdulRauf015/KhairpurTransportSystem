package views;
import controllers.RouteController;
import models.TransportRoute;
import utils.UI;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;

public class RouteViewPanel extends JPanel {
    private RouteController rc;
    public RouteViewPanel(){rc=new RouteController();setOpaque(false);setLayout(new BorderLayout(0,14));init();}

    private void init(){
        JTabbedPane tabs=new JTabbedPane();
        tabs.setFont(new Font("Segoe UI",Font.BOLD,13));
        tabs.addTab("🚌 Bus Routes",   makeTab("Bus"));
        tabs.addTab("🚗 Car Routes",   makeTab("Car"));
        tabs.addTab("🏍️ Bike Routes",  makeTab("Bike"));
        tabs.addTab("✈️ Airline",       makeTab("Airline"));
        JPanel card=UI.card();card.setLayout(new BorderLayout(0,10));
        JLabel lbl=new JLabel("All Available Routes");lbl.setFont(new Font("Segoe UI",Font.BOLD,16));lbl.setForeground(UI.DARK);
        JLabel sub=new JLabel("Select a tab to view routes by transport type");sub.setFont(new Font("Segoe UI",Font.PLAIN,12));sub.setForeground(new Color(140,160,200));
        JPanel hdr=new JPanel(new BorderLayout());hdr.setOpaque(false);hdr.add(lbl,BorderLayout.NORTH);hdr.add(sub,BorderLayout.SOUTH);
        card.add(hdr,BorderLayout.NORTH);card.add(tabs,BorderLayout.CENTER);
        add(card,BorderLayout.CENTER);
    }

    private JPanel makeTab(String type){
        JPanel p=new JPanel(new BorderLayout());p.setBackground(Color.WHITE);
        String[] cols={"#","From","To","Distance","Fare (Rs)","Duration","Status"};
        DefaultTableModel m=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        JTable tbl=new JTable(m);UI.styleTable(tbl);
        tbl.getColumnModel().getColumn(0).setMaxWidth(45);
        tbl.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){
                JLabel l=(JLabel)super.getTableCellRendererComponent(t,v,s,f,r,c);l.setHorizontalAlignment(SwingConstants.CENTER);l.setFont(new Font("Segoe UI",Font.BOLD,12));
                if("Active".equals(v)){l.setForeground(UI.GREEN);l.setText("● Active");}else{l.setForeground(UI.RED);l.setText("● Inactive");}
                if(s)l.setBackground(new Color(210,228,255));else l.setBackground(r%2==0?Color.WHITE:new Color(247,250,255));return l;}
        });
        List<TransportRoute> routes=rc.getByType(type);
        int i=1;for(TransportRoute r:routes){m.addRow(new Object[]{i++,r.getFromCity(),r.getToCity(),r.getDistanceKm()+" km","Rs. "+String.format("%.0f",r.getFare()),r.getDuration(),r.getStatus()});}
        JScrollPane sc=new JScrollPane(tbl);sc.setBorder(BorderFactory.createEmptyBorder());sc.getViewport().setBackground(Color.WHITE);
        p.add(sc,BorderLayout.CENTER);return p;
    }
}
