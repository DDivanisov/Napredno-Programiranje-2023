import java.util.*;
import java.util.stream.Collectors;

class SortingPlace<T extends Comparable<T>> {
    private List<T> list;
    private int toGet;

    public SortingPlace() {
        list = new ArrayList<>();
        toGet = 0;
    }

    public void makeList(T elem) {
        list.add(elem);
    }

    public void sort() {
        Comparator<T> comparator = Comparator.<T>naturalOrder();
        this.list = list.stream().sorted(comparator).collect(Collectors.toList());
    }

    public List<T> getList() {
        return list;
    }

    public T getNext() {
        T elem = list.get(toGet);
        toGet++;
        return elem;
    }
}

class BlockContainer<T extends Comparable<T>> {
    private List<Set<T>> containers;
    private int sizeOfEachBlock;


    public BlockContainer(int size) {
        this.sizeOfEachBlock = size;
        this.containers = new ArrayList<>();
    }

    public void add(T element) {
        Set<T> lastBlock;
        if (containers.isEmpty() || containers.get(containers.size() - 1).size() == sizeOfEachBlock) {
            lastBlock = new TreeSet<>();
            containers.add(lastBlock);
        } else {
            lastBlock = containers.get(containers.size() - 1);
        }
        lastBlock.add(element);
    }

    public boolean remove(T element) {
        for (int i = containers.size() - 1; i >= 0; i--) {
            Set<T> block = containers.get(i);
            if (block.remove(element)) {
                if (block.isEmpty()) {
                    containers.remove(i);
                }
                return true;
            }
        }
        return false;
    }

    public void sort() {
        SortingPlace<T> sp = new SortingPlace<>();
        for (Set<T> c : containers) {
            for (T elem : c) {
                sp.makeList(elem);
            }
        }
        sp.sort();
        List<T> list = sp.getList();
        this.containers = new ArrayList<>();
        for (T elem : list) {
            add(elem);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean notLast = false;
        for (Set<T> element : containers) {
            StringBuilder sb_1 = new StringBuilder();
            if (notLast)
                sb_1.append(",[");
            else
                sb_1.append("[");
            notLast = true;
            boolean notFirst = false;
            for (T miniElement : element) {
                if (notFirst)
                    sb_1.append(", ");
                notFirst = true;
                sb_1.append(miniElement.toString());
            }
            sb_1.append("]");
            sb.append(sb_1.toString());
        }
        return sb.toString();
    }
}

public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for (int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);

        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);

        BlockContainer<String> stringBC = new BlockContainer<>(size);
        String lastString = null;
        for (int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}
