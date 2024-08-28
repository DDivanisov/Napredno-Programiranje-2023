//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//class DateUtil {
//    public static long durationBetween(LocalDateTime start, LocalDateTime end) {
//        return Duration.between(start, end).toMinutes();
//    }
//}
//class Car{
//    private String registration;
//    private String spot;
//    private LocalDateTime timestamp;
//    private boolean entrance;
//
//    public Car(String registration, String spot, LocalDateTime timestamp, boolean entrance) {
//        this.registration = registration;
//        this.spot = spot;
//        this.timestamp = timestamp;
//        this.entrance = entrance;
//    }
//
//    public String getRegistration() {
//        return registration;
//    }
//
//    public String getSpot() {
//        return spot;
//    }
//
//    public LocalDateTime getTimestamp() {
//        return timestamp;
//    }
//
//    public boolean isEntrance() {
//        return entrance;
//    }
//
//    @Override
//    public String toString() {
//        return String.format("Registration number: %s Spot: %S Start timestamp: %s", registration, spot, timestamp);
//    }
//}
//class CarHistory{
//    private int timesParked;
//    private String registration;
//    private String spot;
//    private LocalDateTime entryTime;
//    private LocalDateTime exitTime;
//
//    public CarHistory(String registration, String spot) {
//        this.timesParked = 1;
//        this.registration = registration;
//        this.spot = spot;
//    }
//
//    public void setTimesParked(int timesParked) {
//        this.timesParked = timesParked + 1;
//    }
//
//    public void setExitTime(LocalDateTime exitTime) {
//        this.exitTime = exitTime;
//    }
//
//    public void setEntryTime(LocalDateTime entryTime) {
//        this.entryTime = entryTime;
//    }
//
//    public int getTimesParked() {
//        return timesParked;
//    }
//
//    public String getRegistration() {
//        return registration;
//    }
//
//    public String getSpot() {
//        return spot;
//    }
//
//    public LocalDateTime getEntryTime() {
//        return entryTime;
//    }
//
//    public LocalDateTime getExitTime() {
//        return exitTime;
//    }
//
//    @Override
//    public String toString() {
//        return String.format("Registration number: %s Spot: %s Start timestamp: %s End timestamp: %s Duration in minutes: %s",registration,spot,entryTime,exitTime,DateUtil.durationBetween(entryTime,exitTime));
//    }
//}
//class Parking{
//    private Map<String, Car>parking;
//    private Map<CarHistory,CarHistory>history;
//    private int capacity;
//
//    public Parking(int capacity){
//        this.capacity = capacity;
//        this.history = new TreeMap<>();
//        parking = new TreeMap<>();
//    }
//    public void update(String registration, String spot, LocalDateTime timestamp, boolean entrance) {
//        if (entrance){
//            parking.put(spot, new Car(registration, spot, timestamp, entrance));
//            CarHistory ch = new CarHistory(registration, spot);
//            ch.setEntryTime(timestamp);
//            history.put(ch, ch);
//        }
//        else {
//            parking.remove(spot);
//            history.get(new CarHistory(registration, spot)).setExitTime(timestamp);
//        }
//    }
//
//    public void currentState() {
//        System.out.printf("Capacity filled: %2.2f%% \n",(parking.size()*1.0/capacity*1.0)*100.00);
//        List<Car>cars = new ArrayList<>(parking.values());
//        Comparator<Car>comparator = Comparator.comparing(Car::getTimestamp).reversed();
//        cars.stream().sorted(comparator).forEach(System.out::println);
//    }
//
//    public void history() {
//
//    }
//
//    public Map<String, Integer> carStatistics() {
//
//        return null;
//    }
//
//    public Map<String, Double> spotOccupancy(LocalDateTime start, LocalDateTime end) {
//        return null;
//    }
//}
//public class ParkingTesting {
//
//    public static <K, V extends Comparable<V>> void printMapSortedByValue(Map<K, V> map) {
//        map.entrySet().stream()
//                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//                .forEach(entry -> System.out.println(String.format("%s -> %s", entry.getKey().toString(), entry.getValue().toString())));
//
//    }
//
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        int capacity = Integer.parseInt(sc.nextLine());
//
//        Parking parking = new Parking(capacity);
//
//        while (sc.hasNextLine()) {
//            String line = sc.nextLine();
//            String[] parts = line.split("\\s+");
//            if (parts[0].equals("update")) {
//                String registration = parts[1];
//                String spot = parts[2];
//                LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
//                boolean entrance = Boolean.parseBoolean(parts[4]);
//                parking.update(registration, spot, timestamp, entrance);
//            } else if (parts[0].equals("currentState")) {
//                System.out.println("PARKING CURRENT STATE");
//                parking.currentState();
//            } else if (parts[0].equals("history")) {
//                System.out.println("PARKING HISTORY");
//                parking.history();
//            } else if (parts[0].equals("carStatistics")) {
//                System.out.println("CAR STATISTICS");
//                printMapSortedByValue(parking.carStatistics());
//            } else if (parts[0].equals("spotOccupancy")) {
//                LocalDateTime start = LocalDateTime.parse(parts[1]);
//                LocalDateTime end = LocalDateTime.parse(parts[2]);
//                printMapSortedByValue(parking.spotOccupancy(start, end));
//            }
//        }
//    }
//}
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class DateUtil {
    public static long durationBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }
}

class Car {
    private String registration;
    private String spot;
    private LocalDateTime timestamp;


    public Car(String registration, String spot, LocalDateTime timestamp) {
        this.registration = registration;
        this.spot = spot;
        this.timestamp = timestamp;

    }

    public String getRegistration() {
        return registration;
    }

    public String getSpot() {
        return spot;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }


    @Override
    public String toString() {
        return String.format("Registration number: %s Spot: %s Start timestamp: %s", registration, spot, timestamp);
    }
}

class CarHistory {
    private String registration;
    private String spot;
    private List<LocalDateTime>timeHistory;

    public CarHistory(){
        timeHistory = new ArrayList<>();
    }
    public void addTimestamp(LocalDateTime timeStamp){
        timeHistory.add(timeStamp);
    }
    public int duration(){
        int duration = 0;
        for(int i=0;i < timeHistory.size();i+=2){
            duration = duration + (int) DateUtil.durationBetween(timeHistory.get(i), timeHistory.get(i + 1));
        }
        return duration;
    }

    @Override
    public String toString() {
        return String.format("Registration number: %s Spot: %s Start timestamp: %s End timestamp: %s Duration in minutes: %s\n",registration,spot,timeHistory.get(0),timeHistory.get(1),duration());
    }
}

class Parking {
    private Map<String, Car> parking;
    private Map<Integer, CarHistory> history;
    private int capacity;
    static int time;

    public Parking(int capacity) {
        this.capacity = capacity;
        this.history = new TreeMap<>();
        this.parking = new TreeMap<>();
        this.time = 0;
    }

    public void update(String registration, String spot, LocalDateTime timestamp, boolean entrance) {
        if (entrance) {
            Car car = new Car(registration, spot, timestamp);
            parking.put(spot, car);
            CarHistory ch = new CarHistory();
            ch.addTimestamp(timestamp);
            history.put(Parking.time = Parking.time + 1, ch);
        } else {
            Car car = parking.remove(spot);
            history.get(car.getRegistration()).addTimestamp(timestamp);
        }
    }





    public void currentState() {
        double currentOccupancy = (double) parking.size() / capacity * 100;
        System.out.printf("Capacity filled: %.2f%%%n", currentOccupancy);

        List<Car> cars = new ArrayList<>(parking.values());
        Comparator<Car> comparator = Comparator.comparing(Car::getTimestamp).reversed();
        cars.stream().sorted(comparator).forEach(System.out::println);
    }

    public void history() {
        System.out.println("Parking History (sorted by parking duration, descending):");
        Comparator<CarHistory> comparator = Comparator.comparing(CarHistory::duration).reversed();
        history.values().stream()
                .sorted(comparator)
                .forEach(System.out::println);
    }

    public Map<String, Integer> carStatistics() {

        return null;
    }

    public Map<String, Double> spotOccupancy(LocalDateTime start, LocalDateTime end) {
        return null;
    }
}

public class ParkingTesting {

    public static <K, V extends Comparable<V>> void printMapSortedByValue(Map<K, V> map) {
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(String.format("%s -> %s", entry.getKey().toString(), entry.getValue().toString())));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int capacity = Integer.parseInt(sc.nextLine());

        Parking parking = new Parking(capacity);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equals("update")) {
                String registration = parts[1];
                String spot = parts[2];
                LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
                boolean entrance = Boolean.parseBoolean(parts[4]);
                parking.update(registration, spot, timestamp, entrance);
            } else if (parts[0].equals("currentState")) {
                System.out.println("PARKING CURRENT STATE");
                parking.currentState();
            } else if (parts[0].equals("history")) {
                System.out.println("PARKING HISTORY");
                parking.history();
            } else if (parts[0].equals("carStatistics")) {
                System.out.println("CAR STATISTICS");
                printMapSortedByValue(parking.carStatistics());
            } else if (parts[0].equals("spotOccupancy")) {
                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);
                printMapSortedByValue(parking.spotOccupancy(start, end));
            }
        }
    }
}
