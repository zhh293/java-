package algthrom;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Test {
   /* public static void main(String[] args) {
        int[]candidates={10,1,2,7,6,1,5};
        int target=8;
        List<List<Integer>> lists = combinationSum2(candidates, target);
        System.out.println(lists);
    }*/
   /* public static List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        int length=candidates.length-1;
        while(length>=0&&candidates[length]>target){
            length--;
        }
        int pre=0;
        Set<List<Integer>>list =new HashSet<>();
        List<Integer>List=new ArrayList<>();
        int count=0;
        int j=length-1;
        // 1 2 2 2 5
        for(int i=length;i>=0;i--){
            System.out.println("i的值为"+i);
            j=i-1;
            pre=j;
            count=candidates[i];
            List.add(count);
            while(j>=0){
                System.out.println("我在这里，j的值为"+j+"集合为"+List);
                if(count>target&&List.size()==1){
                    list.add(new ArrayList<>(List));
                    List.clear();
                    break;
                }else if(count>target){
                    int number=List.size()-1;
                    System.out.println("number的值为"+number);
                    count-=List.get(number);
                    List.remove(number);

                    //1 1 2 5 6 7 10
                    continue;
                }
                if(count==target){
                    Collections.sort(List);
                    list.add(new ArrayList<>(List));
                    //相等了之后，妥善处理，我既要保证每个数都照顾到，两个的时候，
                    //我感觉j这个数字的处理非常重要，不能随便的++，--
                    //此时不需要再看后面了，因为后面的数字一定小于等于最后一个数字
                    //第一个元素是固定的，这个毋庸置疑，然后需要看看从第二个元素往后是否能进行拆分
                    //能拆分的话，等拆分完全之后直接break就可以了
                   *//* 输出
[[1,4,5],[2,4,4],[2,3,5],[1,2,3,4]]
1 1 2 3 4 4 4 4 4 5
预期结果
[[1,1,3,5],[1,1,4,4],[1,2,3,4],[1,4,5],[2,3,5],[2,4,4]]*//*
                    for(int k=0;k<List.size()-1;k++){
                        ArrayList<Integer> integers = new ArrayList<>(List);
                        integers.remove(k);
                        count-=List.get(k);
                        int next=j-1;
                        while(next>=0){
                            if(count+candidates[next]>target){
                                next--;
                                continue;
                            }
                            if(count+candidates[next]==target){
                                integers.add(candidates[next]);
                                Collections.sort(integers);
                                list.add(new ArrayList<>(integers));
                                break;
                            }
                            if(count+candidates[next]<target){
                                count+=candidates[next];
                                integers.add(candidates[next]);
                                next--;
                            }
                        }
                    }
                    break;
                }
                //7 6 5
                count+=candidates[j];
                System.out.println("count的值为"+count);
                List.add(candidates[j]);
                System.out.println("List的值为"+List.toString());
                j--;
            }
            if(count==target){
                System.out.println("这里出来的集合石"+List);
                Collections.sort(List);
                list.add(new ArrayList<>(List));
            }
            System.out.println("这里出来的dadada集合石"+list);
            List.clear();
        }
        return  new ArrayList<>(list);
    }*/
   /*public int possibleStringCount(String word) {
       //找到字母变化的分界线，这个分界线中包含的相同的字母数-1就是这一块对应的可能性，加一起即可，count起始为1
       Map<Character,Integer>map=new HashMap<>();
       int count=1;
       map.put(word.charAt(0),count);
       for(int i=1;i<word.length();i++){
           if(map.containsKey(word.charAt(i))){
               Integer i1 = map.get(word.charAt(i));
               i1++;
               map.put(word.charAt(i),i1);
           }else{
               map.put(word.charAt(i),1);
               count+=(map.get(word.charAt(i-1))-1);
               map.remove(word.charAt(i-1));
           }
       }
       return count;
   }*/
 /*  public boolean validPalindrome(String s) {
        char[] pre=s.toCharArray();
       for(int i=0;i<s.length();i++){
           if(!isPromeString(s)){
               //把pre[i]变成空字符
               pre[i]=' ';
              String str=new String(pre);
              if(isPromeString(str)){
                  return true;
              }
              pre[i]=s.charAt(i);
           }
       }
       return false;
   }
    public boolean isPromeString(String s){
        for(int i=0;i<s.length()/2;i++){
            if(s.charAt(i)!=s.charAt(s.length()-i-1)){
                return false;
            }
        }
        return true;
    }
}*/
   /*public List<List<Integer>> permuteUnique(int[] nums) {
       //[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]
       //怎么办呢，怎么才能找到一个合理的规律呢
       //感觉还是要用递归，因为每一次反转完都是一个独立的数组，还能继续反转
       //我选择使用一个集合来装所有可能的情况来实现机器帮我去重
       //怎么样翻能把所有情况都翻出来呢
       Integer []num=new Integer[nums.length];
       for(int i=0;i<nums.length;i++){
       num[i]=nums[i];
       }
       Set<List<Integer>> list=new HashSet<>();
       getAllList(num,0,list);
       return new ArrayList<>(list);
   }
   public void getAllList(Integer[] nums,int index,Set<List<Integer>> list){
       if(index==nums.length){
          list.add(new ArrayList<>(List.of(nums)));
          return;
       }
       //把num转换成list
       List<Integer> list1=new ArrayList<>(List.of(nums));
      Collections.swap(list1,index,nums.length-1);
       getAllList(nums,index+1,list);
      Collections.swap(list1,index,nums.length-1);
   }*/


   /* public boolean validPalindrome(String s) {
        char[] pre=s.toCharArray();
        for(int i=0;i<s.length();i++){
            if(!isPromeString(s)){
                pre[i]='\0';
                String str=new String(pre);
                StringBuilder sb=new StringBuilder();
                for (int i1 = 0; i1 < str.length(); i1++) {
                    if(str.charAt(i1) == '\0')continue;
                    sb.append(str.charAt(i1));
                }
                String sb1=sb.toString();
                if(isPromeString(sb1)){
                    return true;
                }
                pre[i]=s.charAt(i);
            }else{
                return true;
            }
            System.out.println();
        }
        return false;
    }
    public boolean isPromeString(String s){
        for(int i=0;i<s.length()/2;i++){
            if(s.charAt(i)!=s.charAt(s.length()-i-1)){
                return false;
            }
        }
        return true;
    }*/
    List<String>list=new ArrayList<>();
    StringBuilder sb=new StringBuilder();
    static int left=1;
    static int right=0;
    public List<String> generateParenthesis(int n) {
        //n表示能生成的对数，那就是说明了递归的深度了
        sb.append('(');
        backTrending(n,list,sb,true);
        return list;
    }
    public void backTrending(int n,List<String>list,StringBuilder str,boolean panduan){
        if(str.length()==2*n){
            //把这个字符串添加进去
            StringBuilder sb1=new StringBuilder(str);
            list.add(sb1.toString());
            return;
        }
        for(int i=1;i<=n*2;i++){
            //这个边界不好把握啊，fuck
            //往这里面添加吧，关键怎么保证左括号能遇上右括号呢，要不就弄一个静态变量，专门来查看左括号和右括号的数量，有左括号的时候才能放右括号，而且右括号的数量不能多于左括号，而且最后左括号数量一定与右括号数量一样，
            if(right<left){
                //看看左括号满了吗，没满的话随便加，满了的话只能加右括号了
                if(left==n){
                    sb.append(')');
                    right++;
                }else{
                    //两个随便加一个
                    if(panduan){
                        sb.append('(');
                        panduan=false;
                    }else{
                        sb.append(')');
                        panduan=true;
                    }
                }
            }
            if(right==left){
                sb.append('(');
                left++;
            }
            backTrending(n,list,str,panduan);
            //回退逻辑，把最后一位移除，并且把左右括号的数量做一个调整
            char c = str.charAt(str.length()-1);
            sb.deleteCharAt(sb.length()-1);
            if(c=='('){
                left--;
            }else{
                right--;
            }
        }
    }

       /*Set<List<Integer>> list=new HashSet<>();
       List<Integer>list1=new ArrayList<>();

       public List<List<Integer>> permute(int[] nums) {
           backTrending(nums,list,list1,0);
           return new ArrayList<>(list);
       }
        void backTrending(int []nums,Set<List<Integer>> list,List<Integer>list1,int index){
           if(index==nums.length-1){
               list.add(new ArrayList<>(list1));
               return;
           }
           for(int i=index;i<nums.length;i++){
               swap(nums,index,i);
               list1.add(nums[i]);
               backTrending(nums,list,list1,index+1);
               list1.removeLast();
               swap(nums,index,i);
           }
       }
        void swap(int []nums,int a,int b){
           int temp=nums[a];
           nums[a]=nums[b];
           nums[b]=temp;
       }*/
    /*Set<List<Integer>> list=new HashSet<>();
    List<Integer> list1=new ArrayList<>();
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        backTrending(candidates,target,list,list1,0);
        return new ArrayList<>(list);
    }
    public void backTrending(int[]candidates,int target,Set<List<Integer>> list,List<Integer> list1,int count){
        if(count==target){
            Collections.sort(list1);
            System.out.println(list1);
            list.add(new ArrayList<>(list1));
            return;
        }
        if(count>target){
            return;
        }
        for(int i=0;i<candidates.length;i++){
            count+=candidates[i];
            list1.add(candidates[i]);
            backTrending(candidates,target,list,list1,count);
            count-=candidates[i];
            list1.remove(list1.size()-1);
        }
        //回退
    }*/
}