package transport.ctm.model;

import java.io.Serializable;

public class innerCell extends ctmCell implements Serializable {

    protected int[] father_id;
    protected    int[] child_id;

    public innerCell(){
        this.father_id = new int[]{};
        this.child_id = new int[]{};
    }

    public int[] getFather_id() {
        return father_id;
    }

    public int[] getChild_id() {
        return child_id;
    }

    public void setFather_id(int[] father_id) {
        this.father_id = father_id;
    }

    public void setChild_id(int[] child_id) {
        this.child_id = child_id;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
