/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transport.ctm.util;

import CGA.Model.Chrome;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import transport.ctm.main.IntegerSolution;
import transport.ctm.main.solution;
import transport.ctm.model.*;
import transport.graph.DefaultWeightLabelEdge;
import transport.graph.Edge;
import transport.graph.Garage;
import transport.graph.Vertex;

import java.util.*;

/**
 *
 * @author Tony
 */
public class constant {

    /*单个元胞的最大承载交通量(veh)*/
    public static final int CL_CAP = 14 ;
    /*3相位*/
    public static final int phase_3 = 3 ;
    /*4相位*/
    public static final int phase_4 = 4 ;
    /*5相位*/
    public static final int phase_5 = 5 ;

    public static List<Vertex> vertexs = new LinkedList<>();
    /*存放元胞对象*/
    public static List ctms = new LinkedList<>();
    /*存放路段*/
    public static List<Edge> ctmls =  new LinkedList<>();
    /*存放路段元胞链接*/
    public static List<ctmLinks> clks =  new LinkedList<>();
    /*存放交叉元胞链接*/
    public static List<ctmLinks> clkts = new LinkedList<>();
    /*交叉口对象*/
    public static List<intersection> cints =  new LinkedList<>();
    /*节点车库*/
    public static List<Garage> lg = new LinkedList<>();

    public static Graph<String, DefaultWeightLabelEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightLabelEdge.class);

    public static Map<String,Double> btMaps = new HashMap<>();

    public static Map<String,Double> cosMaps = new HashMap<>();

    public static List<IntegerSolution> chrs = new ArrayList<>();

    public static Chrome[][][] d3_chrs = new Chrome[7][7][7];

    public static solution[][][] solutions = new solution[5][5][4];


    public static IntegerSolution[][] d2_chrs = new IntegerSolution[10][10];

}
