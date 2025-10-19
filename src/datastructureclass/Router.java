package datastructureclass;

import java.util.*;

class Router {

    private int maxLimit;
    private Deque<int[]> queue;
    private Map<Integer,List<int[]>> map;

    public Router(int memoryLimit) {
        maxLimit = memoryLimit;
        queue = new LinkedList<>();
        map = new HashMap<>();
    }

    public boolean addPacket(int source, int destination, int timestamp) {
        //先看看有没有重复的
        //判断有没有重复的
        int[]pre=new int[]{source,destination,timestamp};
        List<int[]> ints = map.get(timestamp);
        if(ints!=null&&!ints.isEmpty()){
            for (int[] ints1 : ints) {
                if(Arrays.equals(pre,ints1)){
                    return false;
                }
            }
        }
        if(ints==null){
            ints = new ArrayList<>();
        }
        if(queue.size()==maxLimit){
            int[] ints1 = queue.pollLast();
            List<int[]> ints2 = null;
            if (ints1 != null) {
                ints2 = map.get(timestamp);
            }
            if(ints2!=null&&!ints2.isEmpty()){
                for (int[] ints21 : ints2) {
                    if(Arrays.equals(ints1,ints21)){
                        ints2.remove(ints21);
                    }
                }
            }
            queue.addFirst(pre);
        }else{
            queue.addFirst(pre);
        }
        ints.add(new int[]{source,destination});
        map.put(timestamp,ints);
        return true;
        //没有重复的，看看满了没有
        //满了移除再放
        //没满直接放
    }

    public int[] forwardPacket() {
        return queue.pollLast();
        //移除最旧的
    }

    public int getCount(int destination, int startTime, int endTime) {
        Set<Map.Entry<Integer, List<int[]>>> entries = map.entrySet();
        int count=0;
        for (Map.Entry<Integer, List<int[]>> entry : entries) {
            if(entry.getKey()>=startTime&&entry.getKey()<=endTime){
                List<int[]> value = entry.getValue();
                for(int[] ints : value){
                    if(ints[1]==destination){
                        count++;
                    }
                }
            }
        }
        return count;
//        返回当前存储在路由器中（即尚未转发）的，且目标地址为指定 destination 且时间戳在范围 [startTime, endTime]（包括两端）内的数据包数量。
    }
}
class Pack { // 类名建议首字母大写，符合Java规范
    public int[] data;

    public Pack(int[] data) {
        this.data = data;
    }

    // 已有的equals方法（稍作规范调整）
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 先判断地址，优化性能
        if (o == null || getClass() != o.getClass()) return false; // 排除null和不同类
        Pack pack = (Pack) o;
        // 处理数组为null的情况（避免NPE）
        if (data == null && pack.data == null) return true;
        if (data == null || pack.data == null) return false;
        // 数组长度不同，直接不相等
        if (data.length != pack.data.length) return false;
        // 逐元素比较
        for (int i = 0; i < data.length; i++) {
            if (data[i] != pack.data[i]) return false;
        }
        return true;
    }

    // 重写的hashCode方法
    @Override
    public int hashCode() {
        if (data == null) return 0; // 若数组为null，返回固定值0
        int result = 17; // 初始质数（选17可减少初始碰撞）
        for (int num : data) { // 遍历数组，累积哈希值
            result = 31 * result + num; // 31为乘数，兼顾效率和低碰撞
        }
        return result;
    }
}

/**
 * Your Router object will be instantiated and called as such:
 * Router obj = new Router(memoryLimit);
 * boolean param_1 = obj.addPacket(source,destination,timestamp);
 * int[] param_2 = obj.forwardPacket();
 * int param_3 = obj.getCount(destination,startTime,endTime);
 */
