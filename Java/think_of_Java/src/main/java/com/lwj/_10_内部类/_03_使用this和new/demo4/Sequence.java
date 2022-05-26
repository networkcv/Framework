package com.lwj._10_内部类._03_使用this和new.demo4;

import com.lwj._10_内部类._02_链接到外部类.demo2.Iterator;

/**
 * create by lwj on 2020/3/17
 */
public class Sequence {
    private Object[] items;
    private int next = 0;

    public Sequence(int size) {
        items = new Object[size];
    }

    public void add(Object o) {
        if (next < items.length) {
            items[next++] = o;
        }
    }

    public class SequenceSelector implements Iterator {
        private int i = 0;

        public Sequence sequence() {
            return Sequence.this;
        }

        @Override
        public boolean hashNext() {
            return i != next;
        }

        @Override
        public Object next() {
            return items[i++];
        }
    }

    public Iterator iterator() {
        return new SequenceSelector();
    }
}

