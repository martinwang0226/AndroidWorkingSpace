package androidworkingspacemartin.androidworkingspace.mapModule.model;

import java.io.Serializable;

/**
 * Created by martinwang on 2018/9/3.
 */

public class LocationPointEntity implements Serializable {

    private Double lat;//纬度
    private Double lon;//经度
    private String speed;
    private String Bearing;
    private String altitude;
    private String address;
    private String city;

    public LocationPointEntity(Double lat, Double lon, String speed, String bearing, String altitude, String address, String city) {
        this.lat = lat;
        this.lon = lon;
        this.speed = speed;
        Bearing = bearing;
        this.altitude = altitude;
        this.address = address;
        this.city = city;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getBearing() {
        return Bearing;
    }

    public void setBearing(String bearing) {
        Bearing = bearing;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
