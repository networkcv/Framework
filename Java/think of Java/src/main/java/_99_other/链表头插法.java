package _99_other;

import org.junit.Test;

import java.util.Arrays;

/**
 * create by lwj on 2020/4/3
 */
public class 链表头插法 {
    Node node1 = new Node(1);
    Node node2 = new Node(2);
    Node node3 = new Node(3);

    Node[] oldTable = new Node[3];
    Node[] newTable = new Node[3];

    {
        node1.next = node2;
        node2.next = node3;
        oldTable[1] = node1;
    }


    public static void main(String[] args) throws InterruptedException {
        链表头插法 map = new 链表头插法();
        System.out.println(Arrays.asList(map.oldTable));
        Thread thread = new Thread(() -> {
            map.resize();
        });
        Thread thread2 = new Thread(() -> {
            map.resize();
        });
        thread.start();
        thread2.start();
        thread.join();
        thread2.join();
        System.out.println(Arrays.asList(map.newTable));

    }

    @Test
    public void resize() {
        // 头插法
        Node node = oldTable[1];
        Node next;
        while (node != null) {
            next = node.next;
            node.next = newTable[1];
            newTable[1] = node;
            node = next;
        }
    }
}

