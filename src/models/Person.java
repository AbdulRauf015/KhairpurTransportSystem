package models;

public abstract class Person {
    private int id; private String name; private String contact;
    public Person() {} public Person(int id,String name,String contact){this.id=id;this.name=name;this.contact=contact;}
    public abstract String getRole();
    public int getId(){return id;} public void setId(int id){this.id=id;}
    public String getName(){return name;} public void setName(String n){this.name=n;}
    public String getContact(){return contact;} public void setContact(String c){this.contact=c;}
}
