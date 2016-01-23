package io.alstonlin.pennapps_android;

public class Request {
    private String name;
    private String location;
    private double fee;
    public Request(String name, String location, double fee){
        this.name = name;
        this.location = location;
        this.fee = fee;
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
