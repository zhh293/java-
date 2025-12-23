package algthrom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class 回溯 {
    /*Set<String> list=new HashSet<>();
    StringBuilder sb=new StringBuilder();
    public List<String> generateParenthesis(int n) {
        backtrack(list,sb,0,0,n);
        return new ArrayList<>(list);
    }
    public void backtrack(Set<String>result,StringBuilder sb,int open,int close,int max){
        if(open<close){
            return;
        }
        if(sb.length()==max*2){
            result.add(sb.toString());
            return;
        }

        if(open> max|| close>max){
            return;
        }
        if(open>=close){
            sb.append('(');
            backtrack(result,sb,open+1,close,max);
            sb.deleteCharAt(sb.length()-1);
            sb.append(')');
            backtrack(result,sb,open,close+1,max);
            sb.deleteCharAt(sb.length()-1);
        }
    }*/

    private List<String>list=new ArrayList<>();
    public List<String> restoreIpAddresses(String s) {
        backTrend(s,new StringBuilder(),0,0);
        return list;
    }
    public void backTrend(String s,StringBuilder sb,int count,int start){
        if(count==4&&start==s.length()){
            String pre = sb.substring(0, sb.length() - 1);
            list.add(pre);
            return;
        }
        // 剪枝：剩余字符数超出合理范围（每段最多3位）
        if (count >= 4 || start >= s.length()) {
            return;
        }
        for (int i = 1; i <= 3; i++) {
            // 边界检查：避免索引越界
            if (start + i > s.length()) {
                break;
            }
            // 截取当前段的字符串
            String segment = s.substring(start, start + i);
            // 校验当前段是否合法
            if (!isValidSegment(segment)) {
                continue;
            }

            // 选择：拼接当前段和分隔符
            int len = sb.length();
            sb.append(segment).append('.');
            // 回溯：处理下一段
            backTrend(s, sb, count + 1, start + i);
            // 撤销选择：恢复StringBuilder状态
            sb.setLength(len);
        }
    }
    private boolean isValidSegment(String segment) {
        int len = segment.length();
        // 1. 长度超过3位 或 空字符串：非法
        if (len == 0 || len > 3) {
            return false;
        }
        // 2. 前导零：长度>1且以0开头（比如"01"非法，"0"合法）
        if (len > 1 && segment.charAt(0) == '0') {
            return false;
        }
        // 3. 数值超过255：非法
        int num = Integer.parseInt(segment);
        return num >= 0 && num <= 255;
    }




    /*private List<TreeNode>list=new ArrayList<>();
    public List<TreeNode> generateTrees(int n) {
        Set<Integer>set=new HashSet<>();
        for(int i=1;i<=n;i++){
            TreeNode root=new TreeNode(i);
            set.add(i);
            generateTrees(i,set,root);
            set.remove(i);
        }
        return list;
    }
    public void generateTrees(int n,Set<Integer>set,TreeNode root){
        if(set.size()==n){
            return;
        }
        buildBinaryTree(root,n);
        for(int i=1;i<=n;i++){
            if(set.contains(i)){
                continue;
            }
            //从root开始构建
            set.add(i);
            generateTrees(i,set,root);
            set.remove(i);
        }
    }
    public TreeNode buildBinaryTree(TreeNode root,int data){
        if(root==null){
            TreeNode node=new TreeNode(data);
            return node;
        }
        if(data<root.val){
            TreeNode left=buildBinaryTree(root.left,data);
            root.left=left;
        }else{
            TreeNode right=buildBinaryTree(root.right,data);
            root.right=right;
        }
        return root;
    }
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }*/
}
