package k1.f1;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.SyncFailedException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class Pilot{
    private String name;
    private LocalTime[] laps;

    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("m:ss:SSS");


    public Pilot(String info){
        String[] line = info.trim().split("\\s+");
        this.name = line[0];
        this.laps = new LocalTime[3];
        this.laps[0] = parseTime(line[1]);
        this.laps[1] = parseTime(line[2]);
        this.laps[2] = parseTime(line[3]);
    }

    private LocalTime parseTime(String t){
        String[] p = t.split(":");
        int mm = Integer.parseInt(p[0]);
        int ss = Integer.parseInt(p[1]);
        int ms = Integer.parseInt(p[2]);
        int nanos = ms * 1_000_000;
        return LocalTime.of(0,mm,ss,nanos);
    }

    public LocalTime bestLap(){
        return Arrays.stream(laps).min(LocalTime::compareTo).orElse(laps[0]);
    }

    public String bestLapFormatted() {
        return bestLap().format(OUT); // "mm:ss:SSS" → 01:55:523
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s%n", getName(), bestLapFormatted());

    }

    public String getName() {
        return name;
    }
}

class F1Race {
    private final List<Pilot> list;

    public F1Race() {
        list = new ArrayList<>();
    }

    public void readResults(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);
        while (sc.hasNextLine()) {
            String info = sc.nextLine().trim();
            if (info.isEmpty()) continue;
            String[] parts = info.split("\\s+");
            if (parts.length >= 4) { // name + 3 laps
                list.add(new Pilot(info));
            }
        }
        // sc.close(); // не мора за System.in
    }

    public void printSorted(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        List<Pilot> sorted = list.stream()
                .sorted(Comparator.comparing(Pilot::bestLap))
                .collect(Collectors.toList());

        for (int i = 0; i < sorted.size(); i++) {
            Pilot p = sorted.get(i);
            pw.printf("%d. %-10s%10s%n",
                    i + 1,
                    p.getName(),
                    p.bestLapFormatted()
            );
        }
        pw.flush();
    }

}
