/*
package transport.ctm.main;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import transport.ctm.model.*;
import transport.ctm.util.constant;
import transport.file.util.fileutils;
import transport.graph.*;
import transport.math.util.CellHandle;
import transport.math.util.MathSupplier;

import java.util.*;

import static transport.ctm.main.buildCtm.*;

public class ctm {

    @Test
    public void test(){
        int samplingPeriod = 400;
        int timeStep = 10001;
        double tdf = 0.01;
        IniteGraph gs = new IniteGraph();
        try{
            String path = fileutils.class.getClassLoader().getResource("sf_tab_2.txt").getPath();
            Vertex v  = null ;
            List<Vertex> ols = new ArrayList<Vertex>();
            for (int j = 1; j < 25; j++) {
                v = new Vertex(String.valueOf(j));
                ols.add(v);
            }
            Map<String,Vertex> vertext_map  = fileutils.readGraphDataFromTxtByPath(ols, path);
            */
/*初始化交叉口信息*//*

            fileutils.loadIntData();
            */
/*相位设置、初始化*//*

            setIntPhase();
            int step = 100;
            int cycle = 201;
            List<Garage> lg = new LinkedList<>();
            int[][] ods = CellHandle.zeros(24,24);
            Garage gg = null;
            for (int i = 0; i < 24; i++) {
                gg = new Garage();
                lg.add(gg);
            }

            while(step<timeStep){
                */
/*单源最短路径*//*

                DefaultGraphPath[][] gp = gs.AlgorithmOfShortestPath(vertext_map);
                */
/*交通流加载*//*

                if(step ==100 ||step == 200){
                    //fileutils.networkFlowLoading(tdf,d,step, lg, ods, gp);
                }
                */
/*车流传播*//*

                sim_01.ctmSimulat(step);
                */
/*相位转换*//*

                if(step%3==0){
                    for (int j = 0; j < constant.cints.size(); j++) {
                        if(PhaseFilter(j)){
                            ctmSwitchInt(j);
                        }
                    }
                }
                List ff = constant.ctmls;
                List dd = constant.ctms;
                step+=1;

                for (int j = 0; j < constant.cints.size(); j++) {
                    intersection ints = constant.cints.get(j);
                    Vertex vx = IniteGraph.getVertexByName(ols, ints.nlabel);
                    for (String label : vx.edgList) {
                        Edge curEdge = IniteGraph.getEdgeByLabel(label);

                        int orgCellId = curEdge.getoCell();
                        int endCellId = curEdge.getdCell();
                        endCell edc = initCtm.getEndCell(endCellId);*/
/*当前路段尾元胞*//*

                        Canal ecl = edc.getCanal();

                        String target = curEdge.getEndVertex().label;
                        orginCell ogc = initCtm.getOrginCell(orgCellId);
                        */
/*记录车辆进入路段的时间*//*

                        if (ogc.getClv().size() > 0) {
                            for (Vehicle veo : ogc.clv) {
                                veo.setExtanceTime(step);
                            }
                        }
                        if ( edc.getClv().size()>0) {
                            intersection toInts = initCtm.searchIntSourceByAlias(constant.cints,target);
                            int curPhi = toInts.phase;
                            Map<String, ArrayDeque<Vehicle>> queMap = edc.getQueMap();
                            Iterator<Vehicle> it = edc.getClv().iterator();
                            */
/*存放到达终点车辆*//*

                            ArrayDeque<Vehicle> gvc = new ArrayDeque<>();
                            while(it.hasNext()){
                                Vehicle car = it.next();
                                DefaultGraphPath dgp = car.getDefaultGraphPath();
                                String targetSource = dgp.endVertex;
                                boolean fag = target.equals(targetSource);
                                if (fag) {
                                    it.remove(); */
/*该车辆到达终点 出队*//*

                                    gvc.offer(car);*/
/*回收该车辆*//*

                                }
                                if(!fag){
                                    */
/*该车辆到达路段尾元胞 计算分流系数*//*

                                    LinkedList<DefaultWeightLabelEdge> dwles = dgp.ld;
                                    for (int k = 0; k < dwles.size(); k++) {
                                        String curLane = dwles.get(k).getLabel();
                                        if (curLane.equals(curEdge.getLabel())) {
                                            String steer = fileutils.getSteer(curEdge, ints, dwles, k);*/
/*车辆转向*//*

                                            fileutils.vehicleQeue(queMap, it, car, steer); */
/*车辆进入末尾元胞后，能正确驶入相对应的排队区域*//*

                                            if(steer.equals("S"))ecl.s_n += 1;
                                            if(steer.equals("L"))ecl.l_n += 1;
                                            if(steer.equals("R"))ecl.r_n += 1;
                                            break;
                                        }
                                    }
                                }
                                */
/*通过该路段的时间*//*

                                car.setTravseTime(step-car.getExtanceTime()+1);
                            }
                            */
/*终点车辆进入车库*//*

                            Iterator<Vehicle> iv = gvc.iterator();
                            while (iv.hasNext()){
                                Vehicle car = iv.next();
                                iv.remove();*/
/*出队*//*

                                */
/*存放车辆的车库id*//*

                                String targetSource = car.getDefaultGraphPath().endVertex;
                                LinkedList<Vehicle> gv = fileutils.getGarageByIntId(lg, targetSource).getReachVeh();
                                gv.offer(car);*/
/*加入车库*//*

                            }
                            */
/*更新分流系数*//*

                            fileutils.updatEndCellFlow(edc);
                            */
/*计算合流、分流系数*//*

                            if (curPhi != -1) {
                                int[] atr = toInts.phases.get(curPhi);
                                int tailIndex = atr[0];
                                int toIndex = atr[atr.length - 1];
                                while (tailIndex<=toIndex){
                                    ctmLinks clk = initCtm.getCtmLink(tailIndex);
                                    double t=0,s=0,r=0,l=0,rb=0,sr=0,sl=0,lr=0;
                                    int[] lc = clk.cells;
                                    switch (clk.type) {
                                        case 1:*/
/*合流*//*

                                            boolean fag = clk.intType==4&&(clk.curPhase==2||clk.curPhase==4);
                                            sl = initCtm.getInnerCell(lc[0]).getC_n(); */
/*左转*//*

                                            */
/*(L,R)*//*

                                            if(fag) r = initCtm.getInnerCell(lc[1]).getC_n();*/
/*右转*//*

                                            */
/*(S,R)(L,R)*//*

                                            if(!fag)r = initCtm.getEndCell(lc[1]).getCanal().getR_n();*/
/*右转车辆*//*

                                            if((sl+r)>0){
                                                rb = MathSupplier.div(r,(sl+r));
                                            }
                                            clk.setKp(new double[]{1-rb,rb});
                                            break;
                                        case 2:*/
/*分流*//*

                                            if (ArrayUtils.indexOf(lc,endCellId) == 0) {
                                                if(lc.length==2){
                                                    endCell enc = initCtm.getEndCell(lc[0]);
                                                    r = enc.getCanal().getR_n();*/
/*右转车辆*//*

                                                    t = enc.getC_n();*/
/*路段总车辆数*//*

                                                    if(t>0){
                                                        rb = MathSupplier.div(r,t);
                                                        clk.setKp(new double[]{rb});*/
/*右转比例*//*

                                                    }
                                                }
                                                if(lc.length==3){
                                                    if (t > 0) {
                                                        if(clk.intType==3&&(clk.curPhase==2)){
                                                            */
/*T2(S,L)*//*

                                                            sl = ecl.getS_n()+ecl.getL_n() ;
                                                            s = MathSupplier.div(ecl.s_n,sl);*/
/*直行*//*

                                                            clk.setKp(new double[]{s,1-s});
                                                            */
/*当前相位尾元胞允许的最大输出*//*

                                                            edc.setCur_alow_out(sl);
                                                        }
                                                        if(clk.intType==3&&(clk.curPhase==1)||clk.intType==4&&(clk.curPhase==1||clk.curPhase==3)){
                                                            */
/*T1/TT13(S,R)*//*

                                                            sr = ecl.getS_n() + ecl.getR_n();
                                                            if(sr>0){
                                                                r = MathSupplier.div(ecl.r_n,sr);*/
/*右转*//*

                                                            }
                                                            clk.setKp(new double[]{1-r,r});
                                                            edc.setCur_alow_out(sr); */
/*当前相位尾元胞允许的最大输出*//*

                                                        }
                                                        if(clk.intType==3&&(clk.curPhase==3)||clk.intType==4&&(clk.curPhase==2||clk.curPhase==4)){
                                                            */
/*T3/TT24(L,R)*//*

                                                            lr = ecl.getL_n() + ecl.getR_n();
                                                            if(lr>0){
                                                                l = MathSupplier.div(ecl.l_n,lr);*/
/*左转*//*

                                                            }
                                                            clk.setKp(new double[]{l,1-l});
                                                            edc.setCur_alow_out(lr); */
/*当前相位尾元胞允许的最大输出*//*

                                                        }
                                                    }
                                                }
                                            }
                                            break;
                                    }
                                    tailIndex++;
                                }
                            }
                        }
                    }
                }
            }
        }catch(Exception e){ e.printStackTrace(); }
    }
}
*/
