package _99_other;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * create by lwj on 2020/2/18
 */
public class Test1 {
    public static void main(String[] args) throws IOException {
        merge();
    }

    public static void merge() throws IOException {
        String path = "E://a";
        File file = new File(path);
        File[] files = file.listFiles();
        FileOutputStream fileOutputStream = new FileOutputStream("E://b/a.mp4");
        int len;
        byte[] bytes = new byte[4096];
        int index = files.length - 1;
        for (int i = 0; i <= index; i++) {
            File tmpFile = new File("E://a/" + i + ".mp4");
            if (tmpFile.exists()) {
                FileInputStream fileInputStream = new FileInputStream(tmpFile);
                while (true) {
                    len = fileInputStream.read(bytes);
                    if (len == -1) break;
                    fileOutputStream.write(bytes, 0, len);
                }
            }
        }
        fileOutputStream.close();
        fileOutputStream.close();
    }
}
