// package test;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TableDesignExample extends JFrame {
    private JTable table;
    
    public TableDesignExample() {
        setTitle("Table Design Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 샘플 데이터
        String[][] data = {
            {"John", "Doe", "25"},
            {"Jane", "Smith", "30"},
            {"Bob", "Johnson", "40"}
        };
        
        // 테이블 모델 생성
        String[] columnNames = {"First Name", "Last Name", "Age"};
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        
        // JTable 생성 및 모델 설정
        table = new JTable(model);
        
        // 셀 디자인 설정
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER); // 셀 가운데 정렬
        cellRenderer.setFont(new Font("Arial", Font.BOLD, 14)); // 폰트 설정
        cellRenderer.setBackground(Color.BLACK); // 배경색 설정
        cellRenderer.setForeground(Color.WHITE); // 전경색(글씨색) 설정
        cellRenderer.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // 테두리 설정
        table.setDefaultRenderer(Object.class, cellRenderer);
        
        // 그리드 색상 설정
        table.setGridColor(Color.WHITE);
        
        // JScrollPane에 JTable 추가
        JScrollPane scrollPane = new JScrollPane(table);
        
        // 프레임에 JScrollPane 추가
        getContentPane().add(scrollPane);
        
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new TableDesignExample();
    }
}