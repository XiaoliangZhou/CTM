package transport.ctm.main;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.junit.Test;
import transport.math.util.randomSupplier;

import java.math.BigDecimal;
import java.util.*;

public class math {
    public static Map ggf(int N,double lmd){
        int sum = 0;
        int sp =200;
        List o = new ArrayList<>();
        Map<Integer,Integer> ms = new LinkedHashMap<>();
        for (int k = 0; k < N; k++) {
            int r = randomSupplier.getPoissionDistribution(lmd);
            if(r!=0){
                o.add(r);
            }
            if(sum==N){ break; }
            sum+=r;
        }
        int[] tv = randomSupplier.randomSet(sp,o.size());
        for (int i = 0; i < tv.length; i++) {
            ms.put(tv[i],(int)o.get(i));
        }
        return ms;
    }
    @Test
    public void test1(){
        Map<Integer,Integer> ms = ggf(20,1);
        ms.forEach((k,v)->{
            System.out.println(k+"  :"+v);
        });

       /* double sum = 0.0;
        for (int i = 0; i < lg.size() ; i++) {
            // int s = lg.get(0).ves.size();
            int d =  Arrays.toString(lg.get(0).vesMap.get("2").toArray()).getBytes().length;
            sum+=d;
        }
        System.out.println(sum);
        System.out.println("lg 所占内存：" +(sum/(1024*1024))+"MB");*/

    }
}

