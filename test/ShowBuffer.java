import java.awt.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class DrawingPanel extends JPanel {
    private Color circleColor;

    public DrawingPanel() {
        circleColor = Color.RED;
        setBackground(Color.WHITE);

        JButton changeButton = new JButton("Change");
        changeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeCircleColor();
                repaint();
            }
        });

        setLayout(new BorderLayout());
        add(changeButton, BorderLayout.SOUTH);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int circleSize = Math.min(getWidth(), getHeight()) / 2;
        int x = (getWidth() - circleSize) / 2;
        int y = (getHeight() - circleSize) / 2;

        g.setColor(circleColor);
        g.fillOval(x, y, circleSize, circleSize);
    }

    private void changeCircleColor() {
        circleColor = getRandomColor();
    }

    private Color getRandomColor() {
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        return new Color(r, g, b);
    }
}

public class ShowBuffer {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Circle Color Change Example");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new DrawingPanel());
                frame.setSize(300, 300);
                frame.setVisible(true);
            }
        });
    }
}