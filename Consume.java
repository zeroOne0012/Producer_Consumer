public class Consume implements Runnable {
    // 소비 작업 실행
    private CircularBuffer table;
    private Task task;
    public Consume(CircularBuffer table, String consumerName) {
        this.table = table;
        this.task = new Task(consumerName, 0);
    } // 처음 실행시
    public Consume(CircularBuffer table, Task task) {
        this.table = table;
        this.task = task;
    } // 중단되었던 task 실행시

    @Override
    public void run() {
        try {
            table.consume(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}