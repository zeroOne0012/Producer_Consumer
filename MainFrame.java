import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.LinkedList;
import java.util.Queue;
                                                    //message 출력, 원 그리기, 색칠, 테두리 없는 테이블?
public class MainFrame extends JFrame {

    private Table table;
    private JTextArea waitingList;
    private Queue<Character> taskQueue;
    private JTable taskQueueTable;
    private JPanel queuePanel;
    private JScrollPane scrollPane;
    private JScrollPane taskQueueScrollPane;
    private int tableSize;
    
    public Table debugTable(){
        return table;
    }
    public MainFrame() {
        setTitle("Producer-Consumer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLayout(new BorderLayout());
        table = new Table();
        tableSize = table.getSIZE();
        taskQueue = new LinkedList();
        JPanel buttonPanel = new JPanel();
        JButton produceButton = new JButton("Produce");
        JButton consumeButton = new JButton("Consume");
        JButton startButton = new JButton("Start");
        JButton FinishButton = new JButton("Finish");

                            JButton Refresh = new JButton("Refresh");

        buttonPanel.add(produceButton);
        buttonPanel.add(consumeButton);
        buttonPanel.add(startButton);
        buttonPanel.add(FinishButton);
                            buttonPanel.add(Refresh);

        add(buttonPanel, BorderLayout.NORTH);

        waitingList = new JTextArea();
        scrollPane = new JScrollPane(waitingList);
        add(scrollPane, BorderLayout.CENTER);

        // queuepanel: 원형 버퍼
        queuePanel = new JPanel();
        queuePanel.setLayout(new GridLayout(2, 2));
        for (int i = 0; i < tableSize; i++) {
            JPanel circlePanel = new JPanel();
            circlePanel.setPreferredSize(new Dimension(50, 50));
            circlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            queuePanel.add(circlePanel);
        }
        add(queuePanel, BorderLayout.SOUTH);

        // taskqueue: 작업 명단
        taskQueueTable = new JTable(new QueueModel(taskQueue, "test"));
        taskQueueScrollPane = new JScrollPane(taskQueueTable);
        taskQueueScrollPane.setPreferredSize(new Dimension(150, 0));
        add(taskQueueScrollPane, BorderLayout.WEST);


                        Refresh.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e){
                                renew();
                            }
                        });
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
        
        FinishButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                table.finish();
            }
        });
        startButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (!taskQueue.isEmpty()) {
                char task = taskQueue.poll();
                if (task == 'P') {
                    Thread produce = new Thread(new Produce(table));
                    produce.start();
                } else if (task == 'C') {
                    Thread consume = new Thread(new Consume(table));
                    consume.start();
                }
            } else {
                waitingList.append("No tasks in queue\n");
            }       
        }
        });

        setVisible(true);
    }
    public void renew(){ //갱신
        for (int i = 0; i < 4; i++) {
            waitingList.append(table.getBuffer()[i] + " ");

        }
        waitingList.append("\n");
       
        for (int i = 0; i < tableSize; i++) {
            JPanel circlePanel = (JPanel) queuePanel.getComponent(i);
            if (table.getBuffer()[i] != null) {
                if(table.personInBuffer!=null && Flags.workingSpace==i){
                    if(table.personInBuffer.equals(Table.PRODUCER))
                        circlePanel.setBackground(Color.RED);
                    else
                        circlePanel.setBackground(Color.ORANGE);
                    circlePanel.add(new JLabel(table.getBuffer()[i]));
                }
                else{
                    if(table.personInBuffer!=null)
                    circlePanel.setBackground(Color.GRAY);
                    else 
                    circlePanel.setBackground(Color.GREEN);
                }
            } else {
                circlePanel.setBackground(Color.WHITE);
            }
        }
        taskQueueTable.setModel(new QueueModel(taskQueue, "test"));
        // waitQueueTable.setModel(new QueueModel(table.getWaitQueue(), "test2"));
    }
}