package _12_异常处理;


import java.io.*;

/**
 * create by lwj on 2020/3/30
 * 深入 try with resources
 */
public class Test1 {
    public static void main0(String[] args) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("test.txt")));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("test.txt")));
        ) {
            int b;
            while ((b = bis.read()) != -1) {
                bos.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class Connection implements AutoCloseable {
        void sendData() throws Exception {
            throw new Exception("sendData() exception ");
        }

        public void close() throws Exception {
            throw new Exception("close() exception ");
        }
    }

    public static void main1(String[] args) {
        try {
            Connection connection = new Connection();
            try {
                connection.sendData();
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main2(String[] args) {
        try {
            try (Connection connection = new Connection();) {
                connection.sendData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        try {
            Connection conn = new Connection();
            Throwable exception = null;
            try {
                conn.sendData();
            } catch (Throwable serviceException) {
                exception = serviceException;
                throw serviceException;
            } finally {
                try {
                    conn.close();
                } catch (Throwable IOException) {
//                     exception.initCause(IOException);
                    exception.addSuppressed(IOException);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void test() {
        try {
            RuntimeException runtimeException = new RuntimeException("1");
            runtimeException.addSuppressed(new IOException("11"));
            throw runtimeException;
        } finally {
            throwsFun();
        }
    }

    void throwsFun() {
        throw new RuntimeException("2");
    }

}
