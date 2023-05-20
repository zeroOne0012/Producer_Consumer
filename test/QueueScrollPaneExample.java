import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class QueueScrollPaneExample extends JFrame {
    private Queue<String> queue;

    public QueueScrollPaneExample(Queue<String> queue) {
        this.queue = queue;

        setTitle("Queue ScrollPane Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // 큐의 값을 표시할 JLabel 생성
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.LINE_AXIS));
        for (String item : queue) {
            JLabel label = new JLabel(item);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            labelPanel.add(label);
        }

        // JLabel이 들어갈 JScrollPane 생성
        JScrollPane scrollPane = new JScrollPane(labelPanel);

        getContentPane().add(scrollPane);
    }

    public static void main(String[] args) {
        // 큐 생성 및 값 추가
        Queue<String> queue = new LinkedList<>();
        queue.add("Item 1");
        queue.add("Item 2");
        queue.add("Item 3");
        queue.add("Item 4");
        queue.add("Item 5");
        queue.add("Item 1");
        queue.add("Item 2");
        queue.add("Item 3");
        queue.add("Item 4");
        queue.add("Item 5");
        queue.add("Item 1");
        queue.add("Item 2");
        queue.add("Item 3");
        queue.add("Item 4");
        queue.add("Item 5");
        queue.add("Item 1");
        queue.add("Item 2");
        queue.add("Item 3");
        queue.add("Item 4");
        queue.add("Item 5");

        // 예시 프레임 생성
        SwingUtilities.invokeLater(() -> {
            QueueScrollPaneExample example = new QueueScrollPaneExample(queue);
            example.setVisible(true);
        });
    }
}