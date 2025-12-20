package k1.movies;

import java.util.*;
import java.util.stream.Collectors;

class Movie{
    private final String title;
    private final int[] ratings;
    static int maks = 0;
    public Movie(String title, int[] ratings) {
        this.title = title;
        this.ratings = ratings;
    }

    public String getTitle() {
        return title;
    }
    public int numRatings(){
        return ratings.length;
    }
    public int[] getRatings() {
        return ratings;
    }
    public double getAvgRating(){
        return Arrays.stream(ratings).mapToDouble(r -> (double) r).average().orElse(0.0);
    }
    public double getRatingCoef(){
        return getAvgRating() * (double) ratings.length / maks;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings",title,getAvgRating(),numRatings());
    }
}

class MoviesList{
    private List<Movie> movies;
    public MoviesList() {
        this.movies = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings){
        movies.add(new Movie(title,ratings));
        if(ratings.length > Movie.maks){
            Movie.maks = ratings.length;
        }
    }

    public List<Movie> top10ByAvgRating(){
        return movies.stream().sorted(Comparator.comparing(Movie::getAvgRating).reversed().thenComparing(Movie::getTitle)).limit(10).collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef(){
        return movies.stream().sorted(Comparator.comparing(Movie::getRatingCoef).reversed().thenComparing(Movie::getTitle)).limit(10).collect(Collectors.toList());
    }
}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde