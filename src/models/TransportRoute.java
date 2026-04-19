package models;

public class TransportRoute {
    private int id; private String transportType,fromCity,toCity,duration,status;
    private int distanceKm; private double fare;
    public TransportRoute(){}
    public TransportRoute(int id,String type,String from,String to,int dist,double fare,String dur,String status){
        this.id=id;this.transportType=type;this.fromCity=from;this.toCity=to;this.distanceKm=dist;this.fare=fare;this.duration=dur;this.status=status;
    }
    public int getId(){return id;} public void setId(int id){this.id=id;}
    public String getTransportType(){return transportType;} public void setTransportType(String t){this.transportType=t;}
    public String getFromCity(){return fromCity;} public void setFromCity(String f){this.fromCity=f;}
    public String getToCity(){return toCity;} public void setToCity(String t){this.toCity=t;}
    public int getDistanceKm(){return distanceKm;} public void setDistanceKm(int d){this.distanceKm=d;}
    public double getFare(){return fare;} public void setFare(double f){this.fare=f;}
    public String getDuration(){return duration;} public void setDuration(String d){this.duration=d;}
    public String getStatus(){return status;} public void setStatus(String s){this.status=s;}
    public String getFullRoute(){return fromCity+" → "+toCity;}
    @Override public String toString(){return id+" | "+fromCity+" → "+toCity+" (Rs."+String.format("%.0f",fare)+")";}
}
