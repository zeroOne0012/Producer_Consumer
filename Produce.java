public class Produce implements Runnable {
    // 생산 작업 실행
    private CircularBuffer table;
    private Task task;
    public Produce(CircularBuffer table, String producerName) {
        this.table = table;
        this.task = new Task(producerName, 0);
    } // 처음 생성시
    public Produce(CircularBuffer table, Task task) {
        this.table = table;
        this.task = task;
    } // 중단되었던 task 실행시

    @Override
    public void run() {
        try {
            table.produce(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}