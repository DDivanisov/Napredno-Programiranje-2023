package os;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ParallelSearchThread extends Thread {

    SharedArray arr;
    int start;
    int end;
    int searching;

    //inicijalizacija
    public ParallelSearchThread(SharedArray arr, int start, int end, int searching){
        this.arr=arr;
        this.start=start;
        this.end=end;
        this.searching=searching;
    }

    @Override
    public void run() {
        super.run();

        // se zema spodelenata niza po referenca
        int [] array=arr.getArray();

        int localCount=0; // lokalen brojach
        for (int i=start;i<end;i++){
            if(array[i]==searching){    //ako sme nasle edno pojavuvanje na brojot
                localCount++;
            }
        }

        arr.threadDone(localCount); // otkako ke zavrsime, signalizirame na spodeleniot resurs

        try {
            if(arr.checkMax()==localCount){ // proverka za dali nie sme go nasle max brojot
                System.out.println("I found the max occurences!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class SharedArray {
    private int [] array;
    private int size;
    private int totalOccurences;
    private int maxPerThread;
    private int threadsWorking;

    Semaphore semaphore;    // semafor za sunkronizacija na site tredovi da zavrsat

    Lock lock= new ReentrantLock(); // lock za spodelena promenliva

    public SharedArray(int[] array, int size) {
        this.array = array;
        this.size = size;
        //inicijalizacija na niza
    }

    // setirame kolku paralelni nitki ke rabotat
    public void setThreadsWorking(int threadsWorking) {
        this.threadsWorking = threadsWorking;
        semaphore= new Semaphore(0);

    }

    //koga nitkata ke zavrsi so broenje:
    public void threadDone(int threadFound){
        lock.lock();        // zaklucuva spodelena promenliva
        this.totalOccurences+=threadFound;  //inkrementira
        if(threadFound>this.maxPerThread){
            this.maxPerThread=threadFound;  //setira max
        }
        lock.unlock();              //otklucuva spodelena promenliva
        semaphore.release();        // osloboduva eden permit
    }

    public int getTotalOccurences() throws InterruptedException {
        semaphore.acquire(threadsWorking);  // mora da pocekame site nitki da zavrsat
        semaphore.release(threadsWorking);  // osloboduvame pak ist broj na permiti
        return totalOccurences;             // sega sme sigurni deka site zavrsile so broenje
    }

    public int checkMax() throws InterruptedException {
        semaphore.acquire(threadsWorking);  // mora da pocekame site nitki da zavrsat
        semaphore.release(threadsWorking);
        return maxPerThread;
    }

    public int[] getArray() {
        return array;       // nema potreba od zaklucuvanje- nizata ne se menuva
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Random rand= new Random();

        int n=10000;
        int [] ar=new int[n];
        for(int i=0;i<n;i++){
            ar[i]= rand.nextInt(150);
        }

        SharedArray sa= new SharedArray(ar,n);      //inicijalizacija na spodelen resurs
        int threads=5;
        sa.setThreadsWorking(threads);

        int searchFor=137;

        ArrayList<ParallelSearchThread> apt= new ArrayList<ParallelSearchThread>();
        int chunk=n/threads;        // golemina na parce od nizata so koe ke raboti sekoj thread

        for(int i=0;i<threads;i++) {
            ParallelSearchThread pt = new ParallelSearchThread(sa, chunk*i , chunk*i+chunk, searchFor);
            // kreiraj i startuvaj nov thread
            apt.add(pt);
            pt.start();
        }

        try {
            System.out.println("Total found: "+sa.getTotalOccurences());    //ova ke ceka site da zavrsat so broenje
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i=0;i<threads;i++) {

            apt.get(i).join();
        }


    }
}

