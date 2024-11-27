package Project;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.*;

public class RouteOptimizer {
    static State[] states = {
            new State("Uttar Pradesh", 200, 4),
            new State("Maharashtra", 1500, 30),
            new State("Karnataka", 1900, 35),
            new State("West Bengal", 1500, 30),
            new State("Tamil Nadu", 2200, 40),
            new State("Gujarat", 1000, 20),
            new State("Rajasthan", 500, 10)
    };

    static List<Vehicle> vehicles = new ArrayList<>();
    static List<Delivery> deliveries = new ArrayList<>();
    static List<String> deliveryLocations = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("===== Delivery Route Optimization Menu =====");
            System.out.println("1. View Available States");
            System.out.println("2. Add Delivery Locations");
            System.out.println("3. Add Vehicles");
            System.out.println("4. Assign Packages to Vehicles");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    displayStates();
                    break;
                case 2:
                    addDeliveryLocations(scanner);
                    break;
                case 3:
                    addVehicles(scanner);
                    break;
                case 4:
                    assignPackagesToVehicles(scanner);
                    break;
                case 5:
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    public static void displayStates() {
        System.out.println("Available States:");
        for (State state : states) {
            System.out.println(state.stateName + " - " + state.distanceFromDelhi + " km from Delhi, Time: " + state.averageTime + " hours");
        }
    }

    public static void addDeliveryLocations(Scanner scanner) {
        System.out.println("Enter the number of locations:");
        int numLocations = scanner.nextInt();

        for (int i = 0; i < numLocations; i++) {
            System.out.print("Enter delivery location name: ");
            String location = scanner.next();
            deliveryLocations.add(location);
            System.out.println("Location added: " + location);
        }

        calculateRoutes();
    }

    public static void addVehicles(Scanner scanner) {
        System.out.println("Enter the number of vehicles:");
        int numVehicles = scanner.nextInt();

        for (int i = 0; i < numVehicles; i++) {
            System.out.print("Enter vehicle name: ");
            String vehicleName = scanner.next();
            System.out.print("Enter vehicle capacity: ");
            int capacity = scanner.nextInt();
            System.out.print("Enter vehicle max speed (km/h): ");
            int maxSpeed = scanner.nextInt();

            Vehicle vehicle = new Vehicle(vehicleName, capacity, maxSpeed);
            vehicles.add(vehicle);
            System.out.println("Vehicle added: " + vehicle.getVehicleInfo());
        }

        calculateRoutes();
    }

    public static void assignPackagesToVehicles(Scanner scanner) {
        if (vehicles.isEmpty()) {
            System.out.println("Error: No vehicles available. Please add vehicles first.");
            return;
        }

        System.out.println("Available Delivery Locations:");
        for (int i = 0; i < deliveryLocations.size(); i++) {
            System.out.println((i + 1) + ". " + deliveryLocations.get(i));
        }
        System.out.print("Enter the location number to assign a package: ");
        int locationIndex = scanner.nextInt() - 1;

        if (locationIndex < 0 || locationIndex >= deliveryLocations.size()) {
            System.out.println("Invalid location number. Please try again.");
            return;
        }

        String location = deliveryLocations.get(locationIndex);

        System.out.println("Available Vehicles:");
        for (int i = 0; i < vehicles.size(); i++) {
            System.out.println((i + 1) + ". " + vehicles.get(i).getVehicleInfo());
        }
        System.out.print("Enter the vehicle number to assign the package: ");
        int vehicleIndex = scanner.nextInt() - 1;

        if (vehicleIndex < 0 || vehicleIndex >= vehicles.size()) {
            System.out.println("Invalid vehicle number. Please try again.");
            return;
        }

        Vehicle selectedVehicle = vehicles.get(vehicleIndex);

        Delivery delivery = new Delivery(location, selectedVehicle);
        deliveries.add(delivery);
        System.out.println("Package assigned to vehicle: " + selectedVehicle.getVehicleInfo());
    }

    public static void calculateRoutes() {
        System.out.println("Automatically calculating optimized routes using Dijkstra's algorithm...");

        int sourceIndex = 0;  // Delhi as the source
        int[] distances = new int[states.length];
        boolean[] visited = new boolean[states.length];

        for (int i = 0; i < states.length; i++) {
            distances[i] = Integer.MAX_VALUE;
            visited[i] = false;
        }
        distances[sourceIndex] = 0;

        for (int i = 0; i < states.length - 1; i++) {
            int u = findMinDistanceNode(distances, visited);
            visited[u] = true;

            for (int v = 0; v < states.length; v++) {
                if (!visited[v] && distances[u] != Integer.MAX_VALUE &&
                        distances[u] + states[v].distanceFromDelhi < distances[v]) {
                    distances[v] = distances[u] + states[v].distanceFromDelhi;
                }
            }
        }

        System.out.println("Optimized Routes (Distance from Delhi and Time):");
        for (int i = 0; i < deliveryLocations.size(); i++) {
            String location = deliveryLocations.get(i);
            // Find the distance and time for each location in the `states` array
            int locationDistance = getLocationDistance(location);
            int locationTime = getLocationTime(location);

            System.out.println(location + ": " + locationDistance + " km, Time: " + locationTime + " hours");
        }
    }

    private static int findMinDistanceNode(int[] distances, boolean[] visited) {
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int i = 0; i < distances.length; i++) {
            if (!visited[i] && distances[i] < min) {
                min = distances[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    private static int getLocationDistance(String location) {
        for (State state : states) {
            if (location.equalsIgnoreCase(state.stateName)) {
                return state.distanceFromDelhi;
            }
        }
        return -1;
    }

    private static int getLocationTime(String location) {
        for (State state : states) {
            if (location.equalsIgnoreCase(state.stateName)) {
                return state.averageTime;
            }
        }
        return -1;
    }
}

