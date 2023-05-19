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

    private CircularBuffer table;
    private JTextArea waitingList;
    private JTable taskQueueTable;
    private JPanel queuePanel;
    private JScrollPane scrollPane;
    private JScrollPane taskQueueScrollPane;

    private Queue<String> taskQueue;
    private int tableSize;
    private int width = 1200;
    private int height = 700;
    private int fontSize = height/35;
    private int btnWidth = width/8;
    private int btnHeight = height/12;
    private int margin = width/100;
    
    private JButton btnMaker(String name, int x, int y){
        JButton btn = new JButton(name);
        btn.setFont(new Font("DialogInput", 1, fontSize));
        btn.setForeground(new Color(255, 255, 255));
        btn.setBounds(x, y, btnWidth, btnHeight);
        btn.setHorizontalAlignment(JLabel.CENTER);
        btn.setBackground(Color.BLACK);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return btn;
    }
    public CircularBuffer debugTable(){
        return table;
    }
    public MainFrame() {
        table = new CircularBuffer();
        tableSize = table.getSIZE();
        taskQueue = new LinkedList();

        setTitle("Producer-Consumer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);
        setVisible(true);

        Container main = getContentPane();
        main.setBackground(Color.BLACK);
		main.setLayout(null);

        // 제목
        JLabel titleLabel = new JLabel();
        titleLabel.setFont(new Font("DialogInput", 1, fontSize + 5));
        titleLabel.setForeground(new Color(255, 255, 255));
        titleLabel.setText("생산자 소비자 시뮬레이터");
        titleLabel.setBounds(0, 0, width, height/8);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        main.add(titleLabel);

        // 메뉴에 소개랑 로그 작성하기?
        
        // 수행될 작업 목록, 작업 추가 버튼
        JButton produceButton = btnMaker("Producer", margin, height/8);
        JButton consumeButton = btnMaker("Consumer", margin*2 + btnWidth, height/8);
        main.add(produceButton);
        main.add(consumeButton);

        // taskqueue: 작업 명단
        QueueModel model = new QueueModel(taskQueue, "시나리오", false);
        taskQueueTable = new JTable(model);
        // taskQueueTable.setFont(new Font("DialogInput", 1, fontSize));
        // taskQueueTable.setForeground(new Color(255, 255, 255));
        // taskQueueTable.setBackground(Color.BLACK);
                // CustomCellRenderer cellRenderer = new CustomCellRenderer();
                // taskQueueTable.setDefaultRenderer(Object.class, cellRenderer);
                
        taskQueueScrollPane = new JScrollPane(taskQueueTable);
        taskQueueScrollPane.setBounds(margin, height/8 + btnHeight + margin, btnWidth*2 + margin, height/2);
        main.add(taskQueueScrollPane);
        
        
        // JButton startButton = new JButton("Start");
        // JButton finish_producing = new JButton("Finish producing");
        // JButton finish_consuming = new JButton("Finish consuming");

        // buttonPanel.add(startButton);
        // buttonPanel.add(finish_producing);
        // buttonPanel.add(finish_consuming);
        // main.add(buttonPanel, BorderLayout.NORTH);

        table.addChangeListener(new ChangeListener(){
            public void somethingChanged(){
                renew();
            }
        });

        // waitingList = new JTextArea();
        // scrollPane = new JScrollPane(waitingList);
        // add(scrollPane, BorderLayout.CENTER);

        // // queuepanel: 원형 버퍼
        // queuePanel = new JPanel();
        // queuePanel.setLayout(new GridLayout(2, 2));
        // for (int i = 0; i < tableSize; i++) {
        //     JPanel circlePanel = new JPanel();
        //     circlePanel.setPreferredSize(new Dimension(50, 50));
        //     circlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //     queuePanel.add(circlePanel);
        // }
        // add(queuePanel, BorderLayout.SOUTH);

        

        // produceButton.addActionListener(new ActionListener() {
        // public void actionPerformed(ActionEvent e) {
        //     taskQueue.add('P');
        //     // 버튼을 누를 때마다 업데이트
        //     taskQueueTable.setModel(new QueueModel(taskQueue, "test"));
        // }
        // });

        // consumeButton.addActionListener(new ActionListener() {
        // public void actionPerformed(ActionEvent e) {
        //     taskQueue.add('C');
        //     // 버튼을 누를 때마다 업데이트
        //     taskQueueTable.setModel(new QueueModel(taskQueue, "test"));
        // }
        // });
        
        // finish_producing.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e){
        //         if(table.producerIsInside())
        //             table.finishProducing();
        //     }
        // });
        // finish_consuming.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e){
        //         if(table.consumerIsInside())
        //         table.finishConsuming();
        //     }
        // });
        // startButton.addActionListener(new ActionListener() {
        // public void actionPerformed(ActionEvent e) {
        //     if (!taskQueue.isEmpty()) {
        //         char task = taskQueue.poll();
        //         if (task == 'P') {
        //             Thread produce = new Thread(new Produce(table));
        //             produce.start();
        //         } else if (task == 'C') {
        //             Thread consume = new Thread(new Consume(table));
        //             consume.start();
        //         }
        //     } else {
        //         waitingList.append("No tasks in queue\n");
        //     }       
        // }
        // });









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
        taskQueueTable.setModel(new QueueModel(taskQueue, "시나리오", false));
        // waitQueueTable.setModel(new QueueModel(table.getWaitQueue(), "test2"));
    }
}