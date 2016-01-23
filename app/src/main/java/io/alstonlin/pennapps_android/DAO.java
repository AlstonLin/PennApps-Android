package io.alstonlin.pennapps_android;

public class DAO {
    // TODO: Add paths as constants here
    private static DAO instance;
    private DAO(){
        // TODO: Add stuff to establish connections
    }
    public static DAO getInstance(){
        if (instance == null) instance = new DAO();
        return instance;
    }
    // TODO: Add public non-static methods to use as interface
    // TODO: Add private non-static methods to externalize code used for all HTTP actions
}
