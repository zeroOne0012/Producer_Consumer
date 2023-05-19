package test;
// public class ChangeDetector implements Runnable{
//     private int changeTemp; //변화하기 전 flag 값
//     private MainFrame main;
//     public ChangeDetector(MainFrame main){
//         this.main = main;
//         changeTemp = main.debugTable().getChanged();
//     }
//     @Override
//     public void run() {
//         while(true){
//             int t = main.debugTable().getChanged();
//             if(changeTemp != t){
//                 changeTemp = t;
//                 main.renew();
//             }
//             try {
//                 Thread.sleep(100);
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             }
//         }
//     }
// }
