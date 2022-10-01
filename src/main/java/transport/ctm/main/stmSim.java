package transport.ctm.main;

import CGA.Model.Chrome;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import transport.ctm.model.*;
import transport.ctm.util.constant;
import transport.file.util.fileutils;
import transport.graph.*;
import transport.math.util.AvgUtil;
import transport.math.util.CellHandle;
import transport.math.util.MathSupplier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

import static transport.ctm.main.buildCtm.PhaseFilter;
import static transport.ctm.main.buildCtm.ctmSwitchInt;
import static transport.ctm.main.initCtm.findEdgByName;

/**
 * @author liangxiao.zhou
 * @version V3.0
 * @Title: stmSim.java
 * @Package transport.ctm.main
 * @Description: 车流传播主方法
 * @date Jan 18, 2019 16:11:14 PM
 */
public class stmSim {

    private static int sdtp = 0;
    static double[][] roadFlows = CellHandle.newArray(201, 77);
    static double[][] roadTime = CellHandle.newArray(201, 77);
    static List<intersection> ints = constant.cints;

    static List<Garage> lg = constant.lg;
    static List<Edge> les = constant.ctmls;
    /**
     * @param step
     * @param lg
     */
    public static void roadInfoReady(int step, List ctms, List<Garage> lg) {
        List<Edge> les = constant.ctmls;
        int count=0;
        int c=0;

        double SumDelay = 0;
        String intlabel = "10";
        intersection intsect = getIntByName(intlabel);

        for (Edge le : les) {
            /*路段入口监听器*/
            //raodEntryVehiclListener(step, le, ctms);
            /*计算路段平均旅行时间*/
            c += AvgUtil.getRoadAvg(step, le,count,intsect);
        }
        for (Edge le : les) {
            /*计算交叉延误*/
            SumDelay += getSingelDelay(step, ctms, le, intsect);
            getQueueLength(step, ctms, le, intsect);
            getMaxCapacity( ctms, le, intsect);
        }
        double ssDelay = intsect.getSumDelay();
        ssDelay += SumDelay;
        intsect.setSumDelay(ssDelay);
        //System.out.println("step = "+step+",   "+"SumDelay = "+SumDelay);
        // System.out.println();

    }

    /**
     *
     */
    public static void calBaseDelay() {
        List<Edge> les = constant.ctmls;

        String intlabel = "10";
        intersection intsect = getIntByName(intlabel);

        for (Edge le : les) {
            if(!le.getIntType().equals("-1")){
                /*计算进口道初始流量*/
                AvgUtil.getBaseFlow(le,intsect);
            }
        }

       /* double bsDelay = intsect.getBaseDelay();
        bsDelay += SumDelay;
        intsect.setBaseDelay(bsDelay);*/
        //System.out.println("step = "+step+",   "+"SumDelay = "+SumDelay);



    }
    /**
     * @ 获取交叉口对象
     * @param label
     * @return
     */
    public static intersection getIntByName(String label){
        if(!label.isEmpty()){
            for (intersection intc : ints) {
                if (intc.getNlabel().equals(label)) {
                    return intc;
                }
            }
        }
        return null;
    }
    /**
     * @param ctms
     * @param ctms
     * @param cid
     * @return
     */
    public static int getVehicleSize(List ctms, int cid) {
        return ((ctmCell) ctms.get(cid)).getC1().size();
    }

    /**
     * 相位转换 T型交叉口3相位 ，十字形交叉口 4相位
     * @param step
     */
    public static void switchPhase(int step) {
        for (int j = 0; j < constant.cints.size(); j++) {
                if (PhaseFilter(j)) {
                    ctmSwitchInt(j,step);
                }
          }
    }

    /**
     * 相位转换 T型交叉口3相位 ，十字形交叉口 4相位
     * @param step
     */
    public static void switchPhase(int step, IntegerSolution chr) {
        for (int j = 0; j < constant.cints.size(); j++) {
            ctmSwitchInt(j,step,chr);
        }
    }

    /**
     * 记录车辆进入路段时间
     *
     * @param step    仿真时步
     * @param curEdge 当前路段
     */
    protected static void raodEntryVehiclListener(int step, Edge curEdge, List ctms) {
        int orgCellId = curEdge.getoCell();
        orginCell ogc = initCtm.getOrginCell(ctms, orgCellId);
        /*记录车辆进入路段的时间*/
        Deque<Vehicle> dq = ogc.getC1();
        if (dq.size() > 0) {
            dq.forEach((v) -> {
                v.setExtanceTime(step);
            });
        }
    }

    /**
     * @param step
     * @param lg
     * @param cdg
     */
    protected static void roadExitVehicleListener1(int step, List ctms, List<Garage> lg, Edge cdg) {
        List<ctmLinks> ced = constant.clks;
        endCell edc;
        String intType;
        String endVertex;
        intersection toInts;
        endVertex = cdg.getEndVertex().getLabel();/*当前节点的子节点*/
         /*路段出口对应交叉口*/
        toInts = initCtm.findIntByLabel(ints, endVertex);
        /*获取车辆转向并计算分流比*/
        getVehicleSterAndSpiltRatio(ctms, cdg, toInts);

    }

    /**
     * 计算交叉口排队长度
     * @param ctms
     * @param e
     * @param intsect
     * @return
     */
    public static void getQueueLength(int step,List ctms , Edge e, intersection intsect){
        /*优化信号交叉口*/
        String vertex = "10";
        String endVertex = e.getEndVertex().getLabel();

        if(endVertex.equals(vertex)){
            /*该交叉口进口道*/
            int dt=5;
            int sec;
            ctmCell ctc;


            int startIndex, toIndex;
            toIndex = e.getdCell();
            startIndex = e.getoCell();

            double queueLength = intsect.getQueueLength();

            double c=0;
            while (toIndex>=startIndex) {
                ctc = (ctmCell) ctms.get(toIndex);
                int out = (int)mul(ctc.real_out,dt);
                if(out==0){
                    queueLength+=ctc.getC_n();
                }
                --toIndex;
            }
            intsect.setQueueLength(queueLength);
        }

    }

    /**
     * 计算交叉口排队长度
     * @param ctms
     * @param e
     * @param intsect
     * @return
     */
    public static void getMaxCapacity(List ctms , Edge e, intersection intsect){
        /*优化信号交叉口*/
        String vertex = "10";
        String startVertex = e.getSourceVertex();

        if(startVertex.equals(vertex)){
            /*该交叉口进口道*/
            int dt=5;
            ctmCell ctc;

            int index = e.getoCell();
            ctc = (ctmCell) ctms.get(index);

            double capacity = intsect.getCapcity();
            capacity+=ctc.getC_n();
            intsect.setCapcity(capacity);

        }

    }

    /**
     * 计算交叉口车辆平均延误
     * @param ctms
     * @param e
     * @param intsect
     * @return
     */
    public static double getSingelDelay(int step,List ctms , Edge e, intersection intsect){
        /*优化信号交叉口*/
        String vertex = "10";
        double cDelay=0,sDelay=0,eDelay=0;
        String endVertex = e.getEndVertex().getLabel();

        if(endVertex.equals(vertex)){
            /*该交叉口进口道*/
            int dt=5;
            int sec;
            ctmCell ctc;

            int startIndex, toIndex;

            toIndex = e.getdCell();
            startIndex = e.getoCell();


            while (startIndex <=toIndex) {
                ctc = (ctmCell) ctms.get(startIndex);
                //System.out.println(ctc.getLaneId()+"-----"+ctc.getC_id()+"-----"+c);
                double delay = sub(ctc.c_n,ctc.real_in);
                cDelay += delay;
                ++startIndex;
            }
        }

        return   cDelay;

    }

    /**
     * 重写add
     * @param v1
     * @param v2
     * @return
     */
    private static double add(double v1,double v2){
        return MathSupplier.add(v1,v2);
    }
    /**
     * 获取车辆转向并计算分流比
     *
     * @param ctms
     * @param cdg
     */
    private static void getVehicleSterAndSpiltRatio(List ctms, Edge cdg,intersection toInts) {
        int sec;
        Canal cal ;
        secCell stc;
        String intType;

        /*路段下游对应交叉口*/
        intType = cdg.getIntType();
        sec = Math.addExact(cdg.getInCell(), cdg.getCellNum()) - 1;
        stc = initCtm.getSecCell(ctms, sec);
        cal = stc.getCanal();

        /*以节点为终点的车流到达尾元胞，将在下一时刻全部流出，这部分车辆不占用尾元胞空间*/
        updateReachedVehicleFlow(ctms, cdg);
        /*计算合流比例*/
        getMerScale(ctms,toInts);
        /*车辆排队并进入相应的排队区域，计算分流系数*/
        vehicleQueueAndGetDivScale(stc,ctms, intType,cdg);
    }

    /**
     * 车辆排队，计算分流比
     * @param stc
     * @param intType
     */
    private static void vehicleQueueAndGetDivScale(secCell stc, List ctms,String intType ,Edge cdg) {
        int Type = parseInt(intType);
        Canal cal;
        ArrayDeque<Vehicle> deque = stc.getC1();/*车队*/
        cal = stc.getCanal();
        /*动态路径选择*/
         //subOfVehSterAndDivScaleV2(ctms,cdg,cal, stc, deque,intType); /*车辆转向并计算分流比子方法*/
        //subOfVehSterAndDivScaleV5(ctms,cdg,cal, stc, deque,intType); /*车辆转向并计算分流比子方法*/
        //subOfVehSterAndDivScaleV6(ctms,cdg,cal, stc, deque,intType); /*车辆转向并计算分流比子方法*/
        /*静态路径选择*/
        subOfVehSterAndDivScale1(cal, stc, deque,intType);

        /*T型交叉口分流比例*/
        if (Type==3) {
            updateTOfScale(stc, cal);
        }
        /*十字型交叉口*/
        if (Type==4) {
            updateTTOfScale(stc, cal);
        }
        updatSecCellFlow(stc,cal);
    }

    /**
     * 处理终点车辆
     * @param ctms
     * @param cdg
     */
    private static void updateReachedVehicleFlow(List ctms, Edge cdg) {
        int N0 = 0;
        Edge e;
        Vehicle v;
        endCell edc;
        ArrayDeque<Vehicle> reque;
        for (Map.Entry<String, Integer> entry : cdg.getDcMap().entrySet()) {
            edc = initCtm.getEndCell(ctms, entry.getValue());
            reque = cdg.getRamp().getRque();/*用来存放终点车辆队列*/

            Iterator<Vehicle> dqv = edc.getC1().iterator();
            while (dqv.hasNext()) {
                v = dqv.next();
                boolean fag = v.getSteer().equals("3");
                if (fag) {
                    v.setReach(true);
                    reque.offer(v);
                    N0++;
                }
                if(!fag){
                    edc.getC2().offer(v);
                }
                dqv.remove();/*出队*/
            }
            /**/
            if(N0>0){
                edc.setC_n(edc.getC_n() - N0);
                N0=0;
            }
        }
    }

    /**
     * 车辆转向并计算分流比子方法
     *
     * @param cal
     * @param stc
     * @param deque
     */
    private static void subOfVehSterAndDivScaleV2(List ctms, Edge cdg,Canal cal, secCell stc, ArrayDeque<Vehicle> deque,String intType) {
        /*获取车辆转向*/
        String[] labl = new String[2];
        String ster = "";
        DefaultWeightLabelEdge nextOldRoad;
        DefaultWeightLabelEdge nextNewRoad;
        DefaultGraphPath newPath;
        LinkedList<DefaultWeightLabelEdge> ldws ;
        Iterator<Vehicle> it = deque.iterator();

        String endVertex;
        intersection toInts;
        endVertex = cdg.getEndVertex().getLabel();/*当前节点的子节点*/
        /*路段出口对应交叉口*/
        toInts = initCtm.findIntByLabel(ints, endVertex);
        LinkedList<DefaultWeightLabelEdge> listr ;
        LinkedList<DefaultWeightLabelEdge> listn ;
        double p = 1.0;

        List<Vehicle> lv = new ArrayList<>();
        int cnt=0;
        while (it.hasNext()) {
            int curIndex=0;
            Vehicle v = it.next();
            String curRoadLabel = v.getCurPosLane();
            DefaultGraphPath dgp = v.getDefaultGraphPath();
            LinkedList<DefaultWeightLabelEdge> list1 = new LinkedList<>() ;
            listr = new LinkedList<>();

            listr = dgp.getPaths();/*车辆原始路径*/

            int ds = sdtp;
            int nextIndex = 0;
            int nextNewIndex = 0;
            int size = listr.size();
            nextIndex = getNextIndex(curRoadLabel, listr, nextIndex);
            for (int i = nextIndex; i < listr.size(); i++) {
                list1.add(listr.get(i));
            }
            if (nextIndex < size) {
                nextOldRoad = listr.get(nextIndex);
                if(nextOldRoad!=null){
                    double cgt = getCurStepLaneCongestTime(nextOldRoad.getFreeTime());
                    if(nextOldRoad.getWeight()>cgt){

                        /*原先选择的路径出现拥堵，重新调整*/
                        newPath = IniteGraph.algOfSingleSourcePath(toInts.getNlabel(),dgp.getEndVertex());    /*重新搜索最短路径*/
                        listn = newPath.getPaths();
                        /*重新计算转向*/
                        nextNewRoad = listn.get(0);
                        /*调整路径*/
                        labl = new String[]{curRoadLabel, nextNewRoad.getLabel()};
                        ster = getCarSteer(labl);
                        boolean fag = isEquals(newPath.getPaths(),list1);
                        if(!fag){
                            cnt++;
                            //System.out.println(list1 +"----"+newPath.getPaths());
                            //System.out.println(cnt);
                            if(ster!=null){
                                for (int i = listr.size()-1; i >= nextIndex; i--) {
                                    listr.remove(i);
                                }
                                for (DefaultWeightLabelEdge deg : listn) {
                                    listr.add(deg);
                                }
                            }else{
                                labl = new String[]{curRoadLabel, nextOldRoad.getLabel()};
                                ster = getCarSteer(labl);
                            }
                        }
                        v.setSteer(ster); /*获取转向*/
                    }
                    if(nextOldRoad.getWeight()<=cgt){
                        labl = new String[]{curRoadLabel, nextOldRoad.getLabel()};
                        ster = getCarSteer(labl);
                        v.setSteer(ster); /*获取转向*/
                    }
                }
            }
            if (nextIndex >= size) {
                ster = "3";
                v.setSteer("3");
            }
            /*计算分流比例*/
            setCanalScale(cal, ster);
            /*车辆入队*/
            offerQueue(stc, deque, v, ster);

        }

    }


    /**
     * 车辆转向并计算分流比子方法
     *
     * @param cal
     * @param stc
     * @param deque
     */
    private static void subOfVehSterAndDivScaleV6(List ctms, Edge cdg,Canal cal, secCell stc, ArrayDeque<Vehicle> deque,String intType) {
        /*获取车辆转向*/
        String[] labl = new String[2];
        String ster = "";
        DefaultWeightLabelEdge nextOldRoad;
        DefaultWeightLabelEdge nextNewRoad;
        DefaultWeightLabelEdge replaceRoad;
        DefaultGraphPath newPath;
        DefaultGraphPath replacePath;
        LinkedList<DefaultWeightLabelEdge> ldws ;
        Iterator<Vehicle> it = deque.iterator();

        String endVertex;
        intersection toInts;
        endVertex = cdg.getEndVertex().getLabel();/*当前节点的子节点*/
        /*路段出口对应交叉口*/
        toInts = initCtm.findIntByLabel(ints, endVertex);
        LinkedList<DefaultWeightLabelEdge> listr ;
        LinkedList<DefaultWeightLabelEdge> listn ;
        LinkedList<DefaultWeightLabelEdge> listrpc ;
        double p = 1.0;

        List<Vehicle> lv = new ArrayList<>();
        int cnt=0;
        while (it.hasNext()) {
            int curIndex=0;
            Vehicle v = it.next();
            String curRoadLabel = v.getCurPosLane();
            DefaultGraphPath dgp = v.getDefaultGraphPath();
            LinkedList<DefaultWeightLabelEdge> list1 = new LinkedList<>() ;
            listr = new LinkedList<>();

            listr = dgp.getPaths();/*车辆原始路径*/

            int nextIndex = 0;
            int size = listr.size();
            nextIndex = getNextIndex(curRoadLabel, listr, nextIndex);
            for (int i = nextIndex; i < listr.size(); i++) {
                list1.add(listr.get(i));/*车辆原始路径*/
            }
            if (nextIndex < size) {
                nextOldRoad = listr.get(nextIndex);
                if(curRoadLabel.equals("73")&&nextOldRoad.getLabel().equals("76")){
                    System.out.println("1"+listr.toString());

                }
                if(nextOldRoad!=null){
                    double cgt = getCurStepLaneCongestTime(nextOldRoad.getFreeTime());
                    //Vertex vertex = IniteGraph.getVertexByName(constant.vertexs,toInts.getNlabel());
                    if(nextOldRoad.getWeight()>cgt){
                        /*重新调整*/
                        Vertex vertex = IniteGraph.getVertexByName(constant.vertexs,toInts.getNlabel());
                        List<String> els = vertex.getEdgList();

                        Edge  curLane = findEdgByName(les,curRoadLabel);

                        newPath = IniteGraph.algOfSingleSourcePath(toInts.getNlabel(),dgp.getEndVertex());    /*重新搜索最短路径*/
                        listn = newPath.getPaths();

                        Edge  oldEdge = findEdgByName(les,nextOldRoad.getLabel());
                        ctmCell oc = initCtm.getInCell(ctms,oldEdge.getoCell());
                        double noc = oc.getC_n();

                        /*重新计算转向*/
                        nextNewRoad = listn.get(0);

                        Edge  newEdge = findEdgByName(les,nextNewRoad.getLabel());
                        ctmCell nc = initCtm.getInCell(ctms,newEdge.getoCell());
                        double nnc = nc.getC_n();
                        double w =0;
                        double nw = newPath.getWeight();

                        for (String l : els) {
                            if(!l.equals(nextNewRoad.getLabel())){
                                Edge  replaceEdge = findEdgByName(les,l);
                                if(!curLane.getSourceVertex().equals(replaceEdge.getEndVertex().label)){

                                    ctmCell rpc = initCtm.getInCell(ctms,replaceEdge.getoCell());
                                    double rpinc = rpc.getC_n();

                                    replacePath = IniteGraph.algOfSingleSourcePath(replaceEdge.getEndVertex().getLabel(),dgp.getEndVertex()); /*重新搜索替代最短路径*/
                                    replaceRoad = constant.graph
                                            .getEdge(replaceEdge.getSourceVertex(),replaceEdge.getEndVertex().getLabel());

                                    replacePath.getPaths().add(0,replaceRoad);
                                    listrpc = replacePath.getPaths();

                                    double rweight = replacePath.getWeight() + replaceEdge.getWeight();
                                    /* if(replaceEdge.getLabel().equals("3")){
                                        System.out.println(replacePath.getPaths()+"------"+rweight+"===="+nw +"======"+replaceEdge.getLabel()+"==="+rpinc +" ==="+ nnc );
                                    }*/

                                    if(rweight>nw){
                                        if(rpinc<nnc){
                                            /*调整路径*/
                                            System.out.println(listrpc);
                                            labl = new String[]{curRoadLabel, replaceRoad.getLabel()};
                                            ster = getCarSteer(labl);
                                            boolean fag = isEquals(replacePath.getPaths(),list1);
                                            if(!fag){
                                                if(ster!=null){
                                                    for (int i = listr.size()-1; i >= nextIndex; i--) {
                                                        listr.remove(i);
                                                    }
                                                    for (DefaultWeightLabelEdge deg :  listrpc) {
                                                        listr.add(deg);
                                                    }
                                                }else{
                                                    labl = new String[]{curRoadLabel, nextOldRoad.getLabel()};
                                                    ster = getCarSteer(labl);
                                                }
                                                System.out.println("listr ="+ listr.toString());
                                            }
                                            v.setSteer(ster); /*获取转向*/
                                        }else{
                                            /*调整路径*/
                                            labl = new String[]{curRoadLabel, nextNewRoad.getLabel()};
                                            ster = getCarSteer(labl);
                                            boolean fag = isEquals(newPath.getPaths(),list1);
                                            if(!fag){
                                                if(ster!=null){
                                                    for (int i = listr.size()-1; i >= nextIndex; i--) {
                                                        listr.remove(i);
                                                    }
                                                    for (DefaultWeightLabelEdge deg : listn) {
                                                        listr.add(deg);
                                                    }
                                                }else{
                                                    labl = new String[]{curRoadLabel, nextOldRoad.getLabel()};
                                                    ster = getCarSteer(labl);
                                                }
                                            }
                                            v.setSteer(ster); /*获取转向*/
                                        }
                                    }
                                }

                            }else{
                                /*调整路径*/
                                labl = new String[]{curRoadLabel, nextNewRoad.getLabel()};
                                ster = getCarSteer(labl);
                                boolean fag = isEquals(newPath.getPaths(),list1);
                                if(!fag){
                                    cnt++;
                                    //System.out.println(list1 +"----"+newPath.getPaths());
                                    //System.out.println(cnt);
                                    if(ster!=null){
                                        for (int i = listr.size()-1; i >= nextIndex; i--) {
                                            listr.remove(i);
                                        }
                                        for (DefaultWeightLabelEdge deg : listn) {
                                            listr.add(deg);
                                        }
                                    }else{
                                        labl = new String[]{curRoadLabel, nextOldRoad.getLabel()};
                                        ster = getCarSteer(labl);
                                    }
                                }
                                if(ster==null){
                                    System.out.println(curRoadLabel+"===="+nextNewRoad.getLabel()+"==="+nextOldRoad.getLabel());
                                    System.out.println("ster 为空");
                                }
                                v.setSteer(ster); /*获取转向*/
                            }

                        }
                    }
                    if(nextOldRoad.getWeight()<=cgt){
                        labl = new String[]{curRoadLabel, nextOldRoad.getLabel()};
                        ster = getCarSteer(labl);
                        v.setSteer(ster); /*获取转向*/
                    }
                }
            }
            if (nextIndex >= size) {
                ster = "3";
                v.setSteer("3");
            }
            /*计算分流比例*/
            setCanalScale(cal, ster);
            /*车辆入队*/
            offerQueue(stc, deque, v, ster);

        }

    }
    /**
     * 车辆转向并计算分流比子方法
     *
     * @param cal
     * @param stc
     * @param deque
     */
    private static void subOfVehSterAndDivScaleV5(List ctms, Edge cdg,Canal cal, secCell stc, ArrayDeque<Vehicle> deque,String intType) {
        /*获取车辆转向*/
        String[] labl = new String[2];
        String ster = "";
        DefaultWeightLabelEdge nextOldRoad;
        DefaultWeightLabelEdge nextNewRoad;
        DefaultGraphPath newPath;
        LinkedList<DefaultWeightLabelEdge> ldws ;
        Iterator<Vehicle> it = deque.iterator();

        String endVertex;
        intersection toInts;
        endVertex = cdg.getEndVertex().getLabel();/*当前节点的子节点*/
        /*路段出口对应交叉口*/
        toInts = initCtm.findIntByLabel(ints, endVertex);
        LinkedList<DefaultWeightLabelEdge> listr ;
        LinkedList<DefaultWeightLabelEdge> listn ;
        double p = 1.0;

        List<Vehicle> lv = new ArrayList<>();
        int cnt=0;
        while (it.hasNext()) {
            int curIndex=0;
            Vehicle v = it.next();
            String curRoadLabel = v.getCurPosLane();
            DefaultGraphPath dgp = v.getDefaultGraphPath();
            LinkedList<DefaultWeightLabelEdge> list1 = new LinkedList<>() ;
            listr = new LinkedList<>();

            listr = dgp.getPaths();/*车辆原始路径*/

            int ds = sdtp;
            int nextIndex = 0;
            int nextNewIndex = 0;
            int size = listr.size();
            nextIndex = getNextIndex(curRoadLabel, listr, nextIndex);
            for (int i = nextIndex; i < listr.size(); i++) {
                list1.add(listr.get(i));
            }
            if (nextIndex < size) {
                nextOldRoad = listr.get(nextIndex);
                if(nextOldRoad!=null){
                    double cgt = getCurStepLaneCongestTime(nextOldRoad.getFreeTime());
                    if(nextOldRoad.getWeight()>cgt){
                        /*重新调整*/
                        newPath = IniteGraph.algOfSingleSourcePath(toInts.getNlabel(),dgp.getEndVertex());    /*重新搜索最短路径*/
                        listn = newPath.getPaths();

                        Edge  oldEdge = findEdgByName(les,nextOldRoad.getLabel());
                        ctmCell oc = initCtm.getInCell(ctms,oldEdge.getoCell());
                        double noc = oc.getC_n();

                        /*重新计算转向*/
                        nextNewRoad = listn.get(0);

                        Edge  newEdge = findEdgByName(les,nextNewRoad.getLabel());
                        ctmCell nc = initCtm.getInCell(ctms,newEdge.getoCell());
                        double nnc = nc.getC_n();

                        //System.out.println(dgp.getPaths());
                        //System.out.println(nnc+"-----"+noc+"-----"+nextOldRoad+"-----"+nextNewRoad);
                        //System.out.println(nnc+"-----"+noc+"-----"+dgp.getPaths()+"-----"+newPath.getPaths());
                        if(nnc<noc){
                            /*调整路径*/
                            labl = new String[]{curRoadLabel, nextNewRoad.getLabel()};
                            ster = getCarSteer(labl);
                            boolean fag = isEquals(newPath.getPaths(),list1);
                            if(!fag){
                                cnt++;
                                //System.out.println(list1 +"----"+newPath.getPaths());
                                //System.out.println(cnt);
                                if(ster!=null){
                                    for (int i = listr.size()-1; i >= nextIndex; i--) {
                                        listr.remove(i);
                                    }
                                    for (DefaultWeightLabelEdge deg : listn) {
                                        listr.add(deg);
                                    }
                                }else{
                                    labl = new String[]{curRoadLabel, nextOldRoad.getLabel()};
                                    ster = getCarSteer(labl);
                                }
                            }
                            v.setSteer(ster); /*获取转向*/
                        }else{
                            /*使用原始路径*/
                            labl = new String[]{curRoadLabel, nextOldRoad.getLabel()};
                            ster = getCarSteer(labl);
                            v.setSteer(ster); /*获取转向*/
                        }
                    }
                    if(nextOldRoad.getWeight()<=cgt){
                        labl = new String[]{curRoadLabel, nextOldRoad.getLabel()};
                        ster = getCarSteer(labl);
                        v.setSteer(ster); /*获取转向*/
                    }
                }
            }
            if (nextIndex >= size) {
                ster = "3";
                v.setSteer("3");
            }
            /*计算分流比例*/
            setCanalScale(cal, ster);
            /*车辆入队*/
            offerQueue(stc, deque, v, ster);

        }

    }
    /**
     * 车辆转向并计算分流比子方法
     *
     * @param cal
     * @param stc
     * @param deque
     */
    private static void subOfVehSterAndDivScale1(Canal cal, secCell stc, ArrayDeque<Vehicle> deque,String intType) {
        /*获取车辆转向*/
        String[] labl = new String[2];
        String ster = "";
        DefaultWeightLabelEdge dwles;
        Iterator<Vehicle> it = deque.iterator();
        while (it.hasNext()) {
            Vehicle v = it.next();
            String curRoadLabel = v.getCurPosLane();
            LinkedList<DefaultWeightLabelEdge> listr = v.getDefaultGraphPath().getPaths();/*车辆路径*/
            int nextIndex = 0;
            int size = listr.size();
            for (int i = 0; i < listr.size(); i++) {
                if (listr.get(i).getLabel().equals(curRoadLabel)) {
                    nextIndex = i + 1;
                }
            }
            if (nextIndex < size) {
                dwles = listr.get(nextIndex);
                labl = new String[]{curRoadLabel, dwles.getLabel()};
                ster = getCarSteer(labl);
                v.setSteer(ster); /*获取转向*/
            }
            if (nextIndex >= size) {
                ster = "3";
                v.setSteer("3");
            }
            /*计算分流比例*/
            setCanalScale(cal, ster);
            /*车辆入队*/
            offerQueue(stc, deque, v, ster);

        }
    }


    private static int getCurIndex(String curRoadLabel, LinkedList<DefaultWeightLabelEdge> listr) {
        int curIndex;
        for (int i = 0; i < listr.size(); i++) {
            if (listr.get(i).getLabel().equals(curRoadLabel)) {
                curIndex = i;
                return curIndex;
            }
        }
        return -1;
    }

    private static int getNextIndex(String curRoadLabel, LinkedList<DefaultWeightLabelEdge> listr, int nextIndex) {
        for (int i = 0; i < listr.size(); i++) {
            if (listr.get(i).getLabel().equals(curRoadLabel)) {
                nextIndex = i + 1;
            }
        }
        return nextIndex;
    }

    /**
     * 十字型交叉口
     *
     * @param stc
     * @param cal
     */
    private static void updateTTOfScale(secCell stc, Canal cal) {
        ctmLinks clk;
        int idx = 0;
        double s_n, l_n,r_n;
        double s,l,r,d;
        clk = initCtm.getCtmLink(stc.getLkid());
        int tn = cal.getT_n();
        l = cal.getL_n();
        r = cal.getR_n();
        s = cal.getS_n();
        d = cal.getD_n();
        if(tn>0){
            if((s+d)>0&l==0&r==0){
                clk.setKp(new double[]{0.0,1.0,0.0});
            }
            if((s+d)==0&l>0&r==0){
                clk.setKp(new double[]{1.0,0.0,0.0});
            }
            if((s+d)==0&l==0&r>0){
                clk.setKp(new double[]{0.0,0.0,1.0});
            }
            if(l==0&(s+d)>0&r>0){
                s_n = MathSupplier.div(s + d, tn);
                clk.setKp(new double[]{0.0, s_n,MathSupplier.sub(1.0,s_n)});
            }
            if(r==0&(s+d)>0&l>0){
                s_n = MathSupplier.div(s + d, tn);
                clk.setKp(new double[]{MathSupplier.sub(1.0,s_n), s_n,0.0});
            }
            if((s+d)==0&l>0&r>0){
                r_n = MathSupplier.div(r, tn);
                l_n = MathSupplier.div(l,tn);
                clk.setKp(new double[]{l_n,MathSupplier.sub(1.0,(r_n+l_n)),r_n});
            }
            if((s+d)>0&l>0&r>0){
                s_n = MathSupplier.div(s+d, tn);
                l_n = MathSupplier.div(l,tn);
                clk.setKp(new double[]{l_n,s_n,MathSupplier.sub(1.0,(s_n+l_n))});
            }
        }
        if(tn==0){
            clk.setKp(new double[]{0.0, 0.0,0.0});
        }
    }

    /**
     * T 型交叉口分流
     *
     * @param stc
     * @param cal
     */
    private static void updateTOfScale(secCell stc, Canal cal) {
        ctmLinks clk;
        int idx = 0;
        double[] ratios;
        double r_n = 0, s_n = 0, l_n = 0, var = 0;
        String[] dirs = stc.getCdir();
        String d1 = dirs[0], d2 = dirs[1];

        int tn = cal.getT_n();
        clk = initCtm.getCtmLink(stc.getLkid());
          if(d1.equals("0")){
              /*(S,L)(S,R)*/
              var = cal.getS_n() + cal.getD_n();
              if(tn>0){
                  s_n = MathSupplier.div(var, tn);
              }
              if(tn==0){
                  s_n=0.0;
              }
              ratios = new double[]{s_n, MathSupplier.sub(1,s_n)};
              clk.setKp(ratios);
        }
        if (d1.equals("1") & d2.equals("2")) {
            /*(L,R)*/
            var = cal.getR_n() + cal.getD_n();
            if(tn>0){
                r_n = MathSupplier.div(var,tn);
            }
            if(tn==0){
                r_n=0.0;
            }
            ratios = new double[]{MathSupplier.sub(1,r_n), r_n};
            clk.setKp(ratios);

        }
    }

    /**
     * 车辆排队
     * @param stc
     * @param deque
     * @param v
     */
    private static void offerQueue(secCell stc, ArrayDeque<Vehicle> deque, Vehicle v,String steer) {
        String[] str = stc.getCdir();
        int lgth = str.length;
        ArrayDeque<Vehicle> dq;
        Map<String, ArrayDeque<Vehicle>> queMap = stc.getQueMap();
        if (v != null) {
            /*处理终点车辆车流转向*/
            steer = setUpReachVehicleSter(v, steer, str, lgth);
            dq = queMap.get(steer) == null ? new ArrayDeque<>() : queMap.get(steer);
            dq.offer(v);
            queMap.put(steer, dq);/*进入相应队列*/
            v = deque.poll();/*出队*/
        }
    }

    /**
     * 处理终点车辆车流转向
     */
    private static String setUpReachVehicleSter(Vehicle v, String steer, String[] str, int lgth) {
        if(v.getSteer().equals("3")){
            if(lgth==1){
                boolean fag = str[0].equals("4");
                if(fag){
                    steer = "0";
                }
                if(!fag){
                    steer=str[0];
                }
            }
            if(lgth==2){
                boolean fag = str[0].equals("1")& str[1].equals("2");
                if(fag) {
                    /*终点车流进入右转车流排队区域*/
                    steer="2";
                }
                if(!fag) {
                    /*终点车流进入直行车流排队区域*/
                    steer="0";
                }
            }
        }
        return steer;
    }

    /**
     * 计算分流比
     */
    private static void setCanalScale(Canal cal, String ster) {
        if (ster.equals("0")) cal.s_n += 1;
        if (ster.equals("1")) cal.l_n += 1;
        if (ster.equals("2")) cal.r_n += 1;
        if (ster.equals("3")) cal.d_n += 1;
    }

    /**
     * 合流主方法
     * @param toInts 路段出口节点
     */
    protected static void getMerScale(List ctms, intersection toInts) {
        List<ctmLinks> kkd = constant.clks;
        ctmLinks clk;
        int curPhi = toInts.phase;
        if(curPhi!=-1){
            if(toInts.getPhases().get(curPhi)==null){
                System.out.println();
            }
            int[] atr = toInts.getPhases().get(curPhi);
            int tailIndex = atr[0];
            int toIndex = atr[atr.length - 1];
            while (tailIndex <= toIndex) {
                clk = initCtm.getCtmLink(tailIndex);
                if (clk.getType() == 1) getMerScale1( ctms, clk);/*合流*/
                ++tailIndex;
            }
        }
    }


    /**
     *
     * @param ctms
     * @param clk
     */
    protected static void getMerScale1(List ctms, ctmLinks clk) {
        endCell edc;
        int index = 0,lgth = 0;
        double a = 0, b = 0, c = 0;
        int[] lc = clk.getCells();
        lgth = lc.length;
        if(lgth==3){
            while (index<lgth -1){
                edc = initCtm.getEndCell(ctms,lc[index]);/*获取尾元胞*/
                if(index==0){
                    a = edc.getC_n();
                }
                c+= edc.getC_n();
                index++;
            }
        }
        if(c>0){
           b = MathSupplier.div(a,c);
        }
        clk.setKp(new double[]{b, MathSupplier.sub(1,b)});
    }


    /**
     * 获取当前路段拥挤时的行程时间
     * @param tf 路段自由流时间
     * @return
     */
    public static double getCurStepLaneCongestTime(double tf){
        double cgt = 0.0;
        double vf = 54.0;
        double cgv = 10.0;
        if (!Double.isNaN(tf)) {
            cgt = MathSupplier.div(MathSupplier.mul(vf, tf), cgv);
            return cgt;
        }
        return -1;
    }
    /**
     * 出口渠化
     *
     * @param ecl
     * @param steer 转向
     */
    private static void setCanalScale(Canal ecl, Vehicle car, String steer) {
        boolean isReach;
        isReach = car.isReach();
        if (!isReach) {
            if (steer.equals("S")) ecl.s_n += 1;
            if (steer.equals("L")) ecl.l_n += 1;
            if (steer.equals("R")) ecl.r_n += 1;
        }
        if (isReach) ecl.d_n += 1;

    }

    /**
     * 更新尾元胞车流量
     * @param stc
     */
    public static void updatSecCellFlow(secCell stc, Canal ecl) {
        int Num = getVehicleCount(stc);
        stc.setC_n(Num);
        ecl.setT_n(Num);
    }
    /**
     * 计算尾元胞车辆数
     *
     * @param c
     * @return
     */
    public static int getVehicleCount(secCell c) {
        int sum = 0;
        if (c.getQueMap() != null & c.getQueMap().size() != 0) {
            Iterator<Map.Entry<String, ArrayDeque<Vehicle>>> it = c.getQueMap().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = it.next();
                ArrayDeque dq = (ArrayDeque) entry.getValue();
                sum += dq.size();
            }
        }
        return sum;
    }


    /**
     * 车辆排队
     *
     * @param queMap
     * @param it
     * @param car
     * @param steer
     */
    public static void vehicleQeue(Map<String, ArrayDeque<Vehicle>> queMap, Iterator<Vehicle> it, Vehicle car, String steer) {
        ArrayDeque<Vehicle> deque;
        car.setSteer(steer);  /*车辆转向*/
        deque = queMap.get(steer) == null ? new ArrayDeque<>() : queMap.get(steer);
        deque.offer(car);
        queMap.put(steer, deque);
        it.remove();
    }

    /**
     * 车辆转向子方法
     * @param labl
     * @return
     */
    protected static String getCarSteer(String[] labl) {
        Edge e;
        Edge toEdge = null;
        Edge curEdge = null;
        String steer = "";
        String exitDriect;
        String entryDirect;
        int index = 0;
        while (index<labl.length){
            e = IniteGraph.getEdgeByLabel(labl[index]);
            if(index==0) curEdge = e;
            if(index==1) toEdge = e;
            index++;
        }
        exitDriect = curEdge.getDirect();
        entryDirect = toEdge.getDirect();
        steer = getVehSteer(exitDriect, entryDirect);
 /*       switch (curEdge.getLabel()) {
            case "56":
                switch (toEdge.getLabel()) {
                    case "60":
                        steer = null;
                        break;
                    case "61":
                        steer = getVehSteer("0", entryDirect);
                        break;
                    case "62":
                        steer = getVehSteer("0", "3");
                        break;
                    case "63":
                        steer = getVehSteer("0", entryDirect);
                        break;
                }
                break;
            case "59":
            case "68":
                switch (toEdge.getLabel()) {
                    case "60":
                        steer = getVehSteer(exitDriect, "1");
                        break;
                    case "62":
                        steer = getVehSteer(exitDriect, "3");
                        break;
                    default:
                        steer = getVehSteer(exitDriect, entryDirect);
                }
                break;
            case "64":
                switch (toEdge.getLabel()) {
                    case "60":
                        steer = getVehSteer("2", "1");
                        break;
                    case "61":
                        steer = getVehSteer("2", entryDirect);
                        break;
                    case "63":
                        steer = getVehSteer("2", entryDirect);
                        break;
                    default:
                        steer = getVehSteer(exitDriect, entryDirect);
                }
                break;
            default:
                steer = getVehSteer(exitDriect, entryDirect);
        }*/
        return steer;
    }
    /**
     * 车辆转向
     *
     * @param curLaneDirect
     * @param nextLaneDirect
     * @return
     */
    public static String getVehSteer(String curLaneDirect, String nextLaneDirect) {
        String steer = null;
        if (!StringUtils.isEmpty(curLaneDirect)) {
            if (!StringUtils.isEmpty(nextLaneDirect)) {
                switch (curLaneDirect) {
                    case "2":
                        switch (nextLaneDirect) {
                            case "2":
                                steer = "0";
                                break;
                            case "0":
                                steer = "1";
                                break;
                            case "1":
                                steer = "2";
                                break;
                        }
                        break;
                    case "0":
                        switch (nextLaneDirect) {
                            case "2":
                                steer = "2";
                                break;
                            case "0":
                                steer = "0";
                                break;
                            case "3":
                                steer = "1";
                                break;
                        }
                        break;
                    case "1":
                        switch (nextLaneDirect) {
                            case "2":
                                steer = "1";
                                break;
                            case "3":
                                steer = "2";
                                break;
                            case "1":
                                steer = "0";
                                break;
                        }
                        break;
                    case "3":
                        switch (nextLaneDirect) {
                            case "3":
                                steer = "0";
                                break;
                            case "0":
                                steer = "2";
                                break;
                            case "1":
                                steer = "1";
                                break;
                        }
                        break;
                }
            }
        }
        return steer;
    }
    private static int parseInt(String s){
        if(StringUtils.isNotEmpty(s)){
            return Integer.parseInt(s);
        }
        return -1;
    }

    /**
     * 过滤器
     *
     * @param ints
     * @param curPhi
     * @return
     */
    public static boolean phaseFilter(intersection ints, int curPhi) {
        String intType = ints.intStyle;
        return (intType.equals("3") && (curPhi == 1 || curPhi == 2)) || (intType.equals("4") && (curPhi == 3 || curPhi == 1));
    }

    /**
     * 节点车库
     *
     * @param lg
     * @param intId
     * @return
     */
    public static Garage getGarageByIntId(List<Garage> lg, String intId) {
        if (StringUtils.isNotBlank(intId)) {
            if (!lg.isEmpty()) {
                for (Garage garage : lg) {
                    if (garage.IntId.equals(intId)) {
                        return garage;
                    }
                }
            }
        }
        return null;
    }
    /**
     * 网络加载
     * @param step
     */
    public static void networkFlowLoading100(int step,Map<String,Double> loadMap,List ctms) throws  Exception {
        constant.ctmls.forEach((le)->{
            if(!le.getIntType().equals("-1")){
                Map<Integer,Double> inmaps = le.getInputMaps();
                double r = inmaps.get(step);
                /*获取输入元胞*/
                int inCell_index = le.getInCell();
                ctmCell inc = initCtm.getInCell(ctms,inCell_index);
                double cRate = inc.getC_rate();
                cRate += r;
                inc.setC_rate(cRate);

                //inmaps.remove(step);
            }
        });
      /*  loadMap.forEach((key,value)->{
            Edge le= findEdgByName(constant.ctmls, key);
            *//*获取输入元胞*//*
            int inCell_index = le.getInCell();
            ctmCell inc = initCtm.getInCell(ctms,inCell_index);
            double cRate = inc.getC_rate();
            cRate += value;
            inc.setC_rate(cRate);
        });*/
    }
    /**
     *
     */
    public static  loadVehMap[][]  load() {
        BufferedReader reader = null;
        loadVehMap[][] vMap = new loadVehMap[][]{};
        try {
            String path = fileutils.class.getClassLoader().getResource("jds_025").getPath();
            reader = new BufferedReader(new FileReader(path));
            Gson gson = new GsonBuilder().create();
            vMap = gson.fromJson(reader, loadVehMap[][].class);
            return vMap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 提供精确的减法运算
     *
     * @param v1 被减数
     * @param v2 减数
     * @return double   返回类型
     * @throws
     * @Title: sub
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    private static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return double   返回类型
     * @throws
     * @Title: mul
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static double mul(double v1, double v2) {
        return MathSupplier.mul3(v1, v2);
    }

    /**
     *
     * @param p1
     * @param p2
     * @return
     */
    private static boolean isEquals(List<DefaultWeightLabelEdge> p1, List<DefaultWeightLabelEdge> p2) {
        if (p1 == p2) return true;
        if (p1 == null || p2 == null) return false;
        int size = p1.size();
        if (p2.size() != size) return false;
        for (int i = 0; i < size; i++){
            if(!p1.get(i).getLabel().equals(p2.get(i).getLabel())){
                return false;
            }
        }
        return true;
    }
    public static double roude(double v1) {
        return MathSupplier.roude(v1);
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
    private static double div(double v1, double v2) {
        return MathSupplier.div(v1, v2, 4);
    }

}
