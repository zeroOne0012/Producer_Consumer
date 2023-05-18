public class Consume implements Runnable {

    private Table table;

    public Consume(Table table) {
        this.table = table;
    }

    @Override
    public void run() {
        try {
            String consumerName = "Consumer" + (int) (Math.random() * 100);
            Task consumer = new Task(consumerName, 0);
            String message = table.consume(consumer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}