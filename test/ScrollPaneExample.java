import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class ScrollPaneExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Scroll Pane Example");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(300, 200);

                // 긴 텍스트 생성
                String longText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                        "Sed pulvinar fermentum lectus, et pulvinar orci luctus in. " +
                        "Phasellus egestas metus vel malesuada finibus.";

                // JTextArea 생성 및 속성 설정
                JTextArea textArea = new JTextArea(longText);
                textArea.setEditable(false);

                // JScrollPane에 JTextArea 추가
                JScrollPane scrollPane = new JScrollPane(textArea);

                // JScrollPane의 스크롤 정책 설정 (수직 스크롤바 항상 표시)
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                // JFrame에 JScrollPane 추가
                frame.getContentPane().add(scrollPane);

                // JTextArea의 Document에 DocumentListener 추가
                textArea.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) {
                        // 텍스트가 삽입되었을 때의 동작
                        updateUI();
                    }

                    public void removeUpdate(DocumentEvent e) {
                        // 텍스트가 삭제되었을 때의 동작
                        updateUI();
                    }

                    public void changedUpdate(DocumentEvent e) {
                        // 텍스트 속성이 변경되었을 때의 동작
                        updateUI();
                    }

                    private void updateUI() {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                // UI 업데이트를 수행할 코드 작성
                                frame.revalidate();
                                frame.repaint();
                            }
                        });
                    }
                });

                frame.setVisible(true);
            }
        });
    }
}
