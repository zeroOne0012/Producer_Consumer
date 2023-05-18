// import java.util.Scanner;
public class Produce implements Runnable {
    private Table table;
    private Task task;
    public Produce(Table table) {
        this.table = table;
        String producerName = "Producer" + (int) (Math.random() * 100);
        this.task = new Task(producerName, 0);
    }
    public Produce(Table table, Task task) {
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