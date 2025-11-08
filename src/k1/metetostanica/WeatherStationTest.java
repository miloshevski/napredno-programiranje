package k1.metetostanica;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

class Measurement implements Comparable<Measurement>{
    private final float temperature;
    private final float wind;
    private final float humidity;
    private final float visibility;
    private final Date date;

    public Measurement(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s", temperature, wind, humidity, visibility, date.toString());
    }

    @Override
    public int compareTo(Measurement o) {
        if(Math.abs(date.getTime() - o.date.getTime()) < 150000){
            return 0;
        }else{
            return date.compareTo(o.date);
        }
    }
    public Date getDate(){
        return date;
    }
    public float getTemperature(){
        return temperature;
    }

}

class WeatherStation{
    private final int n;
    private final List<Measurement> list;
    public static final long MS = 86400000;

    public WeatherStation(int n) {
        this.n = n;
        list = new ArrayList<Measurement>();
    }

    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date) {
        Measurement m = new Measurement(temperature, wind, humidity, visibility, date);

        // ignore if within 2.5 minutes of any existing measurement
        for (Measurement mea : list) {
            if (mea.compareTo(m) == 0) {   // <-- compare to the NEW measurement!
                return;
            }
        }

        // keep only last n days relative to the NEW measurement
        list.removeIf(i -> m.getDate().getTime() - i.getDate().getTime() > n * MS);

        // finally add the new measurement
        list.add(m);
    }


    public int total(){
        return list.size();
    }

    public void status(Date from, Date to) {
        List<Measurement> newList = list.stream()
                .filter(i -> (i.getDate().after(from) || i.getDate().equals(from)) &&
                        (i.getDate().before(to)  || i.getDate().equals(to)))
                .sorted((a, b) -> a.getDate().compareTo(b.getDate())) // sort ascending
                .collect(Collectors.toList());

        if (newList.isEmpty()) {
            throw new RuntimeException();
        }

        double avg = newList.stream()
                .mapToDouble(Measurement::getTemperature)
                .average()
                .getAsDouble();

        for (Measurement m : newList) {
            System.out.println(m.toString());
        }
        System.out.printf("Average temperature: %.2f", avg);
    }

}