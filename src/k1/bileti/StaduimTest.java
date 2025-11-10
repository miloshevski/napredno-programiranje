package k1.bileti;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Sector{
    private String code;
    private int seats;
    private final Map<Integer, Boolean> map;
    private int type;

    public Sector(String code, int seat){
        this.code = code;
        this.seats = seat;
        this.map = new HashMap<>();
        this.type = 0;
    }

    public Map<Integer, Boolean> getMap(){
        return map;
    }
    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", code, seats - map.size(), seats, ((double) map.size() / seats) * 100.0);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFreeSeats() {
        return seats - map.size();
    }

    public String getCode() {
        return code;
    }
}

class Stadium{
    private final String name;
    private final Map<String, Sector> map;

    public Stadium(String name){
        this.name = name;
        this.map = new HashMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sectorSizes){
        for(int i=0;i<sectorNames.length;i++){
            map.put(sectorNames[i],new Sector(sectorNames[i],sectorSizes[i]));
        }
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        Map<Integer, Boolean> seats = map.get(sectorName).getMap();

        if(seats.containsKey(seat)){
            throw new SeatTakenException("Taken");
        }

        int sectorType = map.get(sectorName).getType();

        if((type==1 && sectorType==2) || (type==2 && sectorType == 1)){
            throw new SeatNotAllowedException("Not allowed");
        }
        if(type != 0 && sectorType == 0){
            map.get(sectorName).setType(type);
        }
        seats.put(seat,true);
    }
    public void showSectors(){
        map.values().stream().sorted(Comparator.comparing(Sector::getFreeSeats).reversed().thenComparing(Sector::getCode)).forEach(System.out::println);
    }
}


class SeatNotAllowedException extends Exception{
    public SeatNotAllowedException(String message) {
        super(message);
    }
}

class SeatTakenException extends Exception{
    public SeatTakenException(String message) {
        super(message);
    }
}

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
