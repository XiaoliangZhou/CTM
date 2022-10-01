package transport.math.util;
/**
 * @Title: CellHandle.java
 * @Package edu.jiaotong.transport.mathutils
 * @Description: 提供元胞数组相关运算
 * @author liangxiao.zhou
 * @date sept 07, 2018 17:11:14 PM
 * @version V1.0
 */
public class CellHandle {

    // 默认数组填充值
    public static int DEF_CELL_FILL = 1;
    public static double defCellFill = 1;

    /**
     * @描述: 创建 2*N维元胞数组
     * @param row 行数
     * @param row 列数
     * @return array
     * @备注:
     */
    public static int[][] zeros(int row, int col) {
        int[][] array = new int[row][col];
        if (IntegerUtils.isNotNull(row)) {
            for (int i = 0; i < row; i++) {
                if (!IntegerUtils.isNull(col)) {
                    for (int j = 0; j < col; j++) {
                        if (i != j) {
                            array[i][j] = DEF_CELL_FILL;
                        } else {
                            array[i][j] = 0;
                        }
                    }
                }
            }
        }
        return array;
    }
        public static double[][] newArray(int row, int col) {
            double[][] array = new double[row][col];
            if (IntegerUtils.isNotNull(row)) {
                for (int i = 0; i < row; i++) {
                    if (!IntegerUtils.isNull(col)) {
                        for (int j = 0; j < col; j++) {
                            array[i][j] = defCellFill;
                        }
                    }
                }
            }
            return array;
        }

        /**
         * @描述: 控制台输出2*N维元胞数组
         * @param zeros 数组
         * @return
         * @备注:
         */
        public static void printArray(int[][] zeros) {
            for (int i = 0; i < zeros.length; i++) {
                System.out.print(i + "   ");
                for (int r : zeros[i]) {
                    System.out.print(" " + r + " ");
                }
                System.out.println("\n");
            }

        }

        public static int[][] copyCell(int[][] zeros) {
            int[][] new_cells = new int[zeros.length][zeros[0].length];
            for (int i = 0; i < zeros.length; i++) {
                for (int j = 0; j < zeros[0].length; j++) {
                    new_cells[i][j] = zeros[i][j];
                }
            }
            return new_cells;

        }

        public static void main(String[] args) {
            int[][] zeros = zeros(1, 400);
            System.out.println(zeros.length);
            printArray(zeros);
        }
    }
