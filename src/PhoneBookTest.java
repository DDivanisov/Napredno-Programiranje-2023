import java.util.*;
import java.util.stream.Collectors;

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String msg){
        super(msg);
    }
}
class NumberName{
    private String name;
    private String number;

    public NumberName(String number, String name) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return String.format("%s %s", name,number);
    }
}
class PhoneBook{
    private Map<String, List<String>>phonebook;
    private Map<String, String>numberbyName;
    public PhoneBook(){
        phonebook = new TreeMap<>();
        numberbyName = new TreeMap<>();
    }

    public void addContact(String name, String number) throws DuplicateNumberException{
        if(phonebook.containsValue(number)){
            throw new DuplicateNumberException(String.format("Duplicate number: %s", number));
        }
        else {
            if(phonebook.containsKey(name)){
                phonebook.get(name).add(number);
            }
            else {
                List<String>newList = new ArrayList<>();
                newList.add(number);
                phonebook.put(name,newList);
            }
            numberbyName.put(number,name);
        }

    }

    public void contactsByNumber(String num) {
        List<NumberName> res = numberbyName.entrySet().stream()
                .filter(entry -> entry.getKey().contains(num))
                .map(entry -> new NumberName(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        if(!res.isEmpty()){
            Comparator<NumberName> comparator = Comparator.comparing(NumberName::getName).thenComparing(NumberName::getNumber);
            res.stream().sorted(comparator).forEach(System.out::println);
        }
        else {
            System.out.printf("NOT FOUND");
        }
    }

    public void contactsByName(String name) {
        Comparator<String>comparator = Comparator.naturalOrder();
        if(phonebook.containsKey(name)){
            List<String>numbers = phonebook.get(name).stream().sorted(comparator).collect(Collectors.toList());
            numbers.forEach(num -> System.out.printf("%s %s%n", name,num));
        }
        else {
            System.out.printf("NOT FOUND");
        }
    }
}
public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}