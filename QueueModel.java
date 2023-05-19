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
}