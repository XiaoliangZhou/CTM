package transport.ctm.main;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import transport.file.util.fileutils;
import transport.math.util.MathSupplier;

public class jjf {

/*    @Test
    public void test(){
        int[] hd = new int[]{566,637,154};
        System.out.println(ArrayUtils.indexOf(hd,15));

        System.out.println(fileutils.getVehicleSteer("N", "E"));


    }*/
    @Test
    public void test2(){
        double tdf = 0.01;
        do{
            System.out.println(tdf);
            tdf+=MathSupplier.add(tdf,0.01);
        }while (tdf<0.51);
    }
}
