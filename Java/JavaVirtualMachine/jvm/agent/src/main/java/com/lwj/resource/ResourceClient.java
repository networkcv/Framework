package com.lwj.resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Date: 2023/5/29
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class ResourceClient {
    public static void main(String[] args) {
        try {
            ResourceClient resourceClient = new ResourceClient();
            InputStream resourceAsStream = resourceClient.getClass().getClassLoader().getResourceAsStream("a.txt");
            if (resourceAsStream == null) {
                System.out.println("empty");
            }
            print(resourceAsStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void print(InputStream inputStream) throws Exception {
        InputStreamReader reader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader br = new BufferedReader(reader);
        String s = "";
        while ((s = br.readLine()) != null)
            System.out.println(s);
        inputStream.close();
    }
}
