package algthrom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class 回溯 {
    Set<String> list=new HashSet<>();
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
    }
}
