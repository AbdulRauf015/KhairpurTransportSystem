package models;
public class Vehicle {
    private int id,capacity; private String transportType,vehicleNumber,model,status;
    public Vehicle(){}
    public Vehicle(int id,String type,String num,String model,int cap,String status){
        this.id=id;this.transportType=type;this.vehicleNumber=num;this.model=model;this.capacity=cap;this.status=status;
    }
    public int getId(){return id;} public void setId(int i){this.id=i;}
    public String getTransportType(){return transportType;} public void setTransportType(String t){this.transportType=t;}
    public String getVehicleNumber(){return vehicleNumber;} public void setVehicleNumber(String v){this.vehicleNumber=v;}
    public String getModel(){return model;} public void setModel(String m){this.model=m;}
    public int getCapacity(){return capacity;} public void setCapacity(int c){this.capacity=c;}
    public String getStatus(){return status;} public void setStatus(String s){this.status=s;}
    @Override public String toString(){return id+" | "+vehicleNumber+" - "+model;}
}
