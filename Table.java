import java.util.LinkedList;
import java.util.Queue;

public class Table {
    public static final String PRODUCER = "Producer";
    public static final String CONSUMER = "Consumer";
    private final int SIZE = 4;

    private MySemaphore nrfull;
    private MySemaphore nrempty;
    private MySemaphore mutexP;
    private MySemaphore mutexC;

    private String[] buffer; // 원형 버퍼
    private int head; // 새로 생산할 인덱스
    private int tail; // 소비될 물건의 인덱스
    private int productCount; // 버퍼 내의 물건 개수
    private int sleepSeconds = 1;
    private boolean stop = false; // 생산 또는 소비 중단 플래그
    private int somethingChanged = 0; // 변화를 ChangeDetector class에서 감지
    
    public String personInBuffer;

    public Table() {
        buffer = new String[SIZE];
        nrfull = new MySemaphore(0);
        nrempty = new MySemaphore(SIZE);
        mutexP = new MySemaphore(1);
        mutexC = new MySemaphore(1);
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
    public String[] getBuffer(){
        return buffer;
    }
    public Queue<String> getNrfull(){
        return nrfull.getWaitingQueue();
    }
    public Queue<String> getNremptry(){
        return nrempty.getWaitingQueue();
    }
    public Queue<String> getMutexP(){
        return mutexP.getWaitingQueue();
    }
    public Queue<String> getMutexC(){
        return mutexC.getWaitingQueue();
    }

    public void finish(){
        stop=true;
    }
    private void OperationV(MySemaphore sem){
        Task wakeup = sem.V();
        if(wakeup!=null){
            somethingChanged++;
            if(wakeup.getName().contains(PRODUCER)){
                Thread produce = new Thread(new Produce(this, wakeup));
                produce.start();
            } else if(wakeup.getName().contains(CONSUMER)){
                Thread consume = new Thread(new Consume(this, wakeup));
                consume.start();
            } else{
                System.out.println("String Error");
            }
        }
    }
    public void produce(Task producer) throws InterruptedException {
        // state가 1이면 A부터, 2이면 B부터, 3면 C부터

        // A
        if(producer.getState()==Task.FIRST_TIME && !mutexP.P(producer)){
            somethingChanged++;
            return;
        }

        // B
        if((producer.getState()==Task.FIRST_TIME || producer.getState()==Task.MUTEX_CHECKED) && !nrempty.P(producer)){
            somethingChanged++;
            return;
        }

        // C
        personInBuffer = PRODUCER;
        String message = "m" + (int) (Math.random() * 20);

        Flags.workingSpace = head;
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

        personInBuffer = null;
        somethingChanged++; //작업이 끝남
        OperationV(nrfull);
        OperationV(mutexP);
    }

    public String consume(Task consumer) throws InterruptedException {
        // state가 1이면 A부터, 2이면 B부터, 3면 C부터

        // A
        if(consumer.getState()==Task.FIRST_TIME && !mutexC.P(consumer)){
            somethingChanged++;
            return null;
        } // consumer 상호 배제 검사

        // B
        if((consumer.getState()==Task.FIRST_TIME || consumer.getState()==Task.MUTEX_CHECKED) && !nrfull.P(consumer)){
            somethingChanged++;
            return null;
        } // 소비할 상품 유무 검사

        // C
        personInBuffer = CONSUMER;

        Flags.workingSpace = tail; // tail, head return function !!

        String message = buffer[tail];

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

        personInBuffer = null;
        
        OperationV(nrempty);
        OperationV(mutexC);

        return message;
    }
}
