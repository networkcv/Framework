package com.lwj.java8.test;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * create by lwj on 2020/2/29
 */
@Data
@AllArgsConstructor
public class Transaction {
    private Trader trader;
    private int year;
    private int value;
}
