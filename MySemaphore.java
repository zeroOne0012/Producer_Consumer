import java.util.Queue;
import java.util.LinkedList;
public class MySemaphore {
    private int semaphore;
    private Queue<Task> waitingQueue;

    public MySemaphore(int semaphore){
        this.semaphore = semaphore;
        waitingQueue = new LinkedList<Task>();
    }

    public String getState(){
        return semaphore + "";
    }
    public Queue<String> getWaitingQueue(){
        Queue<String> waitingNameQueue = new LinkedList<>();
        for (Task person : waitingQueue) {
            waitingNameQueue.add(person.getName());
        }
        return waitingNameQueue;
    }

    public boolean P(Task task){
        // Critical Section 진입 가능 여부 반환
        task.setState(task.getState()+1);
        if(semaphore>0){
            semaphore--;
            return true;
        } else{ // 중단
            waitingQueue.add(task);
            return false;
        }
    }
    public Task V(){
        // P 연산으로 중단되었던 task wakeup
        if(!waitingQueue.isEmpty()){
            return waitingQueue.poll();
        } else{
            semaphore++;
            return null;
        }
    }
}
