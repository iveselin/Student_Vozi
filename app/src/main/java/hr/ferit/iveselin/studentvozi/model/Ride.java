package hr.ferit.iveselin.studentvozi.model;

import java.util.Calendar;
import java.util.Date;

public class Ride {

    private int numOfPassengers;
    private Calendar timeOfDeparture;
    private RideType rideType;
    private String travelingFrom;
    private String travelingTo;

    public Ride(int numOfPassengers, Calendar timeOfDeparture, RideType rideType, String travelingFrom, String travelingTo) {
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

    public Calendar getTimeOfDeparture() {
        return timeOfDeparture;
    }

    public void setTimeOfDeparture(Calendar timeOfDeparture) {
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

    @Override
    public String toString() {
        return "Ride{" +
                "numOfPassengers=" + numOfPassengers +
                ", timeOfDeparture=" + timeOfDeparture +
                ", rideType=" + rideType +
                ", travelingFrom='" + travelingFrom + '\'' +
                ", travelingTo='" + travelingTo + '\'' +
                '}';
    }
}
