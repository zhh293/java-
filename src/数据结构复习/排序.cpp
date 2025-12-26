//冒泡排序
void bubbleSort(int*arr,int n){
    for(int i=0;i<n;i++){
        for(int j=i+1;j<n;j++){
            if(arr[i]>arr[j]){
                int temp=arr[i];
                arr[i]=arr[j];
                arr[j]=temp;
            }
        }
    }
}

//选择排序
void selectSort(int*arr,int n){
    for(int i=0;i<n;i++){
        int min=i;
        for(int j=i+1;j<n;j++){
            if(arr[j]<arr[min]){
                min=j;
            }
        }
        int temp=arr[i];
        arr[i]=arr[min];
        arr[min]=temp;
    }
}

//插入排序
void insertSort(int*arr,int n){
    for(int i=1;i<n;i++){
        int j=i-1;
        while(j>=0&&arr[j]>arr[j+1]){
            int temp=arr[j];
            arr[j]=arr[j+1];
            arr[j+1]=temp;
            j--;
        }
    }
}

//快速排序
void quickSort(int*arr,int left,int right){
    if(left<right){
        int pivot=partition(arr,left,right);
        quickSort(arr,left,pivot-1);
        quickSort(arr,pivot+1,right);
    }
}


int partition(int*arr,int left,int right){
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


void printArr(int*arr,int n){
    for(int i=0;i<n;i++){
        printf("%d ",arr[i]);
    }
}

//基数排序，希尔排序这些知道排序的原理即可
