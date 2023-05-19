public class Consume implements Runnable {
    private CircularBuffer table;
    private Task task;
    public Consume(CircularBuffer table) {
        this.table = table;
        String consumerName = "Consumer" + (int) (Math.random() * 100);
        this.task = new Task(consumerName, 0);
    }
    public Consume(CircularBuffer table, Task task) {
        this.table = table;
        this.task = task;
    }
    @Override
    public void run() {
        try {
            String message = table.consume(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}