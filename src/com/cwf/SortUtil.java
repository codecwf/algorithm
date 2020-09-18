package com.cwf;

import java.util.Random;

public class SortUtil {
    /*
     *产生随机数组
     */
    public static int[] randomArr(Integer length){
        int arr[] =new int[length] ;
        Random random = new Random();
        for (int i=0;i<length;i++){
            int randomNum = random.nextInt(length);
            arr[i]=randomNum;
        }
        return arr;
    }

    /*
 验证器
  */
    public static Boolean matcher(int[] arr1,int[] arr2){
        for (int i = 0; i < arr1.length ; i++) {
            if(arr1[i]!=arr2[i]){
                System.out.println(arr2[i]);
                System.out.println(arr1[i]);
                return false;
            }
        }
        return true;
    }


    //交换数组中两个数的位置
    public  static  void  swap(int[] arr,int i,int j){
        int temp= arr[j];
        arr[j]=arr[i];
        arr[i] = temp;
    }




}
