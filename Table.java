import java.util.LinkedList;
import java.util.Queue;

public class Table {
    private final int SIZE = 4;
    private int nrfull = 0;
    private int nrempty = SIZE;
    private int mutexP = 1;
    private int mutexC = 1;
    private String[] buffer; // 원형 버퍼
    private int head; // 새로 생산할 인덱스
    private int tail; // 소비될 물건의 인덱스
    private int productCount; // 버퍼 내의 물건 개수
    private int sleepSeconds = 1;
    private Queue<Character> waitQueue; //대기 큐///////////삭제할 것
    private boolean stop = false; // 생산 또는 소비 중단 플래그
    private int somethingChanged = 0; // 변화를 ChangeDetector class에서 감지

    public Table() {
        buffer = new String[SIZE];
        waitQueue = new LinkedList<Character>();
    }
    
    public int getChanged(){
        return somethingChanged;
    }
    public int getSIZE() {
        return this.SIZE;
    }
    public void printQueue() {
        System.out.printf("queue: ");
        for (String i : buffer) {
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
    public String[] getBuffer(){
        return buffer;
    }
    public Queue<Character> getWaitQueue(){
        return waitQueue;
    }

    public void finish(){
        stop=true;
    }

    // 큐에서 값을 하나 빼내는 메소드
    synchronized public String consume() throws InterruptedException {
        while (productCount <= 0) {
            waitQueue.add('C');
            wait();
            if (productCount > 0)
                waitQueue.poll();
        }
        Flags.isWorking = true;
        Flags.workingSpace = tail;

        String message = buffer[tail];


        // mutexP.tryAcquire(SIZE, null)@@@@@@@
        somethingChanged++; //작업이 들어옴 (ui갱신)
        System.out.println("Consuming "+message+"...");
        while(!stop){
            Thread.sleep(sleepSeconds * 100);
        }
        stop = false;
        System.out.println("Consumed "+message);
        buffer[tail] = null;
        tail = (tail + 1) % buffer.length;
        productCount--;
        somethingChanged++; //작업이 끝남

        Flags.isWorking = false;
        Flags.workingPerson = null;
        return message;
    }

    //큐에 값을 삽입하는 메소드

    synchronized public void produce() throws InterruptedException {

        while (productCount >= buffer.length) {
            waitQueue.add('P');
            wait();
            if (productCount < 0)
                waitQueue.poll();
        }
        Flags.isWorking = true;
        Flags.workingSpace = head;
        String message = "m" + (int) (Math.random() * 20);
        buffer[head] = message;


        somethingChanged++; //작업이 들어옴
        System.out.println("Producing "+message+"...");
        while(!stop){
            Thread.sleep(sleepSeconds * 100);
        }
        stop = false;
        System.out.println("Produced " + message);

        head = (head + 1) % buffer.length;
        productCount++;

        Flags.isWorking = false;
        Flags.workingPerson = null;
        somethingChanged++; //작업이 끝남
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
