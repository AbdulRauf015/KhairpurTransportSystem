package views;
import controllers.BookingController;
import models.*;
import utils.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class MyBookingsPanel extends JPanel {
    private BookingController bc; private User user; private String type;
    private JTable table; private DefaultTableModel model;
    private JTextField txtSearch;
    private ArrayList<Integer> ids=new ArrayList<>(); private int selId=-1;
    private Color typeColor;

    public MyBookingsPanel(User user,String type){
        this.user=user;this.type=type;this.bc=new BookingController();
        typeColor=type.equals("Bus")?UI.BLUE:type.equals("Car")?UI.GREEN:type.equals("Bike")?UI.ORG:UI.PURP;
        setOpaque(false);setLayout(new BorderLayout(0,14));init();load();
    }

    private void init(){
        // Stats
        java.util.List<Booking> all=bc.getByUser(user.getId());
        long booked=all.stream().filter(b->type.equals(b.getTransportType())&&"Booked".equals(b.getStatus())).count();
        long total=all.stream().filter(b->type.equals(b.getTransportType())).count();
        double spent=all.stream().filter(b->type.equals(b.getTransportType())&&!"Cancelled".equals(b.getStatus())).mapToDouble(Booking::getFare).sum();
        JPanel stats=new JPanel(new GridLayout(1,3,14,0));stats.setOpaque(false);stats.setPreferredSize(new Dimension(0,85));
        String ico=type.equals("Bus")?"🚌":type.equals("Car")?"🚗":type.equals("Bike")?"🏍️":"✈️";
        stats.add(UI.statCard(ico+" Total "+type,String.valueOf(total),typeColor));
        stats.add(UI.statCard("✅ Active",String.valueOf(booked),UI.GREEN));
        stats.add(UI.statCard("💰 Spent","Rs."+String.format("%.0f",spent),UI.ORG));

        JPanel card=UI.card();card.setLayout(new BorderLayout(0,12));
        JLabel tl=new JLabel(ico+" My "+type+" History");tl.setFont(new Font("Segoe UI",Font.BOLD,15));tl.setForeground(UI.DARK);
        JPanel tr=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));tr.setOpaque(false);
        txtSearch=UI.field();txtSearch.setPreferredSize(new Dimension(190,34));
        JButton bs=UI.btn("🔍 Search",UI.BLUE);JButton br=UI.btn("↺ All",UI.GRAY);
        JButton bc2=UI.btn("✕ Cancel",UI.RED);JButton bp=UI.btn("🖨 Print",UI.GREEN);
        bs.addActionListener(e->search(txtSearch.getText().trim()));
        br.addActionListener(e->{txtSearch.setText("");load();});
        bc2.addActionListener(e->cancel());bp.addActionListener(e->print());
        tr.add(txtSearch);tr.add(bs);tr.add(br);tr.add(Box.createHorizontalStrut(6));tr.add(bc2);tr.add(bp);
        JPanel tb=new JPanel(new BorderLayout());tb.setOpaque(false);tb.add(tl,BorderLayout.WEST);tb.add(tr,BorderLayout.EAST);

        String[] cols={"#","Booking No","Passenger","Route","Vehicle","Date","Fare","Seat","Status"};
        model=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        table=new JTable(model);UI.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(40);table.getColumnModel().getColumn(7).setMaxWidth(65);
        table.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){
                JLabel l=new JLabel();l.setFont(new Font("Segoe UI",Font.BOLD,12));l.setHorizontalAlignment(SwingConstants.CENTER);l.setOpaque(true);
                String val=v!=null?v.toString():"";
                if("Booked".equals(val)){l.setText("Booked");l.setForeground(UI.GREEN);}
                else if("Cancelled".equals(val)){l.setText("Cancelled");l.setForeground(UI.RED);}
                else{l.setText(val);l.setForeground(Color.GRAY);}
                if(s)l.setBackground(new Color(210,228,255));else l.setBackground(r%2==0?Color.WHITE:new Color(247,250,255));
                return l;}
        });
        JScrollPane sc=new JScrollPane(table);sc.setBorder(BorderFactory.createLineBorder(new Color(215,225,242)));sc.getViewport().setBackground(Color.WHITE);
        card.add(tb,BorderLayout.NORTH);card.add(sc,BorderLayout.CENTER);
        add(stats,BorderLayout.NORTH);add(card,BorderLayout.CENTER);
        table.getSelectionModel().addListSelectionListener(e->{int row=table.getSelectedRow();if(row>=0&&row<ids.size()) selId=ids.get(row);});
    }

    private void load(){
        model.setRowCount(0);ids.clear();
        java.util.List<Booking> list=bc.getByUser(user.getId());
        int i=1;for(Booking b:list){
            if(!type.equals(b.getTransportType())) continue;
            model.addRow(new Object[]{i++,b.getBookingNumber(),b.getPassengerName(),b.getRouteName(),b.getVehicleNumber(),b.getTravelDate(),"Rs."+String.format("%.0f",b.getFare()),b.getSeatNumber(),b.getStatus()});
            ids.add(b.getId());
        }
    }
    private void search(String kw){
        model.setRowCount(0);ids.clear();
        java.util.List<Booking> list=bc.getByUser(user.getId());
        int i=1;for(Booking b:list){
            if(!type.equals(b.getTransportType())) continue;
            if(b.getBookingNumber().toLowerCase().contains(kw.toLowerCase())||b.getPassengerName().toLowerCase().contains(kw.toLowerCase())||
               (b.getRouteName()!=null&&b.getRouteName().toLowerCase().contains(kw.toLowerCase()))){
                model.addRow(new Object[]{i++,b.getBookingNumber(),b.getPassengerName(),b.getRouteName(),b.getVehicleNumber(),b.getTravelDate(),"Rs."+String.format("%.0f",b.getFare()),b.getSeatNumber(),b.getStatus()});
                ids.add(b.getId());
            }
        }
    }
    private void cancel(){if(selId==-1){JOptionPane.showMessageDialog(this,"Select a booking first!");return;}if(JOptionPane.showConfirmDialog(this,"Cancel this booking?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){if(bc.cancel(selId)){selId=-1;load();}}}
    private void print(){if(selId==-1){JOptionPane.showMessageDialog(this,"Select a booking to print!");return;}Booking b=bc.getById(selId);if(b!=null) PrintUtil.printBooking(b);}
}
