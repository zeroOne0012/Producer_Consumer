import java.util.Scanner;
public class Main {
    public static void main(String[] args) {  
        MainFrame mf = new MainFrame();
        Thread changeDetector = new Thread(new ChangeDetector(mf));
        changeDetector.start();


        Scanner sc = new Scanner(System.in);
        int choice;
        System.out.println("1: print_queue\n2: print_waitQueue\n4: break");
        while (true) {
            choice = sc.nextInt();
            switch (choice) {

                case 1:
                    mf.debugTable().printQueue();
                    break;
                    case 2:
                    System.out.println("nrfull" + mf.debugTable().getNrfull().toString());
                    System.out.println("nrempty" + mf.debugTable().getNremptry().toString());
                    System.out.println("mutexP" + mf.debugTable().getMutexP().toString());
                    System.out.println("mutexC" + mf.debugTable().getMutexC().toString());

                    break;
            }
            if (choice == 4)
                break;
        }
        sc.close();
    }
}