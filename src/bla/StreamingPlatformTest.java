package bla;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

abstract class StreamItem {
    protected String Name;
    protected List<String> genres;
    protected double finalRating;

    public StreamItem() {
        this.Name = null;
        this.genres = new ArrayList<>();
        this.finalRating = 0.0;
    }

    public String getName() {
        return Name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public double getFinalRating() {
        calcRating();
        return finalRating;
    }

    public abstract void calcRating();

    @Override
    public abstract String toString();
}
class Movie extends StreamItem{
    private List<String>ratings;


    public Movie() {
        super();
        ratings = new ArrayList<>();
    }


    public void makeMovie(String[] parts){
        Name = parts[0];
        genres = List.of(parts[1].split(","));
        ratings = List.of(parts[2].split(" "));
    }
    @Override
    public void calcRating(){
        double mult = Math.min(ratings.size()/20.0, 1.0);
        double r = 0.0;
        for (String rating:ratings) {
            r = r + Double.parseDouble(rating);
        }
        r = r / ratings.size();
        finalRating = r * mult;
    }

    @Override
    public String toString() {
        calcRating();
        return String.format("bla.Movie %s %1.4f", Name, getFinalRating());
    }
}
class Episode{
    private String episode;
    private List<String>ratings;
    private double finalRating;

    public Episode() {
        episode = null;
        ratings = new ArrayList<>();
        finalRating = 0.0;
    }
    public void makeEpisode(String[] parts){
        episode = parts[0];
        for(int i = 1; i< parts.length;i++){
            ratings.add(parts[i]);
        }
    }
    public String getEpisode() {
        return episode;
    }

    public double getFinalRating() {
        calcRating();
        return finalRating;
    }
    public void calcRating(){
        double mult = Math.min(ratings.size()/20.0, 1.0);
        double r = 0.0;
        for (String rating:ratings) {
            r = r + Double.parseDouble(rating);
        }
        r = r / ratings.size();
        finalRating = r * mult;
    }
}
class TVSeries extends StreamItem{
    private List<Episode>episodes;

    public TVSeries() {
        super();
        episodes = new ArrayList<>();

    }

    @Override
    public void calcRating(){
        Comparator<Episode>comparator = Comparator.comparing(Episode::getFinalRating).reversed();
        List<Episode>best3 = episodes.stream().sorted(comparator).limit(3).collect(Collectors.toList());
        finalRating = (best3.get(0).getFinalRating() + best3.get(1).getFinalRating() + best3.get(2).getFinalRating()) / 3.0;
    }

    @Override
    public String toString() {
        calcRating();
        return String.format("TV Show %s %1.4f (%s episodes)", Name, finalRating, episodes.size());
    }

    public void makeTVShow(String[] parts) {
        Name = parts[0];
        genres = List.of(parts[1].split(","));
        for (int i = 2; i< parts.length;i++){
            Episode e = new Episode();
            e.makeEpisode(parts[i].split(" "));
            episodes.add(e);
        }
    }
}
class StreamingPlatform{
    private List<StreamItem> shows;
    Comparator<StreamItem>comparator;
    public StreamingPlatform() {
        shows = new ArrayList<>();
        comparator = Comparator.comparing(StreamItem::getFinalRating).reversed();
    }

    public void addItem(String data) {
        String[] parts;
        parts = data.split(";");
        if(parts.length == 3){
            Movie m = new Movie();
            m.makeMovie(parts);
            shows.add(m);
        }
        else {
            TVSeries tvs = new TVSeries();
            tvs.makeTVShow(parts);
            shows.add(tvs);
        }
    }

    public void listAllItems(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);

        shows.stream().sorted(comparator).forEach(m -> pw.println(m));
        pw.flush();
        pw.close();
    }

    public void listFromGenre(String genre, PrintStream out) {
        PrintWriter pw = new PrintWriter(out);

        shows.stream().sorted(comparator).filter(s -> s.getGenres().contains(genre)).forEach(s -> pw.println(s));

        pw.flush();
        pw.close();
    }
}

public class StreamingPlatformTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StreamingPlatform sp = new StreamingPlatform();
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            String [] parts = line.split(" ");
            String method = parts[0];
            String data = Arrays.stream(parts).skip(1).collect(Collectors.joining(" "));
            if (method.equals("addItem")){
                sp.addItem(data);
            }
            else if (method.equals("listAllItems")){
                sp.listAllItems(System.out);
            } else if (method.equals("listFromGenre")){
                System.out.println(data);
                sp.listFromGenre(data, System.out);
            }
        }

    }
}
