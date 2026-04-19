package controllers;
import database.DBConnection;
import models.Driver;
import java.sql.*;
import java.util.*;

public class DriverController {
    private Connection conn;
    public DriverController(){conn=DBConnection.getConnection();}
    public List<Driver> getAll(){return query("SELECT * FROM drivers ORDER BY transport_type,id");}
    public List<Driver> getByType(String type){
        List<Driver> l=new ArrayList<>();
        try{PreparedStatement ps=conn.prepareStatement("SELECT * FROM drivers WHERE transport_type=? AND status='Active' ORDER BY id");
        ps.setString(1,type);ResultSet rs=ps.executeQuery();while(rs.next()) l.add(extract(rs));}catch(SQLException e){}return l;
    }
    public boolean add(Driver d){
        try{PreparedStatement ps=conn.prepareStatement("INSERT INTO drivers(name,license_number,contact,transport_type,address,status) VALUES(?,?,?,?,?,?)");
        ps.setString(1,d.getName());ps.setString(2,d.getLicenseNumber());ps.setString(3,d.getContact());ps.setString(4,d.getTransportType());ps.setString(5,d.getAddress());ps.setString(6,d.getStatus());
        return ps.executeUpdate()>0;}catch(SQLException e){return false;}
    }
    public boolean update(Driver d){
        try{PreparedStatement ps=conn.prepareStatement("UPDATE drivers SET name=?,license_number=?,contact=?,transport_type=?,address=?,status=? WHERE id=?");
        ps.setString(1,d.getName());ps.setString(2,d.getLicenseNumber());ps.setString(3,d.getContact());ps.setString(4,d.getTransportType());ps.setString(5,d.getAddress());ps.setString(6,d.getStatus());ps.setInt(7,d.getId());
        return ps.executeUpdate()>0;}catch(SQLException e){return false;}
    }
    public boolean delete(int id){try{PreparedStatement ps=conn.prepareStatement("DELETE FROM drivers WHERE id=?");ps.setInt(1,id);return ps.executeUpdate()>0;}catch(SQLException e){return false;}}
    public List<Driver> search(String kw){
        List<Driver> l=new ArrayList<>();
        try{PreparedStatement ps=conn.prepareStatement("SELECT * FROM drivers WHERE name LIKE ? OR license_number LIKE ? OR transport_type LIKE ?");
        ps.setString(1,"%"+kw+"%");ps.setString(2,"%"+kw+"%");ps.setString(3,"%"+kw+"%");
        ResultSet rs=ps.executeQuery();while(rs.next()) l.add(extract(rs));}catch(SQLException e){}return l;
    }
    private List<Driver> query(String sql){List<Driver> l=new ArrayList<>();try{ResultSet rs=conn.createStatement().executeQuery(sql);while(rs.next()) l.add(extract(rs));}catch(SQLException e){}return l;}
    private Driver extract(ResultSet rs) throws SQLException{
        Driver d=new Driver();d.setId(rs.getInt("id"));d.setName(rs.getString("name"));d.setLicenseNumber(rs.getString("license_number"));d.setContact(rs.getString("contact"));d.setTransportType(rs.getString("transport_type"));d.setAddress(rs.getString("address"));d.setStatus(rs.getString("status"));return d;
    }
}
