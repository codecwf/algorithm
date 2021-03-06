package com.cwf.resource;

import com.cwf.util.SortUtil;

public class Selection {
    public static void main(String[] args) {
        System.out.println(SortUtil.check(Selection.class, "selection"));
    }

    //选择排序算法
    public static void selection(int[] arr) {
        int min_pos=0;
        for (int j=0;j<arr.length-1;j++ ){
            min_pos=j;
            //找到数组中最小的值对应的索引
            for(int i =j+1;i<arr.length;i++ ){
                if(arr[i]<arr[min_pos])
                    min_pos=i;
            }
            //将最小位置和 数组中还未排序的第一个进行交换
            int temp= arr[j];
            arr[j]=arr[min_pos];
            arr[min_pos] = temp;
//            System.out.println("经过第"+(j+1)+"次循环最小位置是："+min_pos+"  结果是："+ Arrays.toString(arr));
        }
//        System.out.println(Arrays.toString(arr));
    }
}
