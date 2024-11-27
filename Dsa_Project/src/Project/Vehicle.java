package Project;

public class Vehicle {
    String vehicleName;
    int capacity;
    int maxSpeed;

    public Vehicle(String vehicleName, int capacity, int maxSpeed) {
        this.vehicleName = vehicleName;
        this.capacity = capacity;
        this.maxSpeed = maxSpeed;
    }

    public String getVehicleInfo() {
        return "Vehicle: " + vehicleName + ", Capacity: " + capacity + " packages, Max Speed: " + maxSpeed + " km/h";
    }
}
