package lwj.bk._00_Utils;

public class Node {
   public Node next =null;
   public  int data;
    public Node(){}

    public Node(int value) {
        this.data = value;
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                '}';
    }
}
