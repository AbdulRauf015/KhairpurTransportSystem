package controllers;
import database.DBConnection;
import models.Vehicle;
import java.sql.*;
import java.util.*;

public class VehicleController {
    private Connection conn;
    public VehicleController(){conn=DBConnection.getConnection();}
    public List<Vehicle> getAll(){return query("SELECT * FROM vehicles ORDER BY transport_type,id");}
    public List<Vehicle> getByType(String type){
        List<Vehicle> l=new ArrayList<>();
        try{PreparedStatement ps=conn.prepareStatement("SELECT * FROM vehicles WHERE transport_type=? AND status='Active' ORDER BY id");
        ps.setString(1,type);ResultSet rs=ps.executeQuery();while(rs.next()) l.add(extract(rs));}catch(SQLException e){}return l;
    }
    public boolean add(Vehicle v){
        try{PreparedStatement ps=conn.prepareStatement("INSERT INTO vehicles(transport_type,vehicle_number,model,capacity,status) VALUES(?,?,?,?,?)");
        ps.setString(1,v.getTransportType());ps.setString(2,v.getVehicleNumber());ps.setString(3,v.getModel());ps.setInt(4,v.getCapacity());ps.setString(5,v.getStatus());
        return ps.executeUpdate()>0;}catch(SQLException e){return false;}
    }
    public boolean update(Vehicle v){
        try{PreparedStatement ps=conn.prepareStatement("UPDATE vehicles SET transport_type=?,vehicle_number=?,model=?,capacity=?,status=? WHERE id=?");
        ps.setString(1,v.getTransportType());ps.setString(2,v.getVehicleNumber());ps.setString(3,v.getModel());ps.setInt(4,v.getCapacity());ps.setString(5,v.getStatus());ps.setInt(6,v.getId());
        return ps.executeUpdate()>0;}catch(SQLException e){return false;}
    }
    public boolean delete(int id){try{PreparedStatement ps=conn.prepareStatement("DELETE FROM vehicles WHERE id=?");ps.setInt(1,id);return ps.executeUpdate()>0;}catch(SQLException e){return false;}}
    public List<Vehicle> search(String kw){
        List<Vehicle> l=new ArrayList<>();
        try{PreparedStatement ps=conn.prepareStatement("SELECT * FROM vehicles WHERE vehicle_number LIKE ? OR model LIKE ? OR transport_type LIKE ?");
        ps.setString(1,"%"+kw+"%");ps.setString(2,"%"+kw+"%");ps.setString(3,"%"+kw+"%");
        ResultSet rs=ps.executeQuery();while(rs.next()) l.add(extract(rs));}catch(SQLException e){}return l;
    }
    private List<Vehicle> query(String sql){List<Vehicle> l=new ArrayList<>();try{ResultSet rs=conn.createStatement().executeQuery(sql);while(rs.next()) l.add(extract(rs));}catch(SQLException e){}return l;}
    private Vehicle extract(ResultSet rs) throws SQLException{return new Vehicle(rs.getInt("id"),rs.getString("transport_type"),rs.getString("vehicle_number"),rs.getString("model"),rs.getInt("capacity"),rs.getString("status"));}
}
