package com.cwf.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
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


//        int temp= arr[j];
//        arr[j]=arr[i];
//        arr[i] = temp;
        arr[i]=arr[i]^arr[j];
        arr[j]=arr[i]^arr[j];
        arr[i]=arr[i]^arr[j];
    }


    //检查
    public  static  Boolean  check(Class sortClass,String sortMethodName)   {
        try {
            int arr[] = SortUtil.randomArr(1000);
            //拷贝一份用于验证
            int[] copy = Arrays.copyOf(arr, arr.length);
            // 用自己实现的算法排序
            Method sort = sortClass.getMethod(sortMethodName, int[].class);
            sort.invoke(arr.getClass(),arr);
            //用arrays工具类对复制的数组排序
            Arrays.sort(copy);
            //返回结果
            return SortUtil.matcher(copy, arr);
        }catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
            return  false;
        }
    }



}
