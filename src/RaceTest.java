import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


class Racer{
    private LocalTime start;
    private LocalTime end;
    private int ID;

    public Racer() {
        start = null;
        end = null;
        ID = 0;
    }
    public void setStartTime(String line){
        int h,m,s;
        String parts[];
        parts = line.split(":");
        h = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        s = Integer.parseInt(parts[2]);
        start = LocalTime.of(h, m, s);
    }
    public void setEndTime(String line){
        int h, m, s;
        String parts[];
        parts = line.split(":");
        h = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        s = Integer.parseInt(parts[2]);
        end = LocalTime.of(h, m, s);
    }
    public LocalTime Calculate_time_taken(){
        Duration duration = Duration.between(start, end);
        LocalTime timeTaken = LocalTime.of(Integer.parseInt(String.valueOf(duration.toHours())), Integer.parseInt(String.valueOf(duration.toMinutes() % 60)), Integer.parseInt(String.valueOf(duration.getSeconds()%60)));
        return timeTaken;
    }

    public void setId(String part) {
        ID = Integer.parseInt(part);
    }

    public Integer getID() {
        return ID;
    }

    @Override
    public String toString() {

        return String.format("%s %s", ID, Calculate_time_taken());
    }
}
class TeamRace{

    public static void findBestTeam(InputStream in, PrintStream out) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        PrintWriter pw =  new PrintWriter(out);
        Map<Integer, Racer>results = new TreeMap<>();
        String line;
        while ((line = bf.readLine()) != null){
            String parts[];
            parts = line.split(" ");
            Racer person = new Racer();
            person.setStartTime(parts[1]);
            person.setEndTime(parts[2]);
            person.setId(parts[0]);

            results.put(person.getID(), person);
        }
        Comparator<Racer>comparator = Comparator.comparing(Racer::Calculate_time_taken);
        List<Racer> resultRacers = new ArrayList<>(results.values());
        List<Racer>best4 = resultRacers.stream().sorted(comparator).limit(4).collect(Collectors.toList());
        best4.stream().forEach(r -> pw.println(r));
        int seconds1 = best4.get(0).Calculate_time_taken().toSecondOfDay();
        int seconds2 = best4.get(1).Calculate_time_taken().toSecondOfDay();
        int seconds3 = best4.get(2).Calculate_time_taken().toSecondOfDay();
        int seconds4 = best4.get(3).Calculate_time_taken().toSecondOfDay();
        int s = seconds1 + seconds3 + seconds4 + seconds2;

        int hours = s / 3600;
        int minutes = (s % 3600) / 60;
        int seconds = s % 60;

        pw.printf(String.valueOf(LocalTime.of(hours, minutes,seconds)));
        bf.close();
        pw.flush();
        pw.close();

    }
}
public class RaceTest {
    public static void main(String[] args) {
        try {
            TeamRace.findBestTeam(System.in, System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}