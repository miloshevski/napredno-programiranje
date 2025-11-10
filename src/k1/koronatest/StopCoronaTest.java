//package k1.koronatest;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//interface ILocation{
//    double getLongitude();
//
//    double getLatitude();
//
//    LocalDateTime getTimestamp();
//}
//
//class UserAlreadyExistException extends Exception{
//    public UserAlreadyExistException(String message) {
//        super(message);
//    }
//}
//
//class UserIdAlreadyExistsException extends Exception{
//    public UserIdAlreadyExistsException(String message) {
//        super(message);
//    }
//}
//
//class User {
//    private String name;
//    private String id;
//    private final List<ILocation> locations = new ArrayList<>();
//    private LocalDateTime detectedAt;
//
//    public User(String name, String id) {
//        this.name = name;
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public List<ILocation> getLocations() {
//        return locations;
//    }
//
//    public LocalDateTime getDetectedAt() {
//        return detectedAt;
//    }
//    public void addLocations()
//}
//
//class StopCoronaApp{
//    private final Map<String,User> users = new HashMap<>();
//    private static final double DIST_THRESH = 2.0;       // <= 2 units
//    private static final long TIME_THRESH_SEC = 5 * 60;
//
//    public StopCoronaApp(){
//    }
//
//    public void addUser(String name, String id) throws UserIdAlreadyExistsException {
//        if(map.containsKey(id)){
//            throw new UserIdAlreadyExistsException(String.format("User with id: %d already exists",id));
//        }
//        map.put(id, new User(name,id));
//    }
//
//    public void addLocations(String id, List<ILocation> iLocations){
//        User u = users.get(id);
//        if(u != null && iLocations != null){
//            u.
//        }
//    }
//}
//
//public class StopCoronaTest {
//
//    public static double timeBetweenInSeconds(ILocation location1, ILocation location2) {
//        return Math.abs(Duration.between(location1.getTimestamp(), location2.getTimestamp()).getSeconds());
//    }
//
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//
//        StopCoronaApp stopCoronaApp = new StopCoronaApp();
//
//        while (sc.hasNext()) {
//            String line = sc.nextLine();
//            String[] parts = line.split("\\s+");
//
//            switch (parts[0]) {
//                case "REG": //register
//                    String name = parts[1];
//                    String id = parts[2];
//                    try {
//                        stopCoronaApp.addUser(name, id);
//                    } catch (UserAlreadyExistException e) {
//                        System.out.println(e.getMessage());
//                    }
//                    break;
//                case "LOC": //add locations
//                    id = parts[1];
//                    List<ILocation> locations = new ArrayList<>();
//                    for (int i = 2; i < parts.length; i += 3) {
//                        locations.add(createLocationObject(parts[i], parts[i + 1], parts[i + 2]));
//                    }
//                    stopCoronaApp.addLocations(id, locations);
//
//                    break;
//                case "DET": //detect new cases
//                    id = parts[1];
//                    LocalDateTime timestamp = LocalDateTime.parse(parts[2]);
//                    stopCoronaApp.detectNewCase(id, timestamp);
//
//                    break;
//                case "REP": //print report
//                    stopCoronaApp.createReport();
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//
//    private static ILocation createLocationObject(String lon, String lat, String timestamp) {
//        return new ILocation() {
//            @Override
//            public double getLongitude() {
//                return Double.parseDouble(lon);
//            }
//
//            @Override
//            public double getLatitude() {
//                return Double.parseDouble(lat);
//            }
//
//            @Override
//            public LocalDateTime getTimestamp() {
//                return LocalDateTime.parse(timestamp);
//            }
//        };
//    }
//}
//
