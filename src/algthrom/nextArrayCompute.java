package algthrom;

public class nextArrayCompute {
    public static void computeNext(int[]next,String str){
        int j=0;
        int i=1;
        next[0]=0;
        while(i<next.length){
            if(str.charAt(i)==str.charAt(j)){
                next[i]=j+1;
                i++;
                j++;
            }
            else{
                if(j==0){
                    next[i]=0;
                    i++;
                }
                else{
                    j=next[j-1];
                }
            }
        }
    }
    public static void main(String[] args) {
        String str="abcaabbcabcaabdab";
        int[]next=new int[str.length()];
        computeNext(next,str);
        for (int j : next) {
            System.out.print(j + " ");
        }
    }
}
