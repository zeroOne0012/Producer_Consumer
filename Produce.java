// import java.util.Scanner;
public class Produce implements Runnable {

    private Table table;
    public Produce(Table table) {
        this.table = table;
    }

    @Override
    public void run() {
        try {
            String producerName = "Producer" + (int) (Math.random() * 100);
            Task producer = new Task(producerName, 0);
            table.produce(producer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}