import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;


import java.util.LinkedList;
public class MainFrame extends JFrame {

    private Table table;
    private JTextArea waitingList;
    private Queue<Character> taskQueue;
    private JTable taskQueueTable;
    private JTable waitQueueTable;
    private JPanel queuePanel;
    private JScrollPane scrollPane;
    private JScrollPane taskQueueScrollPane;
    private JScrollPane waitQueueScrollPane;
    
                public Table tempGetTable(){
                    return table;
                }
    public MainFrame() {
        setTitle("Producer-Consumer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLayout(new BorderLayout());
        table = new Table(4);
        taskQueue = new LinkedList();
        JPanel buttonPanel = new JPanel();
        JButton produceButton = new JButton("Produce");
        JButton consumeButton = new JButton("Consume");
        JButton startButton = new JButton("Start");
        JButton FinishButton = new JButton("Finish");

        buttonPanel.add(produceButton);
        buttonPanel.add(consumeButton);
        buttonPanel.add(startButton);
        buttonPanel.add(FinishButton);
        add(buttonPanel, BorderLayout.NORTH);

        waitingList = new JTextArea();
        scrollPane = new JScrollPane(waitingList);
        add(scrollPane, BorderLayout.CENTER);

        // Create a JPanel with four circles to represent the state of table.queue
        queuePanel = new JPanel();
        queuePanel.setLayout(new GridLayout(1, 4));
        for (int i = 0; i < table.getCapacity(); i++) {
            JPanel circlePanel = new JPanel();
            circlePanel.setPreferredSize(new Dimension(50, 50));
            circlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            queuePanel.add(circlePanel);
        }
        add(queuePanel, BorderLayout.SOUTH);

    // Create JTable for task queue
        taskQueueTable = new JTable(new QueueModel(taskQueue, "test"));
        taskQueueScrollPane = new JScrollPane(taskQueueTable);
        taskQueueScrollPane.setPreferredSize(new Dimension(150, 0));
        add(taskQueueScrollPane, BorderLayout.WEST);

        // Create JTable for wait queue
        waitQueueTable = new JTable(new QueueModel(table.getWaitQueue(), "test2"));
        waitQueueScrollPane = new JScrollPane(waitQueueTable);
        waitQueueScrollPane.setPreferredSize(new Dimension(150, 0));
        add(waitQueueScrollPane, BorderLayout.EAST);


        produceButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            taskQueue.add('P');
            // 버튼을 누를 때마다 업데이트
            taskQueueTable.setModel(new QueueModel(taskQueue, "test"));
        }
        });

        consumeButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            taskQueue.add('C');
            // 버튼을 누를 때마다 업데이트
            taskQueueTable.setModel(new QueueModel(taskQueue, "test"));
        }
        });
        
        // FinishButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e){
        //         taskQueueTable.setModel(new QueueModel(taskQueue));
        //         waitQueueTable.setModel(new QueueModel(table.getWaitQueue()));
        //         System.out.println("@");
        //     }
        // });
        startButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (!taskQueue.isEmpty()) {
                char task = taskQueue.poll();
                if (task == 'P') {
                    Flags.ISDONE = false;
                    Thread provider = new Thread(new Producer(table));
                    provider.start();
                    // waitingList.append("Produced\n");
                } else if (task == 'C') {
                    Flags.ISDONE = false;
                    Thread consumer = new Thread(new Consumer(table));
                    consumer.start();
                    // waitingList.append("Consumed\n");
                }
            } else {
                waitingList.append("No tasks in queue\n");
            }
            // 버튼을 누를 때마다 업데이트
            // circleQueue.setModel(new QueueTable(table.getQueue()));
            // while(!Flags.ISDONE){

            // }

            // String waitQueueString = "";
            // for (char c : table.getWaitQueue()) {
            //     waitQueueString += c + " ";
            // }
            // waitingList.setText(waitQueueString);

            // Update the circles to represent the state of table.queue            
        }
        });

    setVisible(true);
    }
    public void renew(){
        for (int i = 0; i < 4; i++) {
            waitingList.append(table.getQueue()[i] + " ");

        }
        waitingList.append("\n");
       
        for (int i = 0; i < table.getCapacity(); i++) {
            JPanel circlePanel = (JPanel) queuePanel.getComponent(i);
            if (table.getQueue()[i] != null) {
                circlePanel.setBackground(Color.GREEN);
            } else {
                circlePanel.setBackground(Color.WHITE);
            }
        }
        taskQueueTable.setModel(new QueueModel(taskQueue, "test"));
        waitQueueTable.setModel(new QueueModel(table.getWaitQueue(), "test2"));
    }
}
// public MainFrame() {
//     setTitle("Producer-Consumer");
//     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//     setSize(500, 500);
//     setLayout(new BorderLayout());
//     table = new Table(4);
// taskQueue = new LinkedList<Character>();

// JPanel buttonPanel = new JPanel();
// JButton produceButton = new JButton("Produce");
// JButton consumeButton = new JButton("Consume");
// JButton startButton = new JButton("Start");
// buttonPanel.add(produceButton);
// buttonPanel.add(consumeButton);
// buttonPanel.add(startButton);
// add(buttonPanel, BorderLayout.NORTH);

// waitingList = new JTextArea();
// JScrollPane scrollPane = new JScrollPane(waitingList);
// add(scrollPane, BorderLayout.CENTER);

// JTable taskQueueTable = new JTable(new QueueModel(taskQueue));
// JTable circleQueue = new JTable(new QueueModel(table.getQueue()));
// JTable waitQueueTable = new JTable(new QueueModel(table.getWaitQueue()));

// JScrollPane taskQueueScrollPane = new JScrollPane(taskQueueTable);
// JScrollPane tableQueueScrollPane = new JScrollPane(circleQueue);
// JScrollPane waitQueueScrollPane = new JScrollPane(waitQueueTable);

// add(taskQueueScrollPane, BorderLayout.WEST);
// add(tableQueueScrollPane, BorderLayout.CENTER);
// add(waitQueueScrollPane, BorderLayout.EAST);

// produceButton.addActionListener(new ActionListener() {
//     public void actionPerformed(ActionEvent e) {
//         taskQueue.add('P');
//         waitingList.append("Producing...\n");
//         // 버튼을 누를 때마다 업데이트
//         taskQueueTable.setModel(new QueueModel(taskQueue));
//     }
// });

// consumeButton.addActionListener(new ActionListener() {
//     public void actionPerformed(ActionEvent e) {
//         taskQueue.add('C');
//         waitingList.append("Consuming...\n");
//         // 버튼을 누를 때마다 업데이트
//         taskQueueTable.setModel(new QueueModel(taskQueue));
//     }
// });

// startButton.addActionListener(new ActionListener() {
//     public void actionPerformed(ActionEvent e) {
//         if (!taskQueue.isEmpty()) {
//             char task = taskQueue.poll();
//             if (task == 'P') {
//                 Thread provider = new Thread(new Producer(table));
//                 provider.start();
//                 waitingList.append("Produced\n");
//             } else if (task == 'C') {
//                 Thread consumer = new Thread(new Consumer(table));
//                 consumer.start();
//                 waitingList.append("Consumed\n");
//             }
//         } else {
//             waitingList.append("No tasks in queue\n");
//         }
//         // 버튼을 누를 때마다 업데이트
//         circleQueue.setModel(new QueueModel(table.getQueue()));
//         waitQueueTable.setModel(new QueueModel(table.getWaitQueue()));
//         String waitQueueString = "";
//         for (char c : table.getWaitQueue()) {
//             waitQueueString += c + " ";
//         }
//         waitingList.setText(waitQueueString);
//     }
// });

// setVisible(true);
// }




// import java.awt.BorderLayout;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.util.LinkedList;
// import java.util.Queue;

// import javax.swing.JButton;
// import javax.swing.JFrame;
// import javax.swing.JPanel;
// import javax.swing.JScrollPane;
// import javax.swing.JTable;
// import javax.swing.JTextArea;
// import javax.swing.table.DefaultTableModel;

// public class MainFrame extends JFrame {

// private Table table;
// private JTextArea waitingList;
// private Queue<Character> taskQueue;
// private Queue<Character> waitingQueue;
// private JTable taskTable;
// private DefaultTableModel taskTableModel;

// public MainFrame() {
//     setTitle("Producer-Consumer");
//     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//     setSize(500, 500);
//     setLayout(new BorderLayout());

//     table = new Table(4);
//     taskQueue = new LinkedList<Character>();
//     waitingQueue = new LinkedList<Character>();

//     JPanel buttonPanel = new JPanel();
//     JButton produceButton = new JButton("Produce");
//     JButton consumeButton = new JButton("Consume");
//     JButton startButton = new JButton("Start");
//     buttonPanel.add(produceButton);
//     buttonPanel.add(consumeButton);
//     buttonPanel.add(startButton);
//     add(buttonPanel, BorderLayout.NORTH);

//     waitingList = new JTextArea();
//     JScrollPane scrollPane = new JScrollPane(waitingList);
//     add(scrollPane, BorderLayout.CENTER);

//     String[] columnNames = {"Task 1", "Task 2", "Task 3", "Task 4"};
//     Object[][] data = {table.getTaskArray()};
//     taskTableModel = new DefaultTableModel(data, columnNames);
//     taskTable = new JTable(taskTableModel);
//     JScrollPane taskScrollPane = new JScrollPane(taskTable);
//     add(taskScrollPane, BorderLayout.SOUTH);

//     produceButton.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//             taskQueue.add('P');
//             waitingList.append("Producing...\n");
//         }
//     });

//     consumeButton.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//             taskQueue.add('C');
//             waitingList.append("Consuming...\n");
//         }
//     });

//     startButton.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//             if (!taskQueue.isEmpty()) {
//                 char task = taskQueue.poll();
//                 if (task == 'P') {
//                     try {
//                         table.produce();
//                         waitingList.append("Produced\n");
//                     } catch (InterruptedException ex) {
//                         ex.printStackTrace();
//                     }
//                 } else if (task == 'C') {
//                     try {
//                         table.consume();
//                         waitingList.append("Consumed\n");
//                     } catch (InterruptedException ex) {
//                         ex.printStackTrace();
//                     }
//                 }
//                 taskTableModel.setValueAt(table.getTaskArray(), 0, 0);
//             } else {
//                 waitingList.append("No tasks in queue\n");
//             }
//         }
//     });

//     setVisible(true);
// }
// }

// import java.awt.BorderLayout;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.util.LinkedList;
// import java.util.Queue;

// import javax.swing.JButton;
// import javax.swing.JFrame;
// import javax.swing.JPanel;
// import javax.swing.JScrollPane;
// import javax.swing.JTable;
// import javax.swing.JTextArea;


// import java.util.LinkedList;
// import javax.swing.table.AbstractTableModel;

// class QueueModel extends AbstractTableModel {
//     private Queue<String> queue;

//     public QueueModel(String[] queue) {
//         this.queue = new LinkedList<String>();
//         for (String s : queue) {
//             this.queue.add(s);
//         }
//     }

//     public QueueModel(Queue<String> queue) {
//         this.queue = queue;
//     }

//     @Override
//     public int getRowCount() {
//         return queue.size();
//     }

//     @Override
//     public int getColumnCount() {
//         return 1;
//     }

//     @Override
//     public Object getValueAt(int rowIndex, int columnIndex) {
//         return queue.toArray(new String[0])[rowIndex];
//     }

//     @Override
//     public String getColumnName(int column) {
//         return "Queue";
//     }
// }
// public class MainFrame extends JFrame {

// private Table table;
// private JTextArea waitingList;
// private Queue<String> taskQueue;

// public MainFrame() {
//     setTitle("Producer-Consumer");
//     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//     setSize(500, 500);
//     setLayout(new BorderLayout());

//     table = new Table(4);
//     taskQueue = new LinkedList<String>();

//     JPanel buttonPanel = new JPanel();
//     JButton produceButton = new JButton("Produce");
//     JButton consumeButton = new JButton("Consume");
//     JButton startButton = new JButton("Start");
//     buttonPanel.add(produceButton);
//     buttonPanel.add(consumeButton);
//     buttonPanel.add(startButton);
//     add(buttonPanel, BorderLayout.NORTH);

//     waitingList = new JTextArea();
//     JScrollPane scrollPane = new JScrollPane(waitingList);
//     add(scrollPane, BorderLayout.CENTER);

//     JTable taskQueueTable = new JTable(new QueueModel(taskQueue));
//     JTable circleQueue = new JTable(new QueueModel(table.getQueue()));

// JScrollPane taskQueueScrollPane = new JScrollPane(taskQueueTable);
// JScrollPane tableQueueScrollPane = new JScrollPane(circleQueue);

// add(taskQueueScrollPane, BorderLayout.WEST);
// add(tableQueueScrollPane, BorderLayout.CENTER);

//     produceButton.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//             taskQueue.add("Producer");
//             waitingList.append("Producing...\n");
//         }
//     });

//     consumeButton.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//             taskQueue.add("Consumer");
//             waitingList.append("Consuming...\n");
//         }
//     });

//     startButton.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//             if (!taskQueue.isEmpty()) {
//                 String task = taskQueue.poll();
//                 if (task == "Producer") {
//                     Thread provider = new Thread(new Producer(table));
//                     provider.start();
//                     waitingList.append("Produced\n");
//                 } else if (task == "Consumer") {
//                     Thread consumer = new Thread(new Consumer(table));
//                     consumer.start();
//                     waitingList.append("Consumed\n");
//                 }
//             } else {
//                 waitingList.append("No tasks in queue\n");
//             }
//         }
//     });

//     setVisible(true);
// }
// }

// // // import javax.swing.*;
// // // import java.awt.*;
// // // import java.awt.event.*;

// // // public class MainFrame extends JFrame {

// // //     public MainFrame() {
// // //         setTitle("생산자 소비자 테스트");
// // //         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// // //         setSize(500, 500);
// // //         setLayout(null);
// // //         setVisible(true);
// // //         setLocation(100, 100);

// // //         Container c = getContentPane();
// // //         JButton P = new JButton("Produce");
// // //         c.add(P);
// // //         P.addActionListener(new ActionListener() {
// // //             int choice;

// // //             @Override
// // //             public void actionPerformed(ActionEvent e) {
// // //                 choice = 1;
// // //             }
// // //         });

// // //     }

// // // }
// // import javax.swing.*;
// // import java.awt.*;
// // import java.awt.event.*;

// // public class MainFrame extends JFrame {
// //     private Table table;
// //     private JTextArea waitingList;

// //     public MainFrame() {
// //         setTitle("Producer-Consumer");
// //         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// //         setSize(500, 500);
// //         setLayout(new BorderLayout());

// //         table = new Table(4);

// //         JPanel buttonPanel = new JPanel();
// //         JButton produceButton = new JButton("Produce");
// //         JButton consumeButton = new JButton("Consume");
// //         JButton startButton = new JButton("Start");
// //         buttonPanel.add(produceButton);
// //         buttonPanel.add(consumeButton);
// //         buttonPanel.add(startButton);
// //         add(buttonPanel, BorderLayout.NORTH);

// //         waitingList = new JTextArea();
// //         JScrollPane scrollPane = new JScrollPane(waitingList);
// //         add(scrollPane, BorderLayout.CENTER);

// //         produceButton.addActionListener(new ActionListener() {
// //             public void actionPerformed(ActionEvent e) {
// //                 try {
// //                     table.produce();
// //                     waitingList.append("Producing...\n");
// //                 } catch (InterruptedException ex) {
// //                     ex.printStackTrace();
// //                 }
// //             }
// //         });

// //         consumeButton.addActionListener(new ActionListener() {
// //             public void actionPerformed(ActionEvent e) {
// //                 try {
// //                     table.consume();
// //                     waitingList.append("Consuming...\n");
// //                 } catch (InterruptedException ex) {
// //                     ex.printStackTrace();
// //                 }
// //             }
// //         });

// //         startButton.addActionListener(new ActionListener() {
// //             public void actionPerformed(ActionEvent e) {
// //                 // execute the first inserted task
// //             }
// //         });

// //         setVisible(true);
// //     }
// // }