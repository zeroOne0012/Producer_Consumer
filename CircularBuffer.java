import java.util.ArrayList;
import java.util.List;

public class CircularBuffer {
    public static final String PRODUCER = "Producer";
    public static final String CONSUMER = "Consumer";
    private final int SIZE = 4;

    private MySemaphore nrfull;
    private MySemaphore nrempty;
    private MySemaphore mutexP;
    private MySemaphore mutexC; // 세머퍼

    private String[] buffer; // 원형 버퍼
    private int head; // 새로 생산할 인덱스
    private int tail; // 소비될 물건의 인덱스
    private int sleepSeconds = 1; // stop 감지 주기: 0.1초
    private String recentInputMessage; // 최근 input message
    private String recentOutputMessage; // 최근 output message

    private boolean _finishProducing = false; // true: 생산 작업 finish
    private boolean _finishConsuming = false; // true: 소비 작업 finish
    
    private String producerInside = null; // 생산중인 생산자
    private String consumerInside = null; // 소비중인 소비자

    private List<ChangeListener> listeners; // buffer 변화를 MainFrame에서 감지

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
    public String getProducerInside(){
        return producerInside;
    }
    public String getConsumerInside(){
        return consumerInside;
    }

    public String getBufferValue(int index){
        if(index < 0 || index >= SIZE) return null;
        else return buffer[index];
    } // 버퍼 값을 읽음
    // public void printQueue() {
    //     System.out.printf("queue: ");
    //     for (String i : buffer) {
    //     System.out.printf(i + " ");
    //     }
    //     System.out.println();
    // }
    public String[] getBuffer(){
        return buffer;
    }
    public MySemaphore getNrfull(){
        return nrfull;
    }
    public MySemaphore getNrempty(){
        return nrempty;
    }
    public MySemaphore getMutexP(){
        return mutexP;
    }
    public MySemaphore getMutexC(){
        return mutexC;
    }

    public String getInput(){
        return recentInputMessage;
    }
    public String getOutput(){
        return recentOutputMessage;
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
        producerInside = producer.getName();
        String message = "m" + (int) (Math.random() * 100);
        buffer[head] = message;
        recentInputMessage = message;

        notifyListeners(); //생산이 시작됨 (gui 갱신)
        System.out.println("Producing "+message+"...");
        _finishProducing = false;
        while(!_finishProducing){
            Thread.sleep(sleepSeconds * 100);
        }
        _finishProducing = false;
        System.out.println("Produced " + message);

        head = (head + 1) % buffer.length;
        producerInside = null;
        notifyListeners(); //생산이 끝남 (gui 갱신)

        OperationV(nrfull);
        notifyListeners(); // 세머퍼 gui 갱신
        OperationV(mutexP);
        notifyListeners(); // 세머퍼 gui 갱신
    }

    public void consume(Task consumer) throws InterruptedException {
        // state가 1이면 A부터, 2이면 B부터, 3면 C부터

        // A
        if(consumer.getState()==Task.FIRST_TIME && !mutexC.P(consumer)){
            notifyListeners();
            return;
        } // consumer 상호 배제 검사

        // B
        if((consumer.getState()==Task.FIRST_TIME || consumer.getState()==Task.MUTEX_CHECKED) && !nrfull.P(consumer)){
            notifyListeners();
            return;
        } // 소비할 상품 유무 검사

        // C
        consumerInside=consumer.getName();

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
        consumerInside=null;
        notifyListeners(); // 소비가 끝남 (gui 갱신)

        OperationV(nrempty);
        notifyListeners(); // 세머퍼 gui 갱신
        
        OperationV(mutexC);
        recentOutputMessage = message;
        notifyListeners(); // 세머퍼, output gui 갱신
    }
}
