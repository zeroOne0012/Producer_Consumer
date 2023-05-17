public class ChangeDetector implements Runnable{
    private int changeTemp; //변화하기 전 flag 값
    private MainFrame main;
    public ChangeDetector(MainFrame main){
        this.main = main;
        changeTemp = Flags.somethingChanged;
    }
    @Override
    public void run() {
        while(true){
            if(changeTemp != Flags.somethingChanged){
                //
                main.renew();

                Flags.somethingChanged = Flags.somethingChanged%100;
                changeTemp = Flags.somethingChanged;
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
