package _10_内部类._02_链接到外部类.demo2;

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

    private class SequenceSelector implements Iterator {
        private int i = 0;

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

