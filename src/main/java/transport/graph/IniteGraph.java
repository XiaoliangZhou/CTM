package transport.graph;

import org.apache.commons.lang3.StringUtils;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.alg.interfaces.KShortestPathAlgorithm;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.scoring.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;
import transport.ctm.util.constant;
import transport.file.util.fileutils;

import java.io.IOException;
import java.util.*;

/**
 * @Title: initeGraph.java
 * @Package edu.jiaotong.transport.graph
 * @Description: 节点实体类
 * @author liangxiao.zhou
 * @date Oct 08, 2018 16:11:14 PM
 * @version V1.0
 */
public class IniteGraph {
    private static final Graph<String, DefaultWeightLabelEdge> g = constant.graph;
    private static final String[] v = new String []{
            "1","2","3","4","5","6",
            "7", "8","9","10","11",
            "12","13", "14","15","16",
            "17","18","19", "20","21","22","23", "24","25","26","27"};

    /**
     * 初始化路网
     */
    public static void setNetwork(){

        Graphs.addAllVertices(g, Arrays.asList(v));
        /*路网模型*/
        constant.ctmls.forEach((le) -> {
            /*获取路段标识对应的Edge object*/
            g.addEdge(le.getSourceVertex(),
                    le.getEndVertex().label, new DefaultWeightLabelEdge(le.getLabel(),le.getFreeTime()));
            g.setEdgeWeight(le.getSourceVertex(), le.getEndVertex().label,le.getWeight());

            /*终点*/
            le.getSteerMaps().put(0,0.0);
            /*左转*/
            le.getSteerMaps().put(1,0.0);
            /*右转*/
            le.getSteerMaps().put(2,0.0);
            /*直行*/
            le.getSteerMaps().put(3,0.0);
            /*初始化*/
            if(le.getLabel().equals("4")||le.getLabel().equals("14")){
                /*终点*/
                le.getSteerMaps().put(0,0.0);
                /*左转*/
                le.getSteerMaps().put(1,0.0);
                /*右转*/
                le.getSteerMaps().put(2,0.0);
                /*直行*/
                le.getSteerMaps().put(3,0.0);

                if(le.getLabel().equals("4")||le.getLabel().equals("14")){
                    for (String s : v) {
                        le.getReachMaps().put(s,0);
                        Map<String,Integer> rMaps = new HashMap<>();
                        for (String v : v) {
                            if(!s.equals(v)){
                                rMaps.put(v,0);
                                //le.getInputMaps().put(s,rMaps);
                            }
                        }

                    }
                }

            }

        });
        for (String s : v) {
            constant.btMaps.put(s,0.0);
            constant.cosMaps.put(s,0.0);
        }

    }
    /**
     * 单源最短路算法
     * @Title: AlgorithmOfShortPath
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return void   返回类型
     * @throws
     */
    public static  DefaultGraphPath[][] AlgorithmOfShortestPath (){
        /*更新路段阻抗*/
        constant.ctmls.forEach((le)->{
            g.setEdgeWeight(le.getSourceVertex(), le.getEndVertex().label,le.getWeight());
        });
        List<Edge> cd = constant.ctmls;
        DefaultGraphPath[][] gp = new DefaultGraphPath[v.length][v.length];
        Set<Vertex> vertextSet = new HashSet(Arrays.asList(v));
        int count=0;
        double t1=0,t2 = 0,t3=0,t4=0;
        for (int i = 0; i < v.length; i++) {
            /*单源最短路径 起始节点*/
          /*  ShortestPathAlgorithm.SingleSourcePaths<String,
                    DefaultWeightLabelEdge> pathsTree = new DijkstraShortestPath<>(g).getPaths(v[i]);*/
           ShortestPathAlgorithm.SingleSourcePaths<String,
                    DefaultWeightLabelEdge> pathsTree = new BidirectionalDijkstraShortestPath<>(g).getPaths(v[i]);
           /* ShortestPathAlgorithm.SingleSourcePaths<String,
                    DefaultWeightLabelEdge> pathsTree  = new FloydWarshallShortestPaths<>(g).getPaths(v[i]);*/
            /*双向启发式搜索算法*/
            /* AStarAdmissibleHeuristic<String>  heuristic = new ALTAdmissibleHeuristic(g,vertextSet);
                    ShortestPathAlgorithm.SingleSourcePaths<String,
                    DefaultWeightLabelEdge> pathsTree = new BidirectionalAStarShortestPath<>(g, heuristic).getPaths(v[i]);*/

            /*目标节点*/
            for (int j = 0; j < v.length; j++) {
                if(i != j){

                    GraphPath<String, DefaultWeightLabelEdge> p = pathsTree.getPath(v[j]);
                    DefaultGraphPath dgp =  getDefaultGraphPath(p);
                    //System.out.println(p+"----"+ p.getWeight());
                    //System.out.println(i+"----->"+j+"------"+p
                    gp[i][j] = dgp;
                    count++;

              /*      if(i==1 & j==3){
                        t1=p.getWeight();
                        System.out.println(p+":"+p.getWeight());
                    }
                    if(i==1 & j==0){
                        t2=p.getWeight();
                    }
                    if(i==0 & j==2){
                        t3=p.getWeight();
                    }
                    if(i==2 & j==3){
                        t4=p.getWeight();
                    }*/

                }
            }
        }
       /* if(t1>t2+t3+t4){
            System.out.println("t2+t3+t4 = "+ t2+t3+t4);
        }*/

      /*  System.out.println("t2+t3+t4 = "+( t2+t3+t4));
        System.out.println("t2 = "+( t2));*/
        return gp;
    }

    /**
     * 任意两个节点之间最短路径
     * @param sourceVertex 源节点
     * @param targetVertex 目标节点
     * @return
     */
    public static DefaultGraphPath algOfSingleSourcePath(String sourceVertex ,String targetVertex){
        /*单源最短路径 起始节点*/
       /* constant.ctmls.forEach((le)->{
            g.setEdgeWeight(le.getSourceVertex(), le.getEndVertex().label,le.getWeight());
        });*/
       /* ShortestPathAlgorithm.SingleSourcePaths<String,
                DefaultWeightLabelEdge> pathsTree = new DijkstraShortestPath<>(g).getPaths(sourceVertex);*/
        ShortestPathAlgorithm.SingleSourcePaths<String,
                DefaultWeightLabelEdge> pathsTree = new BidirectionalDijkstraShortestPath<>(g).getPaths(sourceVertex);
        GraphPath<String, DefaultWeightLabelEdge> p = pathsTree.getPath(targetVertex);
        DefaultGraphPath dgp =  getDefaultGraphPath(p);

        return dgp;
    }

    /**
     * 获取DefaultGraphPath
     * @Title: getVertexByName
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param p
     * @return DefaultGraphPath   返回类型
     * @throws
     */
    public static DefaultGraphPath getDefaultGraphPath(GraphPath<String, DefaultWeightLabelEdge> p){
        LinkedList<DefaultWeightLabelEdge> path = new LinkedList<>();
        for (DefaultWeightLabelEdge edge : p.getEdgeList()) {
            path.add(edge);
        }
        DefaultGraphPath dgp = new DefaultGraphPath();
        dgp.setStartVertex(p.getStartVertex());
        dgp.setEndVertex(p.getEndVertex());
        dgp.setPaths(path);
        dgp.setWeight(p.getWeight());

        return dgp;
    }


    @Deprecated
    public static LinkedList<DefaultWeightLabelEdge> initEdgelist(GraphPath<String, DefaultWeightLabelEdge> p){
        String targetSource;
        Iterator<DefaultWeightLabelEdge> it = p.getEdgeList().iterator();
        while (it.hasNext()){
            DefaultWeightLabelEdge defEdge = it.next();
            if(!p.getEndVertex().equals(defEdge.getTarget())){
               // defEdge.setNextRoadLabel(it.next().getLabel());
            }
        }
        return (LinkedList<DefaultWeightLabelEdge>)p.getEdgeList();
    }
     /**
     * 获取节点对象
     * @Title: getVertexByName
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param list  集合
     * @param label 节点标识
     * @return Vertex   返回类型
     * @throws
     */
     public static Vertex getVertexByName(List<Vertex> list,String label){
        if(list != null && !list.isEmpty()){
            for (Vertex vertex : list) {
                if(vertex.label.endsWith(label)){
                    return vertex;
                }                 
            }
        }  
        return null;
    }

    public static Edge getEdgeByLabel(String label){
        List<Edge> el = constant.ctmls;
        if(el != null && !el.isEmpty()){
            if(StringUtils.isNotBlank(label)){
                for (Edge edge : el) {
                    if(edge.getLabel().equals(label)){
                        return edge;
                    }
                }
            }
        }
        return null;
    }
    public static void setNetVertex() {
        Vertex v = null;
        List<Vertex> ols = new ArrayList<Vertex>();
        for (int j = 1; j < 28; j++) {
            v = new Vertex(String.valueOf(j));
            constant.vertexs.add(v);
        }
    }
    /**
     * 加载路网数据
     * @return
     * @throws Exception
     */
    protected static void loadNetworkData() throws Exception  {
        /*路网数据*/
        String path = fileutils.class.getClassLoader().getResource("sf_tab_4.txt").getPath();
        fileutils.readGraphDataFromTxtByPath(constant.vertexs, path);
        /*路网模型*/
        setNetwork();
    }

    public static void main(String[] args) throws Exception {
        loadNetworkData();
        AlgorithmOfShortestPath();

    }
}
















