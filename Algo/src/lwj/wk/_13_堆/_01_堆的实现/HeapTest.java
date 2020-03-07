package lwj.wk._13_堆._01_堆的实现;

/**
 * create by lwj on 2019/3/31
 */
public class HeapTest {
    int[] a;
    int i = 1;  //数组的index 从1开始存放数据，方便判断 指向即将要插入数据的位置
    int n;      //数组的capacity

    public HeapTest(int n) {
        this.n = n;
        a = new int[n + 1];
    }

    public void insert(int data) {
        if (i > n) return;
        a[i] = data;
        bottom2TopHeapify(a,i++);
    }

    public void removeMax() {
        if (i == 1) return;
        swap(a, 1, --i);
        top2BottomHeapify(a, n-1, 1);

    }

    public int getMax() {
        if (i == 1) return -1;
        return a[1];
    }

    public void print() {
        System.out.print("堆:");
        int num=1;
        while(num<i){
            System.out.print(a[num++]+" ");
        }
        System.out.println();
    }

    public void bottom2TopHeapify(int[] a,  int i) {
        while (i / 2 >= 1 && a[i] > a[i / 2]) {
            swap(a, i, i / 2);
        }

    }

    public void top2BottomHeapify(int[] a, int n, int i) {
        while (true) {
            int maxPos = i;
            if (i * 2 <= n && a[i] < a[i * 2]) maxPos = i * 2;
            if (i * 2 + 1 <= n && a[maxPos] < a[i * 2 + 1]) maxPos = i * 2 + 1;
            if (maxPos == i) break;
            swap(a, i, maxPos);
            i = maxPos;
        }
    }

    private void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static void main(String[] args) {
        HeapTest heap = new HeapTest(3);
        heap.print();
        heap.removeMax();
        heap.print();
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        heap.print();
        heap.removeMax();
        heap.print();
        heap.insert(4);
        heap.print();
        System.out.println(heap.getMax());
    }
}
