package lib;

import java.util.Vector;

/**
 * Created by AdminPC on 13.03.14.
 */
public class Main {
    public static void main(String args[]) {
        int T = 500;
        Data data = new Data();
        Handler[] h = new Handler[T];
        Thread[] t = new Thread[T];
        for(int i = 0; i < T; ++i) {
            h[i] = new Handler(data);
            t[i] = new Thread(h[i]);
        }
        for(int i = T-1; i >= 0; --i) {
            t[i].start();

        }
/*
        for(int i = 4; i >= 0; --i) {
            try {
                t[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
*/
/*
        while(t[0].isAlive() || t[1].isAlive() || t[2].isAlive() || t[3].isAlive() || t[4].isAlive()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
*/
        //System.out.println("Main is sleep )");
        data.ready();

        for(int i = 0; i < T; ++i) {
            System.out.println(i+1+" поток обработал "+h[i].getCounterCount());
        }

        int sum = 0;
        for(int i = 0; i < T; ++i) {
            sum += h[i].getCounterCount();
        }
        System.out.println(sum);
    }
}

class Data {
    final public int N = 1000000;
    private Vector<Integer> vector;
    private boolean isReady = false;
    private Integer returnNum = new Integer(0);


    public Data() {
        vector = new Vector<Integer>();
        for(int i = 0; i < N; ++i)
            vector.addElement(i);
    }

    public synchronized void getElem(Counter count) {
         // получаем первый элемент и обрабатываем
        if(!vector.isEmpty()) {
            vector.remove(vector.firstElement());
            count.inc();
            if(vector.size() % 10 == 0)
                notify();
        }
        else {
            isReady = true;
            notify();
        }
    }

    public synchronized void getElem(MyInt i) {
        // получаем первый элемент и обрабатываем
        if(!vector.isEmpty()) {
            i.set(vector.firstElement());
            vector.remove(vector.firstElement());

            if(vector.size() % 10 == 0)
                notify();
        }
        else {
            isReady = true;
            notify();
        }
    }

    public synchronized Integer getElem() {
        // получаем первый элемент и обрабатываем
        if(!vector.isEmpty()) {
            returnNum = vector.firstElement();
            vector.remove(vector.firstElement());

            if(vector.size() % 10 == 0)
                notify();

            return returnNum;
        }
        else {
            isReady = true;
            notify();
            return null;
        }
    }

    public synchronized boolean isEmpty() {
        return vector.isEmpty();
    }

    public synchronized boolean isReady() {
        return isReady;
    }

    public synchronized void ready() {
        while(!isReady)
            try {
                System.out.println(String.format("%.2f%% passed",100 - (vector.size() / (double)N * 100.0) ));
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}

class Counter {
    private int count = 0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void inc() {
        ++count;
    }
}

class Handler implements Runnable {
    Data data;
    Counter counter = new Counter();
    public Handler(Data data) {
        this.data = data;
    }

    @Override
    public void run() {
        MyInt i = new MyInt();
        Integer elem;

        while (!data.isReady()) {
            //data.getElem(counter);
            //data.getElem(i);
            elem = data.getElem();
            if(elem != null && elem % 10 == 0) //i.isSet()
                counter.inc();
            //System.out.println("\t\t"+Thread.currentThread().getName()+" -  "+i.get());

            Thread.yield();
        }
        //System.out.println("End thread: " + Thread.currentThread().getName());
    }

    public int getCounterCount() {
        return counter.getCount();
    }
}

class MyInt {
    private int i;
    private boolean isSet = false;
    public int get() {
        isSet = false;
        return i;
    }

    public void set(int i) {
        isSet = true;
        this.i = i;
    }

    public boolean isSet() {
        return isSet;
    }
}