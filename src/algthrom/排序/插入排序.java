package algthrom.排序;

public class 插入排序 {
    public static void main(String[] args) {
        int[] arr = {5, 4, 3, 2, 1};
        insertSort(arr);
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
   /* 插入排序的核心思想
    插入排序是一种简单直观的排序算法，它的设计灵感来源于人们整理扑克牌的方式。其基本思路是：
    将数组分为两个部分：已排序部分和未排序部分
    初始时，已排序部分只有第一个元素
            每次从未排序部分取出一个元素
    将该元素插入到已排序部分的正确位置
    重复步骤3-4，直到所有元素都被处理*/

    /* 想象你在整理一手扑克牌：
     开始时，你手中只有一张牌（第一张），这张牌自然是有序的
     拿起第二张牌，与手中的牌比较，放在合适的位置（前面或后面）
     拿起第三张牌，在手中已有的有序牌中从右到左依次比较，找到合适的插入位置
     重复这个过程，直到所有牌都整理完成
     在程序实现中：
     我们通常假设数组的第一个元素已经是"已排序"的
     从第二个元素开始，逐个将其插入到前面已排序部分的正确位置
             插入过程通过将比当前元素大的元素向右移动来实现*/
    public static void insertSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int temp = arr[i];
            for (int j = i - 1; j >= 0; j--) {
                if (arr[j] > temp) {
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                } else {
                    break;
                }
            }
        }
    }
}
