import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;

interface ChangeListener{ // 다중 원형 버퍼의 상태 변화 감지
    void somethingChanged();
}
public class MainFrame extends JFrame {

    private CircularBuffer mainBuffer; // 다중 원형 버퍼

    private Queue<String> taskQueue;    // 시나리오 (tasks 큐)
    private final int width = 1400;           // 실행 창 width
    private final int height = 800;           // 실행 창 height
    private final int fontSize = height/35;   // 폰트 크기
    private final int btnWidth = width/10;    // 버튼 width
    private final int btnHeight = height/12;  // 버튼 height
    private final int margin = width/100;     // 균일화된 요소간 간격

    private int producerNum = 1;    // 몇 번째 생산/소비자인지
    private int consumerNum = 1;    // task 이름 부여시 사용

    private JLabel[] msgLabel;      // 버퍼의 값 표시

    private JLabel ongoingTask;     // 버퍼에 생산/소비자 존재 여부

    private JLabel inValue;         // 
    private JLabel outValue;        // 최근 input/output
    
    private JLabel mutexPState;     // mutexP  value
    private JLabel mutexCState;     // mutexC  value
    private JLabel nrfullState;     // nrfull  value
    private JLabel nremptyState;    // nrempty value
    private JLabel mutexPQueueText;  // mutexP  queue 현황
    private JLabel mutexCQueueText;  // mutexC  queue 현황
    private JLabel nrfullQueueText;  // nrfull  queue 현황
    private JLabel nremptyQueueText; // nrempty queue 현황

    private final int radius = height/6;
    private final int diameter = radius*2;
    private final int X = width/2;
    private final int Y = height/2 - btnHeight; // 원의 좌표

    private void initialTaskScenario(){ // 시나리오
        taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        taskQueue.add(CircularBuffer.PRODUCER + producerNum++);

        taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);

        taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
    }
    public MainFrame() {
        mainBuffer = new CircularBuffer();
        taskQueue = new LinkedList<>();

        initialTaskScenario(); // 주어진 시나리오 입력

        setTitle("Producer-Consumer simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);
        setVisible(true);

        Container main = getContentPane();
        main.setBackground(Color.BLACK);
		main.setLayout(null);

        // 제목
        int titleHeight = height/8; // titleLabel height
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
        JScrollPane taskPane = new JScrollPane(taskText);
        taskPane.setBounds(westElementsLocationX, titleHeight + btnHeight + margin*2, btnWidth*2+margin, height/3);
        main.add(taskPane);

        taskText.setEditable(false);
        taskText.setBackground(Color.BLACK);
        taskText.setFont(new Font("Gadugi", 1, fontSize-6));
        taskText.setForeground(new Color(255, 255, 255));
        taskText.setText(getString(taskQueue, true));


        // 버퍼 현황 표시
        int[] msgLabelLocation = {X-btnWidth/2-margin + radius, Y-btnHeight/2 - radius, X-btnWidth/2-margin + radius, Y-btnHeight/2 + radius,
        X-btnWidth/2-margin - radius, Y-btnHeight/2 + radius, X-btnWidth/2-margin - radius, Y-btnHeight/2 - radius};
        msgLabel = new JLabel[4];
        for(int i = 0; i < 4; i++){
            msgLabel[i] = new JLabel();
            msgLabel[i] = labelSetter(msgLabel[i], null, fontSize);
            msgLabel[i].setBounds(msgLabelLocation[i*2], msgLabelLocation[i*2 +1], btnWidth, btnHeight/2);
            main.add(msgLabel[i]);
        }

        // "Producing..." or "Consuming..."
        ongoingTask = new JLabel();
        ongoingTask = labelSetter(ongoingTask, null, fontSize);
        ongoingTask.setBounds(width-btnWidth*2-margin*4, margin*2, btnWidth*2 + margin*2, btnHeight/3*5);
        ongoingTask.setHorizontalAlignment(JLabel.RIGHT);
        main.add(ongoingTask);
        

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

        inValue = new JLabel();
        inValue = labelSetter(inValue, "", fontSize);
        inValue.setBounds(eastElementsLocationX, titleHeight + btnHeight*4, btnWidth, btnHeight/2*3);
        inValue.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        main.add(inValue);

        JLabel out = new JLabel();
        out = labelSetter(out, "out", fontSize);
        out.setBounds(eastElementsLocationX + btnWidth + margin*3, titleHeight + btnHeight*3, btnWidth, btnHeight);
        main.add(out);

        outValue = new JLabel();
        outValue = labelSetter(outValue, "", fontSize);
        outValue.setBounds(eastElementsLocationX + btnWidth + margin*3, titleHeight + btnHeight*4, btnWidth, btnHeight/2*3);
        outValue.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        main.add(outValue);


        int southElementsLocationY = height/5*3; // 하단 요소들 최소 y값

        // mutexP
        // "mutexP" 텍스트
        JLabel mutexPLabel = new JLabel();
        mutexPLabel = labelSetter(mutexPLabel, "mutexP", fontSize);
        mutexPLabel.setBounds(width/2-btnWidth-margin, southElementsLocationY, btnWidth, btnHeight/2);
        main.add(mutexPLabel);
        // mutexP 세머퍼 값
        mutexPState = new JLabel();
        mutexPState = labelSetter(mutexPState, mainBuffer.getMutexP().getState(), fontSize);
        mutexPState.setBounds(width/2-btnWidth, southElementsLocationY + btnHeight/2, btnWidth - margin*2, btnHeight);
        mutexPState.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        main.add(mutexPState);
        // mutexP 큐
        mutexPQueueText = new JLabel();
        JScrollPane mutexPQueuePane = new JScrollPane(mutexPQueueText);
        mutexPQueuePane.setBounds(margin*7, southElementsLocationY + btnHeight/2, btnWidth*3, btnHeight);
        main.add(mutexPQueuePane);
        // mutexP 큐 text setting
        mutexPQueuePane.getViewport().setBackground(Color.BLACK);
        mutexPQueueText.setHorizontalAlignment(JLabel.RIGHT);
        mutexPQueueText.setFont(new Font("Gadugi", 1, fontSize-6));
        mutexPQueueText.setForeground(new Color(255, 255, 255));
        mutexPQueueText.setText(getString(mainBuffer.getMutexP().getWaitingQueue(), false));

        // mutexC
        // "mutexC" 텍스트
        JLabel mutexCLabel = new JLabel();
        mutexCLabel = labelSetter(mutexCLabel, "mutexC", fontSize);
        mutexCLabel.setBounds(width/2, southElementsLocationY, btnWidth, btnHeight/2);
        main.add(mutexCLabel);
        // mutexC 값
        mutexCState = new JLabel();
        mutexCState = labelSetter(mutexCState, mainBuffer.getMutexC().getState(), fontSize);
        mutexCState.setBounds(width/2 + margin, southElementsLocationY + btnHeight/2, btnWidth - margin*2, btnHeight);
        mutexCState.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        main.add(mutexCState);
        // mutexC 큐
        mutexCQueueText = new JLabel();
        JScrollPane mutexCQueuePane = new JScrollPane(mutexCQueueText);
        mutexCQueuePane.setBounds(width-margin*7-btnWidth*3, southElementsLocationY + btnHeight/2, btnWidth*3, btnHeight);
        main.add(mutexCQueuePane);
        // mutexC 큐 text setting
        mutexCQueuePane.getViewport().setBackground(Color.BLACK);
        mutexCQueueText.setFont(new Font("Gadugi", 1, fontSize-6));
        mutexCQueueText.setForeground(new Color(255, 255, 255));
        mutexCQueueText.setText(getString(mainBuffer.getMutexC().getWaitingQueue(), false));


        int semComponentDistance = btnHeight*2; // 세머퍼 정보 gui의 상단부, 하단부의 Y좌표 시작점 거리

        // nrfull
        // "nrfull" 텍스트
        JLabel nrfullLabel = new JLabel();
        nrfullLabel = labelSetter(nrfullLabel, "nrfull", fontSize);
        nrfullLabel.setBounds(width/2-btnWidth-margin, southElementsLocationY + semComponentDistance, btnWidth, btnHeight/2);
        main.add(nrfullLabel);
        // nrfull 세머퍼 값
        nrfullState = new JLabel();
        nrfullState = labelSetter(nrfullState, mainBuffer.getNrfull().getState(), fontSize);
        nrfullState.setBounds(width/2-btnWidth, southElementsLocationY + btnHeight/2 + semComponentDistance, btnWidth - margin*2, btnHeight);
        nrfullState.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        main.add(nrfullState);
        // nrfull 큐
        nrfullQueueText = new JLabel();
        JScrollPane nrfullQueuePane = new JScrollPane(nrfullQueueText);
        nrfullQueuePane.setBounds(margin*7, southElementsLocationY + btnHeight/2 + semComponentDistance, btnWidth*3, btnHeight);
        main.add(nrfullQueuePane);
        // mutexP 큐 text setting
        nrfullQueuePane.getViewport().setBackground(Color.BLACK);
        nrfullQueueText.setHorizontalAlignment(JLabel.RIGHT);
        nrfullQueueText.setFont(new Font("Gadugi", 1, fontSize-6));
        nrfullQueueText.setForeground(new Color(255, 255, 255));
        nrfullQueueText.setText(getString(mainBuffer.getNrfull().getWaitingQueue(), false));

        // nrempty
        // "nrempty" 텍스트
        JLabel nremptyLabel = new JLabel();
        nremptyLabel = labelSetter(nremptyLabel, "nrempty", fontSize);
        nremptyLabel.setBounds(width/2, southElementsLocationY + semComponentDistance, btnWidth, btnHeight/2);
        main.add(nremptyLabel);
        // nrempty 값
        nremptyState = new JLabel();
        nremptyState = labelSetter(nremptyState, mainBuffer.getNrempty().getState(), fontSize);
        nremptyState.setBounds(width/2 + margin, southElementsLocationY + btnHeight/2+semComponentDistance, btnWidth - margin*2, btnHeight);
        nremptyState.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        main.add(nremptyState);
        // nrempty 큐
        nremptyQueueText = new JLabel();
        JScrollPane nremptyQueuePane = new JScrollPane(nremptyQueueText);
        nremptyQueuePane.setBounds(width-margin*7-btnWidth*3, southElementsLocationY + btnHeight/2+semComponentDistance, btnWidth*3, btnHeight);
        main.add(nremptyQueuePane);
        // nrempty 큐 text setting
        nremptyQueuePane.getViewport().setBackground(Color.BLACK);
        nremptyQueueText.setFont(new Font("Gadugi", 1, fontSize-6));
        nremptyQueueText.setForeground(new Color(255, 255, 255));
        nremptyQueueText.setText(getString(mainBuffer.getNrempty().getWaitingQueue(), false));


        mainBuffer.addChangeListener(new ChangeListener(){
            public void somethingChanged(){
                renew();
            }
        }); // 버퍼 상태 변화시 gui 갱신


        produceButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
            taskText.setText(getString(taskQueue, true)); //task gui 갱신
        }
        });

        consumeButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
            taskText.setText(getString(taskQueue, true)); //task gui 갱신
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
                taskText.setText(getString(taskQueue, true)); //task gui 갱신
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

    public void renew(){ // 갱신

        for(int i = 0; i < 4; i++){
            msgLabel[i].setText(mainBuffer.getBufferValue(i));
        }   // mainBuffer 값 갱신

        String ongoing = ""; // 버퍼에 생산자/소비자 있는지 여부 갱신
        if(mainBuffer.producerIsInside()) ongoing += " Producing...";
        if(mainBuffer.consumerIsInside()) ongoing += " Consuming...";
        ongoingTask.setText(ongoing);

        mutexPState.setText(mainBuffer.getMutexP().getState()); // mutexP 갱신
        mutexCState.setText(mainBuffer.getMutexC().getState()); // mutexC 갱신
        nrfullState.setText(mainBuffer.getNrfull().getState()); // nrfull 갱신
        nremptyState.setText(mainBuffer.getNrempty().getState()); // nrempty 갱신
        mutexPQueueText.setText(getString(mainBuffer.getMutexP().getWaitingQueue(), false)); // mutexP 큐 갱신
        mutexCQueueText.setText(getString(mainBuffer.getMutexC().getWaitingQueue(), false)); // mutexC 큐 갱신
        nrfullQueueText.setText(getString(mainBuffer.getNrfull().getWaitingQueue(), false)); // nrfull 큐 갱신
        nremptyQueueText.setText(getString(mainBuffer.getNrempty().getWaitingQueue(), false)); // nrempty 큐 갱신

        inValue.setText(mainBuffer.getInput());     // in  갱신
        outValue.setText(mainBuffer.getOutput());   // out 갱신

        repaint(); // mainBuffer GUI 갱신
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.WHITE);
        g.fillArc(X-radius,Y-radius,diameter,diameter,0,360);
        // 원형 버퍼 default color: 흰색

        for (int i = 0; i < mainBuffer.getSIZE(); i++) { // 값이 있는 공간 채색
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
    
    public String getString(Queue<String> queue, boolean mode){
        // String queue를 하나의 String으로
        // mode==true: 세로, mode==false: 가로
        String resultString = "";
        for(String name : queue){
            resultString += name;
            if(mode)
                resultString += "\n";
            else
                resultString += "   ";
        }
        return resultString;
    }
}


