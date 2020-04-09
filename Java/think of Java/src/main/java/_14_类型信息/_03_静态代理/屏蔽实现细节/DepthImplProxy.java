package _14_类型信息._03_静态代理.屏蔽实现细节;

/**
 * create by lwj on 2020/4/7
 */
public class DepthImplProxy {
    private DepthImpl depth = new DepthImpl();

    public void exec() {
        depth.start();
        depth.exec();
        depth.end();
    }

    // 功能的底层实现
    private class DepthImpl {
        public void start() {
            System.out.println("start");
        }

        public void exec() {
            System.out.println("exec");
        }

        public void end() {
            System.out.println("end");
        }

    }

}
