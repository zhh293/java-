package algthrom;

import java.util.*;

public class 字谜游戏 {
    public boolean predictWord(String word1){
        List<Character> list=new ArrayList<>();
        for (char c:word1.toCharArray()){
            list.add(c);
        }
        Set<Character> set=new HashSet<>(list);
        int i=0;
        Scanner sc=new Scanner(System.in);
        do{
            System.out.println("请输入一个单词:");
            String word2=sc.next();
            boolean result= printResult(word1,word2,set);
            if(result)return true;
            i++;
        }while (i<6);
        return false;
    }
    private boolean printResult(String word1,String word2,Set<Character>set){
        if(!word1.equals(word2)){
            System.out.println("输入的单词长度有误！");
        }
        boolean flag=true;
        List<Integer>list=new ArrayList<>();
        for (int i = 0; i < word1.length(); i++){
            if (word1.charAt(i)==word2.charAt(i)){
                list.add(状态.绿色.ordinal());
            }else if (set.contains(word2.charAt(i))){
                list.add(状态.黄色.ordinal());
                flag=false;
            }else{
                list.add(状态.灰色.ordinal());
                flag=false;
            }
        }
        System.out.println(list);
        return flag;
    }
    private void convert(List<Integer>list){
        for (int i = 0; i < list.size(); i++){
            switch (list.get(i)){
                case 0:
                    System.out.print("G");
                    break;
                case 1:
                    System.out.print("Y");
                    break;
                case 2:
                    System.out.print("B");
                    break;
            }
        }
    }
}
enum 状态{
    绿色,
    黄色,
    灰色
}

//新生的寒假作业，做着玩一玩