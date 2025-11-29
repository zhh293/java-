package datastructure1;

public class TestFindSumPairs {
    public static void main(String[] args) {
        int[] nums1 = {1, 1, 2, 2, 2, 3};
        int[] nums2 = {1, 4, 5, 2, 5, 4};
        
        FindSumPairs findSumPairs = new FindSumPairs(nums1, nums2);
        
        // 测试count方法
        System.out.println("Count(7): " + findSumPairs.count(7)); // 应该输出2 (1+4 和 2+5)
        
        // 测试add方法
        findSumPairs.add(3, 2); // nums2[3]从2变为4
        
        // 再次测试count方法
        System.out.println("Count(7) after add: " + findSumPairs.count(7)); // 应该输出3 (1+4, 2+5, 1+4)
        System.out.println("Count(8) after add: " + findSumPairs.count(8)); // 应该输出2 (2+4 twice)
    }
}