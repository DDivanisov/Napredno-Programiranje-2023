//half done!
import java.util.*;
import java.util.stream.Collectors;

class Film{
    private String title;
    private int[] ratings;
    private double rating;
    private double coefrating;

    public Film(String title, int[] ratings) {
        this.title = title;
        this.rating = 0.0;
        this.coefrating = 0.0;
        this.ratings = ratings;
    }
    public void calcRating(){
        rating = Arrays.stream(ratings).average().orElse(0.0);
    }
    public void calCoefRating(int n){
        coefrating = rating * (ratings.length / n * 1.0);
    }
    public double getRating(){
        return rating;
    }

    public double getCoefrating() {
        return coefrating;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return String.format("%s (%1.2f) of %d ratings",title,rating,ratings.length);
    }
}
class MoviesList{
    private List<Film>movies;
    private int n;

    public MoviesList() {
        movies = new ArrayList<>();
        n = 0;
    }

    public void addMovie(String title, int[] ratings) {
        Film movie = new Film(title, ratings);
        movie.calcRating();
        n = n + ratings.length;
        movies.add(movie);
    }

    public List<Film> top10ByAvgRating() {
        Comparator<Film>comparator = Comparator.comparing(Film::getRating).thenComparing(Film::getTitle).reversed();
        return movies.stream().sorted(comparator).limit(10).collect(Collectors.toList());
         
    }

    public List<Film> top10ByRatingCoef() {
        Comparator<Film>comparator = Comparator.comparing(Film::getCoefrating).thenComparing(Film::getTitle).reversed();
        movies.forEach(film -> film.calCoefRating(n));

        return movies.stream().sorted(comparator).limit(10).collect(Collectors.toList());
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
        List<Film> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Film movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Film movie : movies) {
            System.out.println(movie);
        }
    }
}

