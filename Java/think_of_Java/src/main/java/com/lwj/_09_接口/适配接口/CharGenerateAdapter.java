package com.lwj._09_接口.适配接口;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Scanner;

/**
 * create by lwj on 2020/3/16
 */
public class CharGenerateAdapter extends CharGenerate implements Readable {
    public int count = 10;

    @Override
    public int read(CharBuffer cb) throws IOException {
        if (count-- == 0) {
            return -1;
        }
        cb.append(next());
        return 1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(new CharGenerateAdapter());
        while (scanner.hasNext()) {
            System.out.println(scanner.next());
        }

    }
}
