package algthrom.排序;

public class QuickSort {
    public static void main(String[] args) {
        int[] arr = {5, 4, 3, 2, 1};
        quickSort(arr, 0, arr.length - 1);
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
    public static void quickSort(int[] arr, int left, int right) {
            //先选基准点，比基准点小的在左边，比基准点大的在右边
            //先写一个函数调整元素的位置
            //然后以基准点为界，对左右两部分进行递归，直到左右指针相遇，这时会得到一个有序的数组
        if(left<right){
            int pivot=partition(arr,left,right);
            quickSort(arr,left,pivot-1);
            quickSort(arr,pivot+1,right);
        }
    }
    private static int partition(int[] arr, int left, int right) {
        int pivot=arr[left];
        while(left<right){
            while(left<right&&arr[right]>=pivot) right--;
            arr[left]=arr[right];
            while(left<right&&arr[left]<=pivot) left++;
            arr[right]=arr[left];
        }
        arr[left]=pivot;
        return left;
    }
}
