package k1.avioni;

import java.util.*;

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

class Airport{
    private String name;
    private String country;
    private String code;
    private int passengers;
    private List<Flight> poletuvacki;
    private List<Flight> sletuvacki;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        this.poletuvacki = new ArrayList<>();
        this.sletuvacki = new ArrayList<>();
    }

    public void addPoletuvacki(Flight f){
        poletuvacki.add(f);
    }
    public void addSletuvacki(Flight s){
        sletuvacki.add(s);
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

    public List<Flight> getPoletuvacki() {
        return poletuvacki;
    }

    public List<Flight> getSletuvacki() {
        return sletuvacki;
    }
    @Override
    public String toString() {
        return String.format("%s (%s)%n%s%n%d", name, code, country, passengers);
    }
}

class Flight implements Comparable<Flight>{
    private String from;
    private String to;
    private int time;
    private int duration;
    private Airport fromAirport;
    private Airport toAirport;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
        this.fromAirport = Airports.getAirportFromCode(from);
        this.toAirport = Airports.getAirportFromCode(to);
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
    public int compareTo(Flight o) {
        return Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime).thenComparing(Flight::getFrom).compare(this,o);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flight)) return false;
        Flight other = (Flight) o;
        return Objects.equals(from, other.from)
                && Objects.equals(to, other.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        int end = time + duration;

        return String.format("%s-%s %02d:%02d-%02d:%02d %s%dh%02dm", from, to, time / 60, time % 60, (end / 60) % 24, end % 60, (end / 60) / 24 > 0 ? "+1d " : "", duration / 60, duration % 60);
    }
}

class Airports{
    private static List<Airport> list;

    public Airports(){
        this.list = new ArrayList<>();
    }

    public void addAirport(String name, String country, String code, int passengers){
        list.add(new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration){
        Flight f = new Flight(from, to, time, duration);

        list.stream().filter(a -> a.getCode().equals(from)).forEach(a -> a.addPoletuvacki(f));
        list.stream().filter(a -> a.getCode().equals(to)).forEach(a -> a.addSletuvacki(f));
    }

    public static Airport getAirportFromCode(String code){
        Airport target = list.stream().filter(a -> a.getCode().equals(code)).findFirst().orElse(null);
        return target;
    }
    public void showFlightsFromAirport(String code){
        Airport target = getAirportFromCode(code);
        if (target == null) return;

        System.out.println(target); // toString ти е OK

        // групирај по дестинација, TreeMap ги реди клучевите лексикографски
        Map<String, List<Flight>> byDest = new TreeMap<>();
        for (Flight f : target.getPoletuvacki()) {
            byDest.computeIfAbsent(f.getTo(), k -> new ArrayList<>()).add(f);
        }

        int idx = 1;
        for (Map.Entry<String, List<Flight>> e : byDest.entrySet()) {
            List<Flight> flights = e.getValue();

            // печати по растечки time без sort(): вади го секогаш минимумот
            while (!flights.isEmpty()) {
                int minPos = 0;
                for (int i = 1; i < flights.size(); i++) {
                    if (flights.get(i).getTime() < flights.get(minPos).getTime()) {
                        minPos = i;
                    }
                }
                Flight min = flights.remove(minPos);
                System.out.printf("%d. %s%n", idx++, min.toString());
            }
        }
    }


    public void showDirectFlightsFromTo(String from, String to){
        Airport fromAirport = getAirportFromCode(from);
        if (fromAirport == null){
            System.out.println(String.format("No flights from %s to %s",from,to));
        }

        List<Flight> direct = new ArrayList<>();
        for (Flight f : fromAirport.getPoletuvacki()) {
            if (f.getFrom().equals(from) && f.getTo().equals(to)) {
                direct.add(f);
            }
        }
        boolean flag = false;
        while (!direct.isEmpty()) {
            flag = true;
            int minPos = 0;
            for (int i = 1; i < direct.size(); i++) {
                if (direct.get(i).getTime() < direct.get(minPos).getTime()) {
                    minPos = i;
                }
            }
            Flight min = direct.remove(minPos);
            System.out.println(min.toString());
        }
        if(!flag){
            System.out.println(String.format("No flights from %s to %s",from,to));
        }
    }

    public void showDirectFlightsTo(String to){
        Airport target = getAirportFromCode(to);
        if (target == null) return;

        List<Flight> incoming = new ArrayList<>(target.getSletuvacki());

        while (!incoming.isEmpty()) {
            int minPos = 0;
            for (int i = 1; i < incoming.size(); i++) {
                Flight a = incoming.get(i);
                Flight b = incoming.get(minPos);
                if (a.getTime() < b.getTime()
                        || (a.getTime() == b.getTime()
                        && a.getFrom().compareTo(b.getFrom()) < 0)) {
                    minPos = i;
                }
            }
            Flight min = incoming.remove(minPos);
            System.out.println(min.toString());
        }

    }

}

