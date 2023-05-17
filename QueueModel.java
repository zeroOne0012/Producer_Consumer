import java.util.Queue;

import javax.swing.table.AbstractTableModel;

class QueueModel extends AbstractTableModel {
    private Queue<Character> queue;
    public QueueModel(Queue<Character> queue) {
        this.queue = queue;
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
