package transport.file.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import transport.ctm.main.initCtm;
import transport.ctm.model.*;
import transport.ctm.util.constant;
import transport.graph.*;
import transport.math.util.CellHandle;
import transport.math.util.MathSupplier;

import java.io.*;
import java.util.*;
import java.io.IOException;


/**
 * @Title: fileutils.java
 * @Package transport.fileutils
 * @Description: 提供文件的读写操作
 * @author liangxiao.zhou
 * @date sept 08, 2018 10:11:14 PM
 * @version V1.0
 */
public class fileutils {

    private static List<ctmLinks> ctms = constant.ctms;
    private static double vs = 15.0; // 自由流速度
    private static double cl = 150.0;// 元胞长度
    private static double stepTime = 10.0;


    /**
     * 提供元胞数组写入txt的method
     * @Title: writeToTxtBypath
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param path 路径
     * @param matrix_cells 元胞数组
     * @return void   返回类型
     * @throws
     */
    public static void writeToTxtBypath(String path,double[][] matrix_cells) throws Exception {
        BufferedWriter bfdr = new BufferedWriter(new FileWriter(new File(path)));
        /* 创建“文件输出流”对应的BufferedWriter*/
        /* 它对应缓冲区的大小是16，即缓冲区的数据>=16时，会自动将缓冲区的内容写入到输出流。*/
        try {
            if (path != null && path.length() != 0) {
                File file = new File(path);
                if (!file.exists()) {
                    file.createNewFile();
                }
                for (double[] matrix_cell : matrix_cells) {
                    for (double v : matrix_cell) {
                        if(v!=0.0000){
                            String c = MathSupplier.roundByScale(v,4);
                            bfdr.write(c + "    ");
                        }
                    }
                    /*将“换行符\r\n”写入到输出流中*/
                    bfdr.write("\r\n");
                }
                bfdr.flush();
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); } catch (SecurityException e) {
            e.printStackTrace(); } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * 提供元胞数组写入txt的method
     * @Title: writeToTxtBypath
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param path 路径
     * @param matrix_cells 元胞数组
     * @return void   返回类型
     * @throws
     */
    public static void writeNetAvgToTxtBypath(String path,double[] matrix_cells) throws Exception {
        BufferedWriter bfdr = new BufferedWriter(new FileWriter(new File(path)));
        /* 创建“文件输出流”对应的BufferedWriter*/
        /* 它对应缓冲区的大小是16，即缓冲区的数据>=16时，会自动将缓冲区的内容写入到输出流。*/
        try {
            if (path != null && path.length() != 0) {
                File file = new File(path);
                if (!file.exists()) {
                    file.createNewFile();
                }
                for (double netAvg : matrix_cells) {
                    String c = MathSupplier.roundByScale(netAvg,3);
                    bfdr.write(c + "    ");
                    /*将“换行符\r\n”写入到输出流中*/
                    bfdr.write("\r\n");
                }
                bfdr.flush();
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); } catch (SecurityException e) {
            e.printStackTrace(); } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化路网信息 待完善...
     * @Title: readGraphDataFromTxtByPath
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param vlist 节点集合
     * @param path
     * @return Map<String, Vertex>    返回类型
     * @throws :IOException Exception
     */
    public static void readGraphDataFromTxtByPath(List<Vertex> vlist, String path) throws IOException, Exception {
        File file = new File(path);
        BufferedReader bfrde = new BufferedReader(new FileReader(file));
        double w = 0.0;// 权重
        int i = 1;
        Vertex v = null;
        String lineStr = null;
        List<String> els = null;
        try {
            // 读取TXT 数据
            while ((lineStr = bfrde.readLine()) != null) {
                // 循环取出数据并封装到数组
                String[] vStr = lineStr.split(",");
                int k = Integer.parseInt(vStr[0]);
                i = (i < k) ? k : i ;
                Vertex vtx = IniteGraph.getVertexByName(vlist, String.valueOf(i));
                els = IniteGraph.getVertexByName(vlist, String.valueOf(i)).edgList;// 节点所对应的边
                w = MathSupplier.div(MathSupplier.mul(cl, Integer.parseInt(vStr[3])), vs*stepTime, 1);/* 计算权重 每个车辆在自由流条件下的走行时间 */
                /* 初始化路段元胞、链接、路段*/
                initCtm.initiazeCtm(vStr, vlist, els, w,ctms);
            }
            vlist.forEach((v1)->{
                constant.vertexs.add(v1);
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 关闭流
        bfrde.close();
    }
    /**
     * close BufferedWriter
     * @Title: closeBufferWrite
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param: bfdr BufferedWriter
     * @return void   返回类型
     * @throws: Exception
     */
    public static void closeBufferWrite(BufferedWriter bfdr) throws Exception {
        bfdr.flush();
        bfdr.close();
    }
    /**
     * 加载json文件 初始化交叉口
     * @Title: loadJson
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return void   返回类型
     * @throws: FileNotFoundException
     */
    public static void loadIntData() {
        BufferedReader reader = null;
        List<intersection> ctm_ints = new ArrayList<>();
        try {
            String path = fileutils.class.getClassLoader().getResource("intsection.json").getPath();
            reader = new BufferedReader(new FileReader(path));
            Gson gson = new GsonBuilder().create();
            constant.cints = Arrays.asList(gson.fromJson(reader, intersection[].class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * 读取O-D 需求矩阵
     * @Title: loadODDataByPath
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return void   返回类型
     * @throws: FileNotFoundException e
     */
    public static double[][] loadODDataByPath() throws FileNotFoundException {
        int row = 24, col = 24;
        double[][] od = CellHandle.newArray(row, col);
        try {
            File file = new File(fileutils.class.getClassLoader().getResource("sf_tab_3.txt").getPath());
            BufferedReader bfdr = new BufferedReader(new FileReader(file));
            String lStr = null;
            Gson gson = new Gson();
            int i = 0;
            while((lStr = bfdr.readLine()) != null) { /*读取行数据*/
                String[] str = lStr.split(" ");
                for (int j = 0; j < col; j++) {
                    od[i][j] = Double.valueOf(str[j]);
                }
                i+= 1;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return od;
    }

}
