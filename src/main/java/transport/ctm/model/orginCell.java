package transport.ctm.model;

import java.io.Serializable;

public class orginCell extends ctmCell implements Serializable {

    /*来自路段流量1*/
    public double f_l_in;

    public double f_l_in_1;

    public double getF_l_in() {
        return f_l_in;
    }

    public void setF_l_in(double f_l_in) {
        this.f_l_in = f_l_in;
    }

    public double getF_l_in_1() {
        return f_l_in_1;
    }

    public void setF_l_in_1(double f_l_in_1) {
        this.f_l_in_1 = f_l_in_1;
    }
}
