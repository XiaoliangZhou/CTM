package transport.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * @标题: Vertex.java
 * @包名: edu.jiaotong.transport.graph
 * @描述: 节点实体类
 * @作者: liangxiao.zhou
 * @时间: Oct 08, 2018 16:11:14 PM
 * @版权: (c) 2018, 兰州交通大学
 */
public  class Vertex{
    // 默认值
    public static final int def_dis = Integer.MAX_VALUE ;
    // 节点标识
    public String label ;
    //节点所对应的有向边标识
    public List<String> edgList ;

    public double btc;

    public Vertex(String label){
            this.label = label ;
            this.edgList = new ArrayList<String>();
    }

    public static int getDef_dis() {
        return def_dis;
    }

    public String getLabel() {
        return label;
    }

    public List<String> getEdgList() {
        return edgList;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setEdgList(List<String> edgList) {
        this.edgList = edgList;
    }

    public double getBtc() {
        return btc;
    }

    public void setBtc(double btc) {
        this.btc = btc;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "label='" + label + '\'' +
                ", edgList=" + edgList +
                '}';
    }
}
