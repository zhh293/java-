package datastructure1;

public class sparsearray {
    public static void main(String[] args) {
        //稀疏数组，对二维数组进行压缩，当一个二维数组大部分元素为零或者为同一个值，可以用稀疏数组来保存
        /*二维数组转稀疏数组的思路
遍历原始的二维数组，得到有效数据的个数 sum
根据 sum 就可以创建稀疏数组 sparseArr int [sum + 1][3]
将二维数组的有效数据存入到稀疏数组
稀疏数组转原始的二维数组的思路
先读取稀疏数组的第一行，根据第一行的数据，创建原始的二维数组，比如上面的 chessArr2 = int [11][11]
在读 取稀疏数组后几行的数据，并赋给原始的二维数组即可*/
        int [][]chessarr=new int[11][11];
        chessarr[1][2]=1;
        chessarr[2][4]=2;
        int sum=0;
        for(int i=0;i<chessarr.length;i++){
            for(int j=0;j<chessarr[i].length;j++){
                if(chessarr[i][j]!=0){
                    sum++;
                }
            }
        }
        int [][]arr=new int[sum+1][3];
        arr[0][0]=11;
        arr[0][1]=11;
        arr[0][2]=sum;
        int row=1;
        for(int i=0;i<chessarr.length;i++){
            for(int j=0;j<chessarr[i].length;j++){
                if(chessarr[i][j]!=0){
                    arr[row][0]=i;
                    arr[row][1]=j;
                    arr[row][2]=chessarr[i][j];
                    row++;
                }
            }
        }
        System.out.println();
        for(int i=0;i<arr.length;i++){
            for(int j=0;j<arr[i].length;j++){
                System.out.print(arr[i][j]+" ");
            }
            System.out.println();
        }
        //逆转的操作有手就行，自己写吧
    }
}
