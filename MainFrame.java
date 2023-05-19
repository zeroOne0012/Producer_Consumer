import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.LinkedList;
import java.util.Queue;

interface ChangeListener{
    void somethingChanged();
}
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
        setSize(800, 500);
        setLayout(new BorderLayout());
        table = new Table();
        tableSize = table.getSIZE();
        taskQueue = new LinkedList();
        JPanel buttonPanel = new JPanel();
        JButton produceButton = new JButton("Produce");
        JButton consumeButton = new JButton("Consume");
        JButton startButton = new JButton("Start");
        JButton finish_producing = new JButton("Finish producing");
        JButton finish_consuming = new JButton("Finish consuming");

        buttonPanel.add(produceButton);
        buttonPanel.add(consumeButton);
        buttonPanel.add(startButton);
        buttonPanel.add(finish_producing);
        buttonPanel.add(finish_consuming);

        table.addChangeListener(new ChangeListener(){
            public void somethingChanged(){
                renew();
            }
        });

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
        
        finish_producing.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(table.producerIsInside())
                    table.finishProducing();
            }
        });
        finish_consuming.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(table.consumerIsInside())
                table.finishConsuming();
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
       
        final int[] index = {1,3,2,0};
        for (int i = 0; i < tableSize; i++) {
            JPanel circlePanel = (JPanel) queuePanel.getComponent(index[i]);
            if (table.getBuffer()[i] != null) {
                circlePanel.setBackground(Color.GREEN);
                
            } else {
                circlePanel.setBackground(Color.WHITE);
            }
        }
        if(table.producerIsInside()){
            JPanel circlePanel = (JPanel) queuePanel.getComponent(index[table.getHead()]);
            circlePanel.setBackground(Color.RED);
        }
        if(table.consumerIsInside()){
            JPanel circlePanel = (JPanel) queuePanel.getComponent(index[table.getTail()]);
            circlePanel.setBackground(Color.ORANGE);
        }
        taskQueueTable.setModel(new QueueModel(taskQueue, "test"));
        // waitQueueTable.setModel(new QueueModel(table.getWaitQueue(), "test2"));
    }
}