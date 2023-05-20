import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CircularBuffer {
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
    private boolean _finishProducing = false; // true: 생산 작업 finish
    private boolean _finishConsuming = false; // true: 소비 작업 finish
    
    private boolean _producerIsInside = false;
    private boolean _consumerIsInside = false;

    private List<ChangeListener> listeners;


    public CircularBuffer() {
        buffer = new String[SIZE];
        nrfull = new MySemaphore(0);
        nrempty = new MySemaphore(SIZE);
        mutexP = new MySemaphore(1);
        mutexC = new MySemaphore(1);
        listeners = new ArrayList<>();
    }
    

    public int getSIZE() {
        return this.SIZE;
    }
    public int getHead(){
        return head;
    }
    public int getTail(){
        return tail;
    }
    public boolean producerIsInside(){
        return _producerIsInside;
    }
    public boolean consumerIsInside(){
        return _consumerIsInside;
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
    public MySemaphore getNrfull(){
        return nrfull;
    }
    public MySemaphore getNremptry(){
        return nrempty;
    }
    public MySemaphore getMutexP(){
        return mutexP;
    }
    public MySemaphore getMutexC(){
        return mutexC;
    }

    public void finishProducing(){
        _finishProducing=true;
    }
    public void finishConsuming(){
        _finishConsuming=true;
    }
    public void addChangeListener(ChangeListener listener){
        listeners.add(listener);
    }
    private void notifyListeners(){
        for (ChangeListener listener : listeners) {
            listener.somethingChanged();
        }
    }

    private void OperationV(MySemaphore sem){
        Task wakeup = sem.V();
        if(wakeup!=null){
            notifyListeners();
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
            notifyListeners();
            return;
        }

        // B
        if((producer.getState()==Task.FIRST_TIME || producer.getState()==Task.MUTEX_CHECKED) && !nrempty.P(producer)){
            notifyListeners();
            return;
        }

        // C
        _producerIsInside = true;
        String message = "m" + (int) (Math.random() * 100);

        buffer[head] = message;

        notifyListeners(); //생산이 시작됨 (gui 갱신)
        System.out.println("Producing "+message+"...");
        _finishProducing = false;
        while(!_finishProducing){
            Thread.sleep(sleepSeconds * 100);
        }
        _finishProducing = false;
        System.out.println("Produced " + message);

        head = (head + 1) % buffer.length;
        productCount++;

        _producerIsInside = false;
        notifyListeners(); //생산이 끝남 (gui 갱신)
        OperationV(nrfull);
        notifyListeners(); // 세머퍼 gui 갱신
        OperationV(mutexP);
        notifyListeners(); // 세머퍼 gui 갱신
    }

    public String consume(Task consumer) throws InterruptedException {
        // state가 1이면 A부터, 2이면 B부터, 3면 C부터

        // A
        if(consumer.getState()==Task.FIRST_TIME && !mutexC.P(consumer)){
            notifyListeners();
            return null;
        } // consumer 상호 배제 검사

        // B
        if((consumer.getState()==Task.FIRST_TIME || consumer.getState()==Task.MUTEX_CHECKED) && !nrfull.P(consumer)){
            notifyListeners();
            return null;
        } // 소비할 상품 유무 검사

        // C
        _consumerIsInside=true;

        String message = buffer[tail];

        notifyListeners(); // 소비가 시작됨 (gui갱신)
        System.out.println("Consuming "+message+"...");
        _finishConsuming = false;
        while(!_finishConsuming){
            Thread.sleep(sleepSeconds * 100);
        } 
        _finishConsuming = false;
        System.out.println("Consumed "+message);
        buffer[tail] = null;
        tail = (tail + 1) % buffer.length;
        productCount--;

        _consumerIsInside=false;
        notifyListeners(); // 소비가 끝남 (gui 갱신)
        OperationV(nrempty);
        notifyListeners(); // 세머퍼 gui 갱신
        OperationV(mutexC);
        notifyListeners(); // 세머퍼 gui 갱신
        return message;
    }
}
