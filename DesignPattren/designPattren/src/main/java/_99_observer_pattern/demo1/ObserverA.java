package _99_observer_pattern.demo1;

/**
 * create by lwj on 2019/1/22
 */
public class ObserverA implements Observer {

    private  int myState;   //myState 与目标对象的state的值一致

    public int getMyState() {
        return myState;
    }

    public void setMyState(int myState) {
        this.myState = myState;
    }

    @Override
    public void update(Subject subject) {
        myState = ((ConcreteSubject)subject).getState();

    }
}
