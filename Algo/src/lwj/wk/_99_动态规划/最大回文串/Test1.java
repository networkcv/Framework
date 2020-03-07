package lwj.wk._99_动态规划.最大回文串;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author liuwangjie
 */
public class Test1 {

    /**
     * 对含有特殊字符的字符串进行拆分
     */
    public static String[] stringFilter(String str) {
        String regEx = "[^a-zA-Z0-9]";
        Pattern p = Pattern.compile(regEx);
        return p.split(str);
    }

    /**
     * * 遍历生成的二维数组，递归查找最长路径
     *
     * @param mux   转换生成的二维数组
     * @param path  一次查找的字符串
     * @param paths 最准查询集合，进行去重
     */
    private static void getAllLcs(String a, String b, int[][] mux, int i, int j, String path, Set<String> paths) {

        StringBuilder pathBuilder = new StringBuilder(path);
        while (i > 0 && j > 0) {
            if (a.charAt(i - 1) == b.charAt(j - 1)) {
                pathBuilder.append(a.charAt(i - 1));
                --i;
                --j;
            } else {
                if (mux[i - 1][j] > mux[i][j - 1]) {
                    --i;
                } else if (mux[i - 1][j] < mux[i][j - 1]) {
                    --j;
                } else {
                    getAllLcs(a, b, mux, i - 1, j, pathBuilder.toString(), paths);
                    getAllLcs(a, b, mux, i, j - 1, pathBuilder.toString(), paths);
                    return;
                }
            }
        }
        paths.add(pathBuilder.toString());
    }

    /**
     * 原字符串反转，成为查找最大公共子序列问题，
     *
     * @param input 待查找字符串
     * @return 查找结果
     */

    public static String findLCS(String input) {
        if (input == null || input.length() == 0) {
            return "";
        }

        // 要返回的结果
        StringBuilder result = new StringBuilder();

        // 将字符串反转
        String reverse = new StringBuilder(input).reverse().toString();

        // 字符串长度
        int len = input.length();

        // 矩阵 -> 二维数组
        int[][] tmp = new int[len + 1][len + 1];

        for (int i = 1; i < len + 1; i++) {
            for (int j = 1; j < len + 1; j++) {
                if (input.charAt(i - 1) == reverse.charAt(j - 1)) {
                    tmp[i][j] = tmp[i - 1][j - 1] + 1;
                } else {
                    tmp[i][j] = Math.max(tmp[i - 1][j], tmp[i][j - 1]);
                }
            }
        }

        Set<String> paths = new HashSet<String>() {
        };
        Test1.getAllLcs(input, reverse, tmp, input.length(), reverse.length(), "", paths);

        return String.join("/", paths);
    }

    public static String maxs(String input) {
        String[] prepare = stringFilter(input);
        StringBuffer sb = new StringBuffer();
        for (String str : prepare) {
            String result = findLCS(str);
            sb.append(result);
            sb.append("/");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static void main(String[] args) {
        int[] ints = new int[5];
        System.out.println(ints.length);
        // TODO 输出最长对称字符串：goog
        String input1 = "google";

        System.out.println(maxs(input1));
        // TODO 输出3个最长对称字符串：aba/aca/ada
        String input2 = "abcda";
        System.out.println(maxs(input2));
        // TODO 输出2个最长对称字符串：pop/upu，不会输出特殊字符的对称字符串p-p
        String input3 = "pop-upu";
        System.out.println(maxs(input3));
    }
}