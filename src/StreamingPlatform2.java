import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.security.KeyPair;
import java.util.*;
import java.util.stream.Collectors;

class CosineSimilarityCalculator {

    public static double cosineSimilarity(Map<String, Integer> c1, Map<String, Integer> c2) {
        return cosineSimilarity(c1.values(), c2.values());
    }

    public static double cosineSimilarity(Collection<Integer> c1, Collection<Integer> c2) {
        int[] array1;
        int[] array2;
        array1 = c1.stream().mapToInt(i -> i).toArray();
        array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1 = 0, down2 = 0;

        for (int i = 0; i < c1.size(); i++) {
            up += (array1[i] * array2[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down1 += (array1[i] * array1[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down2 += (array2[i] * array2[i]);
        }

        return up / (Math.sqrt(down1) * Math.sqrt(down2));
    }
}
class Movie{
    private String id;
    private String name;
    private List<Integer>ratings;
    private Double rating;

    public Movie(String id, String name) {
        this.id = id;
        this.name = name;
        this.ratings = new ArrayList<>();
        this.rating = 0.0;
    }
    public void addRating(int rating){
        ratings.add(rating);
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getRatings() {
        return ratings;
    }
    public void calcRating(){
        rating = ratings.stream().mapToDouble(r -> (double) r).average().orElse(0.0);
    }
    public Double getRating() {
        calcRating();
        return rating;
    }

    @Override
    public String toString() {
        return String.format("Movie ID: %s Title: %s Rating: %1.2f",id,name,rating);
    }
}
class User{
    private String id;
    private String username;
    private Map<String, Integer>ratedMovie;
    private List<PairM> moviesrated;

    public User(String id, String name) {
        ratedMovie = new TreeMap<>();
        moviesrated = new ArrayList<>();
        this.id = id;
        this.username = name;
    }
    public void rateMovie(int rating, String movieId){
        if(ratedMovie.containsKey(movieId))
            ratedMovie.remove(movieId);
        ratedMovie.put(movieId, rating);
    }
    public void makemovieRating(int rating, Movie movie){
        moviesrated.add(new PairM(movie, rating));
    }
    public Map<String, Integer> getMap(){
        return ratedMovie;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("User ID: %s Name: %s\n", id, username));
        Comparator<PairM>comparator = Comparator.comparing(PairM::getRating).reversed();
        List<PairM> best_rating =  moviesrated.stream().sorted(comparator).collect(Collectors.toList());
        double best_r = best_rating.get(0).getRating();
        moviesrated.stream().sorted(comparator).filter(pairM -> pairM.getMovie().getRating()!=0).sorted(Comparator.comparing(PairM::getMovieRating).reversed()).filter(pairM -> pairM.getRating() == best_r).forEach(pairM -> sb.append(pairM.getMovie().toString() + "\n"));
        return sb.toString();
    }

    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }
}
class Pair{
    private User user;
    private Double rating;

    public Pair(User user, double sim) {
        this.user = user;
        this.rating = sim;
    }

    public Double getRating(){
        return rating;
    }

    public User getUser() {
        return this.user;
    }
}
class PairM{
    private Movie movie;
    private Double rating;

    public PairM(Movie movie, double sim) {
        this.movie = movie;
        this.rating = sim;
    }

    public Double getRating(){
        return rating;
    }

    public Movie getMovie() {
        return this.movie;
    }
    public Double getMovieRating(){
        return this.movie.getRating();
    }
}
class StreamingPlatform{
    private Map<String,Movie>movies;
    private Map<String, User>users;

    public StreamingPlatform(){
        movies = new TreeMap<>();
        users = new TreeMap<>();
    }

    public void addMovie(String id, String name) {
        movies.put(id,new Movie(id, name));
    }

    public void addUser(String id, String name) {
        users.put(id,new User(id, name));
        for (Movie m:movies.values()) {
            users.get(id).rateMovie(0, m.getId());
        }
    }

    public void addRating(String userId, String movieId, int rating) {
        movies.get(movieId).addRating(rating);
        users.get(userId).rateMovie(rating, movieId);
        users.get(userId).makemovieRating(rating,movies.get(movieId));
    }

    public void topNMovies(int n) {
        Comparator<Movie>comparator = Comparator.comparing(Movie::getRating).reversed();
        movies.values().stream().sorted(comparator).limit(n).forEach(System.out::println);
    }

    public void favouriteMoviesForUsers(List<String> users) {
        users.stream().forEach(user -> System.out.println(this.users.get(user)));
    }

    public void similarUsers(String userId) {
        List<Pair> similarUsers = new ArrayList<>();
        for (User user:users.values()) {
            if(!user.getId().equals(userId)){
                double sim = CosineSimilarityCalculator.cosineSimilarity(user.getMap(), users.get(userId).getMap());
                similarUsers.add(new Pair(user, sim));
            }
        }
        Comparator<Pair> comparator = Comparator.comparing(Pair::getRating).reversed();
        similarUsers.stream().sorted(comparator).forEach(pair -> System.out.printf("User ID: %s Name: %s %s\n", pair.getUser().getId(), pair.getUser().getUsername(),pair.getRating()));

    }
}
public class StreamingPlatform2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StreamingPlatform sp = new StreamingPlatform();

        while (sc.hasNextLine()){
            String line = sc.nextLine();
            String [] parts = line.split("\\s+");

            if (parts[0].equals("addMovie")) {
                String id = parts[1];
                String name = Arrays.stream(parts).skip(2).collect(Collectors.joining(" "));
                sp.addMovie(id ,name);
            } else if (parts[0].equals("addUser")){
                String id = parts[1];
                String name = parts[2];
                sp.addUser(id ,name);
            } else if (parts[0].equals("addRating")){
                //String userId, String movieId, int rating
                String userId = parts[1];
                String movieId = parts[2];
                int rating = Integer.parseInt(parts[3]);
                sp.addRating(userId, movieId, rating);
            } else if (parts[0].equals("topNMovies")){
                int n = Integer.parseInt(parts[1]);
                System.out.println("TOP " + n + " MOVIES:");
                sp.topNMovies(n);
            } else if (parts[0].equals("favouriteMoviesForUsers")) {
                List<String> users = Arrays.stream(parts).skip(1).collect(Collectors.toList());
                System.out.println("FAVOURITE MOVIES FOR USERS WITH IDS: " + users.stream().collect(Collectors.joining(", ")));
                sp.favouriteMoviesForUsers(users);
            } else if (parts[0].equals("similarUsers")) {
                String userId = parts[1];
                System.out.println("SIMILAR USERS TO USER WITH ID: " + userId);
                sp.similarUsers(userId);
            }
        }
    }
}
