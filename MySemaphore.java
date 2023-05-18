import java.util.Queue;
import java.util.LinkedList;
public class MySemaphore {
    private Queue<Task> waitingQueue;
    private int permit;
    public MySemaphore(int permit){
        waitingQueue = new LinkedList<Task>();
        this.permit = permit;
    }

    public Queue<String> getWaitingQueue(){
        Queue<String> waitingNameQueue = new LinkedList<>();
        for (Task person : waitingQueue) {
            waitingNameQueue.add(person.getName());
        }
        return waitingNameQueue;
    }

    public boolean P(Task task){
        //Critical Section 진입 가능 여부 반환
        task.setState(task.getState()+1);
        if(permit>0){
            permit--;
            return true;
        } else{
            waitingQueue.add(task);
            return false;
        }
    }
    public Task V(){
        if(!waitingQueue.isEmpty()){
            return waitingQueue.poll();
        } else{
            permit++;
            return null;
        }
    }
}
