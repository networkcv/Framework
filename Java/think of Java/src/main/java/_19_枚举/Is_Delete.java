package _19_枚举;

/**
 * create by lwj on 2020/3/11
 */
public enum Is_Delete {
    // public static final Is_Delete delete=new Is_Delete(1);
    DELETE(1), NOT_DELEtE(0);

    public final int val1;

    Is_Delete(int i) {
        val1 = i;
    }

    @Override
    public String toString() {
        return "Is_Delete{" +
                "val1=" + val1 +
                '}';
    }
}
