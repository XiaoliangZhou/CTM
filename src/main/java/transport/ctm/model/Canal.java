package transport.ctm.model;

import java.io.Serializable;

public class Canal implements Serializable {
    /*总车辆数*/
    public int t_n;
    /*直行车辆*/
    public int s_n ;
    /*右转车辆*/
    public int r_n;
    /*左转车辆*/
    public int l_n;
    /*终点车辆*/
    public int d_n;

    public Canal() {
    }



    public int getT_n() {
        int t_n = getR_n() + getS_n() + getL_n() + getD_n();
        return t_n;
    }

    public int getS_n() {
        return s_n;
    }

    public int getR_n() {
        return r_n;
    }

    public int getL_n() {
        return l_n;
    }

    public void setT_n(int t_n) {
        this.t_n = t_n;
    }

    public void setS_n(int s_n) {
        this.s_n = s_n;
    }

    public void setR_n(int r_n) {
        this.r_n = r_n;
    }

    public void setL_n(int l_n) {
        this.l_n = l_n;
    }

    public int getD_n() {
        return d_n;
    }

    public void setD_n(int d_n) {
        this.d_n = d_n;
    }
}
