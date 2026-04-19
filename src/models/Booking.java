package models;
public class Booking {
    private int id,routeId,vehicleId,driverId,bookedBy;
    private String bookingNumber,transportType,passengerName,passengerContact;
    private String travelDate,seatNumber,specialNote,status;
    private double fare;
    // Display fields
    private String routeName,vehicleNumber,driverName,bookedByName;
    public Booking(){}
    public int getId(){return id;} public void setId(int i){this.id=i;}
    public String getBookingNumber(){return bookingNumber;} public void setBookingNumber(String b){this.bookingNumber=b;}
    public String getTransportType(){return transportType;} public void setTransportType(String t){this.transportType=t;}
    public String getPassengerName(){return passengerName;} public void setPassengerName(String p){this.passengerName=p;}
    public String getPassengerContact(){return passengerContact;} public void setPassengerContact(String p){this.passengerContact=p;}
    public int getRouteId(){return routeId;} public void setRouteId(int r){this.routeId=r;}
    public int getVehicleId(){return vehicleId;} public void setVehicleId(int v){this.vehicleId=v;}
    public int getDriverId(){return driverId;} public void setDriverId(int d){this.driverId=d;}
    public String getTravelDate(){return travelDate;} public void setTravelDate(String t){this.travelDate=t;}
    public double getFare(){return fare;} public void setFare(double f){this.fare=f;}
    public String getSeatNumber(){return seatNumber;} public void setSeatNumber(String s){this.seatNumber=s;}
    public String getSpecialNote(){return specialNote;} public void setSpecialNote(String s){this.specialNote=s;}
    public String getStatus(){return status;} public void setStatus(String s){this.status=s;}
    public int getBookedBy(){return bookedBy;} public void setBookedBy(int b){this.bookedBy=b;}
    public String getRouteName(){return routeName;} public void setRouteName(String r){this.routeName=r;}
    public String getVehicleNumber(){return vehicleNumber;} public void setVehicleNumber(String v){this.vehicleNumber=v;}
    public String getDriverName(){return driverName;} public void setDriverName(String d){this.driverName=d;}
    public String getBookedByName(){return bookedByName;} public void setBookedByName(String b){this.bookedByName=b;}
    public String getTypeIcon(){
        if(transportType==null) return "🎫";
        switch(transportType){case "Bus":return "🚌";case "Car":return "🚗";case "Bike":return "🏍️";case "Airline":return "✈️";default:return "🎫";}
    }
}
