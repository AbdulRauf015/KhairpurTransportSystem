package controllers;
import database.DBConnection;
import models.TransportRoute;
import java.sql.*;
import java.util.*;

public class RouteController {
    private Connection conn;
    public RouteController(){conn=DBConnection.getConnection();}

    public List<TransportRoute> getAll(){return query("SELECT * FROM transport_routes ORDER BY transport_type,id");}
    public List<TransportRoute> getByType(String type){
        List<TransportRoute> l=new ArrayList<>();
        try{PreparedStatement ps=conn.prepareStatement("SELECT * FROM transport_routes WHERE transport_type=? AND status='Active' ORDER BY id");
        ps.setString(1,type);ResultSet rs=ps.executeQuery();while(rs.next()) l.add(extract(rs));}catch(SQLException e){}return l;
    }
    public List<TransportRoute> getActive(){return query("SELECT * FROM transport_routes WHERE status='Active' ORDER BY transport_type,id");}
    public List<TransportRoute> search(String kw){
        List<TransportRoute> l=new ArrayList<>();
        try{PreparedStatement ps=conn.prepareStatement("SELECT * FROM transport_routes WHERE from_city LIKE ? OR to_city LIKE ? OR transport_type LIKE ?");
        ps.setString(1,"%"+kw+"%");ps.setString(2,"%"+kw+"%");ps.setString(3,"%"+kw+"%");
        ResultSet rs=ps.executeQuery();while(rs.next()) l.add(extract(rs));}catch(SQLException e){}return l;
    }
    public TransportRoute getById(int id){
        try{PreparedStatement ps=conn.prepareStatement("SELECT * FROM transport_routes WHERE id=?");ps.setInt(1,id);ResultSet rs=ps.executeQuery();if(rs.next()) return extract(rs);}catch(SQLException e){}return null;
    }
    public boolean add(TransportRoute r){
        try{PreparedStatement ps=conn.prepareStatement("INSERT INTO transport_routes(transport_type,from_city,to_city,distance_km,fare,duration,status) VALUES(?,?,?,?,?,?,?)");
        ps.setString(1,r.getTransportType());ps.setString(2,r.getFromCity());ps.setString(3,r.getToCity());ps.setInt(4,r.getDistanceKm());ps.setDouble(5,r.getFare());ps.setString(6,r.getDuration());ps.setString(7,r.getStatus());
        return ps.executeUpdate()>0;}catch(SQLException e){return false;}
    }
    public boolean update(TransportRoute r){
        try{PreparedStatement ps=conn.prepareStatement("UPDATE transport_routes SET transport_type=?,from_city=?,to_city=?,distance_km=?,fare=?,duration=?,status=? WHERE id=?");
        ps.setString(1,r.getTransportType());ps.setString(2,r.getFromCity());ps.setString(3,r.getToCity());ps.setInt(4,r.getDistanceKm());ps.setDouble(5,r.getFare());ps.setString(6,r.getDuration());ps.setString(7,r.getStatus());ps.setInt(8,r.getId());
        return ps.executeUpdate()>0;}catch(SQLException e){return false;}
    }
    public boolean delete(int id){try{PreparedStatement ps=conn.prepareStatement("DELETE FROM transport_routes WHERE id=?");ps.setInt(1,id);return ps.executeUpdate()>0;}catch(SQLException e){return false;}}

    private List<TransportRoute> query(String sql){List<TransportRoute> l=new ArrayList<>();try{ResultSet rs=conn.createStatement().executeQuery(sql);while(rs.next()) l.add(extract(rs));}catch(SQLException e){}return l;}
    private TransportRoute extract(ResultSet rs) throws SQLException{
        return new TransportRoute(rs.getInt("id"),rs.getString("transport_type"),rs.getString("from_city"),rs.getString("to_city"),rs.getInt("distance_km"),rs.getDouble("fare"),rs.getString("duration"),rs.getString("status"));
    }
}
