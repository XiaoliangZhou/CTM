package transport.math.util;
/**
 * @Title: IntegerUtils.java
 * @Package edu.jiaotong.transport.mathutils
 * @Description:
 * @author liangxiao.zhou
 * @date sept 07, 2018 17:11:14 PM
 * @version V1.0
 */
public class IntegerUtils
{
	/**
	 * @descr 返回数字是否为空或为0
	 * @param integer 数字       
	 * @return 返回数字是否为空
	 */
	@Deprecated
	public static boolean isNotNull(Integer integer){
		return !isNull(integer);
	}

	/**
	 * @descr 返回数字是否为空或为0
	 * @param integer  数字   
	 * @return 返回数字是否为空
	 */
	public static boolean isNull(Integer integer){
		return integer == null || integer == 0;
	}
        /**
         * 
         * @param str
         * @return 
         */
        public static int toInt(String str){
             return Integer.parseInt(str);
        }
}
