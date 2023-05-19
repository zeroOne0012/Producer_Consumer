import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.LinkedList;
import java.util.Queue;

interface ChangeListener{
    void somethingChanged();
}
public class MainFrame extends JFrame {

    private CircularBuffer table;
    private JTextArea waitingList;
    private JPanel queuePanel;

    private Queue<String> taskQueue;
    private int tableSize;
    private int width = 1300;
    private int height = 700;
    private int fontSize = height/35;
    private int btnWidth = width/10;
    private int btnHeight = height/12;
    private int margin = width/100;
    
    private QueueModel taskModel;

    public CircularBuffer debugTable(){
        return table;
    }

    private JButton btnMaker(String name, int x, int y){
        // 버튼 gui 기본 설정
        JButton btn = new JButton(name);
        btn.setFont(new Font("DialogInput", 1, fontSize));
        btn.setForeground(new Color(255, 255, 255));
        btn.setBounds(x, y, btnWidth, btnHeight);
        btn.setHorizontalAlignment(JLabel.CENTER);
        btn.setBackground(Color.BLACK);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return btn;
    }
    
    private JScrollPane scrollPaneMaker(JTable table, JTableHeader header, DefaultTableCellRenderer renderer){
        // 큐 gui 기본 설정
        header.setFont(new Font("DialogInput", 0, fontSize)); // 폰트 설정
        header.setBackground(Color.BLACK); // 타이틀 배경색
        header.setForeground(Color.WHITE); // 타이틀 글씨색
        // header.setBorder(null);
        
        renderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER); // 셀 가운데 정렬
        renderer.setFont(new Font("DialogInput", 0, fontSize+100));  // 왜안돼???@@@@@@@@
        renderer.setBackground(Color.BLACK); // 셀 배경색
        renderer.setForeground(Color.WHITE); // 셀 글씨색
        table.setDefaultRenderer(Object.class, renderer);
        
        table.setGridColor(Color.BLACK);
        table.getColumnModel().getColumn(0).setCellRenderer(renderer);

        JScrollPane returnPane = new JScrollPane(table);
        return returnPane;
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
        
        // 수행될 작업 목록, 작업 추가 버튼
        JButton produceButton = btnMaker("Producer", margin, height/8);
        JButton consumeButton = btnMaker("Consumer", margin*2 + btnWidth, height/8);
        main.add(produceButton);
        main.add(consumeButton);

        taskModel= new QueueModel(taskQueue, "시나리오", false);
        JTable taskQueueTable = new JTable(taskModel);
        JTableHeader tableHeader = taskQueueTable.getTableHeader();
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        JScrollPane taskQueueScrollPane = scrollPaneMaker(taskQueueTable, tableHeader, cellRenderer);
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

        

        produceButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            taskQueue.add("Producer");
            taskModel.fireTableDataChanged(); //taskQueueScrollPane gui 갱신
        }
        });

        consumeButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            taskQueue.add("Consumer");
            // 버튼을 누를 때마다 업데이트
            taskModel.fireTableDataChanged(); //taskQueueScrollPane gui 갱신    
        }
        });
        
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
        taskModel.fireTableDataChanged();
    }
}