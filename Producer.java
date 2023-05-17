// import java.util.Scanner;
public class Producer implements Runnable {

    private Table table;
    public Producer(Table table) {
        this.table = table;
    }

    @Override
    public void run() {
        try {
            // Scanner sc = new Scanner(System.in);
            // String message = sc.nextLine();
            table.produce();
            // sc.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}