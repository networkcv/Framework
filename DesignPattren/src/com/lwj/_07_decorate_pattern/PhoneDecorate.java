package com.lwj._07_decorate_pattern;

/**
 * create by lwj on 2019/8/3
 */
public abstract class PhoneDecorate implements Phone{
    private  Phone phone;
    public PhoneDecorate(Phone phone){
        this.phone=phone;
    }

    @Override
    public void call() {
        phone.call();
    }
}
