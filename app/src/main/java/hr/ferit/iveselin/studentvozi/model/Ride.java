package hr.ferit.iveselin.studentvozi.model;

import java.util.Date;

public class Ride {

    private int numOfPassengers;
    private Date timeOfDeparture;
    private RideType rideType;
    private String travelingFrom;
    private String travelingTo;

    public Ride(int numOfPassengers, Date timeOfDeparture, RideType rideType, String travelingFrom, String travelingTo) {
        this.numOfPassengers = numOfPassengers;
        this.timeOfDeparture = timeOfDeparture;
        this.rideType = rideType;
        this.travelingFrom = travelingFrom;
        this.travelingTo = travelingTo;
    }

    public int getNumOfPassengers() {
        return numOfPassengers;
    }

    public void setNumOfPassengers(int numOfPassengers) {
        this.numOfPassengers = numOfPassengers;
    }

    public Date getTimeOfDeparture() {
        return timeOfDeparture;
    }

    public void setTimeOfDeparture(Date timeOfDeparture) {
        this.timeOfDeparture = timeOfDeparture;
    }

    public RideType getRideType() {
        return rideType;
    }

    public void setRideType(RideType rideType) {
        this.rideType = rideType;
    }

    public String getTravelingFrom() {
        return travelingFrom;
    }

    public void setTravelingFrom(String travelingFrom) {
        this.travelingFrom = travelingFrom;
    }

    public String getTravelingTo() {
        return travelingTo;
    }

    public void setTravelingTo(String travelingTo) {
        this.travelingTo = travelingTo;
    }
}
