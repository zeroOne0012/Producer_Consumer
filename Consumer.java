public class Consumer implements Runnable {

    private Table table;

    public Consumer(Table table) {
        this.table = table;
    }

    @Override
    public void run() {
        try {
            String message = table.consume();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}