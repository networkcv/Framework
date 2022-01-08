package _99_observer_pattern.demo2;

import java.util.Observable;
import java.util.Observer;

/**
 * create by lwj on 2019/1/23
 */
public class ObserverA implements Observer {
    private int myState;
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("arg"+arg);
        myState=((ConcreteSubject)o).getState();
    }

    public int getMyState() {
        return myState;
    }

    public void setMyState(int myState) {
        this.myState = myState;
    }
}
