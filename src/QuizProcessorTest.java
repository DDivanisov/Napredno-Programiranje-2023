import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

class QuizExeption extends Exception{
    public QuizExeption(){
        super("A quiz must have same number of correct and selected answers");
    }
}

class Quiz{
    private List<String>C_answ;
    private List<String>S_answ;
    private int corect;
    private int incorect;

    public Quiz() {
        C_answ = new ArrayList<>();
        S_answ = new ArrayList<>();
        corect = 0;
        incorect = 0;
    }
    public void MakeLists(String c_a, String s_a){
        String parts[] = c_a.split(", ");
        C_answ.addAll(Arrays.asList(parts));
        parts = s_a.split(", ");
        S_answ.addAll(Arrays.asList(parts));
    }
    public void countCorect(){
        for(int i =0; i < C_answ.size(); i++){
            if (S_answ.get(i).equals(C_answ.get(i))){
                corect++;
            }
            else {
                incorect++;
            }
        }
    }
    public double CalculatePoints(){
        double result = 0.0;
        countCorect();
        result = corect*1.0 - incorect*0.25;
        return result;
    }
    public int getLen_C_ans(){
        return C_answ.size();
    }
    public int getLen_S_ans(){
        return S_answ.size();
    }
}

class QuizProcessor{


    public static Map<Integer, Double> processAnswers(InputStream input) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(input));
        Map<Integer, Double> result = new TreeMap<>();
        String line;
        while((line = bf.readLine()) != null) {

            try {
                String parts[] = line.split(";");
                int index = Integer.parseInt(parts[0]);
                Quiz q = new Quiz();
                q.MakeLists(parts[1], parts[2]);
                if (q.getLen_C_ans() != q.getLen_S_ans()) {
                    throw new QuizExeption();
                }
                else{
                    double r = q.CalculatePoints();
                    result.put(index, r);
                }
            }
            catch(QuizExeption e){
                System.out.printf("%s\n", e.getMessage());
            }
        }
        bf.close();
        return result;
    }
}

public class QuizProcessorTest {
    public static void main(String[] args) throws IOException {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));

    }
}

