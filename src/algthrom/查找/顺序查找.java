package algthrom.查找;

public class 顺序查找 {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    }
    public void search(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                System.out.println("找到目标元素，索引为：" + i);
                return;
            }
        }
        System.out.println("未找到目标元素");
    }
}
