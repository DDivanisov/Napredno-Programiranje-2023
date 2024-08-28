package os;

class ThreadClass extends Thread{
    String name;
    Incrementer incrementer;
    public ThreadClass(Incrementer incrementer, String name){
        this.incrementer = incrementer;
        this.name = name;
    }
    @Override
    public void run() {
        super.run();
        //System.out.print("It's born my G!\n");
        for(int i=1; i<=20;i++){
            System.out.printf("This is the %sth cycle of thread %s\n",i,name);
            incrementer.increment();
        }

    }
}
class Incrementer{
    private int coount = 0;

    public synchronized void increment() {
        this.coount++;
    }

    public int getCoount() {
        return coount;
    }
}
public class Synchronization {
    public static void main(String[] args) throws InterruptedException {
        Incrementer incrementer = new Incrementer();
        ThreadClass t1 = new ThreadClass(incrementer,"T1");
        ThreadClass t2 = new ThreadClass(incrementer,"T2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.printf("%s",incrementer.getCoount());
    }
}

