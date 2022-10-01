package transport.math.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @Title: MathHandle.java
 * @Package edu.jiaotong.transport.mathutils
 * @Description:
 * @author liangxiao.zhou
 * @date sept 07, 2018 17:11:14 PM
 * @version V1.0
 */
public class MathSupplier {
	// 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;
    private static final int DEFAUTL_SCALE = 3;

    // 这个类不能实例化
    public  MathSupplier() {
    }
	/**
	 * 提供精确的加法运算
	 * @Title: add
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param v1 被加数
	 * @param v2 加数
	 * @return double   返回类型
	 * @throws
	 */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

	/**
	 * 提供精确的减法运算
	 * @Title: sub
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param v1  被减数
	 * @param v2  减数
	 * @return double   返回类型
	 * @throws
	 */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }
	/**
	 * 提供精确的乘法运算
	 * @Title: mul
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return double   返回类型
	 * @throws
	 */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        //return b1.multiply(b2).doubleValue();
        return  b1.multiply(b2).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

	/**
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double mul3(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.multiply(b2).doubleValue();
	}
	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入
	 * @Title: div
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param v1  被除数
	 * @param v2  除数
	 * @return double   返回类型
	 * @throws
	 */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEFAUTL_SCALE);
    }
	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * @Title: div
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param v1  被除数
	 * @param v2  除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return double   返回类型
	 * @throws
	 */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * @Title: div
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param v1  被除数
	 * @param v2  除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return double   返回类型
	 * @throws
	 */
	public static double divScale(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * 提供精确的小数位四舍五入处理
	 * @Title: round
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param v  需要四舍五入的数字
	 * @return double   返回类型
	 * @throws
	 */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
	/**
	 * 提供精确的小数位四舍五入处理。默认两位小数
	 * @Title: roundDefault
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param v  需要四舍五入的数字
	 * @return double   返回类型
	 * @throws
	 */
    public static double roundDefault(double v) {
        return round(v, DEFAUTL_SCALE);
    }

	/**
	 * 取整函数
	 * @Title: divide
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param a 除数
	 * @param b 被除数
	 * @return int   返回类型
	 * @throws
	 */
	public static int divide(int a ,int b) {
		return (int)a / b;
	}
	/**
	 * 取余函数
	 * @Title: rem
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param a 除数
	 * @param b 被除数
	 * @return int   返回类型
	 * @throws
	 */
	public static int rem(int a,int b) {
		return a % b ;
	}
	/**
	 * 提供数组元素的求和运算
	 * @Title: sumCells
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return int   返回类型
	 * @throws
	 */
	public static int sumCells(int[][] cells) {
		int sum = 0;
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[0].length; j++) {
				if(cells[i][j] > 0) {
					sum += cells[i][j];
				}
			}
		}
		return sum;
	}
	/**
	 * 取中间值
	 * @Title: median
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param a
	 * @param b
	 * @param c
	 * @return double   返回类型
	 * @throws
	 */
	public static double median(double a,double b,double c){
		if ((b - a) * (a - c) >= 0) {
		    return a;
		} else if ((a - b) * (b - c) >= 0) {
		    return b;
		} else {
            return c;
		}
	}
	/**
	 * 返回三个数的最小值
	 * @Title: medmin
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param a
	 * @param b
	 * @param c
	 * @return double   返回类型
	 * @throws
	 */
	public static double medmin(double a,double b,double c){
            double min = (a < b) ? a :b ;
            min = (min < c) ? min : c ;
            return min;
        }
	/**
	 * 返回三个数的最小值
	 * @Title: medmin
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param a
	 * @param b
	 * @param c
	 * @return double   返回类型
	 * @throws
	 */
	public static double medmax(double a,double b,double c){
		double min = (a > b) ? a :b ;
		min = (min < c) ? min : c ;
		return min;
	}

	public static double roude(double v1) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		return b1.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 将double格式化为指定小数位的String，不足小数位用0补全
	 * @param v     需要格式化的数字
	 * @param scale 小数点后保留几位
	 * @return
	 */
	public static String roundByScale(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The   scale   must   be   a   positive   integer   or   zero");
		}
		if(scale == 0){
			return new DecimalFormat("0").format(v);
		}
		String formatStr = "0.";
		for(int i=0;i<scale;i++){
			formatStr = formatStr + "0";
		}
		return new DecimalFormat(formatStr).format(v);
	}

	public static String contCells(int[][] cells) {
		int cot = 0;
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[0].length; j++) {
				if(cells[i][j] == 1) {
					cot  = cot+1;
				}
			}
		}
		return "车辆数N = "+ cot;
	}
	
	public static void main(String[] args) {
//		int[][] ksd = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};
//		System.out.println(mul(1.5096, 0.22));;
//		System.out.println(contCells(ksd));    
		//System.out.println(median(2.0, 34.7, 13.5));

	/*	System.out.println(div(mul(12,150),875));
		System.out.println(mul(div(mul(12,150),875),3.6));*/
		System.out.println(div(375,751));

	}
	
}
