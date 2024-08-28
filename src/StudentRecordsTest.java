import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * January 2016 Exam problem 1
 */
class Smer{
    private String name;
    private List<Student1>students;
    private int tens,nines,eights,sevens,sixs;
    public Smer(String name) {
        this.name = name;
        this.eights = 0;
        this.nines = 0;
        this.tens = 0;
        this.sixs = 0;
        this.sevens = 0;
        this.students = new ArrayList<>();
    }
    public void addStudnet(Student1 s1){
        students.add(s1);
    }

    public int getTens() {
        return tens;
    }

    public void countGrades10(){
        students.forEach(s ->{
            s.CountGrades();
            this.tens += s.getTens();
            this.nines += s.getNines();
            this.sevens += s.getSevens();
            this.eights += s.getEights();
            this.sixs += s.getSixs();

        });
    }
    public String makeStars(int n){
        StringBuilder sb =new StringBuilder();

        for(int i=0;i<n;i += 10){
            sb.append("*");
        }
        return sb.toString();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String stars = makeStars(sixs);
        sb.append(String.format("%s\n",name));
        sb.append(String.format(" 6 | %s(%d)\n",stars,sixs));
        stars = makeStars(sevens);
        sb.append(String.format(" 7 | %s(%d)\n",stars,sevens));
        stars = makeStars(eights);
        sb.append(String.format(" 8 | %s(%d)\n",stars,eights));
        stars = makeStars(nines);
        sb.append(String.format(" 9 | %s(%d)\n",stars,nines));
        stars = makeStars(tens);
        sb.append(String.format("10 | %s(%d)",stars,tens));

        return sb.toString();
    }
}
class Student1{
    private String id;
    private String smer;
    private List<Integer>grades;
    private int tens,nines,eights,sevens,sixs;
    private Double avgGrade;

    public Student1(String id, String smer) {
        this.id = id;
        this.avgGrade = 0.0;
        this.smer = smer;
        this.eights = 0;
        this.nines = 0;
        this.tens = 0;
        this.sixs = 0;
        this.sevens = 0;
        this.grades = new ArrayList<>();
    }
    public void makeGrades(int grade){
        grades.add(grade);
    }

    public String getId() {
        return id;
    }
    public void CountGrades(){
        grades.stream().forEach(g ->{
            if(g == 6)
                sixs++;
            if(g == 7)
                sevens++;
            if(g == 8)
                eights++;
            if(g == 9)
                nines++;
            if(g == 10)
                tens++;
        });
    }
    public String getSmer() {
        return smer;
    }
    public double getAvgGrade(){
        avgGrade = grades.stream().mapToDouble(g -> g).average().orElse(0.0);
        return avgGrade;
    }

    public List<Integer> getGrades() {
        return grades;
    }
    public int getTens() {
        return tens;
    }
    public int getNines() {
        return nines;
    }

    public int getEights() {
        return eights;
    }

    public int getSevens() {
        return sevens;
    }

    public int getSixs() {
        return sixs;
    }

    @Override
    public String toString() {
        return String.format("%s %1.2f", id, getAvgGrade());
    }
}
class StudentRecords{
    private Map<String, List<Student1>> students;
    private Map<String, Smer>smers;
    public StudentRecords(){
        students = new HashMap<>();
        smers = new HashMap<>();
    }
    public int readRecords(InputStream in) throws IOException {
        int i = 0;
        String line;
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));

        while((line = bf.readLine()) != null){
            i++;
            String[] parts = line.split(" ");
            Student1 s = new Student1(parts[0], parts[1]);
            for (int j=2;j< parts.length;j++){
                s.makeGrades(Integer.parseInt(parts[j]));
            }
            if(students.containsKey(parts[1])){
                students.get(parts[1]).add(s);
            }
            else {
                List<Student1>s1 = new ArrayList<>();
                s1.add(s);
                students.put(parts[1],s1);
            }
            if(smers.containsKey(parts[1])){
                smers.get(parts[1]).addStudnet(s);
            }
            else {
                Smer smer = new Smer(parts[1]);
                smer.addStudnet(s);
                smers.put(parts[1],smer);
            }
        }
        bf.close();
        return i;
    }

    public void writeTable(PrintStream out) {
        Comparator<Student1>comparator = Comparator.comparing(Student1::getAvgGrade).reversed();
        PrintWriter pw = new PrintWriter(out);
        List<String>keys = students.keySet().stream().sorted().collect(Collectors.toList());
        keys.forEach(k -> {
            pw.println(k);
            students.get(k).stream().sorted(comparator.thenComparing(Student1::getId))
                    .forEach(pw::println);
        }
        );
        pw.flush();
    }

    public void writeDistribution(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);

        Comparator<Smer>comparator = Comparator.comparing(Smer::getTens).reversed();
        smers.values().forEach(Smer::countGrades10);
        smers.values().stream().sorted(comparator).forEach(pw::println);

        pw.flush();
        pw.close();
    }
}
public class StudentRecordsTest {
    public static void main(String[] args) throws IOException {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}

// your code here