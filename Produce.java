// import java.util.Scanner;
public class Produce implements Runnable {
    private CircularBuffer table;
    private Task task;
    public Produce(CircularBuffer table, String producerName) {
        this.table = table;
        this.task = new Task(producerName, 0);
    }
    public Produce(CircularBuffer table, Task task) {
        this.table = table;
        this.task = task;
    }

    @Override
    public void run() {
        try {
            table.produce(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}