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
    
    private JLabel mutexPState;     // mutexP  갱신시 사용
    private JLabel mutexCState;     // mutexC  갱신시 사용
    private JLabel nrfullState;     // nrfull  갱신시 사용
    private JLabel nremptyState;    // nrempty 갱신시 사용

    private int producerNum = 1;
    private int consumerNum = 1;    // 몇 번째 생산/소비자인지
    

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
        


        // 작업 추가 버튼
        int westElementsLocationX = margin;
        JButton produceButton = btnSetter("Producer", westElementsLocationX, titleHeight + margin);
        JButton consumeButton = btnSetter("Consumer", westElementsLocationX + margin + btnWidth, titleHeight + margin);
        main.add(produceButton);
        main.add(consumeButton);

        // 수행될 작업 목록
        JTextArea taskText = new JTextArea();
        taskText.setEditable(true);
        JScrollPane taskPane = new JScrollPane(taskText);
        taskPane.setBounds(westElementsLocationX, titleHeight + btnHeight + margin*2, btnWidth*2+margin, height/3);
        main.add(taskPane);

        taskText.setBackground(Color.BLACK);
        taskText.setFont(new Font("Gadugi", 1, fontSize-6));
        taskText.setForeground(new Color(255, 255, 255));
        taskText.setText(getString(taskQueue));

        
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

        
        //sibal bb

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
            taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
            taskText.setText(getString(taskQueue)); //task gui 갱신
        }
        });

        consumeButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
            taskText.setText(getString(taskQueue)); //task gui 갱신
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
                String taskPerformer = taskQueue.poll();
                taskText.setText(getString(taskQueue)); //task gui 갱신
                if (taskPerformer.contains(CircularBuffer.PRODUCER)) {
                    Thread produce = new Thread(new Produce(mainBuffer, taskPerformer));
                    produce.start();
                } else if (taskPerformer.contains(CircularBuffer.CONSUMER)) {
                    Thread consume = new Thread(new Consume(mainBuffer, taskPerformer));
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
    
    public String getString(Queue<String> queue){
        String resultString = "";
        for(String name : queue){
            resultString += name + "\n";
        }
        return resultString;
    }
}


