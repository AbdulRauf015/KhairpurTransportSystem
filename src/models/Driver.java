package models;
public class Driver extends Person {
    private String licenseNumber,transportType,address,status;
    public Driver(){}
    public Driver(int id,String name,String lic,String contact,String type,String addr,String status){
        super(id,name,contact); this.licenseNumber=lic; this.transportType=type; this.address=addr; this.status=status;
    }
    @Override public String getRole(){return transportType+" Driver";}
    public String getLicenseNumber(){return licenseNumber;} public void setLicenseNumber(String l){this.licenseNumber=l;}
    public String getTransportType(){return transportType;} public void setTransportType(String t){this.transportType=t;}
    public String getAddress(){return address;} public void setAddress(String a){this.address=a;}
    public String getStatus(){return status;} public void setStatus(String s){this.status=s;}
    @Override public String toString(){return id+" | "+getName()+" ("+transportType+")";}
    private int id; public void setDriverId(int i){this.id=i;setId(i);}
}
