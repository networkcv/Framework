package com.lwj._07_decorate_pattern;

/**
 * create by lwj on 2019/8/3
 */
public class Client {
    public static void main(String[] args){
        Phonex phonex = new Phonex();
        Phone musicPhonex = new TimePhone(new MusicPhonex(phonex));
        musicPhonex.call();
    }
}
