package _04_prototype_pattern.demo2;

import java.util.Date;

public class Sheep implements Cloneable {
    private String name;
    private Date birthday;
    private Wool wool;

    public void getWool(){
        Integer num = wool.getNum();
        if(wool.getNum()!=0){
            wool.setNum( --num);
            System.out.println("剪了一次");
        }else {
            System.out.println("没羊毛了");
        }
    }
    public void setWool(Wool wool){
        this.wool=wool;
    }



    @Override
    protected Object clone() throws CloneNotSupportedException {
        Object clone = super.clone();
        return clone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
