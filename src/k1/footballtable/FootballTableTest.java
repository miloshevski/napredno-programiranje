package k1.footballtable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Team implements Comparable<Team>{
    private String name;
    private int played;
    private int won;
    private int draw;
    private int  lost;
    private int points;
    private int dadeni;
    private int primeni;


    public void update(int plus,int minus){
        played++;
        won += plus > minus ? 1 : 0;
        draw += plus == minus ? 1 : 0;
        lost += plus < minus ? 1 : 0;
        dadeni+=plus;
        primeni+=minus;
        points = won * 3 + draw;
    }

    public Team(String name) {
        this.name = name;
        dadeni = 0;
        primeni = 0;
    }

    public int getPoints(){
        return points;
    }
    public int getDiff(){
        return  dadeni - primeni;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d", name, played, won, draw, lost, getPoints());
    }


    @Override
    public int compareTo(Team o) {
        return Comparator
                .comparingInt(Team::getPoints).reversed()
                .thenComparingInt(Team::getDiff).reversed()
                .thenComparing(Team::getName)
                .compare(this, o);
    }

}

class FootballTable{
    private Map<String,Team> teams;
    public FootballTable(){
        this.teams = new HashMap<>();
    }
    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals){
        if(teams.containsKey(homeTeam)){
            teams.get(homeTeam).update(homeGoals,awayGoals);
        }else{
            Team t = new Team(homeTeam);
            t.update(homeGoals,awayGoals);
            teams.put(homeTeam,t);
        }
        if(teams.containsKey(awayTeam)){
            teams.get(awayTeam).update(awayGoals,homeGoals);
        }else {
            Team tt = new Team(awayTeam);
            tt.update(awayGoals,homeGoals);
            teams.put(awayTeam,tt);
        }
    }
    public void printTable(){
        List<Team> list = teams.values().stream().sorted(Comparator.comparing(Team::getPoints).thenComparing(Team::getDiff).reversed().thenComparing(Team::getName)).collect(Collectors.toList());
        list.forEach(i -> System.out.printf("%2d. %s%n", list.indexOf(i) + 1, i.toString()));

    }
}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here


