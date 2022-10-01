
package transport.ctm.util;

import java.util.List;

/**
 * @Title: strutil.java
 * @Package edu.jiaotong.transport.ctm.util
 * @Description: strutil
 * @author liangxiao.zhou
 * @date Jan 18, 2019 16:11:14 PM
 * @version V1.0
 */
public class strutil<T> {

    /**
     * 往字符串数组追加新数据
     * @Title: ctm_simulation
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param arr
     * @param value 元胞集合
     * @return int[]   返回类型
     * @throws
     */
    public static int[] insert(int[] arr, int value) {
            //获取数组长度
            int size = arr.length;
            //新建临时字符串数组，在原来基础上长度加一
            int[] tmp = new int[size + 1];
            //先遍历将原来的字符串数组数据添加到临时字符串数组
            for (int i = 0; i < size; i++){
                tmp[i] = arr[i];
            }
            //在最后添加上需要追加的数据
            tmp[size] = value;
            //返回拼接完成的字符串数组
            return tmp;
    }

    /**
     * 往double数组追加新数据
     * @Title: ctm_simulation
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param arr
     * @param value 元胞集合
     * @return int[]   返回类型
     * @throws
     */
    /**
     * 在数组指定位置追加元素
     * @Title: ctm_simulation
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param arr 原数组
     * @param value 元素
     * @param index 位置
     * @return int[]   返回类型
     * @throws
     */
    public static int[] insert(int[] arr, int value,int index){
        if(index < 0 ){
            throw new ArrayIndexOutOfBoundsException("Illegal index : "+index);
        }
        if(index > arr.length){
            index = arr.length;
        }
        int newarr[] = new int[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            newarr[i] = arr[i];
        }
        for (int i = newarr.length - 1; i > index; i--) {
            newarr[i] = newarr[i - 1];
        }
        newarr[index] = value;
        return newarr;
    }
    public static void insert(double[] arr, double value,int index){
        if(index < 0 ){
            throw new ArrayIndexOutOfBoundsException("Illegal index : "+index);
        }
        if(index > arr.length){
            index = arr.length;
        }
        double newarr[] = new double[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            newarr[i] = arr[i];
        }
        for (int i = newarr.length - 1; i > index; i--) {
            newarr[i] = newarr[i - 1];
        }
        newarr[index] = value;
        arr=newarr;
    }

    /**
     * 转换成二维数组
     * @Title: switchArray
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param phases 一维数组
     * @return int[][]   返回类型
     * @throws
     */
    public static int[][] switchArray(int[] phases){
        if (phases != null || phases.length != 0) {
            int[][] phi = new int[phases.length][2];
            for (int i = 0; i < phases.length; i++)
                for (int j = 0; j < 2; j++) {
                    phi[i][j] = phases[i] + j;
                }
            return phi;
        }
        return null;
    }
    /**
     * @descip 判断集合是否为空
     * @param list
     * @return
     */
    public static boolean isNotNull(List list){ return list!=null && !list.isEmpty(); }

    public static void main(String[] args){
        int[] ste = new int[]{715,717,719,721};
        //switchArray(ste);
        System.out.println(insert(ste,98,20));

    }

}
