package controllers;
import database.DBConnection;
import models.Booking;
import java.sql.*;
import java.util.*;

public class BookingController {
    private Connection conn;
    public BookingController(){conn=DBConnection.getConnection();}

    private static final String SELECT_ALL =
        "SELECT b.*,CONCAT(r.from_city,' → ',r.to_city) AS route_name,v.vehicle_number,d.name AS driver_name,u.full_name AS booked_by_name " +
        "FROM bookings b LEFT JOIN transport_routes r ON b.route_id=r.id LEFT JOIN vehicles v ON b.vehicle_id=v.id " +
        "LEFT JOIN drivers d ON b.driver_id=d.id LEFT JOIN users u ON b.booked_by=u.id ";

    public List<Booking> getAll(){return query(SELECT_ALL+"ORDER BY b.id DESC");}
    public List<Booking> getByUser(int userId){
        List<Booking> l=new ArrayList<>();
        try{PreparedStatement ps=conn.prepareStatement(SELECT_ALL+"WHERE b.booked_by=? ORDER BY b.id DESC");
        ps.setInt(1,userId);ResultSet rs=ps.executeQuery();while(rs.next()) l.add(extract(rs));}catch(SQLException e){}return l;
    }
    public List<Booking> getByType(String type){
        List<Booking> l=new ArrayList<>();
        try{PreparedStatement ps=conn.prepareStatement(SELECT_ALL+"WHERE b.transport_type=? ORDER BY b.id DESC");
        ps.setString(1,type);ResultSet rs=ps.executeQuery();while(rs.next()) l.add(extract(rs));}catch(SQLException e){}return l;
    }
    public List<Booking> search(String kw){
        List<Booking> l=new ArrayList<>();
        try{PreparedStatement ps=conn.prepareStatement(SELECT_ALL+"WHERE b.booking_number LIKE ? OR b.passenger_name LIKE ? OR b.transport_type LIKE ?");
        ps.setString(1,"%"+kw+"%");ps.setString(2,"%"+kw+"%");ps.setString(3,"%"+kw+"%");
        ResultSet rs=ps.executeQuery();while(rs.next()) l.add(extract(rs));}catch(SQLException e){}return l;
    }
    public Booking getById(int id){
        try{PreparedStatement ps=conn.prepareStatement(SELECT_ALL+"WHERE b.id=?");ps.setInt(1,id);ResultSet rs=ps.executeQuery();if(rs.next()) return extract(rs);}catch(SQLException e){}return null;
    }
    public boolean add(Booking b){
        try{PreparedStatement ps=conn.prepareStatement("INSERT INTO bookings(booking_number,transport_type,passenger_name,passenger_contact,route_id,vehicle_id,driver_id,travel_date,fare,seat_number,special_note,status,booked_by) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1,b.getBookingNumber());ps.setString(2,b.getTransportType());ps.setString(3,b.getPassengerName());ps.setString(4,b.getPassengerContact());
        ps.setInt(5,b.getRouteId());ps.setInt(6,b.getVehicleId());ps.setInt(7,b.getDriverId());ps.setString(8,b.getTravelDate());
        ps.setDouble(9,b.getFare());ps.setString(10,b.getSeatNumber());ps.setString(11,b.getSpecialNote());ps.setString(12,b.getStatus());ps.setInt(13,b.getBookedBy());
        return ps.executeUpdate()>0;}catch(SQLException e){System.err.println(e.getMessage());return false;}
    }
    public boolean cancel(int id){try{PreparedStatement ps=conn.prepareStatement("UPDATE bookings SET status='Cancelled' WHERE id=?");ps.setInt(1,id);return ps.executeUpdate()>0;}catch(SQLException e){return false;}}
    public boolean delete(int id){try{PreparedStatement ps=conn.prepareStatement("DELETE FROM bookings WHERE id=?");ps.setInt(1,id);return ps.executeUpdate()>0;}catch(SQLException e){return false;}}
    public String generateNumber(){
        try{ResultSet rs=conn.createStatement().executeQuery("SELECT COUNT(*) FROM bookings");if(rs.next()) return String.format("BK-%04d",rs.getInt(1)+1);}catch(SQLException e){}return "BK-0001";
    }
    public double getTotalIncome(){try{ResultSet rs=conn.createStatement().executeQuery("SELECT SUM(fare) FROM bookings WHERE status!='Cancelled'");if(rs.next()) return rs.getDouble(1);}catch(SQLException e){}return 0;}
    public List<Booking> getToday(){return query(SELECT_ALL+"WHERE DATE(b.travel_date)=CURDATE() ORDER BY b.id DESC");}
    private List<Booking> query(String sql){List<Booking> l=new ArrayList<>();try{ResultSet rs=conn.createStatement().executeQuery(sql);while(rs.next()) l.add(extract(rs));}catch(SQLException e){}return l;}
    private Booking extract(ResultSet rs) throws SQLException{
        Booking b=new Booking();b.setId(rs.getInt("id"));b.setBookingNumber(rs.getString("booking_number"));b.setTransportType(rs.getString("transport_type"));
        b.setPassengerName(rs.getString("passenger_name"));b.setPassengerContact(rs.getString("passenger_contact"));
        b.setRouteId(rs.getInt("route_id"));b.setVehicleId(rs.getInt("vehicle_id"));b.setDriverId(rs.getInt("driver_id"));
        b.setTravelDate(rs.getString("travel_date"));b.setFare(rs.getDouble("fare"));b.setSeatNumber(rs.getString("seat_number"));
        b.setSpecialNote(rs.getString("special_note"));b.setStatus(rs.getString("status"));b.setBookedBy(rs.getInt("booked_by"));
        b.setRouteName(rs.getString("route_name"));b.setVehicleNumber(rs.getString("vehicle_number"));b.setDriverName(rs.getString("driver_name"));b.setBookedByName(rs.getString("booked_by_name"));
        return b;
    }
}
