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



/* AI REVIEW COMMENTS
 * # Review of RouteOptimizer.java
 * 
 * ## 1. COMPLEXITY ANALYSIS
 * 
 * ### Time Complexity
 * 
 * - **Main Loop (menu operations)**: The loop runs until the user chooses to exit. Regardless of the number of vehicles or delivery locations, this part runs in O(n) where n is the number of times the menu is presented to the user.
 * 
 * - **`displayStates()`**: This function iterates through the array of states (fixed size of 7). The time complexity is O(7), which simplifies to O(1) since this is constant.
 * 
 * - **`addDeliveryLocations()`**:
 *   - Reading the number of locations is O(1).
 *   - For each location entry, adding to the list takes O(1) per addition, resulting in O(numLocations).
 * 
 * - **`addVehicles()`**:
 *   - Similarly, reading vehicle details is O(1).
 *   - For each vehicle entry, adding to the list also takes O(1) per addition, leading to O(numVehicles).
 * 
 * - **`assignPackagesToVehicles()`**:
 *   - Validating vehicles and locations involves O(1) for checking the sizes and printing.
 *   - Finding the vehicle and location details involves iterating through the lists, which takes O(numVehicles + numLocations) in the worst case.
 * 
 * - **`calculateRoutes()`**:
 *   - This method implements Dijkstra's algorithm which has a time complexity of O(V^2) for a graph with V vertices (here, the states).
 *   - Finding the minimum distance node in this case runs in O(V) for each node, and this happens for all V states.
 * 
 * - **`getLocationDistance()` and `getLocationTime()`**:
 *   - Both methods iterate through the states to find the corresponding state for a location. The time complexity is O(V) for each call.
 * 
 * ### Total Time Complexity
 * Accumulating all above, we find:
 * - For `addDeliveryLocations()`: O(numLocations)
 * - For `addVehicles()`: O(numVehicles)
 * - For `assignPackagesToVehicles()`: O(numVehicles + numLocations)
 * - For `calculateRoutes()`: O(V^2)
 * - Each state-query method runs in O(V).
 * 
 * Thus, the overall complexity can be approximated to:
 * \[ O(V^2 + numLocations + numVehicles) \]
 * 
 * ### Space Complexity
 * 
 * - **Static Arrays**: The `states` array takes O(V) space, where V = number of states (fixed at 7).
 * - **List Sizes**: Each of the `vehicles`, `deliveries`, and `deliveryLocations` lists grows dynamically based on user input, thus it can take O(numVehicles + numDeliveries + numLocations).
 * - **Dijkstra-specific Arrays**: The `distances` and `visited` arrays both require O(V) space.
 * 
 * ### Total Space Complexity
 * Assuming deposits from user inputs do not exceed a proportional volume compared to states:
 * \[ O(V + numVehicles + numDeliveries + numLocations) \]
 * 
 * ## 2. OPTIMIZATION OPPORTUNITIES
 * 
 * ### Identified Sections to Optimize
 * 1. **Dijkstra's Algorithm**: The current implementation uses a simple 2D array, making it O(V^2). We can optimize this by using a priority queue (min heap) to reduce the time complexity to O(E log V), where E is the number of edges.
 *   
 * 2. **State Lookup** in `getLocationDistance()` and `getLocationTime()`: These methods iterate over states, leading to O(V) complexity. We can use a HashMap for O(1) average-case lookups.
 * 
 * ### Optimized Implementation
 * Below is an optimized version of `calculateRoutes()` using a priority queue along with HashMaps for state lookups.
 * 
 * ```java
 * import java.util.*;
 * 
 * public static void calculateRoutes() {
 *     System.out.println("Automatically calculating optimized routes using Dijkstra's algorithm...");
 * 
 *     int sourceIndex = 0;  // Delhi as the source
 *     Map<String, State> stateMap = new HashMap<>();
 *     
 *     for (State state : states) {
 *         stateMap.put(state.stateName.toLowerCase(), state); // Using lowercase for case-insensitive search.
 *     }
 * 
 *     PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1])); // {index, distance}
 *     pq.add(new int[]{sourceIndex, 0});
 *     Set<Integer> visited = new HashSet<>();
 * 
 *     while (!pq.isEmpty()) {
 *         int[] node = pq.poll();
 *         int u = node[0], currentDistance = node[1];
 * 
 *         if (visited.contains(u)) continue;
 *         visited.add(u);
 * 
 *         for (int v = 0; v < states.length; v++) {
 *             // This simulates the edge weights (distances)
 *             int distance = states[v].distanceFromDelhi;
 *             if (!visited.contains(v) && currentDistance + distance < distances[v]) {
 *                 distances[v] = currentDistance + distance;
 *                 pq.add(new int[]{v, distances[v]});
 *             }
 *         }
 *     }
 * 
 *     System.out.println("Optimized Routes (Distance from Delhi and Time):");
 *     for (String location : deliveryLocations) {
 *         State state = stateMap.get(location.toLowerCase());
 *         if (state != null) {
 *             System.out.println(state.stateName + ": " + state.distanceFromDelhi + " km, Time: " + state.averageTime + " hours");
 *         }
 *     }
 * }
 * ```
 * 
 * ## 3. ALGORITHMIC INSIGHTS
 * 
 * ### Techniques/Patterns
 * - **Dijkstra's Algorithm**: A well-known graph algorithm for finding the shortest paths between nodes.
 * - **Static Arrays vs. Dynamic Lists**: The code optimally uses lists for variable-sized data while employing static arrays for fixed-size data.
 *   
 * ### Alternative Approaches
 * 1. **A* Algorithm**: Depending on the nature of the distance from Delhi, A* could yield faster results, especially when a heuristic is available (like straight-line distance).
 *    - Complexity: Similar to Dijkstra's but may outperform in practice with a good heuristic.
 *   
 * 2. **Floyd-Warshall Algorithm**: This could be used for the all-pairs shortest path but comes with a higher O(V^3) cost.
 * 
 * ### Trade-Off Comparison
 * - Dijkstra's algorithm with a priority queue is more efficient than the basic implementation for larger graphs.
 * - A* can outperform Dijkstra’s approach with heuristics, but requires more setup.
 *   
 * ## 4. EDGE CASES & TESTING
 * 
 * ### Edge Cases Identified
 * 1. **Zero Vehicles or Locations**: What happens if the user does not add any vehicles or locations and then tries to assign packages?
 * 2. **Invalid Input Handling**: Handling for non-integer inputs when expecting integers should be put in place.
 * 3. **Maximum Capacity for Vehicles**: What if all vehicles are at full capacity, yet many deliveries are queued?
 * 
 * ### Suggested Test Cases
 * 1. **Base Cases**: No vehicles or locations added.
 * 2. **Single Location and Single Vehicle**: Ensure all functions still perform correctly with minimal data.
 * 3. **Extreme Values**: Large numbers of vehicles and delivery locations, testing upper boundaries of assumptions about list sizes not exceeding expected limits.
 * 4. **Validation Outputs**: Check outputs of calculated routes for accuracy against known inputs.
 * 
 * In conclusion, the `RouteOptimizer` can benefit from various optimizations, chiefly through algorithm enhancement and better memory management, without fundamentally altering the original design principles.
 */

// --- Auto Review (DSA) ---
// DSA Review (Linked List)
// Complexity hints: has nested loops (likely O(n^2) or worse)
// Easy: Detect and remove cycle (Floyd) and find cycle start.
// Medium: Merge two sorted lists; iterative vs. recursive tradeoffs.
// Hard: LRU cache with list+hash; operations and complexity.
