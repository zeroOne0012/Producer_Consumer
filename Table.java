import java.util.LinkedList;
import java.util.Queue;

public class Table {

    private String[] queue;
    private int capacity;
    private int head;
    private int tail;
    private int productCount;
    private static int sleepSeconds;
    private Queue<Character> waitQueue;

    public Table(int capacity) {
        this.capacity = capacity;
        queue = new String[capacity];
        waitQueue = new LinkedList<Character>();
        sleepSeconds = 1;
    }
    
    public int getCapacity() {
        return this.capacity;
    }
    public void printQueue() {
        System.out.printf("queue: ");
        for (String i : queue) {
        System.out.printf(i + " ");
        }
        System.out.println();
    }
    public void printWaitQueue() {
        System.out.printf("waitQueue: ");
        for (char i : waitQueue) {
        System.out.printf(i + " ");
        }
        System.out.println();
    }
    public String[] getQueue(){
        return queue;
    }
    public Queue<Character> getWaitQueue(){
        return waitQueue;
    }


    // 큐에서 값을 하나 빼내는 메소드
    public synchronized String consume() throws InterruptedException {
        while (productCount <= 0) {
            waitQueue.add('C');
            wait();
            if (productCount > 0)
                waitQueue.poll();
        }

        String message = queue[tail];
        queue[tail] = null;
        tail = (tail + 1) % queue.length;
        productCount--;

        Flags.somethingChanged++;
        System.out.println("Consuming "+message+"...");
        // while(!isDone){
        Thread.sleep(sleepSeconds * 1000);
        // }
        System.out.println("Consumed "+message);
        notify();
        return message;
    }

    //큐에 값을 삽입하는 메소드

    public synchronized void produce() throws InterruptedException {

        while (productCount >= queue.length) {
            waitQueue.add('P');
            wait();
            if (productCount < 0)
                waitQueue.poll();
        }
        String message = "m" + (int) (Math.random() * 20);
        queue[head] = message;
        head = (head + 1) % queue.length;
        productCount++;

        Flags.somethingChanged++;
        System.out.println("Producing "+message+"...");
        Thread.sleep(sleepSeconds * 1000);
        System.out.println("Produced " + message);
        notify();
    }
}
// public class Table {
// private int capacity;
// private String[] queue;
// private int head;
// private int tail;
// private int productCount;
// private int sleepSeconds;
// private char[] taskArray;

// public Table(int capacity) {
//     this.capacity = capacity;
//     queue = new String[capacity];
//     taskArray = new char[capacity];
// }

// public synchronized void produce() throws InterruptedException {

//     String message = "m" + (int) (Math.random() * 20);
//     while (productCount >= queue.length) {
//         wait();
//     }
//     queue[head] = message;
//     taskArray[head] = 'P';
//     head = (head + 1) % queue.length;
//     productCount++;

//     notify();
// }

// public synchronized String consume() throws InterruptedException {
//     while (productCount <= 0) {
//         wait();
//     }

//     String message = queue[tail];
//     taskArray[tail] = 'C';
//     queue[tail] = null;
//     tail = (tail + 1) % queue.length;
//     productCount--;

//     notify();

//     return message;
// }

// public void printQueue() {
//     System.out.printf("queue: ");
//     for (String i : queue) {
//         System.out.printf(i + " ");
//     }
//     System.out.println();
// }

// public int getCapacity() {
//     return this.capacity;
// }

// public char[] getTaskArray() {
//     return this.taskArray;
// }
// }
