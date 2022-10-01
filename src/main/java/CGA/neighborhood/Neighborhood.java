package CGA.neighborhood;

import CGA.Model.Chrome;

import java.io.Serializable;
import java.util.List;

public interface Neighborhood extends Serializable {
    List<Chrome> getNeighbors(Chrome[][] var1, int var2,int var3);
    List<Chrome> getNeighbors(Chrome[][][] var1, int var2,int var3,int var4);
}
