import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.LinkedList;
import java.util.Queue;

interface ChangeListener{ // 다중 원형 버퍼의 상태 변화 감지
    void somethingChanged();
}
public class MainFrame extends JFrame {

    private CircularBuffer mainBuffer;

    private Queue<String> taskQueue;    // 시나리오 (tasks 큐)
    private int bufferSize;             // 원형 버퍼 사이즈 (=4)
    private int width = 1400;           // 실행 창 width
    private int height = 800;           // 실행 창 height
    private int fontSize = height/35;   // 폰트 크기
    private int btnWidth = width/10;    // 버튼 width
    private int btnHeight = height/12;  // 버튼 height
    private int margin = width/100;     // 균일화된 요소간 간격
    private int titleHeight = height/8; // titleLabel height
    
    private QueueModel taskModel;   // taskQueue 갱신시 사용
    private JLabel mutexPState;     // mutexP  갱신시 사용
    private JLabel mutexCState;     // mutexC  갱신시 사용
    private JLabel nrfullState;     // nrfull  갱신시 사용
    private JLabel nremptyState;    // nrempty 갱신시 사용
    private QueueModel mutexPModel;
    

    public CircularBuffer debugTable(){
        return mainBuffer;
    }

    private JButton btnSetter(String name, int x, int y){
        // 버튼 gui 기본 설정
        JButton btn = new JButton(name);
        btn.setFont(new Font("Gadugi", 1, fontSize));
        btn.setForeground(new Color(255, 255, 255));
        btn.setBounds(x, y, btnWidth, btnHeight);
        btn.setHorizontalAlignment(JLabel.CENTER);
        btn.setBackground(Color.BLACK);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return btn;
    }
    
    private JScrollPane scrollPaneSetter(JTable table, JTableHeader header, DefaultTableCellRenderer renderer){
        // 큐 gui 기본 설정
        header.setFont(new Font("Gadugi", 0, fontSize)); // 폰트 설정
        header.setBackground(Color.BLACK); // 타이틀 배경색
        header.setForeground(Color.WHITE); // 타이틀 글씨색
        // header.setBorder(null);
        
        renderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER); // 셀 가운데 정렬
        renderer.setFont(new Font("Gadugi", 0, fontSize+100));  // 왜안돼???@@@@@@@@
        renderer.setBackground(Color.BLACK); // 셀 배경색
        renderer.setForeground(Color.WHITE); // 셀 글씨색
        table.setDefaultRenderer(Object.class, renderer);
        
        table.setGridColor(Color.BLACK);
        table.getColumnModel().getColumn(0).setCellRenderer(renderer);

        JScrollPane returnPane = new JScrollPane(table);
        return returnPane;
    }

    private JLabel labelSetter(JLabel label, String text, int font){
        label.setFont(new Font("Gadugi", 1, font));
        label.setForeground(new Color(255, 255, 255));
        label.setText(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }
    
    
    public MainFrame() {
        mainBuffer = new CircularBuffer();
        bufferSize = mainBuffer.getSIZE();
        taskQueue = new LinkedList();

        setTitle("Producer-Consumer simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);
        setVisible(true);

        Container main = getContentPane();
        main.setBackground(Color.BLACK);
		main.setLayout(null);

        // 제목
        JLabel titleLabel = new JLabel();
        titleLabel = labelSetter(titleLabel, "Producer-Consumer simulation", fontSize+10);
        titleLabel.setBounds(0, 0, width, titleHeight);
        main.add(titleLabel);
        


        // 수행될 작업 목록, 작업 추가 버튼
        int westElementsLocationX = margin;
        JButton produceButton = btnSetter("Producer", westElementsLocationX, titleHeight + margin);
        JButton consumeButton = btnSetter("Consumer", westElementsLocationX + margin + btnWidth, titleHeight + margin);
        main.add(produceButton);
        main.add(consumeButton);

        taskModel= new QueueModel(taskQueue, "Tasks scenario", false);
        JTable taskQueueTable = new JTable(taskModel);
        JTableHeader tableHeader = taskQueueTable.getTableHeader();
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        JScrollPane taskQueueScrollPane = scrollPaneSetter(taskQueueTable, tableHeader, cellRenderer);
        taskQueueScrollPane.setBounds(westElementsLocationX, titleHeight + btnHeight + margin*2, btnWidth*2 + margin, height/3);
        main.add(taskQueueScrollPane);
        
        
        mainBuffer.addChangeListener(new ChangeListener(){
            public void somethingChanged(){
                renew();
            }
        }); // 버퍼 상태 변화시 gui 갱신



        int eastElementsLocationX = width/4*3; // 우측 요소들 x축 위치

        JButton startButton = btnSetter("Start a task", 0,0);
        startButton.setBounds(eastElementsLocationX, titleHeight + margin, btnWidth*2 + margin*3, btnHeight);
        main.add(startButton);

        JButton finish_producing = btnSetter("Produced",0,0);
        finish_producing.setBounds(eastElementsLocationX, titleHeight+btnHeight+margin*2, btnWidth + margin, btnHeight);
        main.add(finish_producing);
        JButton finish_consuming = btnSetter("Consumed", 0,0);
        finish_consuming.setBounds(eastElementsLocationX+btnWidth + margin*2, titleHeight+btnHeight+margin*2, btnWidth + margin, btnHeight);
        main.add(finish_consuming);

        

        JLabel in = new JLabel();
        in = labelSetter(in, "in", fontSize);
        in.setBounds(eastElementsLocationX, titleHeight + btnHeight*3, btnWidth, btnHeight);
        main.add(in);

        JLabel inValue = new JLabel();
        inValue = labelSetter(inValue, "d", fontSize);
        inValue.setBounds(eastElementsLocationX, titleHeight + btnHeight*4, btnWidth, btnHeight/2*3);
        inValue.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        main.add(inValue);

        JLabel out = new JLabel();
        out = labelSetter(out, "out", fontSize);
        out.setBounds(eastElementsLocationX + btnWidth + margin*3, titleHeight + btnHeight*3, btnWidth, btnHeight);
        main.add(out);

        JLabel outValue = new JLabel();
        outValue = labelSetter(outValue, "", fontSize);
        outValue.setBounds(eastElementsLocationX + btnWidth + margin*3, titleHeight + btnHeight*4, btnWidth, btnHeight/2*3);
        outValue.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        main.add(outValue);


        int southElementsLocationY = height/5*3;

        JLabel mutexPLabel = new JLabel();
        mutexPLabel = labelSetter(mutexPLabel, "mutexP", fontSize);
        mutexPLabel.setBounds(width/2-btnWidth-margin, southElementsLocationY, btnWidth, btnHeight/2);
        main.add(mutexPLabel);

        mutexPState = new JLabel();
        mutexPState = labelSetter(mutexPState, mainBuffer.getMutexP().getState(), fontSize);
        mutexPState.setBounds(width/2-btnWidth, southElementsLocationY + btnHeight/2, btnWidth - margin*2, btnHeight);
        mutexPState.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        main.add(mutexPState);

        
        // mutexPModel = new QueueModel(mainBuffer.getMutexP().getWaitingQueue(), "", true);
        // JTable pTable = new JTable(mutexPModel);
        // JTableHeader pHeader = pTable.getTableHeader();
        // DefaultTableCellRenderer pRenderer = new DefaultTableCellRenderer();
        // JScrollPane pScrollPane = scrollPaneSetter(pTable, pHeader, pRenderer);
        // pScrollPane.setBounds(width/4, southElementsLocationY + btnHeight/2, width/4, btnHeight);
        // main.add(pScrollPane);

        JLabel mutexCLabel = new JLabel();
        mutexCLabel = labelSetter(mutexCLabel, "mutexC", fontSize);
        mutexCLabel.setBounds(width/2, southElementsLocationY, btnWidth, btnHeight/2);
        main.add(mutexCLabel);

        mutexCState = new JLabel();
        mutexCState = labelSetter(mutexCState, mainBuffer.getMutexC().getState(), fontSize);
        mutexCState.setBounds(width/2 + margin, southElementsLocationY + btnHeight/2, btnWidth - margin*2, btnHeight);
        mutexCState.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        main.add(mutexCState);




        ////////////////////////////////////////////////////////////////// working



        produceButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            taskQueue.add(CircularBuffer.PRODUCER);
            taskModel.fireTableDataChanged(); //taskQueueScrollPane gui 갱신
        }
        });

        consumeButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            taskQueue.add(CircularBuffer.CONSUMER);
            // 버튼을 누를 때마다 업데이트
            taskModel.fireTableDataChanged(); //taskQueueScrollPane gui 갱신    
        }
        });
        
        finish_producing.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(mainBuffer.producerIsInside())
                    mainBuffer.finishProducing();
            }
        });
        finish_consuming.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(mainBuffer.consumerIsInside())
                mainBuffer.finishConsuming();
            }
        });
        startButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (!taskQueue.isEmpty()) {
                String task = taskQueue.poll();
                if (task.equals(CircularBuffer.PRODUCER)) {
                    Thread produce = new Thread(new Produce(mainBuffer));
                    produce.start();
                } else if (task.equals(CircularBuffer.CONSUMER)) {
                    Thread consume = new Thread(new Consume(mainBuffer));
                    consume.start();
                }
            }   
        }
        });






        renew();


    }
    public void renew(){ // 갱신
        mutexPState.setText(mainBuffer.getMutexP().getState()); // mutexP 갱신
        mutexCState.setText(mainBuffer.getMutexC().getState()); // mutexC 갱신
        taskModel.fireTableDataChanged(); // 시나리오 갱신 
        repaint(); // mainBuffer 갱신
    }

    @Override
    public void paint(Graphics g) { 
        int radius = height/6;
        int diameter = radius*2;
        int X = width/2;
        int Y = height/2 - btnHeight;
        super.paint(g);
        g.setColor(Color.WHITE);
        g.fillArc(X-radius,Y-radius,diameter,diameter,0,360);
        // 원형 버퍼 default color: 흰색

        

        for (int i = 0; i < bufferSize; i++) { // 값이 있는 공간 채색
            if (mainBuffer.getBuffer()[i] != null) {
                g.setColor(Color.BLACK);
                g.fillArc(X-radius,Y-radius,diameter,diameter,i*(-90),90);
            }
        }
        if(mainBuffer.producerIsInside()){ // 생산중인 공간 채색
            g.setColor(Color.DARK_GRAY);
            g.fillArc(X-radius,Y-radius,diameter,diameter,mainBuffer.getHead()*(-90),90);
        }
        if(mainBuffer.consumerIsInside()){ // 소비중인 공간 채색
            g.setColor(Color.GRAY);
            g.fillArc(X-radius,Y-radius,diameter,diameter,mainBuffer.getTail()*(-90),90);
        }
        
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(X, Y-radius, X, Y+radius); // 버퍼의 세로 선
        g.drawLine(X-radius, Y, X+radius, Y);   // 버퍼의 가로 선
        g.drawArc(X-radius, Y-radius, diameter, diameter, 0, 360);

        g.setColor(Color.WHITE);
       }
}


