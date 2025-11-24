package algthrom.排序;

public class 选择排序 {
    public static void main(String[] args) {
        int[] arr = {5, 4, 3, 2, 1};
        selectSort(arr);
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
    private  static void selectSort(int[] arr){
         for(int i=0;i<arr.length;i++){
             int min=i;
             for(int j=i+1;j<arr.length;j++){
                 if(arr[j]<arr[min]){
                     min=j;
                 }
             }
             if(min!=i){
                 int temp=arr[i];
                 arr[i]=arr[min];
                 arr[min]=temp;
             }
         }
    }


    /*现在我来详细解释一下选择排序的思路：
    选择排序的核心思想
    选择排序是一种简单直观的排序算法，它的工作原理如下：
    找最值：在未排序的数据中找到最小（或最大）元素
    放到位：将这个元素放到已排序序列的末尾
    缩小范围：从未排序部分继续寻找下一个最值
    重复执行：重复以上步骤直到所有元素都被处理
            具体实现过程
    以升序排列为例：
    初始状态：整个数组都是未排序区域
    第1轮：在全部n个元素中找到最小值，将其与第1个元素交换位置
    第2轮：在剩下的n-1个元素中找到最小值，将其与第2个元素交换位置
    第i轮：在剩下的n-i+1个元素中找到最小值，将其与第i个元素交换位置
            直到最后只剩一个元素为止
    算法特点
    时间复杂度：无论什么情况都是 O(n²)，因为需要进行 n-1 轮比较
    空间复杂度：O(1)，只需要常量级额外空间
    稳定性：不稳定排序，相同元素的相对位置可能改变
    原地排序：是原地排序算法，不需要额外存储空间*/


}
