import java.util.LinkedList;
import java.util.Queue;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class MyLinkedList<E> extends LinkedList<String> implements Queue<String>{ // 큐의 메소드 추가 구현
    public MyLinkedList(){
        super();
    }
    private void readTaskScenarioFile(){ // 시나리오 읽기
        try(BufferedReader br = new BufferedReader(new FileReader("Scenario.txt"))){
            String line;
            while((line = br.readLine()) != null){
                add(line);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        // taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        // taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        // taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        // taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        // taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        // taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        // taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        // taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        // taskQueue.add(CircularBuffer.PRODUCER + producerNum++);

        // taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        // taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        // taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        // taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        // taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        // taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        // taskQueue.add(CircularBuffer.PRODUCER + producerNum++);
        // taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        // taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        // taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);

        // taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        // taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        // taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        // taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
        // taskQueue.add(CircularBuffer.CONSUMER + consumerNum++);
    }
    public String getString(boolean mode){
        // String queue를 하나의 String으로
        // mode==true: 세로, mode==false: 가로
        String resultString = "";
        for(String name : this){
            resultString += name;
            if(mode)
                resultString += "\n";
            else
                resultString += "   ";
        }
        return resultString;
    }
}
