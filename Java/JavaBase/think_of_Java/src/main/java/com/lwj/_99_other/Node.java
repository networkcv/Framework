package com.lwj._99_other;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * create by lwj on 2020/4/3
 */
@Data
@ToString
@NoArgsConstructor
public  class Node {
    int value;
    Node next;

    public Node(int value) {
        this.value = value;
    }
}
