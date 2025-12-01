package algthrom.查找;

public class 二分查找 {
    public void BinarySearch(int[]nums,int target){
        int left=0;
        int right=nums.length-1;
        while(left<=right){
            int mid=(left+right)/2;
            if(nums[mid]==target){
                System.out.println(mid);
                return;
            }else if(nums[mid]>target){
                right=mid-1;
            }else{
                left=mid+1;
            }
            System.out.println("没有找到");
        }
    }
}
