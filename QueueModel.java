import java.util.Queue;
import javax.swing.table.AbstractTableModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

class QueueModel extends AbstractTableModel {
    private Queue<String> queue;
    private String title;
    private boolean mode; // true: 행 모드, false: 열 모드

    public QueueModel(Queue<String> queue, String title, boolean mode) {
        this.queue = queue;
        this.title = title;
        this.mode = mode;
    }
    
    public String getColumnName(int col) {
        return title;
    }
    public int getColumnCount() {
        return mode ? queue.size() : 1;
    }
    
    public int getRowCount() {
        return mode ? 1 : queue.size();
    }
    
    public Object getValueAt(int row, int col) {
        return mode ? queue.toArray()[col] : queue.toArray()[row];
    }



    public Class<?> getColumnClass(int col) {
        return String.class; // 데이터 타입으로 String을 사용하도록 설정
    }



}
class CustomCellRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // 폰트 설정
        Font font = new Font("Arial", Font.BOLD, 20);
        component.setFont(font);
        
        // 테두리 설정
        Border border = BorderFactory.createLineBorder(Color.WHITE);
        ((JComponent) component).setBorder(border);
        
        // 배경색 설정
        component.setBackground(Color.BLACK);
        component.setForeground(Color.WHITE);
        
        return component;
    }
}