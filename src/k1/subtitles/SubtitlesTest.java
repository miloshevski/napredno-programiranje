package k1.subtitles;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

class Subtitle{
    private int id;
    private LocalTime from;
    private LocalTime to;
    private String text;

    public Subtitle(int id, String text, String time){
        this.id = id;
        this.text = text;
        String[] times = time.split("-->");
        String [] timeFrom = times[0].trim().split("[:,]");
        String []  timeTo = times[1].trim().split("[:,]");
        this.from = LocalTime.of(Integer.parseInt(timeFrom[0]), Integer.parseInt(timeFrom[1]), Integer.parseInt(timeFrom[2]), 1000000 * Integer.parseInt(timeFrom[3]));
        this.to = LocalTime.of(Integer.parseInt(timeTo[0]), Integer.parseInt(timeTo[1]), Integer.parseInt(timeTo[2]), 1000000 * Integer.parseInt(timeTo[3]));
    }

    public void shift(int ms){
        from = from.plus(ms, ChronoUnit.MILLIS);
        to = to.plus(ms,ChronoUnit.MILLIS);
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
        return String.format("%d%n%s --> %s%n%s%n", id, from.format(dtf), to.format(dtf), text);
    }
}

class Subtitles{
    private final List<Subtitle> list;
    public Subtitles(){
        this.list = new ArrayList<>();
    }
    public int loadSubtitles(InputStream in){
        Scanner sc = new Scanner(in);
        while (sc.hasNext()){
            int id = sc.nextInt();
            sc.nextLine();
            String time = sc.nextLine();
            List<String> strings = new ArrayList<>();

            while (sc.hasNext()){
                String line = sc.nextLine();
                if(line.trim().length() == 0){
                    break;
                }
                strings.add(line);
            }
            list.add(new Subtitle(id,String.join("\n",strings),time));
        }
        return list.size();
    }
    public void shift(int ms){
        list.forEach(s -> s.shift(ms));
    }
    public void print(){
        list.forEach(System.out::println);
    }
}