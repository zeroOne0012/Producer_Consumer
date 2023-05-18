import java.util.Queue;

import javax.swing.table.AbstractTableModel;

class QueueModel extends AbstractTableModel { // x?
    private Queue<Character> queue;
    private String title;
    public QueueModel(Queue<Character> queue, String title) {
        this.queue = queue;
        this.title = title;
    }
    
    public String getColumnName(int col) {
        return title;
    }
    public int getColumnCount() {
        return 1;
    }
    
    public int getRowCount() {
        return queue.size();
    }
    
    public Object getValueAt(int row, int col) {
        return queue.toArray()[row];
    }
}
