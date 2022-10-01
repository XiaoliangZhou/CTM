package transport.ctm.main;

import org.junit.Test;
import transport.ctm.model.ctmCell;
import transport.ctm.model.secCell;
import transport.graph.Vehicle;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Map;

public class Queue {

    @Test
    public void test1(){
        /* LinkedList l = new LinkedList();
        l.offer("1");
        l.offer("2");
        l.offer("3");
        l.offer("4");
        l.offer("5");

        l.peek();
        l.poll();
        l.pollFirst();
        l.pollLast();
        l.peek();*/


        ArrayDeque<String> queue = new ArrayDeque<String>();
        // 进队
        queue.offer("aa");
        queue.offer("bb");
        queue.offerFirst("abcd");
        queue.offerLast("abcd");
        queue.push("123");
        queue.push("23");
        queue.addFirst("1111");
        queue.addLast("1111");

        // 查看队头元素
        System.out.println(queue.peek()); //aa

        // 出队
        System.out.println(queue.poll()); //aa
        System.out.println(queue.poll()); //bb


        System.out.println(queue);
    }
/*
    private static void updateDiVehicle(secCell c1,
                                        ctmCell c2,
                                        ctmCell c3,
                                        String[] steer, double f1, double f2) {
        int N1 = (int)f1,N2 = (int)f2;
        int startIndex = 0;
        int idx0 = 0,idx1 = 0 ;
        Iterator<Map.Entry<String,ArrayDeque<Vehicle>>> it = c1.getQueMap().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = it.next();
            String k = (String)entry.getKey();
            ArrayDeque<Vehicle> dq1 = (ArrayDeque<Vehicle>) entry.getValue();
            if(k.equals(steer[0])){
                while(idx0<N1){
                    offerQueue(c2,dq1);
                    idx0++;
                }
            }
            if(k.equals(steer[1])){
                while(idx1<N2){
                    offerQueue(c3, dq1);
                    idx1++;
                }
            }
        }
    }*/

    /**
     * 车辆入队
     * @param c
     * @param dq1
     */
    private static void offerQueue(ctmCell c, ArrayDeque<Vehicle> dq1) {
        Vehicle v;
        v = dq1.peek();
        if (v!=null){
            v = dq1.poll();
            /*进入相应队列*/
            c.getC1().offer(v);
            v.setCurCellPosition(c.c_id);
        }
    }

    @Test
    public void test2(){
        ArrayDeque<Vehicle> dq1 = new ArrayDeque<Vehicle>();
        Vehicle v1 = new Vehicle();
        v1.setVehId("v1");
       // v1.setNextIntoCell(1);
        Vehicle v2 = new Vehicle();
        v2.setVehId("v2");
       // v2.setNextIntoCell(1);
        Vehicle v3 = new Vehicle();
        v3.setVehId("v3");
        //v3.setNextIntoCell(1);
        Vehicle v4 = new Vehicle();
        v4.setVehId("v4");
        //v4.setNextIntoCell(1);
        Vehicle v5 = new Vehicle();
        v5.setVehId("v5");
       // v5.setNextIntoCell(1);



        dq1.offer(v1);
        dq1.offer(v2);
        dq1.offer(v3);
        dq1.offer(v4);
        dq1.offer(v5);

        ArrayDeque<Vehicle> dq2 = new ArrayDeque<Vehicle>();
        Vehicle v11 = new Vehicle();
        v11.setVehId("v11");
        //v11.setNextIntoCell(2);
        Vehicle v21 = new Vehicle();
        v21.setVehId("v21");
        //v21.setNextIntoCell(2);
        Vehicle v31 = new Vehicle();
        v31.setVehId("v31");
       // v31.setNextIntoCell(2);
        Vehicle v41 = new Vehicle();
        v41.setVehId("v41");
        //v41.setNextIntoCell(2);

        dq2.offer(v11);
        dq2.offer(v21);
        dq2.offer(v31);
        dq2.offer(v41);

        secCell c1 = new secCell();
        Map<String,ArrayDeque<Vehicle>> queueMap = c1.getQueMap();
        queueMap.put("S",dq2);
        queueMap.put("R",dq1);

        ctmCell c2 = new ctmCell();
        c2.setC_id(2);
        ctmCell c3 = new ctmCell();
        c3.setC_id(1);
        double f1 = 3.0,f2 = 2.0;
        String[] steer = new String[]{"S","R"};

        System.out.println(c1.getQueMap().entrySet().iterator().next().getValue().size());

        //updateDiVehicle(c1,c2,c3,steer,f1,f2);






    }

}
