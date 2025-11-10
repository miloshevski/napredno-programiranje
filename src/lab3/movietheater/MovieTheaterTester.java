package lab3.movietheater;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.*;
import java.util.*;

class Movie {
    private String title;
    private String genre;
    private int year;
    private double avgRating;

    public Movie(String title, String genre, int year, double avgRating){
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.avgRating = avgRating;
    }


    @Override
    public String toString() {
        return String.format("%s, %s, %d, %.2f",title,genre,year,avgRating);
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public double getAvgRating() {
        return avgRating;
    }
}

class MovieTheater{
    private List<Movie> list;
    public MovieTheater(){
        list = new ArrayList<>();
    }

    public void readMovies(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        int n = Integer.parseInt(reader.readLine());
        for(int i=0;i<n;i++){
            String title = reader.readLine();
            String genre = reader.readLine();
            int year = Integer.parseInt(reader.readLine());
            String []ratings = reader.readLine().split("\\s+");
            double d = Arrays.stream(ratings).mapToDouble(Double::parseDouble).average().orElse(0);
            Movie m = new Movie(title,genre,year, d);
            list.add(m);
        }

    }
    public void printByGenreAndTitle(){
        list.stream().sorted(Comparator.comparing(Movie::getGenre).thenComparing(Movie::getTitle)).forEach(System.out::println);
    }
    public void printByYearAndTitle(){
        list.stream().sorted(Comparator.comparing(Movie::getYear).thenComparing(Movie::getTitle)).forEach(System.out::println);
    }
    public void printByRatingAndTitle(){
        list.stream().sorted(Comparator.comparingDouble(Movie::getAvgRating).reversed().thenComparing(Movie::getTitle)).forEach(System.out::println);
    }
}

public class MovieTheaterTester {
    public static void main(String[] args) {
        MovieTheater mt = new MovieTheater();
        try {
            mt.readMovies(System.in);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("SORTING BY RATING");
        mt.printByRatingAndTitle();
        System.out.println("\nSORTING BY GENRE");
        mt.printByGenreAndTitle();
        System.out.println("\nSORTING BY YEAR");
        mt.printByYearAndTitle();
    }
}