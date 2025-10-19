package algthrom;

public class KMP {
    // KMP算法实现
    public static int strStr(String haystack, String needle) {
        if (needle.isEmpty()) return 0;
        int[] lps = computeLPSArray(needle);
        int i = 0; // 文本指针
        int j = 0; // 模式指针
        while (i < haystack.length()) {
            if (needle.charAt(j) == haystack.charAt(i)) {
                j++;
                i++;
            }
            if (j == needle.length()) {
                return i - j; // 找到匹配，返回起始索引
            } else if (i < haystack.length() && needle.charAt(j) != haystack.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1]; // 回退j
                } else {
                    i++; // j=0时无法回退，i前移
                }
            }
        }
        return -1; // 未找到匹配
    }

    // 计算部分匹配表（最长前缀后缀数组）
    private static int[] computeLPSArray(String pattern) {
        int[] lps = new int[pattern.length()];
        int len = 0;
        int i = 1;
        lps[0] = 0; // lps[0]始终为0

        while (i < pattern.length()) {//AAAB
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1]; // 回退len
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    public static void main(String[] args) {
        String haystack = "ABABDABACDABABCABAB";
        String needle = "ABABCABAB";
        int index = strStr(haystack, needle);
        System.out.println("Pattern found at index: " + index); // 输出10
    }
}
