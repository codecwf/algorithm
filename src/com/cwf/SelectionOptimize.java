package com.cwf;

import java.util.Arrays;
import java.util.Random;

public class SelectionOptimize {

    public static void main(String[] args) {
        int arr[] =SortUtil.randomArr(1000);
        //拷贝一份用于验证
        int[] copy = Arrays.copyOf(arr, arr.length);
        // 用自己实现的算法排序
        selectionSort(arr);
        //用arrays工具类对复制的数组排序
        Arrays.sort(copy);
        //验证
        System.out.println(SortUtil.matcher(copy, arr));
    }

    public static  void selectionSort(int[] arr){
        int min_pos=0;
        int max_pos=0;
        for (int j=0;j<arr.length/2;j++ ){
            min_pos=j;
            max_pos=arr.length-1-j;
            //找到数组中最小的值对应的索引
            for(int i =j+1;i<arr.length;i++ ){
                min_pos=arr[i]<arr[min_pos]?i:min_pos;
                max_pos=arr[arr.length-i-1]>arr[max_pos]?(arr.length-i-1):max_pos;
            }
            //将最小位置和 数组中还未排序的第一个进行交换
            SortUtil.swap(arr,j,min_pos);
            //将最小位置和 数组中还未排序的最后一个进行交换
            if(j == max_pos ){//处理最大位置刚好被最小数换走的情况
                SortUtil.swap(arr, min_pos, arr.length-j-1);
            } else if(max_pos!=min_pos){//防止最小位置交换后 又换回来
                SortUtil.swap(arr,arr.length-j-1,max_pos);
            }
            System.out.println("经过第"+(j+1)+"次循环最小位置是："+min_pos+"最大位置是："+max_pos+"  结果是："+ Arrays.toString(arr));
        }
    }


}
