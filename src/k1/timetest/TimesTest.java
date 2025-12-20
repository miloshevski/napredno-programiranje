package k1.timetest;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Scanner;

class TimeTable{
    List<Time> times;

    public void readTimes(InputStream inputStream) throws UnsupportedFormatException, InvalidTimeException {
        Scanner sc = new Scanner(inputStream);
        String[] line = sc.nextLine().split("\\s+");
        for(String s : line){
            if(!checkFormat(s)){
                throw new UnsupportedFormatException(s);
            }
            int h = Integer.parseInt(s.split("[.:]")[0]);
            int m = Integer.parseInt(s.split("[.:]")[1]);

            if(!checkTime(h,m)){
                throw new InvalidTimeException(s);
            }
            times.add(new Time(h,m));
        }
    }
    public void writeTimes(OutputStream outputStream, TimeFormat format){
        PrintWriter writer = new PrintWriter(outputStream);
        Collections.sort(times);
        for(Time t : times){
            if(format == TimeFormat.FORMAT_24){
                writer.println(t);
            }else{
                writer.println(t.toStringAMPM());
            }
        }
    }
    private boolean checkFormat(String s){
        return s.matches("[0-9.:]");
    }
    private boolean checkTime(int h,int m){
        return h >= 0 && h <= 23 && m >= 0 && m <=59;
    }
}

class Time implements Comparable<Time>{
    int hour;
    int minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public Time(String s){
        String [] line = s.split("[.:]");
        this.hour = Integer.parseInt(line[0]);
        this.minute = Integer.parseInt(line[1]);
    }

    @Override
    public int compareTo(Time o) {
        if (hour == o.hour)
            return minute - o.minute;
        else
            return hour - o.hour;
    }
    @Override
    public String toString() {
        return String.format("%2d:%02d", hour, minute);
    }
    public String toStringAMPM() {
        String part = "AM";
        int h = hour;
        if (h == 0) {
            h += 12;
        } else if (h == 12) {
            part = "PM";
        } else if (h > 12) {
            h -= 12;
            part = "PM";
        }
        return String.format("%2d:%02d %s", h, minute, part);
    }
}

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}
class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String msg) {
        super(msg);
    }
}

class InvalidTimeException extends Exception {
    public InvalidTimeException(String msg) {
        super(msg);
    }
}