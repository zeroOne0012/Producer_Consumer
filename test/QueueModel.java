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


        // private QueueModel taskModel;   // taskQueue 갱신시 사용


        // taskModel= new QueueModel(taskQueue, "Tasks scenario", false);
        // JTable taskQueueTable = new JTable(taskModel);
        // JTableHeader tableHeader = taskQueueTable.getTableHeader();
        // DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        // JScrollPane taskQueueScrollPane = scrollPaneSetter(taskQueueTable, tableHeader, cellRenderer);
        // taskQueueScrollPane.setBounds(westElementsLocationX, titleHeight + btnHeight + margin*2, btnWidth*2 + margin, height/3);
        // main.add(taskQueueScrollPane);


        // renew..
        // taskModel.fireTableDataChanged(); // 시나리오 갱신 

        // private JScrollPane scrollPaneSetter(JTable table, JTableHeader header, DefaultTableCellRenderer renderer){
        //     // 큐 gui 기본 설정
        //     header.setFont(new Font("Gadugi", 0, fontSize)); // 폰트 설정
        //     header.setBackground(Color.BLACK); // 타이틀 배경색
        //     header.setForeground(Color.WHITE); // 타이틀 글씨색
        //     // header.setBorder(null);
            
        //     renderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER); // 셀 가운데 정렬
        //     renderer.setFont(new Font("Gadugi", 0, fontSize+100));  // 왜안돼???@@@@@@@@
        //     renderer.setBackground(Color.BLACK); // 셀 배경색
        //     renderer.setForeground(Color.WHITE); // 셀 글씨색
        //     table.setDefaultRenderer(Object.class, renderer);
            
        //     table.setGridColor(Color.BLACK);
        //     table.getColumnModel().getColumn(0).setCellRenderer(renderer);
    
        //     JScrollPane returnPane = new JScrollPane(table);
        //     return returnPane;
        // }