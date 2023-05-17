// import java.util.Queue;
// import java.util.LinkedList;
// import javax.swing.JTable;
// import javax.swing.table.AbstractTableModel;

// public class QueueTable extends JTable {
//     private Queue<String> queue;

//     public QueueTable(Queue<String> queue) {
//         this.queue = queue;
//         setModel(new QueueTableModel(queue));
//     }
//     public void update() {
//         ((QueueTableModel) getModel()).fireTableDataChanged();
//     }
//     private class QueueTableModel extends AbstractTableModel {
//         private Queue<String> queue;
//         private String[] columnNames = {"Index", "Value"};

//         public QueueTableModel(Queue<String> queue) {
//             this.queue = queue;
//         }

//         @Override
//         public int getRowCount() {
//             return queue.size();
//         }

//         @Override
//         public int getColumnCount() {
//             return 2;
//         }

//         @Override
//         public String getColumnName(int column) {
//             return columnNames[column];
//         }

//         @Override
//         public Object getValueAt(int rowIndex, int columnIndex) {
//             if (columnIndex == 0) {
//                 return rowIndex;
//             } else {
//                 return ((LinkedList<String>) queue).get(rowIndex);
//             }
//         }
//     }
// }