package io.alstonlin.pennapps_android;

public class Request {

    private String id;
    private String ownerId;
    private String name;
    private String location;
    private double fee;

    public Request(String id, String ownerId, String name, String location, double fee){
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.location = location;
        this.fee = fee;
    }

    public String getId(){
        return id;
    }

    public String getOwnerId(){
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public double getFee() {
        return fee;
    }
}
