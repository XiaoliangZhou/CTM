
package transport.ctm.main;

import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.StringUtils;
import transport.ctm.model.*;
import transport.ctm.util.constant;
import transport.ctm.util.strutil;
import transport.graph.Edge;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * @author liangxiao.zhou
 * @version V2.0
 * @Title: buildCtm.java
 * @Package transport.ctm.main
 * @Description: 设置相位
 * @date Jan 14, 2019 16:11:14 PM
 */
public class buildCtm {

    private static int a, b, c;
    private static int k_type;
    private static List<intersection> cints = constant.cints; /*节点信息*/
    private static List<ctmLinks> clk = constant.clks;
    private static List<ctmLinks> kts = constant.clkts;

    /**
     * 交叉口相位 待完善....
     * @Title: buidCtmIntersection
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return void    返回类型
     * @throws
     */
    public static void setIntPhase(List ctms) {
        try {
            if (!cints.isEmpty()) {
                for (intersection ints : cints) {
                    List<int[]> rs  = null;
                    initCtm.delMapValueByKey(ints.phaseMap,"0");
                    ints.phaseMap.forEach((key, list) -> {
                        if(!list.isEmpty()){
                            /*相位设置*/
                            setTOrTTPhase2(rs,ctms,
                                    getCellIdByLaneLabel(list),ints,Integer.parseInt(key));
                        }
                    });
                    ints.phaseMap.clear();
                }
            }
        } catch (Exception e) {e.printStackTrace();}
        /*初始化相位*/
        initiazePhase();
        for (int i = 0; i < kts.size(); i++) {
            clk.add(kts.get(i));
        }
        kts.clear();
    }

    /**
     * 初始化相位
     * 相位链接
     */
    public static void initiazePhase() {
        constant.clks.forEach((clk)->{
            if(clk.getInt_label()!=null){
                if(clk.getCurPhase()!=-1){
                    clk.setCurPhase(0);
                }
            }
        });
        for (int i = 0; i < constant.cints.size(); i++) {
            ctmSetPhase(i, 1);
        }
    }
    /**
     * 是否转换相位
     * @param index
     * @return
     */
    public static boolean PhaseFilter(int index){
        int[] p = new int[]{1,13,25,27};
        return !ArrayUtils.contains(p,index+1);
    }
    /**
     * 交叉口相位 待完善...
     * @Title: setRightPhase
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param rs 右转相位
     * @param l 主相位
     * @param ints 节点
     * @param phikey 相位序列
     * @return void  返回类型
     * @throws
     */
    public static void setTOrTTPhase2(List<int[]> rs,List ctms,List<int[]> l, intersection ints, int phikey){
        int startIndex = initCtm.getLinkLength();
        /*创建元胞链接*/
        setctmCellAndLink2(rs,l,ctms,ints,phikey);
        int toIndex = initCtm.getLinkLength()-1 ;
        LinkedHashMap<Integer,int[]> phases = ints.getPhases();
        phases.put(phikey,new int[]{startIndex,toIndex});
        ints.setPhases(phases);

    }
    /**
     * 交叉口相位 待完善...
     * @Title: setRightPhase
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param rs 右转相位
     * @param l 主相位
     * @param ints 节点
     * @param phikey 相位序列
     * @return void  返回类型
     * @throws
     */
    public static void setTOrTTPhase(List<int[]> rs,List ctms,List<int[]> l, intersection ints, int phikey){
        int intStyle = Integer.parseInt(ints.getIntStyle());
        int startIndex = initCtm.getLinkLength();
        List lss = new LinkedList();
        if(!rs.isEmpty()){
            if(intStyle == 2){
                setDeformityPhase(rs,l,ctms,ints); /*处理 1、13、25、27 节点*/
            }
        }
        /*创建元胞链接*/
        setctmCellAndLink(rs,l,ctms,ints,phikey);
        int toIndex = initCtm.getLinkLength()-1 ;
        LinkedHashMap<Integer,int[]> phases = ints.getPhases();
        phases.put(phikey,new int[]{startIndex,toIndex});
        ints.setPhases(phases);

    }
    /**
     * 交叉口相位，包括主相位、右转相位
     * @Title: setRightPhase
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param olist 右转相位
     * @param attr  主相位
     * @param ints  节点
     * @return void    返回类型
     * @throws
     */
    private static void setctmCellAndLink(List<int[]> olist, List<int[]> attr,List ctms,intersection ints, int phikey) {
        int intStyle = Integer.parseInt(ints.getIntStyle());
        if (!olist.isEmpty()&&!attr.isEmpty()) {
            /*十字型交叉口*/
            if(intStyle == 4){
                setTTPhase(olist,attr,ctms,ints); /*十字形交叉口相位处理*/
            }
        }
    }
    /**
     * 交叉口相位，包括主相位、右转相位
     * @Title: setRightPhase
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param olist 右转相位
     * @param attr  主相位
     * @param ints  节点
     * @return void    返回类型
     * @throws
     */
    private static void setctmCellAndLink2(List<int[]> olist, List<int[]> attr,List ctms,intersection ints, int phikey) {
        int intStyle = Integer.parseInt(ints.getIntStyle());
        if (!attr.isEmpty()) {
            /*十字型交叉口*/
            if(intStyle == 4){
                setTTPhase(olist,attr,ctms,ints); /*十字形交叉口相位处理*/
            }

        }
    }

    /**
     * 十字型交叉口
     * @Title: setTOtherPhase
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param attr 右转相位
     * @param attr 主相位
     * @param ints 节点
     * @return void    返回类型
     * @throws
     */
    private static void setTTPhase(List<int[]> olist, List<int[]> attr,List ctms, intersection ints){
        /*合流*/
        for (int[] atr : attr) {
            int[] cf  = new int[]{atr[0], atr[1]};
            endCell edc = initCtm.getEndCell(ctms,atr[0]);
            Edge e = initCtm.findEdgByName(constant.ctmls,edc.getLaneId());
            String[] dir = e.getLabel().split("_");
            setOrdaryCtmLink(1, cf,ints,dir[1]);
        }
    }
    /**
     * 处理 1、13、25、27 节点元胞链接
     * @param olist
     */
    private static void setDeformityPhase(List<int[]> rs,List<int[]> olist,List ctms,intersection ints){
        /*右转链接*/
        rs.forEach((arr)->{
            setSpecialCtmLink(2,arr,ctms,ints);
        });
        /*左转链接*/
        olist.forEach((all)->{
            setSpecialCtmLink(2,all,ctms,ints);
        });
    }

    /**
     * 添加元胞链接
     * @Title: setOrdaryCtmLink
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param kType 链接类型
     * @param lCells 元胞
     * @return int[]    返回类型
     * @throws
     */
    private static void setOrdaryCtmLink(int kType, int[] lCells,intersection ints,String ster){
        List cd = constant.ctms;
        ctmLinks clk = addCtmLink(kType, lCells, ints);
        clk.setAccess(0);
        clk.setL_id("-1");
        clk.setSteer(ster);
        constant.clks.add(clk);
    }

    /**
     *
     * @param kType
     * @param lCells
     * @param ints
     * @return
     */
    private static ctmLinks addCtmLink(int kType, int[] lCells, intersection ints) {
        ctmLinks clk = new ctmLinks();
        int n_link = constant.clks.size();

        initCtm.findEdgByName(constant.ctmls,clk.getL_id());
        clk.setInt_label(ints.nlabel);
        clk.setIntType(Integer.parseInt(ints.intStyle));
        clk.setType(kType);
        clk.setCells(lCells);
        clk.setKp(new double[]{0.0, 0.0});
        clk.setCurPhase(ints.phase);
        return clk;
    }

    /**
     * 添加元胞链接
     * @Title: setOrdaryCtmLink
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param kType 链接类型
     * @param lCells 元胞
     * @return int[]    返回类型
     * @throws
     */
    private static void setSpecialCtmLink(int kType, int[] lCells,List ctms,intersection ints){
        String steer[] ;
        steer = getSpecialRoadSteer(ctms,lCells);
        ctmLinks clk = addCtmLink(kType, lCells, ints);
        clk.setAccess(1);
        clk.setSter(steer);
        constant.clks.add(clk);
    }

    /**
     * 特殊交叉口转向
     * @param lCells
     */
    private static String[] getSpecialRoadSteer(List ctms,int[] lCells) {
        String edgeId;
        String entryEdgeId;
        String[] ster = new String[lCells.length];
        String steer[] = new String[1] ;
        int startIndex = 0;
        while(startIndex<lCells.length){
            edgeId = initCtm.getctmCell(ctms,lCells[startIndex]).getLaneId();/*出口路段id*/
            Edge edge =  initCtm.findEdgByName(constant.ctmls,edgeId);
            if(edge!=null){
                String direct = edge.getDirect();/*路段方向*/
                ster[startIndex] = direct;
                startIndex++;
            }
        }
        steer[0] = stmSim.getVehSteer(ster[0],ster[1]);
        return steer;
    }

    /**
     * 获取元胞链接
     * @Title: getLinkCells
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return int[]    返回类型
     * @throws
     */
    private static int[] getLinkCells(int[] lcells,int v1,int v2,int index){
        strutil sl = new strutil();
        return sl.insert(sl.insert(lcells,v1),v2,index);
    }
    /**
     * 元胞编号校验
     * @Title: ctm_check_cell
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return void  返回类型
     * @throws
     */
    private static boolean checkCell(int[] attrs) {
        if (attrs != null && attrs.length != 0) {
            for (int i = 0; i < attrs.length; i++) {
                if (attrs[i] < 0 || attrs[i] > constant.ctms.size() - 1) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * 获取cell object
     * @Title: getCellIdByLaneLabel
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return ctmCell  返回类型
     * @throws
     */
    @Nullable
    private static List<int[]> getCellIdByLaneLabel(List<String[]> list) {
        if (!list.isEmpty()) {
            List olist = new ArrayList();
            list.forEach((arrs) -> {
                if (arrs != null && arrs.length > 0) {
                    int[] attrs = swCellByLaneArray(arrs);
                    olist.add(attrs);
                }
            });
            return olist;
        }
        return null;
    }

    /**
     *
     * @param lar
     * @return
     */
    private static int[] swCellByLaneArray(String[] lar){
        int lrth = lar.length;
        boolean checkNull = lar != null && lrth > 0 ;
        if (checkNull) {
            Edge  e1;
            Edge  e2;
            int index = 0;
            int[] c = new int[lrth];
            String[] drs = new String[lrth];
            List<Edge> olist = new ArrayList<>();

            e1 = initCtm.findEdgByName(constant.ctmls, lar[0]);
            e2 = initCtm.findEdgByName(constant.ctmls, lar[1]);

            c = new int[]{ e1.getdCell(), e2.getoCell()};
            return c;
        }
        return null;
    }

    /**
     * 字符串转化成Int
     * @param str
     * @return
     */
    private static int parseInt(String str){
        if(StringUtils.isNotEmpty(str)){
              return Integer.parseInt(str);
        }
        return -1;
    }

    /**
     * 获取转向 (S,0)/(L,1)/(R,2)
     * @param drs
     * @return
     */
    private static int getDirect(String[] lars,String[] drs){
        /*(W,0)/(E,1)/(N,2)/(S,3)*/
        if(drs[0].equals("0") & drs[1].equals("0"))
            return 0;
        if(drs[0].equals("0") & drs[1].equals("2"))
            return 2;
        if(drs[0].equals("0") & drs[1].equals("3"))
            return 1;
        if(drs[0].equals("1") & drs[1].equals("1"))
            return 0;
        if(drs[0].equals("1") & drs[1].equals("2"))
            return 1;
        if(drs[0].equals("1") & drs[1].equals("3"))
            return 2;
        if(drs[0].equals("2") & drs[1].equals("2"))
            return 0;
        if(drs[0].equals("2") & drs[1].equals("1"))
            return 2;
        if(drs[0].equals("2") & drs[1].equals("0"))
            return 1;
        if(drs[0].equals("3") & drs[1].equals("3"))
            return 0;
        if(drs[0].equals("3") & drs[1].equals("1"))
            return 1;
        if(drs[0].equals("3") & drs[1].equals("0"))
            return 2;
        return -1;
    }
    /**
     * 初始化节点相位相位 待完善....
     * @Title: ctm_set_phase
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param: index  节点索引
     * @param: phi 初始相位
     * @return void    返回类型
     * @throws
     */
    private static void ctmSetPhase(int index, int phi) {
        if (index > constant.cints.size()) {
            System.err.println("Wrong index of intersection");
        }
        if (phi < 1 || phi > constant.cints.get(index).phases.size()) {
            System.err.println("Wrong phase !");
        }
        constant.cints.get(index).phase = phi;
        //初始化第一相位
        setUpFirstPhase(index, phi);
    }
    /**
     * 转换相位
     * @Title: ctm_switch_int
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param : index  节点索引
     * @return void    返回类型
     * @throws
     */
    public static void ctmSwitchInt(int index,int step) {
        List<intersection> intes = constant.cints;
        List<IntegerSolution> chrs = constant.chrs;

        if (index < 0 || index > intes.size()) {
            System.err.println("error !");
        }
        /*获取交叉口类型*/
        int phi = intes.get(index).getPhase();
        String nlabel = intes.get(index).getNlabel();
        String intStyle = intes.get(index).getIntStyle();
        if(step%4==0){
            switchPhase(index,intes);/*转换相位*/
        }
   /*     if(intStyle.equals("4")){
            *//*十字型交叉口*//*
            if(nlabel.equals("10")){
                //double[] gr =  chrs.get(0).getX();
                List<Integer> gr =  new ArrayList<>();
                gr.add(6);
                gr.add(4);
                gr.add(4);
                gr.add(3);

                if(phi>1){
                    int itart = getPhaseStep(gr,phi);
                    if((step-itart)%gr.get(phi-1)==0){
                        switchPhase(index,intes);*//*转换相位*//*
                    }
                }else{
                    if(step%gr.get(phi-1)==0){
                        System.out.println(gr);
                        switchPhase(index,intes);*//*转换相位*//*
                    }
                }
            }else{
                if(step%4==0){
                    switchPhase(index,intes);*//*转换相位*//*
                }
            }

        }*/
    }
    /**
     * 转换相位2
     * @Title: ctm_switch_int
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param : index  节点索引
     * @return void    返回类型
     * @throws
     */
    public static void ctmSwitchInt(int index, int step, IntegerSolution chr) {
        List<intersection> intes = constant.cints;
        if (index < 0 || index > intes.size()) {
            System.err.println("error !");
        }
        /*获取交叉口类型*/
        int phi = intes.get(index).getPhase();
        String nlabel = intes.get(index).getNlabel();
        String intStyle = intes.get(index).getIntStyle();
        if(intStyle.equals("4")){
            /*十字型交叉口*/
          /*  if(step%4==0){
                switchPhase(index,intes);*//*转换相位*//*
            }*/
            List<Integer> gr =  chr.getX();
            //double[] gr = new double[]{2,5,5,2};
            if(phi>1){
                int sigma = getPhaseStep(gr,phi);
                int delta = step - sigma;
                if(delta%gr.get(phi-1)==0){
                    switchPhase(index,intes);/*转换相位*/
                }
            }else {
                if (step % gr.get(phi - 1) == 0) {
                    //System.out.println(Arrays.toString(gr));
                    switchPhase(index, intes);/*转换相位*/
                }
            }
        }
    }

    /**
     *
     * @param gr
     * @param phi
     * @return
     */
    public static int getPhaseStep(List<Integer> gr,int phi){
        int sStep = 0;
        if(StringUtils.isNotEmpty(gr.toString())&phi>1){
            for (int i = 0; i < phi-1; i++) {
                sStep+=gr.get(i);
            }
        }
        return sStep;
    }

    /**
     *
     * @param index
     * @param intes
     */
    protected static void switchPhase(int index, List<intersection> intes) {
        int phi = intes.get(index).phase;/*上一相位*/
        // update before phase
        updateBeforePhase(index, phi);
        // update before phase
        phi = phi + 1;
        if (phi > intes.get(index).phases.size()) {
            phi = 1;
        }
        // update next phase
        updateNextPhase(index, phi);
    }

    /**
     * 更新上一相位
     * @Title: updateBeforePhase
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param : index  节点索引
     * @param : phi  上一相位编号
     * @return void    返回类型
     * @throws
     */
    private static void updateBeforePhase(int index, int phi) {
        int[] arr = constant.cints.get(index).getPhases().get(phi);
        int startIndex = arr[0];
        int toIndex = arr[arr.length -1];
        while(startIndex<=toIndex){
            constant.clks.get(startIndex).setAccess(0);
            constant.clks.get(startIndex).setCurPhase(0);
            ++startIndex;
        }
    }
    /**
     * 更新下一相位
     * @Title: updateNextPhase
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param : index  节点索引
     * @param : phi  当前相位编号
     * @return void    返回类型
     * @throws
     */
    private static void updateNextPhase(int index,int phi) {
        int[] arr = constant.cints.get(index).getPhases().get(phi);
        int startIndex = arr[0];
        int toIndex = arr[arr.length - 1];
        while(startIndex<=toIndex){
            ctmLinks clk = constant.clks.get(startIndex);
            clk.setAccess(1);
            clk.setCurPhase(phi); /*当前相位*/
            setLinkSteer(phi, clk);/*链接转向*/
            ++startIndex;
        }
        constant.cints.get(index).phase = phi;
    }
    /**
     * 更新下一相位
     * @Title: updateNextPhase
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param : index  节点索引
     * @param : phi  当前相位编号
     * @return void    返回类型
     * @throws
     */
    private static void setUpFirstPhase(int index,int phi) {
        LinkedHashMap<Integer,int[]> phases = constant.cints.get(index).getPhases();
        for (Map.Entry<Integer, int[]> entry : phases.entrySet()) {
            int key = entry.getKey();
            int[] arr = entry.getValue();
            if(key==phi){
               int startIndex = arr[0];
               int toIndex = arr[arr.length - 1];
               while(startIndex<=toIndex){
                   ctmLinks clk = constant.clks.get(startIndex);
                   clk.setAccess(1);
                   clk.setCurPhase(phi); /*当前相位*/
                   setLinkSteer(phi, clk);/*链接转向*/
                   ++startIndex;
               }
               cints.get(index).phase = phi;
           }
           if(key!=phi){
               int startIndex = arr[0];
               int toIndex = arr[arr.length - 1];
               while(startIndex<=toIndex){
                   ctmLinks clk = constant.clks.get(startIndex);
                   clk.setAccess(0);
                   ++startIndex;
               }
           }
        }
    }

    /**
     * @链接转向
     * @param phi
     * @param clk
     */
    private static void setLinkSteer(int phi, ctmLinks clk) {
        boolean fag = clk.getIntType()==3&&phi==1||(clk.getIntType()==4&&(phi==1||phi==3));
        if(clk.getType()==2){
            int length = clk.getCells().length;
            if(length==2) clk.setSter(new String[]{"2"});
            if(length==3){
                if(clk.getIntType()==3&&phi==2) clk.setSter(new String[]{"0","1"});
                if(fag){
                    clk.setSter(new String[]{"0","2"});
                }
                boolean fag1 =clk.getIntType()==3&&phi==3||clk.getIntType()==4&&(phi==2||phi==4);
                if(fag1){
                    clk.setSter(new String[]{"1","2"});
                }
            }
        }
        /*合流*/
        if(clk.getType()==1){
            if(clk.getIntType()==3&&phi==2) clk.setSter(new String[]{"1","2"});
            if(fag){
                clk.setSter(new String[]{"0","2"});
            }
            boolean fag1 =clk.getIntType()==4&&(phi==2||phi==4);
            if(fag1){
                clk.setSter(new String[]{"1","2"});
            }
        }
    }

    /**
     * 检查路段元胞
     * @Title: check_ctmCells
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return boolean    返回类型
     * @throws
     */
    private static boolean checkCtmCells() {
        boolean is_valild = true;
        ctmCell c = (ctmCell)constant.ctms.get(0);
        if (constant.ctms.size() <= 0 || constant.ctms.size() > c.c_cap) {
            is_valild = false;
        }
        return is_valild;
    }
    /**
     * 检查相位
     * @Title: ctm_check_phases
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return boolean    返回类型
     * @throws
     */
    private static boolean ctmCheckPhases() {
        boolean isValild = true;
        for (int i = 0; i < constant.cints.size(); i++) {
            if (constant.cints.get(i).phase < 1 ||
                    constant.cints.get(i).phase > constant.cints.get(i).phases.size()) {
                isValild = false;
                break;
            }
        }
        return isValild;
    }
    /**
     * 右转相位
     * @Title: getRightPhase
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param olist 右转相位
     * @param attr 相位
     * @return list  返回类型
     * @throws
     */
    private static List getRightPhase(List<int[]> olist, List<int[]> attr) {
        if(!attr.isEmpty()){
            List<int[]> ol = new ArrayList<int[]>();
            olist.forEach((arrs) -> {
                boolean flag = initCtm.isNoutEquals(attr.get(0), arrs);
                if (flag) {
                    ol.add(arrs);
                }
            });
            int i = 0;
            while (i < ol.size()) {
                if(attr.size() == 1){
                    return ol;
                }
                boolean fs = initCtm.isEquals(attr.get(1), ol.get(i));
                if (fs) {
                    ol.remove(ol.get(i));
                    continue;
                }
                ++i;
            }
            return ol;
        }
        return null;
    }

    private static void main(String[] args) {
//        fileutils.loadJson(cints);
//        build_ctm_24int(cints);
        //check_ctmCells();
       /* intersection ints = new intersection();
        List<int[]> olist = new ArrayList<>();
        olist.add(new int[]{259, 212});
        olist.add(new int[]{518, 181});
        olist.add(new int[]{170, 176});
        olist.add(new int[]{163, 188});


        List<int[]> attr = new ArrayList<>();
        attr.add(new int[]{259, 176});
        attr.add(new int[]{170, 212});

        List ls = new ArrayList<>();

        LinkedHashMap<Integer,int[]> maps = new LinkedHashMap<>();
        maps.put(1,new int[]{1,2});
        maps.put(2,new int[]{3,4});
        maps.put(3,new int[]{4,5});
        maps.put(4,new int[]{6,7});
        ls.add(maps);

        ls.forEach((mapss)->{
            maps.forEach((key, arr)->{
                System.out.println(key +"_____________"+ArrayUtils.toString(arr));
            });

        });
*/
        int[] p = new int[]{1,2,7,13};
        System.out.println(Arrays.asList(p).contains(1));




    /*    attr.add(new int[]{259, 176});
        attr.add(new int[]{170, 188});*/

        //setctmCellAndLink(olist,attr,ints);
        //List  ol = new ArrayList();
        // List h = getRightPhase(olist,attr);
        //System.out.println(h);
        //System.out.println(ArrayUtils.indexOf(olist.get(1),attr.get(0)[1]));
        /*System.out.println(ArrayUtils.contains(olist.get(0),attr.get(0)[0]));*/
        //System.out.println(Arrays.equals(new int[]{518,181},new int[]{51,181}));


        //set4IntdPhase(olist,attr,ints,4);
        //System.out.println(searchIndex(attr.get(0),olist.get(1)));

        //set4IntPhase(olist,attr,ints,2);
    }

}
