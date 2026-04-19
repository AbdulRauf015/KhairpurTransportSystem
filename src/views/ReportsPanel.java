package views;
import controllers.BookingController;
import models.Booking;
import utils.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportsPanel extends JPanel {
    private BookingController bc; private JTable table; private DefaultTableModel model;

    public ReportsPanel(){bc=new BookingController();setOpaque(false);setLayout(new BorderLayout(0,14));init();loadAll();}

    private void init(){
        // Stats
        List<Booking> all=bc.getAll();
        long buses=all.stream().filter(b->"Bus".equals(b.getTransportType())).count();
        long cars=all.stream().filter(b->"Car".equals(b.getTransportType())).count();
        long bikes=all.stream().filter(b->"Bike".equals(b.getTransportType())).count();
        long airlines=all.stream().filter(b->"Airline".equals(b.getTransportType())).count();
        double income=bc.getTotalIncome();

        JPanel stats=new JPanel(new GridLayout(1,5,12,0));stats.setOpaque(false);stats.setPreferredSize(new Dimension(0,85));
        stats.add(UI.statCard("🚌 Bus",String.valueOf(buses),UI.BLUE));
        stats.add(UI.statCard("🚗 Car",String.valueOf(cars),UI.GREEN));
        stats.add(UI.statCard("🏍️ Bike",String.valueOf(bikes),UI.ORG));
        stats.add(UI.statCard("✈️ Airline",String.valueOf(airlines),UI.PURP));
        stats.add(UI.statCard("💰 Income","Rs."+String.format("%.0f",income),new Color(0,130,120)));

        JPanel card=UI.card();card.setLayout(new BorderLayout(0,12));
        JLabel tl=new JLabel("Reports & Statistics");tl.setFont(new Font("Segoe UI",Font.BOLD,16));tl.setForeground(UI.DARK);

        JPanel btnRow=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));btnRow.setOpaque(false);
        JButton bAll=UI.wideBtn("📋 All Bookings",UI.BLUE,150);
        JButton bToday=UI.wideBtn("📅 Today",UI.GREEN,130);
        JButton bBus=UI.btn("🚌 Bus",UI.BLUE);JButton bCar=UI.btn("🚗 Car",UI.GREEN);
        JButton bBike=UI.btn("🏍️ Bike",UI.ORG);JButton bAir=UI.btn("✈️ Airline",UI.PURP);
        JButton bPrint=UI.wideBtn("🖨 Print List",UI.GRAY,140);
        bAll.addActionListener(e->loadAll());bToday.addActionListener(e->loadToday());
        bBus.addActionListener(e->loadType("Bus"));bCar.addActionListener(e->loadType("Car"));
        bBike.addActionListener(e->loadType("Bike"));bAir.addActionListener(e->loadType("Airline"));
        bPrint.addActionListener(e->printAll());
        btnRow.add(bAll);btnRow.add(bToday);btnRow.add(bBus);btnRow.add(bCar);btnRow.add(bBike);btnRow.add(bAir);btnRow.add(bPrint);

        String[] cols={"#","Booking No","Type","Passenger","Route","Vehicle","Date","Fare","Status"};
        model=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        table=new JTable(model);UI.styleTable(table);table.getColumnModel().getColumn(0).setMaxWidth(40);
        JScrollPane sc=new JScrollPane(table);sc.setBorder(BorderFactory.createLineBorder(new Color(215,225,242)));sc.getViewport().setBackground(Color.WHITE);

        card.add(tl,BorderLayout.NORTH);card.add(btnRow,BorderLayout.CENTER);card.add(sc,BorderLayout.SOUTH);
        // Adjust layout
        JPanel mainCard=UI.card();mainCard.setLayout(new BorderLayout(0,12));mainCard.add(tl,BorderLayout.NORTH);mainCard.add(btnRow,BorderLayout.CENTER);
        JPanel tableCard=UI.card();tableCard.setLayout(new BorderLayout());tableCard.add(sc,BorderLayout.CENTER);

        add(stats,BorderLayout.NORTH);
        JPanel center=new JPanel(new BorderLayout(0,14));center.setOpaque(false);center.add(mainCard,BorderLayout.NORTH);center.add(tableCard,BorderLayout.CENTER);
        add(center,BorderLayout.CENTER);
    }

    private void loadAll(){fill(bc.getAll());}
    private void loadToday(){fill(bc.getToday());}
    private void loadType(String t){fill(bc.getByType(t));}
    private void printAll(){List<Booking> list=bc.getAll();if(list.isEmpty()){JOptionPane.showMessageDialog(this,"No bookings!");return;}PrintUtil.printList(list,"All Bookings Report");}

    private void fill(List<Booking> list){
        model.setRowCount(0);int i=1;
        for(Booking b:list) model.addRow(new Object[]{i++,b.getBookingNumber(),b.getTransportType(),b.getPassengerName(),b.getRouteName(),b.getVehicleNumber(),b.getTravelDate(),"Rs."+String.format("%.0f",b.getFare()),b.getStatus()});
    }
}
