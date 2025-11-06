package k1.timetest;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class TimeTable{
    private final List<String> list;

    public TimeTable(){
        this.list = new ArrayList<>();
    }

    public void readTimes(InputStream inputStream) throws UnsupportedFormatException, InvalidTimeException {
        Scanner sc = new Scanner(inputStream);
        String[] split;

        while(sc.hasNextLine()){
            split=sc.nextLine().split("\\s+");

            for(String s : split){
                if(!isValidFormat(s)){
                    throw new UnsupportedFormatException(s);
                }
                if(!isValidTime(s)){
                    throw new InvalidTimeException(s);
                }
                list.add(s);
            }
        }
    }
    public void writeTimes(OutputStream outputStream, TimeFormat format){
        PrintWriter pw = new PrintWriter(outputStream);

        if(format == TimeFormat.FORMAT_24){
            list.stream().sorted(Comparator.comparing(TimeTable::getTime)).forEach(i -> pw.printf("%5s%n",i));
        }else{
            list.stream().sorted(Comparator.comparing(TimeTable::getTime)).forEach(i -> pw.printf("%8s%n",toAMPM(i)));
        }
    }
    public static String toAMPM(String s){
        if(s.matches("0:[0-5][0-9]")){
            return s.replace("0:","12") + " AM";
        }else if(s.matches("([1-9]|1[01]):[0-5][0-9]")){
            return s + " AM";
        }else if(s.matches("12:[0-5][0-9]")){
            return s + " PM";
        }else{
            return String.format("%d:%s PM", Integer.parseInt(s.substring(0, 2)) - 12, s.substring(3, 5));
        }
    }
    private boolean isValidFormat(String str) {
        return str.matches("\\d+[:.]\\d+");
    }
    private boolean isValidTime(String str){
        return str.matches("([0-9]|1[0-9]|2[0-3])[:.][0-5][0-9]");
    }
    public static int getTime(String str){
        return Integer.parseInt(str.split(":")[0]) * 60 + Integer.parseInt(str.split(":")[1]);
    }
}
class UnsupportedFormatException extends Exception{
    String s;
    public UnsupportedFormatException(String s){
        this.s = s;
    }

    @Override
    public String getMessage() {
        return String.format("%s has unsupported format",s);
    }
}

class InvalidTimeException extends Exception{
    String t;

    public InvalidTimeException(String t) {
        this.t = t;
    }

    @Override
    public String getMessage() {
        return String.format("%s is invalid time");
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