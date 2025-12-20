package k1.temperaturetests;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

class Temperature{
    private String temp;
    private Character unit;

    public Temperature(String temp) {
        this.temp = temp;
        this.unit = temp.charAt(temp.length()-1);
    }

    public Double celsius(){
        if(unit == 'C'){
            return Double.parseDouble(temp.replace("C",""));
        }
        Double val = Double.parseDouble(temp.replace("F",""));
        return ((val - 32)*5)/9;
    }
    public Double feranhajt(){
        if(unit == 'F'){
            return Double.parseDouble(temp.replace("F",""));
        }
        Double val = Double.parseDouble(temp.replace("C",""));
        return (val * 9) / 5 + 32;
    }
}

class DailyTemperatures{
    private Map<Integer, List<Temperature>> list;

    public DailyTemperatures(){
        list = new TreeMap<>();
    }

    public void readTemperatures(InputStream inputStream){
        Scanner sc = new Scanner(inputStream);
        while (sc.hasNextLine()){
            String[]line = sc.nextLine().split("\\s+");
            int day = Integer.parseInt(line[0]);
            List<Temperature> temps = new ArrayList<>();
            for(int i=1;i<line.length;i++){
                temps.add(new Temperature(line[i]));
            }
            list.put(day,temps);
        }
    }
    public Double minTemp(int day,char c){
        if(c=='F'){
            return list.get(day).stream().mapToDouble(Temperature::feranhajt).min().orElse(0.0);
        }
        return list.get(day).stream().mapToDouble(Temperature::celsius).min().orElse(0.0);
    }
    public Double maxTemp(int day,char c){
        if(c=='F'){
            return list.get(day).stream().mapToDouble(Temperature::feranhajt).max().orElse(0.0);
        }
        return list.get(day).stream().mapToDouble(Temperature::celsius).max().orElse(0.0);
    }
    public Double avgTemp(int day, char c){
        if(c == 'F'){
            return list.get(day).stream().mapToDouble(Temperature::feranhajt).average().orElse(0.0);
        }
        return list.get(day).stream().mapToDouble(Temperature::celsius).average().orElse(0.0);

    }

    public void writeDailyStats(OutputStream outputStream, char scale){
        List<Integer> denovi = new ArrayList<>(list.keySet()).stream().sorted().collect(Collectors.toList());
        denovi.forEach(d -> {
            System.out.printf("%3d: Count: %3d Min: %5.2f%s Max: %5.2f%s Avg: %5.2f%s%n",d,list.get(d).size(),minTemp(d,scale),scale,maxTemp(d,scale),scale,avgTemp(d,scale),scale);
        });
    }
}

public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

// Vashiot kod ovde