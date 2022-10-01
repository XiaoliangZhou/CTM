package transport.ctm.main;

import com.google.gson.internal.LinkedHashTreeMap;
import transport.ctm.model.*;
import transport.ctm.util.constant;
import transport.graph.Edge;
import transport.graph.IniteGraph;
import transport.graph.Ramp;
import transport.graph.Vertex;
import org.apache.commons.lang3.StringUtils;
import transport.math.util.MathSupplier;

import java.util.*;

/**
 * @author liangxiao.zhou
 * @version V1.0
 * @Title: initCtm.java
 * @Package transport.ctm.main
 * @Description: 路段元胞、链接初始化
 * @date Jan 14, 2019 16:11:14 PM
 */
public class initCtm {
    /*元胞链接*/
    private static List<ctmLinks> clks = constant.clks;
    private static List<Edge> les = constant.ctmls;
    private static List<ctmLinks> clkts = constant.clkts;

    /**
     * 初始化元胞、链接、路段 待完善...
     *
     * @param vstr  文本行数据
     * @param vlist 节点集合
     * @param els   节点对应的边
     * @param w     权重
     * @return void 返回类型
     * @throws
     * @Title: build_ctm_initiaze
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static void initiazeCtm(String[] vstr,
                                   List<Vertex> vlist,
                                   List<String> els,
                                   double w,
                                   List ctms) throws Exception {
        /*路段id*/
        String l_id = vstr[2];
        /*车道数*/
        int roadNum = Integer.parseInt(vstr[4]);
        /*路段下游对应交叉口类型*/
        String l_t = vstr[7];
        /*当前元胞起始链接*/
        int index = ctms.size();
        /*步长*/
        int indexStep = Integer.parseInt(vstr[3]);
        /*初始化路段元胞*/
        constant.ctms = initCtm.ctmAddCells(vstr, constant.CL_CAP, l_id, index, ctms);
        /*初始化元胞链接*/
        constant.clks = initCtm.ctmAddLinks(ctms,index, indexStep, l_t,l_id);
        /*初始化路段*/
        Edge e = initCtm.ctmAddLane(vstr, index, vlist, constant.CL_CAP, w, roadNum, ctms, l_t,l_id, indexStep);
        /*节点对应路段集合*/
        els.add(e.getLabel());
        /*路段集合*/
        constant.ctmls.add(e);
    }

    /**
     * 初始化路段元胞
     *
     * @param vstr   TXT 行数据
     * @param cl_cap 单个元胞的最大承载交通量(veh)
     * @return List   返回类型
     * @throws
     * @Title: ctmAddCells
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static List<ctmCell> ctmAddCells(String[] vstr, int cl_cap, String lId, int index, List ctms) {
        /*车道元胞个数*/
        double c_lgth = 70;
        double c_egth = 80;
        int ns = ctms.size();
        int cn = Integer.parseInt(vstr[3]);
        String direct = vstr[7];

        if(!direct.equals("-1")){
            /*设置输入元胞*/
            setCell(vstr, ctms, cl_cap,c_lgth,lId, index, 1, new ctmCell());
            /*设置普通元胞*/
            setOrdaryCell(vstr, ctms,cl_cap,c_lgth,lId, cn, index);
            /*设置渠化子元胞*/
            setCanalSubCell(vstr, ctms, lId,c_egth, cn, index, vstr);
        }else{
            /*设置普通元胞*/
            setOrginCell(vstr, ctms,cl_cap,c_lgth,lId, cn, index);

        }
        return ctms;

    }

    /**
     * 渠化子元胞
     * 注意：单车道最多可渠化为2个子元胞
     * @param cn
     * @param n_cell
     */
    private static void setCanalSubCell(String[] vstr, List ctms,
                                        String l_id,
                                        double c_egth,
                                        int cn,
                                        int n_cell,
                                        String[] str) {
        double c_rate,c_cap;
        int index = 0, ctype = 3;
        /*这里采用停止线均衡分配*/
        double a = 0.5,b = 0.2 ,c = 0.3;

        String l_t = vstr[6];/*路段下游对应交叉口类型*/
        int l_n =  Integer.parseInt(vstr[4]);/*车道数*/
        double s_l_c = Double.valueOf(vstr[5]) ;/*单车道通行能力*/

        c_rate = MathSupplier.mul(l_n, s_l_c);
        double cl_cap = 16;
        c_cap = cl_cap * l_n;

        String drs = vstr[7];
        /*只有一个车流去向 渠化为一个子元胞*/
        setCanalCell(ctms, l_id, c_rate, c_cap, ctms.size(), ctype, drs,c_egth);

    }
    /**
     * 添加元胞r
     *
     * @param cn
     * @param n_cell
     */
    private static void setOrginCell(String[] vstr, List ctms, int cl_cap,double c_lgth, String l_id, int cn, int n_cell) {
        int startIndex = n_cell ;
        int flag = 0;
        ctmCell c;
        if (flag == 0) {
            /*路段起始元胞*/
            c = new orginCell();
            double c_rate = MathSupplier.mul(Double.valueOf(vstr[4]), Double.valueOf(vstr[5]));;
            int c_cap = 10000;
            createCell(l_id, c_rate, c_cap, startIndex, 2, c,c_lgth);
            ctms.add(c);
            flag++;
        }
    }
    /**
     * 添加元胞r
     *
     * @param cn
     * @param n_cell
     */
    private static void setOrdaryCell(String[] vstr, List ctms, int cl_cap,double c_lgth, String l_id, int cn, int n_cell) {
        int startIndex = n_cell + 1;
        int toIndex = n_cell + cn - 1;
        int flag = 0;
        while (startIndex <= toIndex) {
            ctmCell c;
            if (flag == 0) {
                /*路段起始元胞*/
                c = new orginCell();
                setCell(vstr, ctms, cl_cap, c_lgth,l_id, startIndex, 2, c);
                flag++;
            }else {
                /*路段中间元胞*/
                c = new ctmCell();
                setCell(vstr, ctms, cl_cap,c_lgth,l_id, startIndex, 0, c);
            }
            startIndex++;
        }
    }
    /**
     * 获取渠化子元胞转向{straight||left||rigth}
     *
     * @return
     */
    private static String setUpEndCellDirect(String[] str) {
        /*路段下游对应交叉口类型*/
        String intType = str[7];
        String drs = null;
        if (!StringUtils.isEmpty(intType)) {
            switch (intType) {
                case "2":case "3":
                    drs = str[8];
                    break;
                case "4":
                    drs = "1&0&2";
                    break;
            }
        }
        return drs;
    }

    /**
     * @param l_id
     * @param c_rate
     * @param c_cap
     * @param cid
     * @param ctype
     */
    private static void setCanalCell(List ctms,
                                     String l_id,
                                     double c_rate,
                                     double c_cap,
                                     int cid, int ctype, String dirCode,double c_egth) {
        endCell c = new endCell();
        createCell(l_id, c_rate/2, c_cap/2, cid, ctype, c,c_egth);
        c.setC_dir(dirCode);
        ctms.add(c);

    }

    /**
     * @param l_id
     * @param cid
     * @param ctype
     */
    private static void setSecCell(String[] vstr,List ctms,int cl_cap,double c_lgth,String l_id, int cid, int ctype) {
        int c_cap = 0;
        double c_rate = 0.0;
        String cdir = vstr[vstr.length - 1];

        secCell c = new secCell();
        c_rate = MathSupplier.mul(Double.valueOf(vstr[4]), Double.valueOf(vstr[5]));
        c_cap = cl_cap * Integer.parseInt(vstr[4]);

        createCell(l_id, c_rate, c_cap, cid, ctype, c,c_lgth);
        c.setCdir(cdir.split("&"));
        ctms.add(c);

    }

    /**
     * @param l_id
     * @param cid
     * @param ctype
     * @param c
     */
    private static void setCell(String[] vstr, List ctms, int cl_cap,double c_lgth,String l_id, int cid, int ctype, ctmCell c) {
        int c_cap = 0;
        double c_rate = 0.0;
        if (ctype == 1) {
            /*输入元胞*/
            c_rate = 0.0;
            c_cap = Double.MAX_EXPONENT;
        }
        if (ctype == 0 || ctype == 2) {
            /*普通元胞*/
            //c_rate = MathSupplier.mul(Double.valueOf(vstr[4]), Double.valueOf(vstr[5]));
            c_rate = MathSupplier.mul(Double.valueOf(vstr[4]), Double.valueOf(vstr[5]));
            c_cap = cl_cap * Integer.parseInt(vstr[4]);
        }
        createCell(l_id, c_rate, c_cap, cid, ctype, c,c_lgth);
        ctms.add(c);
    }


    /**
     * 根据元胞类型添加元胞
     *
     * @param l_id
     * @param cid
     * @param ctype
     */
 /*   @Deprecated
    private static void addCell(String[] vstr, List ctms, String l_id, int cid, int ctype) {
        ctmCell c = null;
        if (ctype == 2) {
            c = new orginCell();
            setCell(vstr, ctms, l_id, cid, ctype, c);
        }
        if (ctype == 0 || ctype == 1) {
            c = new ctmCell();
            setCell(vstr, ctms, l_id, cid, ctype, c);
        }
    }*/


    /**
     * @param l_id
     * @param c_rate
     * @param c_cap
     * @param cid
     * @param ctype
     * @param c
     */
    private static void createCell(String l_id, double c_rate, double c_cap, int cid, int ctype, ctmCell c,double c_lgth) {
        c.setC_id(cid);
        c.setLaneId(l_id);
        c.setC_type(ctype);
        c.setC_rate(MathSupplier.div(Math.round(c_rate),10));
        c.setC_cap(c_cap);
        c.setC_n(0);
        c.setC_lgth(c_lgth);
        c.setPos_in(0);
        c.setPos_in(0);
        c.setPos_in(0.0);
        c.setPos_out(0.0);
        c.setReal_in(0.0);
        c.setReal_out(0.0);
    }

    /**
     * 初始化路段 待完善...
     *
     * @param vstr   txt 行数据
     * @param n_cell 路段元胞数
     * @param vlist  节点
     * @param cl_cap 单个元胞的最大承载交通量(veh)
     * @param w      权重
     * @return Edge   返回类型
     * @throws
     * @Title: ctmAddLane
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static Edge ctmAddLane(String[] vstr,
                                  int n_cell,
                                  List<Vertex> vlist,
                                  int cl_cap,
                                  double w,
                                  int roadNum,
                                  List ctms,
                                  String l_t,String l_id, int cn) {
        Edge e;
        String rs = vstr[1];
        int c_num = Integer.parseInt(vstr[3]);
        /*车道数*/
        int l_n =  Integer.parseInt(vstr[4]);
        /*子节点*/
        Vertex v = IniteGraph.getVertexByName(vlist, rs);
        /*饱和流率*/
        double satRate = Math.round(MathSupplier.mul(Double.valueOf(vstr[4]), Double.valueOf(vstr[5])));
        /*路段最大承载能力*/
        int cap = ParseData(vstr[3]) * cl_cap * ParseData(vstr[4]);
        /*渠化子元胞Map*/
        Map<String, Integer> dcMap = new HashMap<>();

        e = new Edge();
        Ramp ramp = new Ramp(vstr[2]);

        if(!l_t.equals("-1")){
            e.setLabel(vstr[2]);
            e.setIntType(l_t);
            e.setRoadNum(roadNum);
            e.setSourceVertex(vstr[0]);
            e.setEndVertex(v);
            e.setWeight(w);
            e.setTmpWeight(w);
            e.setCap(cap);
            e.setSatRate(satRate);
            e.setTmpRate(satRate);
            e.setCellNum(c_num);
            e.setInCell(n_cell);
            e.setoCell(n_cell + 1);
            e.setdCell(n_cell + cn);
            e.setDcMap(dcMap);
            e.setDirect(vstr[6]);
            e.setRamp(ramp);
            e.setFreeTime(w);
            e.setTravleTime(w);
            e.setTempAvg(0.0);
        }else{
            e.setLabel(vstr[2]);
            e.setIntType(l_t);
            e.setRoadNum(roadNum);
            e.setSourceVertex(vstr[0]);
            e.setEndVertex(v);
            e.setWeight(w);
            e.setTmpWeight(w);
            e.setCap(cap);
            e.setSatRate(satRate);
            e.setTmpRate(satRate);
            e.setCellNum(c_num);
            e.setInCell(-1);
            e.setoCell(n_cell);
            e.setdCell(-1);
            e.setDcMap(dcMap);
            e.setDirect(vstr[6]);
            e.setRamp(ramp);
            e.setFreeTime(w);
            e.setTravleTime(w);
            e.setTempAvg(0.0);
        }

        //返回edge
        return e;
    }

    /**
     * 创建渠化子元胞Map
     * @param n_cell
     * @param ctms
     * @param l_t
     * @param cn
     * @param l_n
     * @return
     */
    protected static Map<String, Integer> createEndCellMap(int n_cell, List ctms, String l_t,String l_id, int cn, int l_n) {
        Map<String, Integer> dcMap = new LinkedHashTreeMap<>();
        int dc_num = n_cell + cn;
        int index = dc_num;

        if (l_t.equals("2")) dcMap.put("-1",index);
        if (l_t.equals("3")) {
            l_n = (l_n>=2) ? 2 : l_n;
            while (index < dc_num + l_n) {
                endCell edc = (endCell) ctms.get(index);
                dcMap.put(edc.getC_dir(),index);
                ++index;
            }
        }
        if (l_t.equals("4")) {
            if(l_id.equals("25")||l_id.equals("32")||l_id.equals("43")||l_id.equals("48")){
                l_n = 2;
                while (index < dc_num + l_n) {
                    endCell edc = (endCell) ctms.get(index);
                    dcMap.put(edc.getC_dir(),index);
                    ++index;
                }
            }else{
                l_n = (l_n>=3) ? 3 : l_n;
                while (index < dc_num + l_n) {
                    endCell edc = (endCell) ctms.get(index);
                    dcMap.put(edc.getC_dir(),index);
                    ++index;
                }
            }

        }
        return dcMap;
    }

    /**
     *
     * @param str
     * @return
     */
    public static final int ParseData(String str) {
        if (!StringUtils.isEmpty(str)) {
            return Integer.parseInt(str);
        }
        return 0;
    }

    /**
     * 初始化元胞链接 待完善.....
     *
     * @param n_cell 元胞个数
     * @return List 元胞链接
     * @throws
     * @Title: ctmAddLinks
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static List<ctmLinks> ctmAddLinks(List ctms,int n_cell, int cn, String l_t,String l_id) {
        int toIndex = cn + n_cell;
        int index = n_cell;

        int secIndex = toIndex - 1;
        ctmCell stc = initCtm.getctmCell(ctms,secIndex);
        /*出口道*/
        if(!l_t.equals("-1")){
            /*进口道*/
            ctmLinks clk;
            while (index < toIndex) {
                clk = new ctmLinks();
                int[] arr = new int[]{};
                arr = new int[]{index, index + 1};
                /*普通元胞链接*/
                if (index == n_cell) {
                    clk.setType(3);
                    clk.setCells(arr);
                    clk.setKp(new double[]{0.0, 0.0});
                    clk.setL_id(l_id);
                    clk.setAccess(1);
                    clkts.add(clk);
                } else {
                    clk.setType(0);
                    clk.setCells(arr);
                    clk.setKp(new double[]{0.0, 0.0});
                    clk.setL_id(l_id);
                    clk.setAccess(1);
                    clks.add(clk);
                }
                index++;
            }
        }
        return clks;
    }

    /**
     * 通过尾元胞id获取edge object
     *
     * @param cid
     * @return
     */
    public static Edge findEdgeByLastCellId(int cid) {
        for (Edge edge : constant.ctmls) {
            if (edge != null && cid != 0) {
                if (edge.getdCell() == cid) {
                    return edge;
                }
            }
        }
        return null;
    }

    /**
     * 通过路段标识获取路段对象
     *
     * @return Edge    返回类型
     * @throws
     * @Title: findEdgByName
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static Edge findEdgByName(List<Edge> ctmls, String l_label) {
        if (!ctmls.isEmpty()) {
            if (!StringUtils.isEmpty(l_label)) {
                for (Edge e : ctmls) {
                    if (e.getLabel().equals(l_label)) {
                        return e;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 通过相位链接标识获取相位链接
     *
     * @return ctmLinks    返回类型
     * @throws
     * @Title: getCtmLink
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static ctmLinks getCtmLink(int lkId) {
        for (int i = 0; i < clks.size(); i++) {
            if (i == lkId) {
                return clks.get(i);
            }
        }
        return null;
    }

    /**
     * 判断两个数组是否相等(有一个元素相同则返回 true)
     *
     * @return boolean    返回类型
     * @throws
     * @Title: isEquals
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static boolean isEquals(int[] atrs, int[] arrs) {
        if (atrs == arrs) {
            return true;
        }
        if (atrs == null || arrs == null) {
            return false;
        }
        if (atrs.length != arrs.length) {
            return false;
        }
        for (int i = 0; i < atrs.length; i++) {
            if (atrs[i] == arrs[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两个数组是否相等(元素全部不相同)
     *
     * @return boolean    返回类型
     * @throws
     * @Title: isEquals
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static boolean isNoutEquals(int[] a, int[] a2) {
        if (a == a2)
            return false;
        if (a == null || a2 == null)
            return false;

        int length = a.length;
        if (a.length == a2.length) {
            int i = 0;
            while (i < length) {
                if (a[i] != a2[i]) {
                    ++i;
                } else {
                    return false;
                }
            }
            if (i == length) {
                return true;
            }
        }
        return false;
    }


    public static Edge getEdgeByEndCell(endCell edc) {
        String l_id;
        if (edc != null) {
            l_id = edc.getLaneId();
            for (Edge edge : les) {
                if (edge != null) {
                    if (edge.getLabel().equals(l_id)) {
                        return edge;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 通过元胞标识获取普通元胞对象
     *
     * @return Edge    返回类型
     * @throws
     * @Title: findEdgByName
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static ctmCell getctmCell(List ctms, int cid) {
        if (!ctms.isEmpty()) {
            for (Object ctc : ctms) {
                ctmCell c = (ctmCell) ctc;
                if (c.c_id == cid) {
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * 获取输入元胞元胞
     *
     * @param cid
     * @return
     */
    public static ctmCell getInCell(List ctms, int cid) {
        if (!ctms.isEmpty()) {
            return (ctmCell) ctms.get(cid);
        }
        return null;
    }

    /**
     * 获取输入元胞元胞
     *
     * @param cid
     * @return
     */
    public static ctmCell getInnerCell(List ctms, int cid) {
        if (!ctms.isEmpty()) {
            return (innerCell) ctms.get(cid);
        }
        return null;
    }
    /**
     * 获取(j-1)元胞
     *
     * @param cid
     * @return
     */
    public static secCell getSecCell(List ctms, int cid) {
        if (!ctms.isEmpty()) {
            return (secCell) ctms.get(cid);
        }
        return null;
    }
    /**
     * 获取尾元胞
     *
     * @param cid
     * @return
     */
    public static endCell getEndCell(List ctms, int cid) {
        if (!ctms.isEmpty()) {
            return (endCell) ctms.get(cid);
        }
        return null;
    }

    /**
     * 获取尾元胞
     *
     * @param cid
     * @return
     */
    public static orginCell getOrginCell(List ctms, int cid) {
        if (!ctms.isEmpty()) {
            return (orginCell) ctms.get(cid);
        }
        return null;
    }

    /**
     * /**
     * 通过节点标识获取节点对象
     *
     * @return intersection    返回类型
     * @throws
     * @Title: queryIntsectionByAlias
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static intersection findIntByLabel(List<intersection> inects, String node_label) {
        if (!inects.isEmpty()) {
            if (!StringUtils.isEmpty(node_label)) {
                for (intersection inect : inects) {
                    if (inect.getNlabel().equals(node_label)) {
                        return inect;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return void    返回类型
     * @throws
     * @Title: getLinkLength
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static int getLinkLength() {
        return constant.clks.size();
    }

    /**
     * 通过key 删除value
     *
     * @return void    返回类型
     * @throws
     * @Title: build_ctm_24_int
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static void delMapValueByKey(Map<String, List<String[]>> map, String key) {
        if (map.containsKey(key)) {
            if (map.get(key) != null) {
                map.remove(key);
            }
        }
    }

    /**
     * 判断两个数组是否如果相同，则返回第一个数组元素下标
     *
     * @param attr 主相位
     * @param arrs 右转相位
     * @return int    返回类型
     * @throws
     * @Title: searchIndex
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static int searchIndex(int[] attr, int[] arrs) {
        boolean isSame;
        isSame = initCtm.isEquals(attr, arrs);
        if (isSame) {
            for (int i = 0; i < attr.length; i++) {
                if (attr[i] == arrs[i]) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
      /*  List l = new ArrayList();
        inCell inc = new inCell();
        orginCell ogc = new orginCell();
        ogc.setC_n(2.0);
        ctmCell ctc = new ctmCell();
        endCell edc = new endCell();
        edc.c_n = 40;
        l.add(inc);
        l.add(ogc);
        l.add(ctc);
        l.add(edc);
        ctmCell c1 = (ctmCell) l.get(0);
        ctmCell c2 = (ctmCell) l.get(1);
        ctmCell c3 = (ctmCell) l.get(3);
        System.out.println(c1);
        System.out.println(c2.getC_n());*/
       /*inCell inc0 = (inCell) l.get(0);
        System.out.println(inc0);
        orginCell ogc0 = (orginCell) l.get(1);
        System.out.println(ogc0);
        endCell edc0 = (endCell) l.get(3);
        System.out.println(edc0.getC_n());
        System.out.println(edc0);*/

        System.out.println((int)(Math.round(MathSupplier.mul(3,0.5))));

    }
}
