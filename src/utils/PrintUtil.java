package utils;
import models.Booking;
import java.awt.*;
import java.awt.print.*;
import java.util.List;

public class PrintUtil {
    public static void printBooking(Booking b){
        PrinterJob job=PrinterJob.getPrinterJob();
        job.setJobName("Booking-"+b.getBookingNumber());
        job.setPrintable((g,pf,pi)->{
            if(pi>0) return Printable.NO_SUCH_PAGE;
            Graphics2D g2=(Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(pf.getImageableX(),pf.getImageableY());
            int x=40,y=40,w=360;
            g2.setColor(new Color(8,25,65));g2.fillRect(x-10,y-10,w+20,55);
            g2.setColor(Color.WHITE);g2.setFont(new Font("Segoe UI",Font.BOLD,18));
            g2.drawString("KHAIRPUR MULTI-TRANSPORT",x+20,y+18);
            g2.setFont(new Font("Segoe UI",Font.PLAIN,11));g2.drawString("Booking Confirmation",x+80,y+35);
            g2.setColor(Color.BLACK);y+=65;
            String[][] rows={
                {"Booking No:",b.getBookingNumber()},
                {"Type:",b.getTypeIcon()+"  "+b.getTransportType()},
                {"Passenger:",b.getPassengerName()},
                {"Contact:",b.getPassengerContact()!=null?b.getPassengerContact():"-"},
                {"Route:",b.getRouteName()!=null?b.getRouteName():"-"},
                {"Vehicle:",b.getVehicleNumber()!=null?b.getVehicleNumber():"-"},
                {"Travel Date:",b.getTravelDate()!=null?b.getTravelDate():"-"},
                {"Seat:",b.getSeatNumber()!=null&&!b.getSeatNumber().isEmpty()?b.getSeatNumber():"-"},
                {"Note:",b.getSpecialNote()!=null&&!b.getSpecialNote().isEmpty()?b.getSpecialNote():"-"},
                {"Fare:","Rs. "+String.format("%.0f",b.getFare())},
                {"Status:",b.getStatus()}
            };
            for(String[] row:rows){
                g2.setFont(new Font("Segoe UI",Font.BOLD,12));g2.drawString(row[0],x,y);
                g2.setFont(new Font("Segoe UI",Font.PLAIN,12));g2.drawString(row[1],x+130,y);
                y+=24;
            }
            g2.setColor(Color.LIGHT_GRAY);g2.drawLine(x,y,x+w,y);y+=15;
            g2.setColor(Color.GRAY);g2.setFont(new Font("Segoe UI",Font.ITALIC,10));
            g2.drawString("Thank you for choosing Khairpur Transport!",x+30,y);
            return Printable.PAGE_EXISTS;
        });
        if(job.printDialog()){try{job.print();}catch(PrinterException e){javax.swing.JOptionPane.showMessageDialog(null,"Print failed: "+e.getMessage());}}
    }

    public static void printList(List<Booking> list,String title){
        PrinterJob job=PrinterJob.getPrinterJob();
        job.setJobName(title);
        job.setPrintable((g,pf,pi)->{
            if(pi>0) return Printable.NO_SUCH_PAGE;
            Graphics2D g2=(Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(pf.getImageableX(),pf.getImageableY());
            int x=20,y=30,pw=(int)pf.getImageableWidth();
            g2.setColor(new Color(8,25,65));g2.fillRect(x-5,y-10,pw-10,40);
            g2.setColor(Color.WHITE);g2.setFont(new Font("Segoe UI",Font.BOLD,14));g2.drawString("KHAIRPUR TRANSPORT - "+title,x+5,y+16);
            y+=45;
            g2.setColor(new Color(230,235,245));g2.fillRect(x-2,y-12,pw-10,20);
            g2.setColor(Color.BLACK);g2.setFont(new Font("Segoe UI",Font.BOLD,10));
            g2.drawString("#",x,y);g2.drawString("Booking",x+20,y);g2.drawString("Type",x+90,y);g2.drawString("Passenger",x+140,y);g2.drawString("Route",x+240,y);g2.drawString("Date",x+360,y);g2.drawString("Fare",x+430,y);
            y+=15;g2.setFont(new Font("Segoe UI",Font.PLAIN,9));
            int i=1;
            for(Booking b:list){
                if(y>pf.getImageableHeight()-30) break;
                if(i%2==0){g2.setColor(new Color(248,250,255));g2.fillRect(x-2,y-10,pw-10,16);}
                g2.setColor(Color.BLACK);
                g2.drawString(String.valueOf(i),x,y);
                g2.drawString(b.getBookingNumber()!=null?b.getBookingNumber():"-",x+20,y);
                g2.drawString(b.getTransportType()!=null?b.getTransportType():"-",x+90,y);
                String pn=b.getPassengerName()!=null?b.getPassengerName():"-";if(pn.length()>14)pn=pn.substring(0,11)+"...";
                g2.drawString(pn,x+140,y);
                String rn=b.getRouteName()!=null?b.getRouteName():"-";if(rn.length()>18)rn=rn.substring(0,15)+"...";
                g2.drawString(rn,x+240,y);
                g2.drawString(b.getTravelDate()!=null?b.getTravelDate():"-",x+360,y);
                g2.drawString("Rs."+String.format("%.0f",b.getFare()),x+430,y);
                y+=16;i++;
            }
            y+=10;g2.setFont(new Font("Segoe UI",Font.BOLD,11));g2.drawString("Total: "+list.size()+" bookings",x,y);
            return Printable.PAGE_EXISTS;
        });
        if(job.printDialog()){try{job.print();}catch(PrinterException e){javax.swing.JOptionPane.showMessageDialog(null,"Print failed: "+e.getMessage());}}
    }
}
