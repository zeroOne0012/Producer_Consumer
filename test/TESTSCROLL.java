import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TESTSCROLL extends JFrame{
    public TESTSCROLL(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(200,400);
        JTextArea taskArea = new JTextArea("TEST TEXT");
        taskArea.setEditable(true);
        JScrollPane taskPane = new JScrollPane(taskArea);
        taskPane.setBounds(0,0,200,200);
        getContentPane().add(taskPane);
        setVisible(true);
    }
    public static void main(String args[]){
        new TESTSCROLL();
    }
                
}
