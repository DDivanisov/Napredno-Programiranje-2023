// Problem with date i changed it to localTime but it has problems
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * I partial exam 2016
 */

class Day{
    private int day;
    private List<Double>temperatures;
    private Character actualType;
    private Character mesurmentType;
    private Double min,max,avg;
    public Day() {
        this.day = 0;
        this.actualType = null;
        this.mesurmentType = null;
        this.avg = 0.0;
        this.min = 0.0;
        this.max =0.0;
        this.temperatures = new ArrayList<>();
    }
    public void makeMaxMinAvg(){
        Double sum = temperatures.stream().mapToDouble(t -> (double)t).sum();
        this.avg = sum/temperatures.size();
        this.max = temperatures.stream().mapToDouble(t -> (double)t).max().orElse(0.0);
        this.min = temperatures.stream().mapToDouble(t -> (double)t).min().orElse(0.0);
    }
    public void makeTemperatures(String temperature){
        actualType = temperature.charAt(temperature.length() - 1);
        String num = temperature.substring(0,temperature.length()-1);
        Double temp = Double.parseDouble(num);
        this.temperatures.add(temp);
    }

    public void setMesurmentType(Character mesurmentType) {
        this.mesurmentType = mesurmentType;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        if(this.mesurmentType.equals('C') && actualType.equals('F')){
            min = (min - 32) * 5.0/9.0;
            avg = (avg - 32) * 5.0/9.0;
            max = (max - 32) * 5.0/9.0;
            this.actualType = 'C';
        }
        else if(this.mesurmentType.equals('F') && actualType.equals('C')){
            min = (min * 9.0/5.0) + 32;
            avg = (avg * 9.0/5.0) + 32;
            max = (max * 9.0/5.0) + 32;
            this.actualType = 'F';
        }
            return String.format("%3d: Count: %3d Min: %6.2f%s Max: %6.2f%s Avg: %6.2f%s",day,temperatures.size(),min,mesurmentType,max,mesurmentType,avg,mesurmentType);
    }
}
class DailyTemperatures{
    private List<Day>days;

    public DailyTemperatures() {
        days = new ArrayList<>();
    }

    public void readTemperatures(InputStream in) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = bf.readLine()) != null){
            String[] parts = line.split(" ");
            Day day = new Day();
            day.setDay(Integer.parseInt(parts[0]));
            for(int i=1;i< parts.length;i++){
                day.makeTemperatures(parts[i]);
            }
            day.makeMaxMinAvg();
            days.add(day);
        }

        bf.close();
    }

    public void writeDailyStats(PrintStream out, char scale) {
        PrintWriter pw = new PrintWriter(out);
        days.stream().forEach(day -> day.setMesurmentType(scale));
        days.stream().sorted(Comparator.comparing(Day::getDay)).forEach(pw::println);

        pw.flush();

    }
}
public class DailyTemperatureTest {
    public static void main(String[] args) throws IOException {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

// Vashiot kod ovde