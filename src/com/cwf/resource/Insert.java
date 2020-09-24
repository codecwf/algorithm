package com.cwf.resource;

import com.cwf.util.SortUtil;

import java.util.Arrays;

public class Insert {
    public static void main(String[] args) {
        int [] arr = {9,0,2,3,8,4,7,5,6,1};
        insert(arr);
        System.out.println(Arrays.toString(arr));
//        SortUtil.check(Insert.class,"insert");
    }
//    冒泡
//    public static void insert(int[] arr){
//        for(int i = 0;i<arr.length-1;i++ ){
//            for(int j=0;j<arr.length-1-i;j++ ){
//                if(arr[j]>arr[j+1]){
//                    SortUtil.swap(arr,j+1,j);
//                }
//            }
//        }
//    }

    public static void insert(int[] arr){

        for(int i = 1;i<arr.length;i++ ){
            int temp = arr[i];
            int j;
            for ( j = i; j >0&&arr[j-1]>temp  ; j--) {
                SortUtil.swap(arr,j,j-1);
            }
            arr[j]= temp;
        }
    }
}
