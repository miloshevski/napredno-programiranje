package airports;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class Airport{
    private String name;
    private String country;
    private String code;
    private int passengers;
    private List<Flight> flights;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        flights = new ArrayList<>();
    }
    public void addFlight(Flight f){
        flights.add(f);
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    public int getPassengers() {
        return passengers;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)",name,code);
    }
}

class Flight{
    private String id;
    private String from;
    private String to;
    private int time;
    private int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;

    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return Airports.formatFlight(this);
    }
}

class Airports{
    private Map<String, Airport> airportMap;
    private Map<String,List<Flight>> fromFlights;
    private Map<String,List<Flight>> toFlights;
    public Airports(){
        airportMap = new HashMap<>();
        fromFlights = new HashMap<>();
        toFlights = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers){
        airportMap.put(code,new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration){
        Flight f = new Flight(from, to, time, duration);
        fromFlights.computeIfAbsent(from,k -> new ArrayList<>()).add(f);
        toFlights.computeIfAbsent(to, k -> new ArrayList<>()).add(f);
        airportMap.get(from).addFlight(f);
    }
    public static String formatFlight(Flight f) {
        int start = f.getTime();
        int duration = f.getDuration();
        int end = start + duration;

        int startHour = start / 60;
        int startMin = start % 60;

        int endHour = (end / 60) % 24;
        int endMin = end % 60;

        boolean nextDay = end >= 24 * 60;

        String timeFormat = String.format("%02d:%02d-%02d:%02d", startHour, startMin, endHour, endMin);
        if (nextDay) {
            timeFormat += " +1d";
        }

        String durationFormat = String.format("%dh%02dm", duration / 60, duration % 60);

        return String.format("%s-%s %s %s", f.getFrom(), f.getTo(), timeFormat, durationFormat);
    }


    public void showFlightsFromAirport(String code) {
        Airport airport = airportMap.get(code);
        System.out.printf("%s (%s)%n%s%n%d%n", airport.getName(), airport.getCode(), airport.getCountry(), airport.getPassengers());

        List<Flight> flights = fromFlights.getOrDefault(code, new ArrayList<>());

        // Сортирање без sort()
        List<Flight> sortedFlights = new ArrayList<>();
        for (Flight f : flights) {
            int i = 0;
            while (i < sortedFlights.size()) {
                Flight other = sortedFlights.get(i);
                int cmp = f.getTo().compareTo(other.getTo());
                if (cmp < 0 || (cmp == 0 && f.getTime() < other.getTime())) {
                    break;
                }
                i++;
            }
            sortedFlights.add(i, f);
        }

        int count = 1;
        for (Flight f : sortedFlights) {
            String output = formatFlight(f);
            System.out.printf("%d. %s%n", count++, output);
        }
    }



    public void showDirectFlightsFromTo(String from, String to){
        List<Flight> flights =  fromFlights.get(from).stream().filter(f ->f.getTo().equals(to)).collect(Collectors.toList());
        if(flights.isEmpty()){
            System.out.printf("No flights from %s to %s%n",from,to);
        }else{
            flights.forEach(System.out::println);
        }
    }

    public void showDirectFlightsTo(String to) {
        List<Flight> flightsTo = new ArrayList<>();

        // Собери ги сите летови што слетуваат на дадениот аеродром
        for (List<Flight> list : fromFlights.values()) {
            for (Flight f : list) {
                if (f.getTo().equals(to)) {
                    // Вметнување сортирано по време
                    int i = 0;
                    while (i < flightsTo.size() && flightsTo.get(i).getTime() < f.getTime()) {
                        i++;
                    }
                    flightsTo.add(i, f);
                }
            }
        }

        if (flightsTo.isEmpty()) {
            System.out.printf("No flights to %s%n", to);
            return;
        }

        int count = 1;
        for (Flight f : flightsTo) {
            System.out.printf("%d. %s%n", count++, Airports.formatFlight(f));
        }
    }

}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde


