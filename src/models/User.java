package models;
public class User extends Person {
    private String username, password, role, fullName;
    public User(){}
    public User(int id,String username,String password,String role,String fullName){
        super(id,fullName,""); this.username=username; this.password=password; this.role=role; this.fullName=fullName;
    }
    @Override public String getRole(){return role;}
    public boolean isAdmin(){return "admin".equalsIgnoreCase(role);}
    public String getUsername(){return username;} public void setUsername(String u){this.username=u;}
    public String getPassword(){return password;} public void setPassword(String p){this.password=p;}
    public void setRole(String r){this.role=r;}
    public String getFullName(){return fullName;} public void setFullName(String f){this.fullName=f;}
    @Override public String toString(){return fullName+" ("+role+")";}
}
