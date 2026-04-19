package controllers;
import database.DBConnection;
import models.*;
import java.sql.*;
import java.util.*;

public class UserController {
    private Connection conn;
    public UserController(){conn=DBConnection.getConnection();}
    public User login(String u,String p){
        try{PreparedStatement ps=conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");ps.setString(1,u);ps.setString(2,p);ResultSet rs=ps.executeQuery();if(rs.next()) return extract(rs);}catch(SQLException e){System.err.println(e.getMessage());}return null;
    }
    public List<User> getAll(){List<User> l=new ArrayList<>();try{ResultSet rs=conn.createStatement().executeQuery("SELECT * FROM users ORDER BY id");while(rs.next()) l.add(extract(rs));}catch(SQLException e){}return l;}
    public boolean add(User u){try{PreparedStatement ps=conn.prepareStatement("INSERT INTO users(username,password,role,full_name,contact) VALUES(?,?,?,?,?)");ps.setString(1,u.getUsername());ps.setString(2,u.getPassword());ps.setString(3,u.getRole());ps.setString(4,u.getFullName());ps.setString(5,u.getContact());return ps.executeUpdate()>0;}catch(SQLException e){return false;}}
    public boolean update(User u){try{PreparedStatement ps=conn.prepareStatement("UPDATE users SET username=?,password=?,role=?,full_name=?,contact=? WHERE id=?");ps.setString(1,u.getUsername());ps.setString(2,u.getPassword());ps.setString(3,u.getRole());ps.setString(4,u.getFullName());ps.setString(5,u.getContact());ps.setInt(6,u.getId());return ps.executeUpdate()>0;}catch(SQLException e){return false;}}
    public boolean delete(int id){try{PreparedStatement ps=conn.prepareStatement("DELETE FROM users WHERE id=?");ps.setInt(1,id);return ps.executeUpdate()>0;}catch(SQLException e){return false;}}
    public boolean exists(String username){try{PreparedStatement ps=conn.prepareStatement("SELECT id FROM users WHERE username=?");ps.setString(1,username);return ps.executeQuery().next();}catch(SQLException e){return false;}}
    public List<User> search(String kw){List<User> l=new ArrayList<>();try{PreparedStatement ps=conn.prepareStatement("SELECT * FROM users WHERE username LIKE ? OR full_name LIKE ?");ps.setString(1,"%"+kw+"%");ps.setString(2,"%"+kw+"%");ResultSet rs=ps.executeQuery();while(rs.next()) l.add(extract(rs));}catch(SQLException e){}return l;}
    private User extract(ResultSet rs) throws SQLException{User u=new User();u.setId(rs.getInt("id"));u.setUsername(rs.getString("username"));u.setPassword(rs.getString("password"));u.setRole(rs.getString("role"));u.setFullName(rs.getString("full_name"));u.setName(rs.getString("full_name"));try{u.setContact(rs.getString("contact"));}catch(Exception ignored){}return u;}
}
