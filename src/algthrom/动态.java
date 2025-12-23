package algthrom;

import java.util.*;

public class 动态 {
    Set<String> set=new HashSet<>();
    StringBuilder sb=new StringBuilder();
    public List<String> generateParenthesis(int n) {
        int[]memo=new int[n*2];
        Arrays.fill(memo,-1);
        DynamicPlanOne(n,memo,0,0,0);
        return new ArrayList<>(set);
    }
    public void DynamicPlanOne(int n,int[]memo,int count,int left,int right){
        if(left>n||right>n){
            return;
        }
        if(count==2*n){
            set.add(sb.toString());
            return;
        }
        //第n次的选择无非就是f(n-1)+右括号或者+左括号
        //如果右括号小于左括号并且数量都小于n，加哪个都可以，如果等于的话，只能加左括号，如果大于的话，直接返回
        if(right<left){
            //添加左括号
            sb.append('(');
            DynamicPlanOne(n,memo,count+1,left+1,right);
            sb.deleteCharAt(sb.length()-1);
            //添加右括号
            sb.append(')');
            DynamicPlanOne(n,memo,count+1,left,right+1);
            sb.deleteCharAt(sb.length()-1);
        }
        if(right==left){
            sb.append('(');
            DynamicPlanOne(n,memo,count+1,left+1,right);
            sb.deleteCharAt(sb.length()-1);
        }
    }
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int [][] memo=new int[obstacleGrid.length][obstacleGrid[0].length];
        for (int i = 0; i < obstacleGrid.length; i++){
            Arrays.fill(memo[i],-1);
        }
        return DynamicPlanTwo(obstacleGrid,obstacleGrid.length-1,obstacleGrid[0].length-1,memo);
    }
    public int DynamicPlanTwo(int[][]obstacleGrid,int x,int y,int[][]memo){
        if(x>=obstacleGrid.length||y>=obstacleGrid[0].length||x<0||y<0){
            return 0;
        }
        if(obstacleGrid[x][y]==1){
            return 0;
        }
        if(x==0&&y==0){
            return 1;
        }
        if(memo[x][y]!=-1){
            return memo[x][y];
        }
        //每一格要么从上面来，要么从左边来，两种情况可以进行递归
        int result1=DynamicPlanTwo(obstacleGrid,x-1,y,memo);
        int result2=DynamicPlanTwo(obstacleGrid,x,y-1,memo);
        memo[x][y]=result1+result2;
        return result1+result2;
    }


    public long maxTaxiEarnings(int n, int[][] rides) {
        Arrays.sort(rides,(a,b)->a[0]-b[0]);
        long[] dp=new long[n+1];
        for(int i=0;i<rides.length;i++){

        }

    }
}
