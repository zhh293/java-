package algthrom.排序;

public class 冒泡排序 {
    public static void main(String[] args) {
        int[] arr = { 5, 4, 3, 2, 1 };
        bubbleSort(arr);
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
    private static void bubbleSort(int[] arr) {
        for(int i=0;i<arr.length;i++){
            for(int j=i+1;j<arr.length;j++){
                if(arr[i]>arr[j]){
                    int temp=arr[i];
                    arr[i]=arr[j];
                    arr[j]=temp;
                }
            }
        }
    }
}
