package algthrom;

import java.util.HashMap;
import java.util.Map;

public class 滑动窗口 {
    /*//无重复字符的最长字串长度、
    String str="abcabcbb";*/
    public static void main(String[] args) {


        String str="abcabcbb";
        int i = lengthOfLongestSubstring(str);
        System.out.println(i);
/*        这页内容讲 无重复字符最长子串问题的 “滑动窗口 + 剪枝” 思路，核心是利用 窗口状态的传递性 优化指针移动：
        两种窗口状态 & 指针策略
        窗口 [left, right] 有重复字符：
        若当前窗口已存在重复，那么 right 再右移（比如到 right+1、末尾），新窗口也一定有重复。
→ 此时移动 left 指针（跳过无效情况，避免重复检查）。
        窗口 [left, right] 无重复字符：
        若当前窗口无重复，那么 left 再右移（比如到 left+1），新窗口 [left+1, right] 也一定无重复。
→ 此时移动 right 指针（拓展窗口，尝试找更长子串）。

        简单说：利用 “状态会传递” 的规律，让 left 和 right 单向移动，减少不必要的遍历，提升效率。*/

    }
    public static int lengthOfLongestSubstring(String s){
        int ans=0;;
        Map<Character,Integer> map=new HashMap<>();
        for(int left=0,right=0;right<s.length();right++){
            if(map.containsKey(s.charAt(right))){
                left=Math.max(map.get(s.charAt(right)),left);
            }
            ans=Math.max(ans,right-left+1);
            map.put(s.charAt(right),right+1);
        }

        return ans;
    }


}
