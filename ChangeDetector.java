public class ChangeDetector implements Runnable{
    private int changeTemp; //변화하기 전 flag 값
    private MainFrame main;
    public ChangeDetector(MainFrame main){
        this.main = main;
        changeTemp = main.debugTable().getChanged();
    }
    @Override
    public void run() {
        while(true){
            int t = main.debugTable().getChanged();
            if(changeTemp != t){
                //
                main.renew();
                changeTemp = t;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
