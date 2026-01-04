package algthrom;

import java.util.Stack;

public class 走迷宫栈实现 {
    private int[][] migong;
    static int[][] direction = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    public 走迷宫栈实现(int[][] migong,Stack<int[]>stack) {
        this.migong = migong;
        this.stack = stack;
    }
    //递归版本
    public boolean findway(int startx,int starty,int endx,int endy){
        if (startx<0||startx>=migong.length||starty<0||starty>=migong[0].length){
            return false;
        }
        //如果四面都是0，则返回false
        if(migong[startx][starty]!=1){
            return false;
        }
        if(startx==endx && starty==endy){
            return true;
        }
        migong[startx][starty] = 2;
        boolean flag = false;
        for(int i=0;i<4;i++){
            int newx = startx+direction[i][0];
            int newy = starty+direction[i][1];
            if(findway(newx,newy,endx,endy)){
                flag = true;
                break;
            }
        }
        if(!flag){
            migong[startx][starty] = 1;
        }
        return flag;
    }


    //我来说一下为什么要标记为2吧，这个问题也是图遍历中比需要注意的一个问题
    //首先记住结论，标记为 2 本质上就是给走过的路做个 “已打卡” 的标记，防止算法走回头路，进而避免无限往返的死循环。
    //下面来进行解释
/*    先看一个极端但典型的小迷宫（仅 2x2，无墙，全是可走的 1）：
    // 测试用的极简迷宫：全是通路，无墙
    int[][] maze = {
            {1, 1},
            {1, 1}
    };
    // 起点(0,0)，终点(1,1)
    走迷宫栈实现 solver = new 走迷宫栈实现(maze);
solver.findway(0,0,1,1); // 执行这行代码，会直接栈溢出

    用你的逻辑分析：为什么会无限递归（死循环）？
    结合你原始的递归代码（无标记 2 的逻辑），我们拆解findway(0,0,1,1)的调用过程：
    调用findway(0,0,1,1)：边界合法，不是终点，migong[0][0]==1，进入循环；
    遍历方向（右、左、下、上），先走右→调用findway(0,1,1,1)；
    调用findway(0,1,1,1)：边界合法，不是终点，migong[0][1]==1，进入循环；
    遍历方向，先走左→调用findway(0,0,1,1)（又回到起点）；
    回到步骤 1，无限重复findway(0,0) ↔ findway(0,1)的相互调用，直到 JVM 的栈空间被耗尽，抛出StackOverflowError（这就是死循环的表现）。
    关键区别：“多遍历几次” vs “无限递归”
    标记已访问（设为 2）：走了(0,0)就标记为 2，下次(0,1)再往左走时，发现(0,0)==2，就不会再调用findway(0,0)，会尝试其他方向（如下），最终走到终点；*/



    private Stack<int[]> stack ;
    //栈版本实现
    //栈版本就可以记录路线了
    public boolean findway2(int startx,int starty,int endx,int endy){
        //先把首项添加进栈
        stack.push(new int[]{startx,starty});
        int[][]visited = new int[migong.length][migong[0].length];
        for (int i = 0; i < visited.length; i++){
            for (int j = 0; j < visited[0].length; j++){
                visited[i][j] = 0;
            }
        }
        visited[startx][starty] = 1;
        while (!stack.isEmpty()) {
            int[] point = stack.pop();
            if (point[0] == endx && point[1] == endy) {
                return true;
            }
            for (int i = 0; i < 4; i++){
                int newx = point[0] + direction[i][0];
                int newy = point[1] + direction[i][1];
                if (newx >= 0 && newx < migong.length && newy >= 0 && newy < migong[0].length
                    && migong[newx][newy] == 1 && visited[newx][newy] == 0) {
                    stack.push(new int[]{newx, newy});
                    visited[newx][newy] = 1;
                }
            }
        }
        return false;
    }

    //记录路径版本的
    public boolean findway3(int startx,int starty,int endx,int endy){
        Stack<int[]>path = new Stack<>();
        stack.push(new int[]{startx,starty});
        int[][]visited = new int[migong.length][migong[0].length];
        for (int i = 0; i < visited.length; i++){
            for (int j = 0; j < visited[0].length; j++){
                visited[i][j] = 0;
            }
        }
        visited[startx][starty] = 1;
        while (!stack.isEmpty()) {
            int [] point = stack.peek();
            if (point[0] == endx && point[1] == endy) {
                path.addAll(stack);
                return true;
            }
            boolean flag = false;
            for (int i = 0; i < 4; i++){
                int newx = point[0] + direction[i][0];
                int newy = point[1] + direction[i][1];
                if (newx >= 0 && newx < migong.length && newy >= 0 && newy < migong[0].length
                    && migong[newx][newy] == 1 && visited[newx][newy] == 0) {
                    stack.push(new int[]{newx, newy});
                    visited[newx][newy] = 1;
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                stack.pop();
            }
        }
        return false;
    }
}
