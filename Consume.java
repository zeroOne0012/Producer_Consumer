public class Consume implements Runnable {
    private Table table;
    private Task task;
    public Consume(Table table) {
        this.table = table;
        String consumerName = "Consumer" + (int) (Math.random() * 100);
        this.task = new Task(consumerName, 0);
    }
    public Consume(Table table, Task task) {
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