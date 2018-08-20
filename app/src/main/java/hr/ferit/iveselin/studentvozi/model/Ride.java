package hr.ferit.iveselin.studentvozi.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Ride {

    private int numOfPassengers;
    private long timeOfDeparture;
    private RideType rideType;
    private String travelingFrom;
    private String travelingTo;
    private String ownerId;
    private List<String> singedUpUsersId;


    public Ride() {
    }

    public Ride(int numOfPassengers, long timeOfDeparture, RideType rideType, String travelingFrom, String travelingTo, String ownerId) {
        this.numOfPassengers = numOfPassengers;
        this.timeOfDeparture = timeOfDeparture;
        this.rideType = rideType;
        this.travelingFrom = travelingFrom;
        this.travelingTo = travelingTo;
        this.ownerId = ownerId;
    }

    public int getNumOfPassengers() {
        return numOfPassengers;
    }

    public void setNumOfPassengers(int numOfPassengers) {
        this.numOfPassengers = numOfPassengers;
    }

    public long getTimeOfDeparture() {
        return timeOfDeparture;
    }

    public void setTimeOfDeparture(long timeOfDeparture) {
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<String> getSingedUpUsersId() {
        return singedUpUsersId;
    }

    public void addSignedUpUsersId(String userIdToAdd) {
        this.singedUpUsersId.add(userIdToAdd);
    }

    @Override
    public String toString() {
        return "Ride{" +
                "numOfPassengers=" + numOfPassengers +
                ", timeOfDeparture=" + timeOfDeparture +
                ", rideType=" + rideType +
                ", travelingFrom='" + travelingFrom + '\'' +
                ", travelingTo='" + travelingTo + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", singedUpUsersId=" + singedUpUsersId +
                '}';
    }
}
