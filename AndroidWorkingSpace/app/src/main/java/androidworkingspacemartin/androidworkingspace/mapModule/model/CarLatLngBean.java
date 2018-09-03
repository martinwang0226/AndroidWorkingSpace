package androidworkingspacemartin.androidworkingspace.mapModule.model;

/**
 * Created by martinwang on 2018/9/3.
 */

public class CarLatLngBean {

    private double Latitude;
    private double Longitude;

    @Override
    public String toString() {
        return "CarLatLngBean{" +
                "Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                '}';
    }

    public CarLatLngBean(double latitude, double longitude) {
        Latitude  = latitude;
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
