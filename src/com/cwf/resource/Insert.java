package com.cwf.resource;

import com.cwf.util.SortUtil;

import java.util.Arrays;

public class Insert {
    public static void main(String[] args) {
        int [] arr = {9,0,2,3,8,4,7,5,6,1};
        insert(arr);
        System.out.println(Arrays.toString(arr));
//        传入当前类的.class对象 和 排序方法名
//        System.out.println(SortUtil.check(Insert.class, "insert"));
    }
    public static void insert(int[] arr){
        for(int i = 1;i<arr.length;i++ ){
            // 用临时变量 存放我们要插入的数据
            int temp = arr[i];
            // 存放临时变量的位置
            int j;
            int count=0;
            // 用该临时变量与它之前的作比较，
            // 如果前面的数比临时变量大，就往后挪一位
            // 直到前面的数比较完或者遇到一个比临时变量小的
            for ( j = i; j >0&&arr[j-1]>temp  ; j--) {
                SortUtil.swap(arr,j,j-1);
                count++;
            }
            System.out.println("经过第"+i+"次循环,temp="+temp+",比较了"+count+"次,结果为"+Arrays.toString(arr));
        }
    }
}
