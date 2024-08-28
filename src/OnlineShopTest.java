import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}


class Product {
    private String category;
    private String id;
    private String name;
    private LocalDateTime dateCreatedAt;
    private double price;
    private int qs;

    public Product(String category, String id, String name, LocalDateTime dateCreatedAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.dateCreatedAt = dateCreatedAt;
        this.price = price;
        this.qs = 0;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDateCreatedAt() {
        return dateCreatedAt;
    }

    public int getQs() {
        return qs;
    }

    public double getPrice() {
        return price;
    }
    public void addSold(int n){
        qs += n;
    }
    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', createdAt=%s, price=%s, quantitySold=%s}",id,name,dateCreatedAt,price,qs);
    }
}


class OnlineShop {
    Map<String, Product>shop;

    OnlineShop() {
        shop = new TreeMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        Product product = new Product(category,id,name,createdAt,price);
        shop.put(id,product);
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        if(!shop.containsKey(id))
            throw new ProductNotFoundException(String.format("Product with id %s does not exist in the online shop!",id));
        else {
            shop.get(id).addSold(quantity);
            return shop.get(id).getPrice() * quantity;
        }

    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<List<Product>> result = new ArrayList<>();
        Comparator<Product>byDate = Comparator.comparing(Product::getDateCreatedAt);
        Comparator<Product>byPrice = Comparator.comparing(Product::getPrice);
        Comparator<Product>byQuantitySold = Comparator.comparing(Product::getQs);
        List<Product>sorted = new ArrayList<>();
        if(category == null){
            if(comparatorType.name().equals("NEWEST_FIRST")){
                sorted = shop.values().stream().sorted(byDate.reversed()).collect(Collectors.toList());
            }
            else if(comparatorType.name().equals("OLDEST_FIRST")){
                sorted = shop.values().stream().sorted(byDate).collect(Collectors.toList());
            }
            else if(comparatorType.name().equals("LOWEST_PRICE_FIRST")){
                sorted = shop.values().stream().sorted(byPrice).collect(Collectors.toList());
            }
            else if(comparatorType.name().equals("HIGHEST_PRICE_FIRST")){
                sorted = shop.values().stream().sorted(byPrice.reversed()).collect(Collectors.toList());
            }
            else if(comparatorType.name().equals("LEAST_SOLD_FIRST")){
                sorted = shop.values().stream().sorted(byQuantitySold.thenComparing(Product::getName)).collect(Collectors.toList());
            }
            else if(comparatorType.name().equals("MOST_SOLD_FIRST")){
                sorted = shop.values().stream().sorted(byQuantitySold.reversed().thenComparing(Product::getId).thenComparing(Product::getName)).collect(Collectors.toList());
            }
        }
        else {
            if(comparatorType.name().equals("NEWEST_FIRST")){
                sorted = shop.values().stream().filter(s -> s.getCategory().equals(category)).sorted(byDate.reversed()).collect(Collectors.toList());
            }
            else if(comparatorType.name().equals("OLDEST_FIRST")){
                sorted = shop.values().stream().filter(s -> s.getCategory().equals(category)).sorted(byDate).collect(Collectors.toList());
            }
            else if(comparatorType.name().equals("LOWEST_PRICE_FIRST")){
                sorted = shop.values().stream().filter(s -> s.getCategory().equals(category)).sorted(byPrice).collect(Collectors.toList());
            }
            else if(comparatorType.name().equals("HIGHEST_PRICE_FIRST")){
                sorted = shop.values().stream().filter(s -> s.getCategory().equals(category)).sorted(byPrice.reversed()).collect(Collectors.toList());
            }
            else if(comparatorType.name().equals("LEAST_SOLD_FIRST")){
                sorted = shop.values().stream().filter(s -> s.getCategory().equals(category)).sorted(byQuantitySold).collect(Collectors.toList());
            }
            else if(comparatorType.name().equals("MOST_SOLD_FIRST")){
                sorted = shop.values().stream().filter(s -> s.getCategory().equals(category)).sorted(byQuantitySold.reversed()).collect(Collectors.toList());
            }
        }
        List<Product>products = new ArrayList<>();
        for (Product p : sorted) {

            if(products.size() < pageSize){
                products.add(p);
            }
            else {
                result.add(products);
                products = new ArrayList<>();
                products.add(p);
            }
        }
        if(products.size()!= 4 || products.size() != 0){
            result.add(products);
        }
        return result;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

