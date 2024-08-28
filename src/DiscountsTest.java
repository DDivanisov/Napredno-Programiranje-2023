import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Discounts
 */
class Triplets{
    private Integer popust;
    private Integer cena;

    public Triplets(Integer popust, Integer cena) {
        this.popust = popust;
        this.cena = cena;
    }

    public Integer getDiscount(){
        return (int) (100.0 - (popust * 1.0 / cena) * 100.0);
    }
    public Integer getabsolutDiff(){
        return Math.abs(popust - cena);
    }

    public Integer getPopust() {
        return popust;
    }
    public Integer getCena(){
        return cena;
    }
}
class Store{
    private String storeName;
    private List<Integer> popusti;
    private List<Integer> ceni;
    private List<Integer> discounts;
    private List<Triplets>allforone;
    private double avgDiscount;
    private int totalDiscount;

    public Store(String name){
        storeName = name;
        allforone = new ArrayList<>();
        totalDiscount = 0;
        avgDiscount = 0.0;
        popusti = new ArrayList<>();
        ceni = new ArrayList<>();
        discounts = new ArrayList<>();
    }

    public void makePopusti_Ceni(String popust_cena){
        String[] parts = popust_cena.split(":");
        int popust = Integer.parseInt(parts[0]);
        int cena = Integer.parseInt(parts[1]);

        popusti.add(popust);
        ceni.add(cena);
        double discount = 100.0 - (popust * 1.0 / cena) * 100.0;
        discounts.add((int) discount);
    }
    public void makeTriplets(){
        allforone = new ArrayList<>();
        for(int i=0;i<popusti.size();i++){
            allforone.add(new Triplets(popusti.get(i), ceni.get(i)));
        }
    }
    public void calcTotalDiscount(){
        for(int i=0;i<popusti.size();i++){
            totalDiscount = totalDiscount + (Math.abs(popusti.get(i) - ceni.get(i)));
        }
    }
    public void calcAvgDiscount(){
        avgDiscount = discounts.stream().mapToDouble(value -> value).sum();
        avgDiscount = avgDiscount / discounts.size();
    }

    public String getStoreName() {
        return storeName;
    }

    public double getAvgDiscount() {
        calcAvgDiscount();
        return avgDiscount;
    }

    public int getTotalDiscount() {
        totalDiscount = 0;
        calcTotalDiscount();
        return totalDiscount;
    }

    @Override
    public String toString() {
        makeTriplets();
        StringBuilder sb = new StringBuilder();
        sb.append(storeName + "\n");
        sb.append(String.format("Average discount: %.1f%%", getAvgDiscount()));
        sb.append(String.format("\nTotal discount: %s", getTotalDiscount()));
        Comparator<Triplets>comparator = Comparator.comparing(Triplets::getDiscount).thenComparing(Triplets::getabsolutDiff).reversed();
        allforone.stream().sorted(comparator).forEach(a -> sb.append(String.format("\n%2d%% %s/%s", a.getDiscount(), a.getPopust(), a.getCena())));
        return sb.toString();
    }
}
class Discounts{
    List<Store>stores;

    public Discounts(){
        stores = new ArrayList<>();
    }

    public int readStores(InputStream in) throws IOException {
        int i = 0;
        BufferedReader bf =  new BufferedReader(new InputStreamReader(in));
        String line;
        while((line = bf.readLine())!= null){
            String[] parts;
            parts = line.split("  ");
            Store store = new Store(parts[0].split(" ")[0]);
            for (int j = 0; j < parts.length;j++) {
                if(j == 0)
                    store.makePopusti_Ceni(parts[j].split(" ")[1]);
                else
                    store.makePopusti_Ceni(parts[j]);
            }
            stores.add(store);
            i++;
        }
        bf.close();
        return i;
    }

    public List<Store> byAverageDiscount() {
        Comparator<Store>comparator = Comparator.comparing(Store::getAvgDiscount).thenComparing(Store::getStoreName).reversed();
        return stores.stream().sorted(comparator).limit(3).collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount() {
        Comparator<Store>comparator = Comparator.comparing(Store::getTotalDiscount).thenComparing(Store::getStoreName);
        return stores.stream().sorted(comparator).limit(3).collect(Collectors.toList());
    }
}


public class DiscountsTest {
    public static void main(String[] args) throws IOException {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}
