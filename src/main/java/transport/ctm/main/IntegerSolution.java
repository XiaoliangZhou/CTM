package transport.ctm.main;


import CGA.MocGAs.ChromeBase;
import CGA.MocGAs.Pareto;
import CGA.Model.Cont;
import com.rits.cloning.Cloner;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 *
 */
public class IntegerSolution extends ChromeBase implements Cloneable, Serializable {
    String Id;
    int index=0;
    double[] objective = new double[Cont.F_NUM];
    int rank ;/*pareto等级*/
    double nd = 0.0;/*个体拥挤度*/
    boolean isCurChr=false;
    List<Integer> x = new ArrayList<>();
    Pareto pareto = new Pareto();

    public IntegerSolution() {
        List<Integer> x = getX();
        for (int i = 0; i < Cont.X_NUM; i++) {
            x.add(0);
        }
    }

    public List<Integer> getX() {
        return x;
    }


    public void setX(List<Integer> x) {
        this.x = x;
    }

    public void setVariableValue(int index,Integer value) {
        this.getX().set(index,value);
    }

    public String getId() {
        return Id;
    }


    public void setId(String id) {
        Id = id;
    }

    public int getRank() {
        return rank;
    }


    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public int getIndex() {
        return index;
    }

    public double[] getObjective() {
        return objective;
    }


    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    public void setObjective(double[] objective) {
        this.objective = objective;
    }

    public Pareto getPareto() {
        return pareto;
    }

    public double getNd() {
        return nd;
    }

    public void setNd(double nd) {
        this.nd = nd;
    }

    public void setPareto(Pareto pareto) {
        this.pareto = pareto;
    }

    public int getNumberOfObjectives(){
        return super.getF().length;
    }
    public double getObjective(int index){
       return this.objective[index];
    }


    /**
     * (1)、浅拷贝：被复制对象的所有值属性都含有与原来对象的相同，而所有的对象引用属性仍然指向原来的对象。
     * (2)、深拷贝：在浅拷贝的基础上，所有引用其他对象的变量也进行了clone，并指向被复制过的新对象。也就是说，一个默认的clone()方法实现机制，仍然是赋值。
     * 如果一个被复制的属性都是基本类型，那么只需要实现当前类的cloneable机制就可以了，此为浅拷贝。
     * <h color='red'>如果被复制对象的属性包含其他实体类对象引用，那么这些实体类对象都需要实现cloneable接口并覆盖clone()方法。</h>
     * <p>https://blog.csdn.net/qq_33314107/article/details/80271963</p>
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws  CloneNotSupportedException {
        Object obj = super.clone();
        return new Cloner().deepClone(obj);
    }
    /**
     * 深度复制，实参类必须实现Serializable接口
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public  Object copy() throws IOException,CloneNotSupportedException, Exception {
        Object o = super.clone();
		//先序列化，写入到流里
       /* ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(o);
        //然后反序列化，从流里读取出来，即完成复制
        ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi = new ObjectInputStream(bi);
        Object d =  oi.readObject();*/

        return new Cloner().deepClone(o);
    }

    public boolean isCurChr() {
        return isCurChr;
    }

    public void setCurChr(boolean curChr) {
        isCurChr = curChr;
    }

    @Override
    public String toString() {
        return "Chrome{" +
                "rank=" + rank +
                ", f=" + Arrays.toString(objective) +
                ", nd=" + nd +
                ", isCurChr=" + isCurChr +
                ", x=" + x +
                ", pareto=" + pareto +
                ", Id='" + Id + '\'' +
                ", index=" + index +
                '}';
    }

    /*public static void main(String[] args) throws Exception {
        Chrome c1 = new Chrome();
        c1.setId("1");
        //c1.setX(new double[]{0.0,0.0});
        c1.setF(new double[]{0.36254,0.96584});

        Chrome c2 = (Chrome) c1.clone();
        //c2.setId("3");
        Pareto ptu = new Pareto();
        Set<Integer> set = new HashSet<>();
        set.add(0);
        set.add(2);
        set.add(4);
        ptu.setPset(set);
        c2.setPareto(ptu);
        c2.getF()[0] = 0.21111;
        c2.getF()[1] = 0.21111;

        Chrome c3 = new Chrome();
        c3.setId("3");

        //c3.setNd(c1.getNd());
        c3.setF(new double[]{0.0,0.0});
      *//*  c2.setNd(new double[]{0.21111,0.21711});*//*
        //c2.setF(new double[]{0.21111,0.12354});
        c2.setId("6");
        System.out.println(c1);
        System.out.println(c2);
        System.out.println(c3);
    }*/
}
