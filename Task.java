public class Task {
    public static final int FIRST_TIME = 0;
    public static final int MUTEX_CHECKED = 1;
    public static final int BUFFER_CHECKED = 2;
    
    private String personName;
    private int state; // P연산으로 중단 후 작업 재시작시 시작 위치에 관한 정보
    public Task(String personName, int state){
        this.personName = personName;
        this.state = state;
    }
    public String getName(){
        return personName;
    }
    public int getState(){
        return state;
    }
    public void setState(int state){
        if(state<0 || state>2){
            System.out.println("Invalid Value!");
            return;
        }
        this.state = state;
    }
}
