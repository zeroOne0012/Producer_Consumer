import java.util.Scanner;
public class Main {
    public static void main(String[] args) {  //wait queue 해결, ui 정리하면 끝?
        // Table table = new Table(4);
        
        // Scanner sc = new Scanner(System.in);
        // int choice;
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
                    mf.tempGetTable().printQueue();
                    break;
                    case 2:
                    mf.tempGetTable().printWaitQueue();
                    break;
            }
            if (choice == 4)
                break;
        }
        sc.close();
    }
}
        // System.out.println("1: produce\n2: consume\n3: print_queue\n4: break");
        // while (true) {
        //     choice = sc.nextInt();
        //     switch (choice) {
        //         case 1:
        //             Thread provider = new Thread(new Producer(table));
        //             provider.start();
        //             break;
        //         case 2:
        //             Thread consumer = new Thread(new Consumer(table));
        //             consumer.start();
        //             break;
        //         case 3:
        //             table.printQueue();
        //             break;
        //     }
        //     if (choice == 4)
        //         break;
        // }
        // sc.close();


// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;

// public class Main {
//     private static Thread criticalThread; // critical section에 진입한 스레드

//     public static void main(String[] args) {
//         JFrame frame = new JFrame("Thread Example");
//         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//         JPanel panel = new JPanel();
//         frame.add(panel);

//         JButton buttonA = new JButton("A");
//         buttonA.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 if (criticalThread == null) {
//                     criticalThread = new Thread(new CriticalSection());
//                     criticalThread.start();
//                 }
//             }
//         });
//         panel.add(buttonA);

//         JButton buttonB = new JButton("B");
//         buttonB.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 if (criticalThread != null) {
//                     criticalThread.interrupt();
//                     criticalThread = null;
//                 }
//             }
//         });
//         panel.add(buttonB);

//         frame.setSize(200, 100);
//         frame.setVisible(true);
//     }

//     static class CriticalSection implements Runnable {
//         @Override
//         public void run() {
//             try {
//                 // critical section 진입
//                 System.out.println("Entering critical section...");
//                 Thread.sleep(5000); // 일부러 시간 지연

//                 // critical section 탈출
//                 System.out.println("Exiting critical section...");
//                 criticalThread = null;
//             } catch (InterruptedException e) {
//                 // 스레드가 강제 종료되었을 때 처리
//                 System.out.println("Thread interrupted. Exiting critical section...");
//                 criticalThread = null;
//             }
//         }
//     }
// }


//////////////////////////


// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.*;

// public class Main extends JFrame {
//     private JLabel la = new JLabel("H");
//     public Main() {
//         setTitle("Test");
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         Container c = getContentPane();
//         c.addMouseListener(new MyMouseListener());
        
//         la.setSize(50, 20); // 레이블의 크기 50x20 설정
//         la.setLocation(30, 30); // 레이블의 위치 (30,30)으로 설정

//         c.setLayout(null);
//         c.add(la);

//         setSize(200,200);
//         setVisible(true);
//     }

//     class MyMouseListener implements MouseListener{ //extends MouseAdapter
//         public void mousePressed(MouseEvent e) {
//             int x = e.getX();
//             int y = e.getY();
//             la.setLocation(x, y);
//         }

//         public void mouseReleased(MouseEvent e) {
//         }
        
//         public void mouseClicked(MouseEvent e) {
//         }

//         public void mouseEntered(MouseEvent e) {
//         }

//         public void mouseExited(MouseEvent e) {
//         }

//     }
//     public static void main(String args[]) {
//         new Main();
//     }
// }







// public class Main {

//     public static void main(String args[]){
//         JFrame f1 = new JFrame("생산자 소비자");
//         JButton b1 = new JButton("btn");
//         b1.setBounds(10, 10, 100, 100);
//         Container c = getContentPane();
//         f1.add(b1);
//         f1.setSize(1000, 700);
//         f1.setLayout(null);
//         f1.setVisible(true);
//         f1.setLocation(50, 20);
//         f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//     }
// }

