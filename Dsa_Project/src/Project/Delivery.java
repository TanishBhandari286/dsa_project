package Project;

import java.time.LocalDateTime;

public class Delivery {
    private String location;
    private Vehicle vehicle;

    public Delivery(String location, Vehicle vehicle) {
        this.location = location;
        this.vehicle = vehicle;
    }

    public String getLocation() {
        return location;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getDeliveryDetails() {
        return "Delivery Location: " + location + ", Vehicle: " + vehicle.getVehicleInfo();
    }
}
